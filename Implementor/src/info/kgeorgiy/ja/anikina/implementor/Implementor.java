package info.kgeorgiy.ja.anikina.implementor;


import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Implementor implements Impler {
        public static class NewMethod {
        Method method;

        public NewMethod(Method method) {
            this.method = method;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NewMethod) {
                NewMethod o = (NewMethod) obj;
                if (hashCode() == o.hashCode()) {
                    return method.getReturnType() == o.method.getReturnType()
                            && method.getName().equals(o.method.getName())
                            && Arrays.equals(method.getParameterTypes(), o.method.getParameterTypes());
                }
            }
            return false;
        }
        static final int mod = (int) 1e9;

        @Override
        public int hashCode() {
            return (method.getReturnType().hashCode()
                    + method.getName().hashCode()
                    + Arrays.hashCode(method.getParameterTypes())) * 30 % mod;
        }
    }


    public Implementor() {
    }

    public static void main(String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            System.err.println("wrong args");
            return;
        }
        try {
            Class<?> clazz = Class.forName(args[0]);
            new Implementor().implement(clazz, Paths.get(args[1]));
        } catch (ClassNotFoundException e) {
            System.err.println("class not found");
        } catch (ImplerException e) {
            System.err.println("exception while execution occurred " + e.getMessage());
        } catch (InvalidPathException e) {
            System.err.println("invalid path to the file");
        }
    }


    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token == null) {
            throw new ImplerException("token is null");
        }
        if (root == null) {
            throw new ImplerException("root is null");
        }
        if (token.isPrimitive() || token.isArray() || token == Enum.class) {
            throw new ImplerException("invalid token");
        }
        if (Modifier.isFinal(token.getModifiers()) || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("invalid token's modifier");
        }
        root = root.resolve(token.getPackageName()
                .replace('.', File.separatorChar))
                .resolve(token.getSimpleName() + "Impl.java");
        if (root.getParent() != null) {
            try {
                Files.createDirectories(root.getParent());
            } catch (IOException e) {
                throw new ImplerException("can't create directory");
            }
        }
        String file = getHeader(token) + getConstructors(token) + getMethods(token) + "\n}";
//        System.err.println(file);
        try (BufferedWriter writer = Files.newBufferedWriter(root)) {
            StringBuilder sb = new StringBuilder();
            for (char ch : file.toCharArray()) {
                sb.append(ch >= 128 ? String.format("\\u%04X", (int) ch) : Character.toString(ch));
            }
            writer.write(sb.toString());
        } catch (IOException e) {
            System.err.println("error while writing occurred");
        }
    }

    private String getHeader(Class<?> token) throws ImplerException {
        String header = "";
        Package currPackage = token.getPackage();
        if (currPackage != null) {
            header = "package " + currPackage.getName() + ";\n";
        }
        header += ("public class " + token.getSimpleName() + "Impl "
                + (token.isInterface() ? "implements " : "extends ")
                + token.getCanonicalName() + " {\n");
        return header;
    }

    private String getConstructors(Class<?> token) throws ImplerException {
        if (token.isInterface()) return "";
        StringBuilder sb = new StringBuilder();
        for (Constructor<?> constructor : token.getDeclaredConstructors()) {
            if (Modifier.isPrivate(constructor.getModifiers())) {
                continue;
            }
            sb.append(methodToString(constructor)).append("\n");
        }
        String res = sb.toString();
        if (res.isEmpty()) {
            throw new ImplerException("can't extend from this class " + token.getCanonicalName());
        }
        return res;
    }


    private void getAbstractMethods(Method[] arrayOfMethods, Set<NewMethod> methods) {
        for (Method m : arrayOfMethods) {
            if (Modifier.isAbstract(m.getModifiers())) {
                methods.add(new NewMethod(m));
            }
        }
    }


    private String getMethods(Class<?> token) throws ImplerException {
        Set<NewMethod> methods = new HashSet<>();
        getAbstractMethods(token.getMethods(), methods);
        while (token != null) {
            if (Modifier.isPrivate(token.getModifiers()) || Modifier.isFinal(token.getModifiers())) {
                throw new ImplerException("can't extend from " + token.getName());
            }
            getAbstractMethods(token.getDeclaredMethods(), methods);
            token = token.getSuperclass();
        }

        StringBuilder sb = new StringBuilder();
        for (NewMethod method : methods) {
            sb.append(methodToString(method.method)).append("\n");
        }
        return sb.toString();
    }

    private String returnType(Executable executable) {
            if (executable instanceof Method) {
                Method method = (Method) executable;
                return method.getReturnType().getCanonicalName() + " " + method.getName();
            }
            return ((Constructor<?>) executable).getDeclaringClass().getSimpleName() + "Impl";
    }


    private String methodToString(Executable executable) {
        String result;
        int modifier = executable.getModifiers()
                & ~Modifier.TRANSIENT
                & ~Modifier.ABSTRACT
                & ~Modifier.NATIVE;
        result = Modifier.toString(modifier) + " "
                + returnType(executable) +
                "(" + getParameters(executable, Parameter::toString) + ")"
                + getExceptions(executable.getExceptionTypes())
                + " {\n" + getBody(executable) + "\n}";
        return result;
    }

    private String getParameters(Executable method, Function<? super Parameter, String> function) {
            return Arrays.stream(method.getParameters()).map(function)
                    .collect(Collectors.joining(", "));
    }

    private String getBody(Executable executable) {
            if (executable instanceof Method) {
                return getBodyOfMethod((Method) executable);
            }
            return getBodyOfConstructor((Constructor<?>) executable);
    }

    private String getBodyOfMethod(Method method) {
        Class<?> type = method.getReturnType();
        if (type.getCanonicalName().equals("void")) {
            return "return;";
        } else if (type.getCanonicalName().equals("boolean")) {
            return "return false;";
        } else if (type.isPrimitive()) {
            return "return 0;";
        }
        return "return null;";
    }


    private String getBodyOfConstructor(Constructor<?> constructor) {
        return "super(" + getParameters(constructor, Parameter::getName) + ");\n";
    }

    private String getExceptions(Class<?>[] types) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i == 0) {
                sb.append(" throws ");
            } else {
                sb.append(", ");
            }
            sb.append(types[i].getCanonicalName());
        }
        return sb.toString();
    }
}

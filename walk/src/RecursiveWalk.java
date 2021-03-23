import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RecursiveWalk {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            System.err.println("wrong input and \\ or output");
            return;
        }
        try {
            Path input = Paths.get(args[0]);
            Path output = Paths.get(args[1]);
            if (output.getParent() != null) {
                try {
                    Files.createDirectories(output.getParent());
                } catch (IOException e) {
                    System.err.println("Couldn't create output path");
                    return;
                }
            }
            try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
                try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
                    String current;
                    while ((current = reader.readLine()) != null) {
                        try {
                            Path path = Paths.get(current);
                            if (Files.isRegularFile(path)) {
                                fileProcessing(path, writer);
                            } else {
                                directoryProcessing(path, writer);
                            }
                        } catch (InvalidPathException e) {
                            writeHash(0, current, writer);
                        }
                    }
                    try {
                        writer.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                } catch (InvalidPathException e) {
                    System.err.println("invalid output file " + output.toString());
                }
            } catch (FileNotFoundException e) {
                System.err.println("input file not found");
            }
        } catch (InvalidPathException e) {
            System.err.println("invalid paths of input or output files");
        } catch (IOException e) {
            System.err.println("invalid names of input or output files");
        }
    }

    private static void writeHash(long hashSum, String file, BufferedWriter writer) {
        try {
            writer.write(String.format("%016x %s\n", hashSum, file));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    private static long getHash(InputStream reader) throws IOException {
        long h = 0, high;
        int c;
        byte[] input = new byte[1024];
        while ((c = reader.read(input)) >= 0) {
            for (int i = 0; i < c; i++) {
                h = (h << 8) + (input[i] & 0xff);
                high = h & 0xff00000000000000L;
                if (high != 0L) {
                    h = h ^ (high >> 48);
                    h = h & ~high;
                }
            }
        }
        return h;
    }

    private static void fileProcessing(Path path, BufferedWriter writer) {
        long hash_sum;
        try (InputStream reader = Files.newInputStream(path)) {
            hash_sum = getHash(reader);
            writeHash(hash_sum, path.toString(), writer);
        } catch (IOException | InvalidPathException e) {
            System.err.println("invalid path " + path.toString());
        }
    }

    private static void directoryProcessing(Path path, BufferedWriter writer) {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.filter(Files::isRegularFile).forEach(x -> {
                fileProcessing(x, writer);
            });
        } catch (IOException | InvalidPathException e) {
            writeHash(0, path.toString(), writer);
        }
    }
}

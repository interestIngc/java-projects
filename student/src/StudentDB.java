import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import info.kgeorgiy.java.advanced.student.*;

public class StudentDB implements GroupQuery {

    final Comparator<Student> COMPARATOR_BY_NAME =
            (Comparator.comparing(Student::getLastName, Comparator.reverseOrder())).
            thenComparing(Student::getFirstName, Comparator.reverseOrder()).
            thenComparing(Student::getId);


    private <T, C extends Collection<T>> C getCollection(List<Student> students, Function<Student, T> f, Collector<T, ?, C> collector) {
        return students.stream().map(f).collect(collector);
    }

    private List<Student> sort(Collection<Student> students, Comparator<Student> cmp) {
        return students.stream().sorted(cmp).collect(Collectors.toList());
    }

    private Stream<Student> find(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream().filter(predicate).sorted(COMPARATOR_BY_NAME);
    }

    private List<Student> findToList(Collection<Student> students, Predicate<Student> predicate) {
        return find(students, predicate).collect(Collectors.toList());
    }


    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getCollection(students, Student::getFirstName, Collectors.toList());
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getCollection(students, Student::getLastName, Collectors.toList());
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getCollection(students, Student::getGroup, Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getCollection(students,
                student -> student.getFirstName() + " " + student.getLastName(),
                Collectors.toList());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return getCollection(students, Student::getFirstName, Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream().max(Comparator.comparing(Student::getId))
                .map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sort(students, Comparator.comparing(Student::getId));
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sort(students, COMPARATOR_BY_NAME);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findToList(students, student -> student.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findToList(students, student -> student.getLastName().equals(name));
    }

    private Stream<Student> findByGroup(Collection<Student> students, GroupName group) {
        return find(students, student -> student.getGroup().equals(group));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findByGroup(students, group).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return findByGroup(students, group).collect(Collectors.toMap(Student::getLastName,
                Student::getFirstName,
                (s1, s2) -> s1.compareTo(s2) < 0 ? s1 : s2));
    }


    private List<Group> getGroups(Collection<Student> students, Comparator<Student> comparator) {
        return  students.stream().collect(
                Collectors.groupingBy(
                        Student::getGroup, Collectors.toList()
                ))
                .entrySet().stream().map(entry -> new Group(entry.getKey(), entry.getValue().
                stream().sorted(comparator).collect(Collectors.toList())))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroups(students, COMPARATOR_BY_NAME);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroups(students, Comparator.comparing(Student::getId));
    }

    private <A, R> GroupName getNameOfGroup(Collection<Student> students, Collector<Student, A, R> collector,
                                            Comparator<Map.Entry<GroupName, R>> comparator) {
        return  students.stream().collect(Collectors.groupingBy(
                Student::getGroup, collector
        )).entrySet().stream().max(comparator).map(Map.Entry::getKey).orElse(null);
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return getNameOfGroup(students,
                Collectors.counting(),
                (entry1, entry2) ->
                        entry1.getValue().equals(entry2.getValue())
                        ? entry1.getKey().compareTo(entry2.getKey()) :
                        entry1.getValue().compareTo(entry2.getValue()
                        )
                );
    }


    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return getNameOfGroup(students, Collectors.mapping(Student::getFirstName, Collectors.toSet()),
                (entry1, entry2) -> entry1.getValue().size() == entry2.getValue().size()
        ? entry2.getKey().compareTo(entry1.getKey()) : Long.compare(entry1.getValue().size(),
                entry2.getValue().size()));
    }
}

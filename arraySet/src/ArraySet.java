import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private static class ReversedSet<T> extends AbstractList<T> implements RandomAccess {
        private final List<T> list;
        private final Boolean reversed;

        private ReversedSet(Collection<T> list, Boolean reversed) {
            this.list = new ArrayList<>(list);
            this.reversed = reversed;
        }

        private ReversedSet(Collection<T> list) {
            this.reversed = false;
            this.list = new ArrayList<>(list);
        }

        private ReversedSet(ReversedSet<T> reversedSet, Boolean reversed) {
            this.list = reversedSet.list;
            this.reversed = reversedSet.reversed ^ reversed;
        }

        public int getIndex(int pos) {
            return reversed ? size() - 1 - pos : pos;
        }

        @Override
        public T get(int pos) {
            return list.get(getIndex(pos));
        }

        @Override
        public Iterator<T> iterator() {
            return Collections.unmodifiableList(list).iterator();
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public ReversedSet<T> subList(int ind1, int ind2) {
            if (ind1 > ind2) {
                throw new IllegalArgumentException("illegal arguments");
            }
            if (reversed) {
                return new ReversedSet<>(list.subList(getIndex(ind2 - 1), getIndex(ind1) + 1), true);
            }
            return new ReversedSet<>(list.subList(ind1, ind2), false);
        }
    }
    private final ReversedSet<E> currSet;
    private final Comparator<? super E> comparator;


    public ArraySet(Collection<? extends E> elements, Comparator<? super E> comparator) {
        SortedSet<E> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(elements);
        currSet = new ReversedSet<>(treeSet);
        this.comparator = comparator;
    }

    public ArraySet(Collection<? extends E> elements) {
        this.comparator = null;
        this.currSet = new ReversedSet<>(new TreeSet<>(elements));
    }

    public ArraySet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.currSet = new ReversedSet<>(new ArrayList<>());
    }

    public ArraySet() {
        this.currSet = new ReversedSet<>(new ArrayList<>());
        this.comparator = null;
    }


    @Override
    public E pollFirst() {
        if (currSet.isEmpty()) {
            throw new NoSuchElementException("set is empty");
        }
        return currSet.get(0);
    }

    @Override
    public E pollLast() {
        if (currSet.isEmpty()) {
            throw new NoSuchElementException("set is empty");
        }
        return currSet.get(size() - 1);
    }

    @Override
    public Iterator<E> iterator() {
        return currSet.iterator();
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("invalid operation");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("invalid operation");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("invalid operation");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("invalid operation");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("invalid operation");
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(new ReversedSet<>(currSet, true),
                Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    public int index(E e) {
        return Collections.binarySearch(currSet, Objects.requireNonNull(e), comparator);
    }

    public int getLower(E e, boolean inclusive) {
        int ind = index(e);
        if (ind < 0) {
            return -ind - 1;
        }
        return inclusive ? ind : ind + 1;
    }

    public int getUpper(E e, boolean inclusive) {
        int ind = index(e);
        if (ind < 0) {
            return -ind - 2;
        }
        return inclusive ? ind : ind - 1;
    }


    public NavigableSet<E> getSubset(E element1, boolean b1, E element2, boolean b2) {
        int from = getLower(element1, b1);
        int to = getUpper(element2, b2) + 1;
        if (from >= to) {
            return new ArraySet<>(comparator);
        }
        return new ArraySet<>(currSet.subList(from, to), comparator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NavigableSet<E> subSet(E element1, boolean b1, E element2, boolean b2) {
        int comp = comparator == null ? ((Comparable<E>) element1).compareTo(element2) :
                comparator.compare(element1, element2);
        if (comp > 0) {
            throw new IllegalArgumentException("first is bigger than second");
        }
        return getSubset(element1, b1, element2, b2);
    }

    @Override
    public NavigableSet<E> headSet(E e, boolean b) {
        if (currSet.isEmpty()) {
            return new ArraySet<>(comparator);
        }
        return getSubset(first(), true, e, b);
    }

    @Override
    public NavigableSet<E> tailSet(E e, boolean b) {
        if (currSet.isEmpty()) {
            return new ArraySet<>(comparator);
        }
        return getSubset(e, b, last(), true);
    }

    @Override
    public int size() {
        return currSet.size();
    }

    private E check(int ind) {
        if (ind < 0 || ind >= size()) {
            return null;
        }
        return currSet.get(ind);
    }

    @Override
    public E lower(E e) {
        return check(getUpper(e, false));
    }

    @Override
    public E floor(E e) {
        return check(getUpper(e, true));
    }

    @Override
    public E ceiling(E e) {
        return check(getLower(e, true));
    }

    @Override
    public E higher(E e) {
        return check(getLower(e, false));
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object object) {
        return Collections.binarySearch(currSet, (E) Objects.requireNonNull(object), comparator) >= 0;
    }

    @Override
    public SortedSet<E> subSet(E e, E e1) {
        return subSet(e, true, e1, false);
    }

    @Override
    public SortedSet<E> headSet(E e) {
        return headSet(e, false);
    }

    @Override
    public SortedSet<E> tailSet(E e) {
        return tailSet(e, true);
    }

    @Override
    public E first() {
        if (currSet.isEmpty()) {
            throw new NoSuchElementException("no elements in the set");
        }
        return currSet.get(0);
    }

    @Override
    public E last() {
        if (currSet.isEmpty()) {
            throw new NoSuchElementException("no elements in the set");
        }
        return currSet.get(size() - 1);
    }
}

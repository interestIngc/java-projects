package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private final int LENGTH = 10;
    private Object[] elements = new Object[LENGTH];

    private void ensureCapacity(int capacity) {
        if (capacity == elements.length) {
            elements = getElements(2 * size);
            head = 0;
        }
    }

    private Object[] getElements(int size) {
        Object[] newElements = new Object[size];
        int tail = (head + this.size) % elements.length;
        if (tail >= head) {
            System.arraycopy(elements, head, newElements, 0, tail - head);
        } else {
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, tail);
        }
        return newElements;
    }

    @Override
    protected void enqueueImpl(Object x) {
        int capacity = size + 1;
        ensureCapacity(capacity);
        int tail = (head + size) % elements.length;
        elements[tail] = x;
        size++;
    }
    @Override
    public Object dequeueImpl() {
        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }
    @Override
    public Object elementImpl() {
        return elements[head];
    }
    @Override
    public void clear() {
        head = 0;
        size = 0;
        elements = new Object[LENGTH];
    }
    @Override
    protected Queue newQueue(final Function<Object, Object> function, final Predicate<Object> predicate) {
        ArrayQueue newQueue = new ArrayQueue();
        for (int i = 0; i < size; i++) {
            Object element = elements[(i + head) % elements.length];
            if (predicate.test(element)) {
                newQueue.enqueue(function.apply(element));
            }
        }
        return newQueue;
    }
}
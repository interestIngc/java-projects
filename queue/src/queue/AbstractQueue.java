package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Object x) {
        assert x != null;
        enqueueImpl(x);
    }

    public Object dequeue() {
        assert size > 0;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    protected abstract void enqueueImpl(Object x);

    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    abstract void clear();
    protected abstract Queue newQueue(Function<Object, Object> function, Predicate<Object> predicate);

    @Override
    public Queue filter(final Predicate<Object> predicate) {
        Function<Object, Object> function = x -> x;
        return newQueue(function, predicate);
    }

    @Override
    public Queue map(final Function<Object, Object> function) {
        Predicate<Object> predicate = x -> true;
        return newQueue(function, predicate);
    }
}
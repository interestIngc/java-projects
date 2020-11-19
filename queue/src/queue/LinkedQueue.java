package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    protected Node head;
    protected Node tail;

    protected void enqueueImpl(Object newElement) {
        Node element = tail;
        tail = new Node(newElement, null);
        if (size == 0) {
            head = tail;
        } else {
            element.next = tail;
        }
        size++;
    }

    @Override
    protected Object dequeueImpl() {
        Object result = head.value;
        head = head.next;
        size--;
        return result;
    }

    @Override
    protected Object elementImpl() {
        return head.value;
    }
    @Override
    public void clear() {
        head = tail;
        size = 0;
    }
    @Override
    protected Queue newQueue(Function<Object, Object> function, Predicate<Object> predicate) {
        LinkedQueue newQueue = new LinkedQueue();
        Node i = head;
        while (i != null) {
            if (predicate.test(i.value)) {
                newQueue.enqueue(function.apply(i.value));
            }
            i = i.next;
        }
        return newQueue;
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }
}

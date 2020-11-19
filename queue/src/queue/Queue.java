package queue;
import java.util.function.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

// n - size(queue);
// INV: n > = 0 && for all 0 <= i < n queue[i] != null;
public interface Queue {

    // PRE: x != null
    // POST: queue[n]' = x; for all i >= 0, i < n, queue[i]' = queue[i];
    void enqueue(Object x);

    // PRE: true
    // POST: result = n && for all 0<= i < n queue[i]' = queue[i];
    int size();

    // PRE: n > 0;
    // POST: for all 0 <= i < n - 1 queue[i]' = queue[i + 1], result = queue[0];
    Object dequeue();

    // PRE: true
    // POST: (for all 0 <= i < n queue[i]' = queue[i]), (res == true && n ==0, || res == false && n > 0)
    boolean isEmpty();

    // PRE: n > 0
    // POST: result = queue[0] && for all 0 <= i < n queue[i]' = queue[i];
    Object element();

    // PRE: predicate(queue) != null
    // POST: queue' = queue,  for all 0 <= ij < n && 0 <= j < len(result) predicate(queue[ij]) == true &&
    // result[j] = queue[ij]
    Queue filter(final Predicate<Object> predicate);

    // PRE: queue != null, function(queue) != null
    // POST: result[i] = function(queue[i]) for all 0<=i < n, queue' = queue;
    Queue map(final Function<Object, Object> function);
}
package receiver;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
    private Queue<Double> queue;
    private int           maxSize;
    private double        sum;

    public MovingAverage(int size) {
        queue = new LinkedList<>();
        maxSize = size;
        sum = 0.0;
    }

    public double next(double val) {
        if (queue.size() == maxSize) {
            sum -= queue.remove();
        }
        queue.add(val);
        sum += val;
        return sum / queue.size();
    }

}

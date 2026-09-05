import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Alternate {
    private static final Lock LOCK = new ReentrantLock();
    private static final Condition PRINT_DONE = LOCK.newCondition();
    private static final int WAIT = 200;

    private static int lastNumber = 2;

    private static class AlternativeTask implements Runnable {
        private final int order;

        public AlternativeTask(int order) {
            this.order = order;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) WAIT * order);
                while (true) {
                    LOCK.lock();
                    assert lastNumber != order : "Order error";
                    System.out.println(order);
                    lastNumber = order;
                    PRINT_DONE.signal();
                    PRINT_DONE.await();
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } finally {
                LOCK.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Thread first = new Thread(new AlternativeTask(1));
        Thread second = new Thread(new AlternativeTask(2));
        first.start();
        second.start();
    }
}
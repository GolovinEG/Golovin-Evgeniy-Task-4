import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {
    private static final int WAIT = 200;
    private static final Lock LOCK_ONE = new ReentrantLock();
    private static final Lock LOCK_TWO = new ReentrantLock();

    private static class LockTask implements Runnable {
        private final int order;

        public LockTask(int order) {
            this.order = order;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) WAIT * order);
                getLock(1).lock();
                System.out.printf("Thread %d acquired LOCK_%s%n", order, order == 1 ? "ONE" : "TWO");
                Thread.sleep(WAIT);
                getLock(2).lock();
                System.out.printf("Thread %d acquired LOCK_%s%n", order, order == 2 ? "ONE" : "TWO");
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } finally {
                LOCK_ONE.unlock();
                LOCK_TWO.unlock();
            }
        }

        private Lock getLock(int lockNumber) {
            return order == lockNumber ? LOCK_ONE : LOCK_TWO;
        }
    }

    public static void main(String[] args) {
        Thread lockOne = new Thread(new LockTask(1));
        Thread lockTwo = new Thread(new LockTask(2));
        lockOne.start();
        lockTwo.start();
    }
}
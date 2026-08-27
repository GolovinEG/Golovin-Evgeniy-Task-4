import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Alternate {
    private static final Lock LOCK = new ReentrantLock();
    private static final Thread FIRST = new Thread() {
        @Override
        public void run() {
            try {
                while (true) {
                    if (LOCK.tryLock(200, TimeUnit.MILLISECONDS)) {
                        System.out.println(1);
                        LOCK.unlock();
                        sleep(300);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };
    private static final Thread SECOND = new Thread() {
        @Override
        public void run() {
            try {
                sleep(100);
                while (true) {
                    if (LOCK.tryLock(200, TimeUnit.MILLISECONDS)) {
                        System.out.println(2);
                        LOCK.unlock();
                        sleep(300);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static void main(String[] args) {
        FIRST.start();
        SECOND.start();
    }
}

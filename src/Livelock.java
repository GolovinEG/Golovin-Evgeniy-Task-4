import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Livelock {
    private static final Lock LOCK_ONE = new ReentrantLock();
    private static final Lock LOCK_TWO = new ReentrantLock();

    private static final Thread FIRST = new Thread() {
        @Override
        public void run() {
            try {
                while (true) {
                    if (LOCK_ONE.tryLock()) {
                        System.out.println("LOCK_ONE acquired by thread FIRST");
                        sleep(1000);
                        if (LOCK_TWO.tryLock()) {
                            System.out.println("LOCK_TWO acquired by thread FIRST");
                            LOCK_ONE.unlock();
                            LOCK_TWO.unlock();
                            break;
                        }
                        LOCK_ONE.unlock();
                        System.out.println("LOCK_ONE released by thread FIRST");
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
                sleep(500);
                while (true) {
                    if (LOCK_TWO.tryLock()) {
                        System.out.println("LOCK_TWO acquired by thread SECOND");
                        sleep(1000);
                        if (LOCK_ONE.tryLock()) {
                            System.out.println("LOCK_ONE acquired by thread SECOND");
                            LOCK_ONE.unlock();
                            LOCK_TWO.unlock();
                            break;
                        }
                        LOCK_TWO.unlock();
                        System.out.println("LOCK_TWO released by thread SECOND");
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

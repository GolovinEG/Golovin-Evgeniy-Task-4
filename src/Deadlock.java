import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {
    private static final Lock LOCK_ONE = new ReentrantLock();
    private static final Lock LOCK_TWO = new ReentrantLock();

    private static final Thread FIRST = new Thread() {
      @Override
      public void run() {
          try {
              LOCK_ONE.lock();
              System.out.println("LOCK_ONE acquired by thread FIRST");
              sleep(1000);
              LOCK_TWO.lock();
              System.out.println("LOCK_TWO acquired by thread FIRST");
              LOCK_ONE.unlock();
              LOCK_TWO.unlock();
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
      }
    };

    private static final Thread SECOND = new Thread() {
        @Override
        public void run() {
            try {
                LOCK_TWO.lock();
                System.out.println("LOCK_TWO acquired by thread SECOND");
                sleep(1000);
                LOCK_ONE.lock();
                System.out.println("LOCK_ONE acquired by thread SECOND");
                LOCK_ONE.unlock();
                LOCK_TWO.unlock();
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

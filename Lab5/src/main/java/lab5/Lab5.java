package lab5;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lab5 {
    static int counter = 0;
    static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) throws IOException, InterruptedException {

        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        int maxCount = 5;
        FileWriter f = new FileWriter("test.txt");

        Thread t1 = new Thread(() -> {
            while (counter <= maxCount) {
                lock.writeLock().lock();
                try {
                    synchronized (f) {
                        counter++;
                        f.write("Thread " + Thread.currentThread().getName() + ", Time " + time.format(LocalTime.now()) + ", counter=" + counter + "\n");
                        lock.writeLock().unlock();
                        Thread.sleep(250);
                    }
                } catch (InterruptedException e) {
                } catch (IOException ex) {
                    Logger.getLogger(Lab5.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Thread t2 = CreateThread(500, maxCount, f, time);
        Thread t3 = CreateThread(1000, maxCount, f, time);

        t1.setName("Перший");
        t2.setName("Другий");
        t3.setName("Третій");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
        System.out.println("THE END");
        f.close();
    }

    static Thread CreateThread(int sleep, int maxCount, FileWriter f, DateTimeFormatter time){
        return new Thread(() -> {
            while (counter < maxCount) {
                lock.writeLock().lock();
                try {
                    synchronized (f) {
                        f.write("Thread " + Thread.currentThread().getName() + ", Time " + time.format(LocalTime.now()) + ", counter=" + counter + "\n");
                        lock.writeLock().unlock();
                        Thread.sleep(sleep);
                    }
                } catch (InterruptedException e) {
                } catch (IOException ex) {
                    Logger.getLogger(Lab5.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

public class Main {
    private static Lock unit_mutex = new ReentrantLock();
    private static Lock cout_mutex = new ReentrantLock();
    private static int total_gold = 0;
    private static int units_count = 0;
    private static final int unit_gold = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        System.out.print("Введите сколько золота в шахте: ");
        total_gold = scanner.nextInt();
        System.out.println();
        System.out.print("Введите количество юнитов: ");
        units_count = scanner.nextInt();
        System.out.println();
        ArrayList<Thread> thread_group = new ArrayList<>();
        for (int i = 0; i < units_count; ++i) {
            thread_group.add(new Thread(() -> {
                int unit_id = Thread.currentThread().hashCode();
                print("Юнит " + unit_id + " начал добычу");
                for (;;) {
                    unit_mutex.lock();
                    try {
                        if (total_gold < unit_gold) {
                            total_gold = 0;
                        }
                        if (total_gold == 0) {
                            print("Больше нет золотв " + unit_id + " закончил ");
                            break;
                        }
                        total_gold -= unit_gold;
                        print("Осталось золота: " + total_gold);
                    } finally {
                        unit_mutex.unlock();
                    }
                    try {
                        Thread.sleep(rand.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        for (Thread t : thread_group) {
            t.start();
        }
        for (Thread t : thread_group) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        scanner.nextLine();
        scanner.nextLine();
    }

    private static void print(String msg) {
        // just avoid interlaced out
        cout_mutex.lock();
        try {
            System.out.println(msg);
        } finally {
            cout_mutex.unlock();
        }
    }
}
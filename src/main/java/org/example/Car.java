package org.example;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static final AtomicInteger atomicWinnersCount;
    private static final Lock lock;
    private static CyclicBarrier barrier;
    private static int carsCount;

    static {
        carsCount = 0;
        lock = new ReentrantLock();
        atomicWinnersCount = new AtomicInteger(0);
    }

    private final String name;
    private final Race race;
    private final int speed;

    public Car(Race race, int speed, CyclicBarrier barrier) {
        this.race = race;
        this.speed = speed;
        this.name = "Участник #" + ++carsCount;
        this.barrier = barrier;
    }


    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится 🕐");
            Thread.sleep(500 + (int) (Math.random() * 800));

            System.out.println(this.name + " готов ✅");
            barrier.await();

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);

                if (i < race.getStages().size() - 1) {
                    Main.outLock.unlock();
                }
            }

            lock.lock();

            int winnerNumber = atomicWinnersCount.incrementAndGet();

            if (winnerNumber <= 3) {
                System.out.println(this.name + " - ФИНИШИРОВАЛ НА " + winnerNumber + "-th МЕСТЕ");
            }

            Main.outLock.unlock();

            lock.unlock();

            barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

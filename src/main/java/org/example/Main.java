package org.example;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static Lock outLock = new ReentrantLock();
    public static final int CARS_COUNT = 5;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        Race race = new Race(new Road(60), new Tunnel(3), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        CyclicBarrier barrier = new CyclicBarrier(CARS_COUNT + 1);


        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 2), barrier);
        }

        for (Car car : cars) {
            new Thread(car).start();
        }

        try {
            barrier.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            barrier.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

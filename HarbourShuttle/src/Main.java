/**
 * Designed and written by Damian Coventry
 * Copyright (c) 2022, all rights reserved
 *
 * Massey University
 * 159.355 Concurrent Systems
 * Assignment 1
 * 2022 Semester 1
 *
 */

import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int NUM_PEOPLE_IN_STATION = 10;
    private static final int NUM_SEATS_IN_SHUTTLE = 10;
    private static final int TOTAL_PEOPLE = 100;

    public static void main(String[] args) {
        try {
            Station aucklandStation = new Station(NUM_PEOPLE_IN_STATION, "Auckland");
            Station northShoreStation = new Station(NUM_PEOPLE_IN_STATION, "North Shore");

            Shuttle shuttle = new Shuttle(NUM_SEATS_IN_SHUTTLE, aucklandStation, northShoreStation);

            CountDownLatch peopleDone = new CountDownLatch(TOTAL_PEOPLE);

            for (int i = 0; i < TOTAL_PEOPLE/2; ++i) {
                Thread t = new Thread(new Person(i, peopleDone, shuttle, northShoreStation, aucklandStation));
                t.setName("Person " + i + " Thread");
                t.start();
            }
            for (int i = 0; i < TOTAL_PEOPLE/2; ++i) {
                Thread t = new Thread(new Person(TOTAL_PEOPLE/2 + i, peopleDone, shuttle, aucklandStation, northShoreStation));
                t.setName("Person " + (TOTAL_PEOPLE/2 + i) + " Thread");
                t.start();
            }

            shuttle.startWorkDay(Math.random() > 0.5 ? Shuttle.Location.AUCKLAND : Shuttle.Location.NORTH_SHORE);

            peopleDone.await();

            shuttle.endWorkDay();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

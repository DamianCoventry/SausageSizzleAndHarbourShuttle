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

public class Person implements Runnable {
    private final int _id;
    private final CountDownLatch _done;
    private final Station _homeStation;
    private final Station _otherStation;
    private final Shuttle _shuttle;

    public Person(int id, CountDownLatch done, Shuttle shuttle, Station homeStation, Station otherStation) {
        _id = id;
        _done = done;
        _shuttle = shuttle;
        _homeStation = homeStation;
        _otherStation = otherStation;
    }

    @Override
    public void run() {
        try {
            _homeStation.waitToEnter();
            System.out.println("Person " + _id + " has entered " + _homeStation.getName() + " station. Waiting for shuttle.");

            _homeStation.waitForShuttleArrival();

            System.out.println("Person " + _id + " is looking for an empty seat in shuttle.");
            _shuttle.locateSeat();

            _homeStation.waitForShuttleDeparture();
            System.out.println("Person " + _id + " is travelling to " + _otherStation.getName() + " station.");

            _homeStation.leave();

            System.out.println("Person " + _id + " is waiting for shuttle to arrive at " + _otherStation.getName() + " station.");
            _shuttle.giveUpSeat();
            _otherStation.waitForShuttleArrival();

            System.out.println("Person " + _id + " has exited shuttle at " + _otherStation.getName() + " station.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            _done.countDown();
            System.out.println("Person " + _id + " is done.");
        }
    }
}

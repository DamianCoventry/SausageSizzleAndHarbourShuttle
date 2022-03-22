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
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Shuttle extends Thread {
    private final Semaphore _emptySeats;
    private final Station _aucklandStation;
    private final Station _northShoreStation;
    private CountDownLatch _fullShuttle = new CountDownLatch(1);
    private Location _currentLocation = Location.AUCKLAND;
    private boolean _homeTime = false;

    public Shuttle(int numSeats, Station aucklandStation, Station northShoreStation) {
        setName("Shuttle Thread");
        _emptySeats = new Semaphore(numSeats);
        _aucklandStation = aucklandStation;
        _northShoreStation = northShoreStation;
    }

    public enum Location { NORTH_SHORE, AUCKLAND }

    public void startWorkDay(Location initialLocation) {
        _currentLocation = initialLocation;
        start();
    }

    public synchronized void endWorkDay() throws InterruptedException {
        _homeTime = true;
        join();
    }

    private synchronized boolean isHomeTime() {
        return _homeTime;
    }

    public synchronized void locateSeat() throws InterruptedException {
        if (_emptySeats.tryAcquire()) {
            if (_emptySeats.availablePermits() == 0) {
                _fullShuttle.countDown();
            }
        }
        else {
            _fullShuttle.countDown();
            _emptySeats.acquire();
        }
    }

    public synchronized void giveUpSeat() {
        _emptySeats.release();
    }

    private synchronized void resetFullShuttle() {
        _fullShuttle = new CountDownLatch(1);
    }

    @Override
    public void run() {
        try {
            notifyInitialLocation();

            WaitResult result = waitForHomeTimeOrFullShuttle();
            while (result == WaitResult.FULL_SHUTTLE) {
                resetFullShuttle();
                travelToOtherSide();
                result = waitForHomeTimeOrFullShuttle();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Shuttle has ended its shift. The driver is going home.");
    }

    private enum WaitResult { HOME_TIME, FULL_SHUTTLE }

    private WaitResult waitForHomeTimeOrFullShuttle() throws InterruptedException {
        while (!_fullShuttle.await(250, TimeUnit.MILLISECONDS) ) {
            if (isHomeTime()) {
                return WaitResult.HOME_TIME;
            }
        }
        return WaitResult.FULL_SHUTTLE;
    }

    private void notifyInitialLocation() {
        if (_currentLocation == Location.AUCKLAND) {
            System.out.println("Shuttle is starting at Auckland station");
            _aucklandStation.notifyShuttleArrival();
        }
        else {
            System.out.println("Shuttle is starting at North Shore station");
            _northShoreStation.notifyShuttleArrival();
        }
    }

    private void travelToOtherSide() throws InterruptedException {
        if (_currentLocation == Location.AUCKLAND) {
            System.out.println("Shuttle is departing from Auckland station");
            _aucklandStation.notifyShuttleDeparture();
            _aucklandStation.resetShuttleArrival();
            crossHarbour();

            System.out.println("Shuttle is arriving at North Shore station");
            _currentLocation = Location.NORTH_SHORE;
            _northShoreStation.notifyShuttleArrival();
            _northShoreStation.resetShuttleDeparture();
        }
        else {
            System.out.println("Shuttle is departing from North Shore station");
            _northShoreStation.notifyShuttleDeparture();
            _northShoreStation.resetShuttleArrival();
            crossHarbour();

            System.out.println("Shuttle is arriving at Auckland station");
            _currentLocation = Location.AUCKLAND;
            _aucklandStation.notifyShuttleArrival();
            _aucklandStation.resetShuttleDeparture();
        }
    }

    private void crossHarbour() throws InterruptedException {
        for (int i = 0; i < 10; ++i) {
            System.out.println("Crossing harbour ... \uD83D\uDE8C");
            Thread.sleep(300);
        }
    }
}

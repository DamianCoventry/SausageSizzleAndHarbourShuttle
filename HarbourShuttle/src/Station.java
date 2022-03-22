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

public class Station {
    private final String _name;
    private final Semaphore _emptySpaces;
    private CountDownLatch _shuttleArrival;
    private CountDownLatch _shuttleDeparture;

    public Station(int numEmptySpaces, String name) {
        _name = name;
        _emptySpaces = new Semaphore(numEmptySpaces);
        resetShuttleArrival();
        resetShuttleDeparture();
    }

    public String getName() {
        return _name;
    }

    public void waitToEnter() throws InterruptedException {
        _emptySpaces.acquire();
    }

    public void leave() {
        _emptySpaces.release();
    }

    public void waitForShuttleArrival() throws InterruptedException {
        _shuttleArrival.await();
    }

    public void notifyShuttleArrival() {
        _shuttleArrival.countDown();
    }

    public void resetShuttleArrival() {
        _shuttleArrival = new CountDownLatch(1);
    }

    public void waitForShuttleDeparture() throws InterruptedException {
        _shuttleDeparture.await();
    }

    public void notifyShuttleDeparture() {
        _shuttleDeparture.countDown();
    }

    public void resetShuttleDeparture() {
        _shuttleDeparture = new CountDownLatch(1);
    }
}

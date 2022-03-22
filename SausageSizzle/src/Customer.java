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

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Customer implements Runnable {
    static private final int MIN_SAUSAGES = 1;
    static private final int MAX_SAUSAGES = 3;
    static private final int MIN_WAIT_TIME = 250;
    static private final int MAX_WAIT_TIME = 850;

    private final Random _random = new Random();
    private final CountDownLatch _served;
    private final Barbecue[] _barbecues;
    private final int _id;
    private final int _numSausages;

    public Customer(CountDownLatch served, int id, Barbecue[] barbecues) {
        _served = served;
        _id = id;
        _barbecues = barbecues;
        _numSausages = chooseNumSausages();
    }

    @Override
    public void run() {
        boolean bought = false;
        try {
            while (!bought) {
                for (int i = 0; i < _barbecues.length && !bought; ++i) {
                    bought = _barbecues[i].buyNumSausages(_numSausages);            // <-- synchronized
                }
                waitPatiently();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (bought) {
                System.out.println("Customer " + _id + " buys " + _numSausages + " sausages");
            }
            else {
                System.out.println("Customer " + _id + " was interrupted, so walked away");
            }
            _served.countDown();
        }
    }

    private int chooseNumSausages() {
        return randomInteger(MIN_SAUSAGES, MAX_SAUSAGES);
    }

    private void waitPatiently() throws InterruptedException {
        Thread.sleep(randomInteger(MIN_WAIT_TIME, MAX_WAIT_TIME));
    }

    private int randomInteger(int min, int max) {
        return min + _random.nextInt((max - min) + 1);
    }
}

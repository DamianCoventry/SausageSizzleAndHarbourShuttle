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
    private int _numSausages;
    private int _currentBbq;

    public Customer(CountDownLatch served, int id, Barbecue[] barbecues) {
        _served = served;
        _id = id;
        _currentBbq = 0;
        _barbecues = barbecues;
        _numSausages = chooseNumSausages();
    }

    @Override
    public void run() {
        try {
            while (_numSausages > 0) {
                if (!buySausage()) {
                    tryNextBbq();
                }
                waitPatiently();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Customer " + _id + " has finished buying sausages");
            _served.countDown();
        }
    }

    private boolean buySausage() {
        boolean bought =_barbecues[_currentBbq].buyOneSausage();
        if (bought) {
            --_numSausages;
            System.out.println("Customer " + _id + " bought a sausage from Barbecue " + _currentBbq);
        }
        return bought;
    }

    private void tryNextBbq() {
        _currentBbq = (++_currentBbq % _barbecues.length);
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

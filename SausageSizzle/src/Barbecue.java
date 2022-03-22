import java.util.Random;

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

public class Barbecue extends Thread {
    static private final int MIN_COOKING_TIME = 150;
    static private final int MAX_COOKING_TIME = 1700;

    private final Random _random = new Random();
    private final int _id;
    private boolean _switchOffGas;
    private int _totalSausages = 0;

    public Barbecue(int id) {
        _id = id;
        _switchOffGas = false;
        System.out.println("Barbecue " + _id + " started");
    }

    @Override
    public void run() {
        try {
            while (!mustSwitchOffGas()) {
                cookSausage();
                notifyCookedSausage();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Barbecue " + _id + " stopped");
        }
    }

    private void cookSausage() throws InterruptedException {
        Thread.sleep(MIN_COOKING_TIME + _random.nextInt((MAX_COOKING_TIME - MIN_COOKING_TIME) + 1));
    }

    // Protect the _switchOffGas variable.
    
    synchronized public void switchOffGas() {
        _switchOffGas = true;
    }

    synchronized private boolean mustSwitchOffGas() {
        return _switchOffGas;
    }

    // Protect the _totalSausages variable.

    synchronized public boolean buyNumSausages(int count) {
        if (_totalSausages >= count) {
            _totalSausages -= count;
            return true;
        }
        return false;
    }

    synchronized public int getNumSausages() {
        return _totalSausages;
    }

    synchronized private void notifyCookedSausage() {
        ++_totalSausages;
        System.out.println("Barbecue " + _id + " has another sausage ready (" + _totalSausages + " in total)");
    }
}

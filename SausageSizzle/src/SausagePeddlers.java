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

public class SausagePeddlers {
    static private final int NUM_BARBECUES = 2;

    private final Barbecue[] _barbecues = new Barbecue[NUM_BARBECUES];

    public SausagePeddlers() {
        for (int i = 0; i < NUM_BARBECUES; ++i) {
            _barbecues[i] = new Barbecue(i);
            _barbecues[i].start();
        }
    }

    public Barbecue[] getBarbecues() {
        return _barbecues;
    }

    public void goHome() throws InterruptedException {
        int totalSausages = 0;
        for (var bbq : _barbecues) {
            bbq.switchOffGas();                         // <-- synchronized
            bbq.join();
            totalSausages += bbq.getNumSausages();      // <-- synchronized
        }
        System.out.println("There are " + totalSausages + " sausages remaining.");
    }
}

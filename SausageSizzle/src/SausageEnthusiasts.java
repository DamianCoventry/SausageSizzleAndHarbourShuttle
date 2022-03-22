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

public class SausageEnthusiasts {
    static private final int NUM_CUSTOMERS = 100;

    private final CountDownLatch _served;

    public SausageEnthusiasts(Barbecue[] barbecues) {
        _served = new CountDownLatch(NUM_CUSTOMERS);

        for (int i = 0; i < NUM_CUSTOMERS; ++i) {
            Thread customer = new Thread(new Customer(_served, i, barbecues));
            customer.start();
        }
    }

    public void waitUntilAllServed() throws InterruptedException {
        _served.await();
        System.out.println("All customers have been served.");
    }
}

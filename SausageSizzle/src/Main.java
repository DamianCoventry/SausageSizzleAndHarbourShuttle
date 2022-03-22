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

public class Main {
    public static void main(String[] args) {
        try {
            var peddlers = new SausagePeddlers();
            var enthusiasts = new SausageEnthusiasts(peddlers.getBarbecues());
            enthusiasts.waitUntilAllServed();
            peddlers.goHome();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

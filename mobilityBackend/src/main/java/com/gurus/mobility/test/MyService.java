package com.gurus.mobility.test;

public class MyService {

    public int performTimeConsumingOperation() {
        try {
            Thread.sleep(100); // Simulate a 100 ms operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 42; // Return a result
    }

}

package com.gurus.mobility.test;

import org.junit.Test;
import org.mockito.Mockito;

public class PerformanceTest {

    @Test
    public void testPerformance() {
        // creation mock objet pour simulation
        MyService myServiceMock = Mockito.mock(MyService.class);

        long startTime = System.currentTimeMillis();
        //appel méthode de perfermance temps
        int result = myServiceMock.performTimeConsumingOperation();

        long endTime = System.currentTimeMillis();

        // Calcul l'execution temps
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " ms");

        long performanceThreshold = 200; // 200 ms
        assert executionTime < performanceThreshold;
    }
}

package com.xebia.loom;


import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LoomTest {

    @Test
    public void aTest() throws InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadExecutor()) {

            // Submits a value-returning task and waits for the result
            Future<String> future = executor.submit(() -> "foo");
            String result = future.join();
           assertEquals("foo", result);
        }
        Thread thread = Thread.startVirtualThread(() -> System.out.println("Hello"));
        thread.join();


        var queue = new SynchronousQueue<String>();

        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(Duration.ofSeconds(2));
                queue.put("done");
            } catch (InterruptedException e) { }
        });

        String msg = queue.take();
    }
}

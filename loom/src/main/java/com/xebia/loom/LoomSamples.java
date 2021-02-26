package com.xebia.loom;

import java.time.Duration;
import java.util.concurrent.SynchronousQueue;

public class LoomSamples {

    public static void main(String[] args) throws InterruptedException {
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

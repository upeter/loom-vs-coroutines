package com.xebia.loom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

public class LoomSamples {


    public static void example1BasicVirtualThread() {
        Thread thread = Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Hello Virtual Thread");
        });
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void example2StructuredConcurrency() {
        var deadline = Instant.now().plusSeconds(2);
        try (ExecutorService executor1 = Executors.newVirtualThreadExecutor().withDeadline(deadline)) {
            try (ExecutorService executor2a = Executors.newVirtualThreadExecutor()) {
                executor2a.submit(() -> System.out.println("other async tasks"));
            }
            try (ExecutorService executor2b = Executors.newVirtualThreadExecutor()) {
                Future<String> future1 = executor2b.submit(() -> "task sub 1");
                Future<String> future2 = executor2b.submit(() -> "task sub 2");
                System.out.println(future1.get() + " - " + future2.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static void example3Queue1() {
        var queue = new SynchronousQueue<String>();
        try (ExecutorService executor1 = Executors.newVirtualThreadExecutor()) {
            executor1.submit(() -> {
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            executor1.submit(() -> {
                try {
                    queue.put("done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public static void example3Queue2() {
        var queue = new SynchronousQueue<String>();
        var t1 = Thread.startVirtualThread(() -> {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        try {
            queue.put("done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private final static Logger LOGGER = LoggerFactory.getLogger("Coroutines");

    public static void main(String[] args) throws InterruptedException {
        example1BasicVirtualThread();
        example2StructuredConcurrency();
        example3Queue1();
        example3Queue2();
    }


}

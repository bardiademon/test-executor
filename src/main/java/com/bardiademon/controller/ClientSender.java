package com.bardiademon.controller;


import com.bardiademon.util.Logger;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ClientSender {
    private static final Logger logger = new Logger(ClientSender.class);

    private final static ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(10);
    }

    public static void sender(final OutputStream outputStream , final String message) {
        logger.trace("Sender running: {}" , message);
        executorService.submit(() -> {
            final PrintWriter writer = new PrintWriter(outputStream , true);
            writer.println(message);
            logger.trace("Successfully sent message: {}" , message);
        });
    }

    public static void die() {
        executorService.shutdown();
    }
}

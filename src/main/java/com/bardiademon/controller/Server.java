package com.bardiademon.controller;

import com.bardiademon.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server {

    private static final Logger logger = new Logger(Server.class);

    private final ExecutorService executorService;

    public Server() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::runServer);
    }

    private void runServer() {
        logger.trace("Successfully run server");
    }

    public void die() {
        executorService.shutdown();
        logger.warn("Shutdown server thread");
    }
}

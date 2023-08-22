package com.bardiademon;

import com.bardiademon.controller.Server;
import com.bardiademon.util.DbConnection;
import com.bardiademon.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Main {

    private static final Logger logger = new Logger(Main.class);

    private static ExecutorService executorService;
    private static Server server;

    public static void main(final String[] args) {
        logger.info("Starting server...");
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            DbConnection.connect().thenAccept(connection -> {
                logger.trace("Successfully connected database!");
                Runtime.getRuntime().addShutdownHook(new Thread(Main::die));
                server = new Server();
            }).exceptionally(fail -> {
                logger.error("Fail to connect database");
                executorService.shutdown();
                return null;
            });
        });
    }

    public static void die() {
        executorService.shutdown();
        logger.warn("Shutdown main thread");
        server.die();
    }
}
package com.bardiademon.controller;

import com.bardiademon.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server {

    private static final Logger logger = new Logger(Server.class);

    private ServerSocket serverSocket;
    private ExecutorService executorClient;
    private final ExecutorService executorService;

    private final Set<ClientReceiver> clientReceivers;

    public Server() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::runServer);
        clientReceivers = new HashSet<>();
    }

    private void runServer() {
        logger.trace("Successfully run server");
        try {
            serverSocket = new ServerSocket(8888);
            executorClient = Executors.newScheduledThreadPool(50);
            while (true) {
                final Socket accept = serverSocket.accept();
                executorClient.submit(() -> clientReceivers.add(ClientReceiver.receiver(accept)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void die() {
        executorService.shutdown();
        executorClient.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.error("Fail to close server" , e);
        }
        clientReceivers.forEach(ClientReceiver::die);
        logger.warn("Shutdown server thread");
    }
}

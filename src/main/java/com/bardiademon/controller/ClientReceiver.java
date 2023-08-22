package com.bardiademon.controller;

import com.bardiademon.Main;
import com.bardiademon.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientReceiver {

    private static final Logger logger = new Logger(ClientReceiver.class);

    private final Socket client;

    private final ExecutorService executorService;

    private final InputStream inputStream;
    private final OutputStream outputStream;

    private boolean die;

    private ExecutorService receiverManagerExecutor;

    private ClientReceiver(final Socket client) throws IOException {
        this.client = client;
        inputStream = client.getInputStream();
        outputStream = client.getOutputStream();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static ClientReceiver receiver(final Socket client) throws IOException {
        final ClientReceiver clientReceiver = new ClientReceiver(client);
        clientReceiver.executorService.submit(clientReceiver::manager);
        return clientReceiver;
    }

    private void manager() {
        receiverManager();
    }

    private void receiverManager() {
        receiverManagerExecutor = Executors.newFixedThreadPool(10);
        receiverManagerExecutor.execute(() -> {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (!die) {
                try {
                    final String line = reader.readLine();
                    if (line == null) {
                        continue;
                    }
                    receiverManagerExecutor.submit(() -> handlerReceiverMessage(line));
                } catch (IOException e) {
                    logger.error("Fail to read line" , e);
                    die();
                }
            }
        });
    }

    private void handlerReceiverMessage(final String message) {
        logger.trace("Receive message: {}" , message);

        if (message.contains("Hi")) {
            ClientSender.sender(outputStream , "Hi, client!");
            return;
        }

        ClientSender.sender(outputStream , "Powered by bardiademon");

    }

    public void die() {
        die = true;
        try {
            client.close();
        } catch (IOException e) {
            logger.error("Fail to close client" , e);
        }
        executorService.shutdown();
        receiverManagerExecutor.shutdown();
        ClientSender.die();
    }
}


package test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(final String[] args) throws IOException {

        final Socket socket = new Socket("localhost" , 8888);

        final OutputStream outputStream = socket.getOutputStream();

        final PrintWriter writer = new PrintWriter(outputStream , true);
        writer.println("Hi, Server");

        final InputStream inputStream = socket.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    System.out.println("Message: " + line);
                    break;
                }
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            executorService.shutdown();
        });


    }
}

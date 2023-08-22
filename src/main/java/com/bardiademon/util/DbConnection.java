package com.bardiademon.util;

import com.bardiademon.data.model.DbConnectionInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DbConnection {

    public static CompletableFuture<Connection> connect() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        return CompletableFuture.supplyAsync(() -> {
            try {
                final String url = String.format("jdbc:mysql://%s:%d/%s" , DbConnectionInfo.HOST , DbConnectionInfo.PORT , DbConnectionInfo.DATABASE_NAME);
                return DriverManager.getConnection(url , DbConnectionInfo.USERNAME , DbConnectionInfo.PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
        } , executorService);
    }

}

package com.bardiademon.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Logger {
    private final Class<?> aClass;

    public Logger(final Class<?> aClass) {
        this.aClass = aClass;
    }

    public void info(final String log , final Object... params) {
        log("info" , "\u001B[37m" , log , params);
    }

    public void trace(final String log , final Object... params) {
        log("trace" , "\u001B[35m" , log , params);
    }

    public void warn(final String log , final Object... params) {
        log("warn" , "\u001B[33m" , log , params);
    }

    public void error(final String log , final Object... params) {
        log("error" , "\u001B[31m" , log , params);
    }

    private void log(final String type , final String color , final String log , final Object... params) {
        final StringBuilder logBuilder = new StringBuilder(log);
        if (!logBuilder.isEmpty()) {
            for (Object param : params) {
                final int indexOf = log.indexOf("{}");
                logBuilder.replace(indexOf , indexOf + 2 , param == null ? "null" : param.toString());
            }
        }

        final String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        final String className = aClass.getSimpleName();

        System.out.printf("%s%s: %s ---- %s ---> %s\n" , color , type.toUpperCase(Locale.ROOT) , nowTime , className , logBuilder);
    }

}

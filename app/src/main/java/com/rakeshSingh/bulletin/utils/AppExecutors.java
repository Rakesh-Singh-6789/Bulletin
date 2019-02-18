package com.rakeshSingh.bulletin.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private final Executor executor;
    private static AppExecutors instance;

    AppExecutors(Executor executor) {
        this.executor = executor;
    }

    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors(Executors.newSingleThreadExecutor());
            return instance;
        }
        return instance;
    }

    public Executor getExecutor() {
        return executor;
    }
}

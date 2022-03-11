package com.example.go4lunch.data.di;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by JeroSo94 on 09/03/2022.
 */
public class ExecutorModule {
    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }
}

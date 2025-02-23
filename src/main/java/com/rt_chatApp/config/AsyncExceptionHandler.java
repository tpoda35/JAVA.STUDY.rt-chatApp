package com.rt_chatApp.config;

import com.rt_chatApp.controller.ExceptionController;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final ExceptionController exceptionController;

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        exceptionController.globalExceptionHandler((Exception) ex);
    }
}

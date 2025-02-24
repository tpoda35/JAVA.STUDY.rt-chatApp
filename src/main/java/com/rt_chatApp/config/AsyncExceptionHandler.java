package com.rt_chatApp.config;

import com.rt_chatApp.controller.ExceptionController;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Asynchronous exception handler that delegates uncaught exceptions
 * to the global exception controller.
 *
 * <p>This class implements {@link AsyncUncaughtExceptionHandler} to ensure that any
 * uncaught exceptions in asynchronous methods are handled by passing
 * them to the global exception handler.</p>
 */

@RequiredArgsConstructor
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final ExceptionController exceptionController;

    /**
     * Handles uncaught exceptions thrown by asynchronous methods.
     *
     * <p>This method delegates the exception handling to the global exception handler
     * provided by {@code ExceptionController}. It receives the exception, the method where
     * the exception was thrown, and the parameters that were passed to that method.</p>
     *
     * @param ex the exception that was thrown during asynchronous execution
     * @param method the method in which the exception occurred
     * @param params the parameters passed to the method when the exception was thrown
     */
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        exceptionController.globalExceptionHandler((Exception) ex);
    }
}

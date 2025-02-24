package com.rt_chatApp.config;

import com.rt_chatApp.controller.ExceptionController;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.Executor;

/**
 * Custom asynchronous threads configuration.
 *
 * <p>This configuration enables asynchronous processing, transaction management,
 * and integrates security context propagation into async tasks.</p>
 */

@Configuration
@EnableAsync
@EnableTransactionManagement
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    private final ExceptionController exceptionController;

    /**
     * Configures the async executor with a fixed thread pool and security context delegation.
     *
     * @return an Executor that supports asynchronous operations with security context propagation.
     */

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setQueueCapacity(150);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    /**
     * Redirects the uncaught exceptions to the global handler.
     *
     * @return AsyncExceptionHandler with the global exception handler.
     */

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler(exceptionController);
    }
}

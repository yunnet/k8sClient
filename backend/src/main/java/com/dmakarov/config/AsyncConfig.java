package com.dmakarov.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig {
  private static final String THREAD_NAME_PREFIX = "async-pool-thread-";
  private final AtomicInteger threadNumber = new AtomicInteger(1);

  /**
   * Creates executor bean to be used in async operations.
   */
  @Bean("asyncExecutor")
  public Executor getAsyncExecutor() {
    return Executors.newCachedThreadPool(
        runnable -> new Thread(runnable, THREAD_NAME_PREFIX + threadNumber.getAndIncrement()));
  }

}

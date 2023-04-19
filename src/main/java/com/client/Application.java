package com.client;

import com.harness.harness.HarnessConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * This is a sample program that demonstrates a simple integration of the
 * ff-java-server-sdk with Harness Feature Flags
 */
@SpringBootApplication
@Import(HarnessConfig.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}

package com.harness.harness;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration to enable hysterix and AOP
 *
 * @author dtamboli
 */
@Configuration
@EnableCircuitBreaker
@EnableAspectJAutoProxy
public class HarnessConfig {
}

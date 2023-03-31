package com.harness.harness;

import com.harness.annotations.FeatureFlag;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.harness.cf.client.api.BaseConfig;
import io.harness.cf.client.api.CaffeineCache;
import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.api.FeatureFlagInitializeException;
import io.harness.cf.client.dto.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Client for Harness Feature Flag
 *
 * @author dtamboli
 */
@Component
public class HarnessClient {

    /**
     * Harness API SDK Key
     */
    @Value("${harness.sdkKey}")
    String apiKey;

    /**
     * CF Client
     */
    CfClient cfClient;

    private static final Logger logger = LoggerFactory.getLogger(HarnessClient.class);

    /**
     *
     */
    private Properties properties;

    public HarnessClient() throws Exception {
        initializeProperties();
    }

    /**
     * Initializes Properties locator to get feature flag value in case of Harness not available
     *
     * @throws IOException
     */
    private void initializeProperties() throws IOException {
        properties = new Properties();
        File file = ResourceUtils.getFile("classpath:featureflags.properties");
        InputStream stream = new FileInputStream(file);
        properties.load(stream);
    }

    /**
     * Initializes Harness CF Client (haarness SDK)
     *
     * @throws FeatureFlagInitializeException
     * @throws InterruptedException
     */
    private void initializeClient() throws FeatureFlagInitializeException, InterruptedException {
        // Create Options
        BaseConfig options = BaseConfig.builder()
                .pollIntervalInSeconds(60)
                .streamEnabled(true)
                .analyticsEnabled(true)
                .cache(new CaffeineCache(100))
                .build();

        // Create the client
        //CfClient cfClient = new CfClient(new HarnessConnector(apiKey, connectorConfig), options);
        cfClient = new CfClient(apiKey, options);
        cfClient.waitForInitialization();
    }

    /**
     * Get Feature Flag value from Harness Client
     * In case of error / circuit break - Get it from Properties file / default value
     *
     * @param featureFlag
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback_featureFlag", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public boolean getValue(FeatureFlag featureFlag) {
        try {
            logger.info("Trying to get the value for flag: {}", featureFlag.name());
            if (cfClient == null) {
                initializeClient();
            }
            final Target target = Target.builder()
                    .identifier("javasdk")
                    .name("JavaSDK")
                    .attribute("location", "emea")
                    .build();

            boolean flagValue = cfClient.boolVariation(featureFlag.name(), target, featureFlag.defaultValue());
            logger.info("Flag: {}, Value: {}" , featureFlag.name() , flagValue);
            return flagValue;
        } catch (FeatureFlagInitializeException | InterruptedException exception) {

        }
        logger.info("Returning Default Feature flag: {}", featureFlag.name() );
        return featureFlag.defaultValue();
    }


    /**
     * In case of Fallback (Harness not responding), Read from Properties file
     *
     * @param featureFlag
     * @return
     */
    private boolean fallback_featureFlag(FeatureFlag featureFlag) {
        logger.info("Fallback method for Harness Feature flag: {}", featureFlag.name() );
        return Boolean.valueOf(properties.getProperty(featureFlag.name()));
    }


}

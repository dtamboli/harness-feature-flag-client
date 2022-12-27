package com.harness.feature;

import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.api.Config;
import io.harness.cf.client.dto.Target;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FeatureService {

    public Boolean getFeatureFlagValue(String key) {
        try {
            /**
             * Put the API Key here from your environment
             */
            String apiKey = "2d55ddb2-b6be-49d3-9f2c-444d30852b4e";
            CfClient cfClient = new CfClient(apiKey, Config.builder().build());
            cfClient.waitForInitialization();

            /**
             * Define you target on which you would like to evaluate the featureFlag
             */
            Target target = Target.builder()
                    .name("User1")
                    .attributes(new HashMap<String, Object>())
                    .identifier("user1@example.com")
                    .build();

            /**
             * This is a sample boolean flag. You can replace the flag value with
             * the identifier of your feature flag
             */
            boolean result =
                    cfClient.boolVariation(key, target, false);
            System.out.println("Boolean variation is " + result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

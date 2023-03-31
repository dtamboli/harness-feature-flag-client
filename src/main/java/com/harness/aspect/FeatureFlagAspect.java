package com.harness.aspect;

import com.harness.annotations.FeatureFlag;
import com.harness.exceptions.FeatureNotEnabledException;
import com.harness.services.FeatureFlagService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Spring Aspect to validate feature flag value
 *
 * @author dtamboli
 */
@Aspect
@Component
public class FeatureFlagAspect {

    private FeatureFlagService featureFlagService;

    public FeatureFlagAspect(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    /**
     * This method validates if feature flag value aligns with expected value or not
     * By Default, Expected Value is true
     *
     * @param joinPoint
     * @param featureFlag
     */
    @Before("execution (* com.harness..*(..)) && @annotation(featureFlag)")
    public void checkFeatureFlag(JoinPoint joinPoint, FeatureFlag featureFlag) {
        boolean harnessFeatureFlag = featureFlagService.isFeatureFlagSet(featureFlag);
        if (harnessFeatureFlag != featureFlag.expectedValue()) {
            throw new FeatureNotEnabledException();
        }
    }


}

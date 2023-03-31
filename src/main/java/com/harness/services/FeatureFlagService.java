package com.harness.services;

import com.harness.annotations.FeatureFlag;

/**
 * Interface to get value of feature flag
 *
 * @author dtamboli
 */
public interface FeatureFlagService {

    /**
     *
     * @param featureFlag
     * @return
     */
    boolean isFeatureFlagSet(FeatureFlag featureFlag);
}

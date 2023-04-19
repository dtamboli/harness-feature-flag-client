package com.client.controllers;

import com.harness.annotations.FeatureFlag;
import com.harness.exceptions.FeatureNotEnabledException;
import com.harness.services.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * REST Endpoint for Feature Flags
 *
 * @author dtamboli
 */
@RequestMapping("/featureFlags")
@RestController
@RequiredArgsConstructor
public class FeatureFlagController {

    @Autowired
    FeatureFlagService featureFlagService;

    /**
     * Without Feature Flag
     *
     * @return
     */
    @GetMapping("/off")
    @FeatureFlag(name = "FeatureFlagTest", expectedValue = false)
    public ResponseEntity<Object> books() {
        return ResponseEntity.ok().body("Feature Flag Off");
    }

    /**
     * With Feature Flag
     *
     * @return
     */
    @GetMapping("/on")
    @FeatureFlag(name = "FeatureFlagTest")
    public ResponseEntity<Object> booksFeature()  {
        boolean result = featureFlagService.isFeatureFlagSet("FeatureFlagTest", false);
        return ResponseEntity.ok().body("Feature Flag On");
    }

    /**
     * Exception Handler
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(FeatureNotEnabledException.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.ok().body("Requested Feature is not available");
    }

}

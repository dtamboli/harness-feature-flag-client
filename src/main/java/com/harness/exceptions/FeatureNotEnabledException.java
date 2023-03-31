package com.harness.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Feature not available Exception
 *
 * @author dtamboli
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Feature is not enabled.")
public class FeatureNotEnabledException extends RuntimeException {
    private static final String MESSAGE = "Feature is not enabled.";

    public FeatureNotEnabledException() {
        super(MESSAGE);
    }
}

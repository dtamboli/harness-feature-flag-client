package com.harness.controllers;

import com.harness.feature.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
@RequiredArgsConstructor
public class BooksServiceConfig implements BooksService {


    @Autowired
    FeatureService cache;

    private BooksService booksService;

    public String getBooksResult() {
        if (cache.getFeatureFlagValue("FeatureFlagTest")) {
            this.booksService = new BooksNewService();
        } else {
            this.booksService = new BooksDefaultService();
        }
        return this.booksService.getBooksResult();
    }

}

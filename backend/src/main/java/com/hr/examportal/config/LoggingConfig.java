package com.hr.examportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);         // enables request body logging
        filter.setMaxPayloadLength(10000);      // increase for large bodies
        filter.setIncludeHeaders(false);        // set to true to log headers
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}

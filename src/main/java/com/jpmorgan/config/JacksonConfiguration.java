package com.jpmorgan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

/**
 * Configure Jackson CSV
 * 
 * @author Carlos Alberto
 *
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public CsvMapper csvMapper() {
        final CsvMapper csvMapper = new CsvMapper();

        // Remove quotes from strings
        csvMapper.configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true);

        return csvMapper;
    }
}

package com.jpmorgan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jpmorgan.service.IntructionProcessor;

/**
 * Application to generate a report of all instructions sent to JP Morgan in the
 * international market.
 * 
 * @author Carlos Alberto
 *
 */
@SpringBootApplication
public class InstructionReportApplication {

    @Bean
    public CommandLineRunner commandLineRunner(final IntructionProcessor intructionProcessor) {
        return (args) -> intructionProcessor.process();
    }

    public static void main(String[] args) {
        SpringApplication.run(InstructionReportApplication.class, args);
    }
}

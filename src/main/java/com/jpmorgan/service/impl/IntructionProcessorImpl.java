package com.jpmorgan.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jpmorgan.service.InstructionReaderService;
import com.jpmorgan.service.InstructionWriterService;
import com.jpmorgan.service.IntructionProcessor;
import com.jpmorgan.support.InstructionVO;

/**
 * Service to manage instruction process
 * 
 * @author Carlos Alberto
 *
 */
@Component
public class IntructionProcessorImpl implements IntructionProcessor {

    private final String instructionFilePath;

    private final InstructionReaderService instructionReaderService;

    private final InstructionWriterService instructionWriterService;

    public IntructionProcessorImpl(@Value("${instruction.file-path:}") final String instructionFilePath,
            final InstructionReaderService instructionReaderService,
            final InstructionWriterService instructionWriterService) {
        this.instructionFilePath = instructionFilePath;
        this.instructionReaderService = instructionReaderService;
        this.instructionWriterService = instructionWriterService;
    }

    @Override
    public void process() throws Exception {
        // Instruction file
        final File instructionFile = new File(instructionFilePath);

        // If file does not exist
        if (!instructionFile.exists()) {
            System.out.println("\nERROR: Invalid intruction file, please verify!");
            return;
        }

        // Read and check pending instructions
        final List<InstructionVO> pendingIntructionList = instructionReaderService
                .readPendingIntruction(instructionFile);

        // Store pending instructions for future checking
        instructionWriterService.writePendingInstruction(instructionFile, pendingIntructionList);
    }
}

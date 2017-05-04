package com.jpmorgan;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jpmorgan.service.InstructionReaderService;
import com.jpmorgan.service.InstructionWriterService;
import com.jpmorgan.support.InstructionVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InstructionReportApplicationTest {

    @Value("${instruction.file-path:}")
    private String instructionFilePath;

    @Autowired
    private InstructionReaderService instructionReaderService;

    @Autowired
    private InstructionWriterService instructionWriterService;

    @Test
    public void main() throws Exception {
        // Instruction file path
        assertThat(instructionFilePath).isNotEmpty();

        // Instruction file
        final File instructionFile = new File(instructionFilePath);
        assertThat(instructionFile).isNotNull();

        // Read and check pending instructions
        final List<InstructionVO> pendingIntructionList = instructionReaderService
                .readPendingIntruction(instructionFile);
        assertThat(pendingIntructionList).isNotEmpty().doesNotContainNull().doesNotHaveDuplicates();

        // Store pending instructions for future checking
        instructionWriterService.writePendingInstruction(instructionFile, pendingIntructionList);
    }
}

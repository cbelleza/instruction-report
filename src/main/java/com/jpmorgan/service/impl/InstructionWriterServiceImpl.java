package com.jpmorgan.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.jpmorgan.service.InstructionWriterService;
import com.jpmorgan.support.InstructionVO;

/**
 * Service to write pending instructions into csv file based on settlement date
 * 
 * @author Carlos Alberto
 *
 */
@Component
public class InstructionWriterServiceImpl implements InstructionWriterService {

    private final static Logger LOGGER = LoggerFactory.getLogger(InstructionWriterServiceImpl.class);

    private final CsvMapper csvMapper;

    public InstructionWriterServiceImpl(final CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
    }

    @Override
    public void writePendingInstruction(final File intructionFile, final List<InstructionVO> pendingInstructionList)
            throws IOException {
        LOGGER.info("Creating instructions backup file");
        final File intructionFileBackup = new File(
                intructionFile.getAbsolutePath().concat("." + LocalDate.now().toString()));
        FileCopyUtils.copy(intructionFile, intructionFileBackup);

        LOGGER.info("Writing pending instructions to csv file");
        final CsvSchema csvSchema = csvMapper.schemaFor(InstructionVO.class);
        // Store instructions into csv
        csvMapper.writer(csvSchema.withUseHeader(true)).writeValues(intructionFile).write(pendingInstructionList);
    }
}

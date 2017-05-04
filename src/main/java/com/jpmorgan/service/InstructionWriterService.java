package com.jpmorgan.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jpmorgan.support.InstructionVO;

public interface InstructionWriterService {

    public void writePendingInstruction(final File intructionFile, final List<InstructionVO> pendingInstructionList)
            throws IOException;
}
package com.jpmorgan.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jpmorgan.support.InstructionVO;

public interface InstructionReaderService {

    public List<InstructionVO> readPendingIntruction(final File intructionFile) throws IOException;
}
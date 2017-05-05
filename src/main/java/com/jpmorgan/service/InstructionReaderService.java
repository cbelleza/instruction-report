package com.jpmorgan.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jpmorgan.support.vo.Instruction;

public interface InstructionReaderService {

    public List<Instruction> readPendingIntruction(final File intructionFile) throws IOException;
}
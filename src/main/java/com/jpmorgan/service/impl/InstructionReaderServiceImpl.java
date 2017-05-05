package com.jpmorgan.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.jpmorgan.service.InstructionReaderService;
import com.jpmorgan.support.Flag;
import com.jpmorgan.support.vo.Instruction;

/**
 * Service to read instructions from csv file and print report on console
 * 
 * @author Carlos Alberto
 *
 */
@Component
public class InstructionReaderServiceImpl implements InstructionReaderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(InstructionReaderServiceImpl.class);

    private final CsvMapper csvMapper;

    private final List<String> exclusiveCurrencyTrade;

    public InstructionReaderServiceImpl(final CsvMapper csvMapper,
            @Value("${currency-trade.exclusive:}") final List<String> exclusiveCurrencyTrade) {
        this.csvMapper = csvMapper;
        this.exclusiveCurrencyTrade = exclusiveCurrencyTrade;
    }

    @Override
    public List<Instruction> readPendingIntruction(final File intructionFile) throws IOException {
        LOGGER.info("Reading instructions csv file");

        // Read csv file
        final MappingIterator<Instruction> instructionIterator = csvMapper.readerFor(Instruction.class)
                .with(CsvSchema.emptySchema().withHeader()).with(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                .readValues(intructionFile);

        final Map<Flag, HashMap<String, BigDecimal>> flagMap = new LinkedHashMap<Flag, HashMap<String, BigDecimal>>();
        flagMap.put(Flag.BUY, new HashMap<String, BigDecimal>());
        flagMap.put(Flag.SELL, new HashMap<String, BigDecimal>());

        final List<Instruction> pendingInstructionList = new ArrayList<Instruction>();

        // Loop instructions
        while (instructionIterator.hasNext()) {
            final Instruction instruction = instructionIterator.next();

            // Buy or Sell
            final Flag flag = instruction.getFlag();
            final String entity = instruction.getEntity();
            final String currency = instruction.getCurrency();

            final LocalDate currentDate = LocalDate.now();
            final LocalDate settlementDate = instruction.getSettlementDate();

            // Case settlement date is <= currentDate
            if (settlementDate.isBefore(currentDate) || settlementDate.isEqual(currentDate)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Reading instruction for entity: " + entity);
                }

                final DayOfWeek settlementDateDayOfWeek = settlementDate.getDayOfWeek();

                if (settlementDateDayOfWeek.equals(DayOfWeek.SATURDAY)) {
                    moveInstruction(pendingInstructionList, instruction, settlementDate, 2);

                } else if (settlementDateDayOfWeek.equals(DayOfWeek.SUNDAY)
                        && !exclusiveCurrencyTrade.contains(currency)) {
                    moveInstruction(pendingInstructionList, instruction, settlementDate, 1);

                } else if (!(exclusiveCurrencyTrade.contains(currency)
                        && !settlementDateDayOfWeek.equals(DayOfWeek.FRIDAY))) {
                    // Ordinary day
                    final BigDecimal pricePerUnit = instruction.getPricePerUnit();
                    final BigDecimal units = instruction.getUnits();
                    final BigDecimal agreedFx = instruction.getAgreedFx();

                    // USD amount of a trade = Price per unit * Units * AgreedFx
                    final BigDecimal amount = pricePerUnit.multiply(units).multiply(agreedFx).setScale(2,
                            BigDecimal.ROUND_HALF_DOWN);

                    // Increment current amount
                    flagMap.get(flag).merge(entity, amount, (v1, v2) -> v1.add(v2));
                }

            } else {
                LOGGER.debug("Moving instruction for future checking");
                pendingInstructionList.add(instruction);
            }
        }

        // Print report by flag
        flagMap.forEach((v1, v2) -> printReport(v1, v2.entrySet()));
        System.out.println("");

        return pendingInstructionList;
    }

    /**
     * Move an instruction to a next working day
     * 
     * @param pendingInstructionList
     * @param instruction
     * @param settlementDate
     * @param days
     */
    private void moveInstruction(final List<Instruction> pendingInstructionList, final Instruction instruction,
            final LocalDate settlementDate, final int days) {
        final LocalDate nextSettlementDate = settlementDate.plusDays(days);
        instruction.setSettlementDate(nextSettlementDate);
        pendingInstructionList.add(instruction);

        if (LOGGER.isWarnEnabled()) {
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("Changing settlement date of entity \"");
            strBuilder.append(instruction.entity);
            strBuilder.append("\" to \"");
            strBuilder.append(nextSettlementDate);
            strBuilder.append("\"");

            LOGGER.warn(strBuilder.toString());
        }
    }

    /**
     * Print on console instructions by flags (BUY / SELL)
     * 
     * @param flag
     * @param instructionSet
     */
    private void printReport(final Flag flag, final Set<Entry<String, BigDecimal>> instructionSet) {
        // Order instructions by the highest amount
        final Map<String, BigDecimal> instructionSortedMap = instructionSet.stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("\n>>> " + flag.name() + " Entities Ranking <<<");
        if (instructionSortedMap.size() > 0) {
            instructionSortedMap.forEach((v1, v2) -> System.out.println(v1 + " = " + v2));
        } else {
            System.out.println("No " + "" + " instructions for the current date");
        }
    }
}
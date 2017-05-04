## Synopsis

**Instruction report** - Manage instructions sent by various clients to JP Morgan in the international market.

This application is based on Spring Boot framework and generates a single fat-jar including all required dependencies.

An external CSV file must be supplied with all instructions. Instructions for the next working day will be moved to a new instruction file and the current file will be backup.

On console will be printed a report grouping entities by flag either BUY or SELL and ordering by highest amount value.

## Build Instructions

Install the Maven client (version 3.* or better). Then clone from GIT and then use Maven:
```
$ git clone ...
$ mvn install
```
## Settings application

To run application you must supply an instruction file:
```
--instruction.file-path = <your instruction file path>
```
## Adjusting log level (optionl)

The default log level is "info", case needed it can be changed by the property
```
--logging.level.com.jpmorgan = <level>
```
## Start example
```
java -jar target/instruction-report-1.0.0.jar InstructionReportApplication 
      --instruction.file-path=D:/instruction-report/instructions/instructions.csv
      --logging.level.com.jpmorgan=warn
```
## Test classes

- InstructionReportApplicationTest

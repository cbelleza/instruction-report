package com.jpmorgan.support.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.jpmorgan.support.Flag;

@JsonPropertyOrder({ "Entity", "Buy/Sell", "AgreedFx", "Currency", "InstructionDate", "SettlementDate", "Units",
        "Price per unit" })
public class Instruction {

    @JsonProperty("Entity")
    public String entity;

    @JsonProperty("Buy/Sell")
    public Flag flag;

    @JsonProperty("AgreedFx")
    public BigDecimal agreedFx;

    @JsonProperty("Currency")
    public String currency;

    @JsonProperty("InstructionDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate date;

    @JsonProperty("SettlementDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate settlementDate;

    @JsonProperty("Units")
    public BigDecimal units;

    @JsonProperty("Price per unit")
    public BigDecimal pricePerUnit;

    public String getEntity() {
        return entity;
    }

    public void setEntity(final String entity) {
        this.entity = entity;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(final Flag flag) {
        this.flag = flag;
    }

    public BigDecimal getAgreedFx() {
        return agreedFx;
    }

    public void setAgreedFx(final BigDecimal agreedFx) {
        this.agreedFx = agreedFx;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(final LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(final BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(final BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
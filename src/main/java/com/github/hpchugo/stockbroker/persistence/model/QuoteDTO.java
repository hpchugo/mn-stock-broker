package com.github.hpchugo.stockbroker.persistence.model;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class QuoteDTO {
    private Integer id;
    private BigDecimal volume;

    public Integer getId() {
        return id;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
}

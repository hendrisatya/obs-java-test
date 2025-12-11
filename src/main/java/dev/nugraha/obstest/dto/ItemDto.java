package dev.nugraha.obstest.dto;

import java.math.BigDecimal;

public record ItemDto(
    Long id,
    String name,
    BigDecimal price,
    Integer stock
) {}

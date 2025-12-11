package dev.nugraha.obstest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull Long itemId,
    @Min(1) Integer quantity
) {}
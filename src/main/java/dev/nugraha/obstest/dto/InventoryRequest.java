package dev.nugraha.obstest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InventoryRequest(
    @NotNull Long itemId,
    @Min(1) Integer quantity,
    @Pattern(regexp = "[TWtw]", message = "Type must be 'T' (Top Up) or 'W' (Withdraw)") String type
) {}

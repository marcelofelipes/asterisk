package com.asterisk.backend.adapter.rest.authentication.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record RegisterConfirmRequestDto(@NotNull @NotBlank
                                        @Pattern(regexp = "^[a-zA-Z0-9]{3}-[a-zA-Z0-9]{3}-[a-zA-Z0-9]{3}$")
                                        String code) {
}

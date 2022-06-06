package com.asterisk.backend.adapter.authentication.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PasswordChangeRequestDto(@NotNull @NotBlank @Size(min = 8, max = 64) String password,
                                       @NotNull @NotBlank @Size(min = 8, max = 64) String passwordConfirmation) {
}

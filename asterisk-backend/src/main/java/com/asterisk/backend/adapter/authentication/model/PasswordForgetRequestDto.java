package com.asterisk.backend.adapter.authentication.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PasswordForgetRequestDto(@NotNull @NotBlank @Size(max = 255) @Email String email) {
}

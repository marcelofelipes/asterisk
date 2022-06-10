package com.asterisk.backend.adapter.rest.authentication.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RegisterRequestDto(@NotNull @NotBlank @Size(min = 6, max = 25) String firstName,
                                 @NotNull @NotBlank @Size(min = 6, max = 25) String lastName,
                                 @NotNull @NotBlank @Size(max = 255) @Email String email,
                                 @NotNull @NotBlank @Size(min = 6, max = 25) String username,
                                 @NotNull @NotBlank @Size(min = 8, max = 64) String password) {
}

package com.asterisk.backend.adapter.rest.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Values can be nullable
 * This states that this variable is not changed
 */
public record UserChangeRequestDto(@NotBlank @Size(min = 6, max = 25) String firstName,
                                   @NotBlank @Size(min = 6, max = 25) String lastName,
                                   @NotBlank @Size(max = 255) @Email String email,
                                   @NotBlank @Size(min = 6, max = 25) String username) {
}

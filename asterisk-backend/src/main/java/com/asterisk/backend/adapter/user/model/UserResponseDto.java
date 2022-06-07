package com.asterisk.backend.adapter.user.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponseDto(UUID id,
                              String firstName,
                              String lastName,
                              String username,
                              String email,
                              String phone,
                              OffsetDateTime createdAt,
                              OffsetDateTime updatedAt) {
}

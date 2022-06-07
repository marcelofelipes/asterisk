package com.asterisk.backend.adapter.user;

import com.asterisk.backend.adapter.authentication.model.PasswordChangeRequestDto;
import com.asterisk.backend.adapter.user.model.UserChangeRequestDto;
import com.asterisk.backend.adapter.user.model.UserResponseDto;
import com.asterisk.backend.mapper.UserMapper;
import com.asterisk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(final UserService userService, final UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("@userSecurity.isAccountOwner(authentication, #userId)")
    public ResponseEntity<?> readUser(@PathVariable final UUID userId) {
        final UserResponseDto userResponseDto = this.userMapper.toUserResponseDto(this.userService.readUser(userId));
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping(value = "/{userId}")
    @PreAuthorize("@userSecurity.isAccountOwner(authentication, #userId)")
    public ResponseEntity<?> updateUser(@PathVariable final UUID userId,
                                        @Valid @RequestBody final UserChangeRequestDto userChangeRequestDto) {
        this.userService.updateUser(userId, userChangeRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{userId}/change-password")
    @PreAuthorize("@userSecurity.isAccountOwner(authentication, #userId)")
    public ResponseEntity<?> changeUserPassword(@PathVariable final UUID userId,
                                                @Valid @RequestBody final PasswordChangeRequestDto passwordChangeRequestDto) {
        final boolean result = this.userService.changePassword(userId, passwordChangeRequestDto);
        if (!result) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}

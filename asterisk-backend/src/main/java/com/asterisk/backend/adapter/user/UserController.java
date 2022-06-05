package com.asterisk.backend.adapter.user;

import com.asterisk.backend.adapter.user.model.UserResponseDto;
import com.asterisk.backend.mapper.UserMapper;
import com.asterisk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

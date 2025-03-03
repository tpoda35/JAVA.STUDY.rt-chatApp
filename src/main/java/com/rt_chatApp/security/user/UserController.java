package com.rt_chatApp.security.user;

import com.rt_chatApp.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller class for the user specific endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUserInfo")
    public UserInfoDto getUserInfo() {
        User user = userService.getUser();

        return UserInfoDto.builder()
                .displayName(user.getDisplayName())
                .userId(user.getId())
                .build();
    }
}

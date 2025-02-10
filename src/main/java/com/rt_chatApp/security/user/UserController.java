package com.rt_chatApp.security.user;

import com.rt_chatApp.Dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUserInfo")
    public UserInfoDto getUserInfo() {
        User user = service.getUser();

        return UserInfoDto.builder()
                .firstname(user.getFirstname())
                .userId(user.getId())
                .build();
    }
}

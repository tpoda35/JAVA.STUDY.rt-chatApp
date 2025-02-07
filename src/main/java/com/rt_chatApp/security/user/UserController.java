package com.rt_chatApp.security.user;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.rt_chatApp.Dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .lastname(user.getLastname())
                .userId(user.getId())
                .build();
    }
}

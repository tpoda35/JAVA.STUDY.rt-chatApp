package com.rt_chatApp.security.user;

import com.rt_chatApp.Dto.UserInfoDto;
import com.rt_chatApp.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JwtService jwtService;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(
            @CookieValue("Authorization") String token
    ){
        String email = jwtService.extractUsername(token);
        if (email == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(UserInfoDto.builder()
                .email(email)
                .build());
    }
}

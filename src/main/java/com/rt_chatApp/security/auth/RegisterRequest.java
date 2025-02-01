package com.rt_chatApp.security.auth;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Firstname field cannot be blank.")
  private String firstname;
  @NotBlank(message = "Lastname field cannot be blank.")
  private String lastname;
  @NotBlank(message = "Email field cannot be blank.")
  @Email(message = "Invalid Email format.")
  private String email;
  private String password;
  private String confirmPassword;

  @PrePersist
  protected void checkPasswords(){
    if (!password.equals(confirmPassword)){
      throw new BadCredentialsException("Passwords does not match.");
    }
  }
}

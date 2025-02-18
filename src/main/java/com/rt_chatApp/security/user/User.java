package com.rt_chatApp.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rt_chatApp.security.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "_user",
        indexes = {
                @Index(name = "idx_unique_identifier", columnList = "uniqueIdentifier", unique = true)
        }
)
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false)
  private String displayName;

  @Column(nullable = false, unique = true)
  private String uniqueIdentifier;

  @Column(unique = true, nullable = false)
  @Email(message = "Invalid email format.")
  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  @ToString.Exclude
  private List<Token> tokens;

  // Not implemented.
  @ManyToMany
  @JoinTable(
          name = "user_friends",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "friend_id")
  )
  @JsonIgnore
  @ToString.Exclude
  private List<User> friends;

  @PrePersist
  protected void createIdentifier(){
    uniqueIdentifier = displayName + "#" + id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

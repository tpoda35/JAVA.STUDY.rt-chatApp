package com.rt_chatApp.security.user;

import com.rt_chatApp.Dto.FriendDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  Optional<User> findByUniqueIdentifier(String uniqueIdentifier);

  /**
   * Custom query for to find the gives user friends, map them to the {@link FriendDto},
   * to solve the N+1 problem and also the lazy loaded objects.
   *
   * @param userId the id of the user whose friends we want to find.
   * @return list of {@link FriendDto}.
   */
  @Query("SELECT new com.rt_chatApp.Dto.FriendDto(f.id, f.displayName, f.status) " +
          "FROM User u JOIN u.friends f WHERE u.id = :userId")
  List<FriendDto> findFriendsByUserId(@Param("userId") Integer userId);
}

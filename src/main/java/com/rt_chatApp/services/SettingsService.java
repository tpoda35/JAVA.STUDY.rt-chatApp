package com.rt_chatApp.services;

import com.rt_chatApp.Exceptions.DisplayNameCooldownException;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import com.rt_chatApp.security.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public void changeDisplayName(String displayName){
        User user = userService.getUser();
        LocalDateTime lastModifiedDNameDate = user.getLastModifiedDNameDate();

        if (lastModifiedDNameDate == null || lastModifiedDNameDate.plusWeeks(2).isBefore(LocalDateTime.now())) {
            user.setDisplayName(displayName);
            user.setLastModifiedDNameDate(LocalDateTime.now());
            userRepository.save(user);
        } else {
            LocalDateTime nextAllowedChangeTime = lastModifiedDNameDate.plusWeeks(2);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formattedTime =
                    nextAllowedChangeTime.toLocalDate().format(dateFormatter)
                            + " " +
                            nextAllowedChangeTime.toLocalTime().format(timeFormatter);


            throw new DisplayNameCooldownException("You cannot change your display name until " + formattedTime);
        }
    }
}

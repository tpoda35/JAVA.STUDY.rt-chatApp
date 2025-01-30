package com.rt_chatApp.services;

import com.rt_chatApp.Model.UsernameGenNum;
import com.rt_chatApp.repository.UsernameGenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameGenService {

    private static final Logger logger = LoggerFactory.getLogger(UsernameGenService.class);
    private final UsernameGenRepository repository;

    @Transactional
    public String generateUsername(){
        logger.info("Generating new username for a user.");
        UsernameGenNum usernameGenNum = repository.findById(1)
                .orElse(null);

        if (usernameGenNum == null){
            logger.info("Failed generating username, UsernameGenNum not found in the database.");
            return null;
        }
        String username = "User" + usernameGenNum.getNum();
        usernameGenNum.setNum(usernameGenNum.getNum() + 1);
        repository.save(usernameGenNum);

        return username;
    }

}

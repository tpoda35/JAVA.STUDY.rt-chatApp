package com.rt_chatApp.controller;

import com.rt_chatApp.services.SettingsService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final static Logger logger = LoggerFactory.getLogger(SettingsController.class);
    private final SettingsService settingsService;

    @PostMapping("/changeDisplayName")
    @Validated
    public void changeDisplayName(
            @RequestParam
            @NotNull(message = "The field cannot be null.")
            @Length(min = 4, max = 15, message = "Length must be between 4 and 15.")
            String newDisplayName
    ) {
        settingsService.changeDisplayName(newDisplayName);
    }

    @PostMapping("/changeIconColor")
    @Validated
    public void changeIconColor(
            @RequestParam
            @NotNull(message = "The color cannot be null.")
            String newIconColor
    ) {
        settingsService.changeIconColor(newIconColor);
    }
}

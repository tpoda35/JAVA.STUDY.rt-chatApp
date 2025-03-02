package com.rt_chatApp.controller;

import com.rt_chatApp.services.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @PostMapping("/changeDisplayName")
    public void changeDisplayName(
            @RequestParam String newDisplayName
    ) {
        settingsService.changeDisplayName(newDisplayName);
    }
}

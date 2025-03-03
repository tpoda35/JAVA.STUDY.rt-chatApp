package com.rt_chatApp.thymeleaf.controller;

import com.rt_chatApp.thymeleaf.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService service;

    @GetMapping("/home")
    public String redirectToIndex(
            Model model,
            Authentication authentication
    ) {
        if (isAuthenticated(authentication)) {
            model.addAttribute("dName", service.getAuthenticatedUserName());
            model.addAttribute("currentPage", "messages");
            return "index";
        }
        return "redirect:/login";
    }

    @GetMapping("/groups")
    public String redirectToGroups(
            Model model,
            Authentication authentication
    ) {
        if (isAuthenticated(authentication)) {
            model.addAttribute("dName", service.getAuthenticatedUserName());
            model.addAttribute("currentPage", "groups");
            return "groups";
        }
        return "redirect:/login";
    }

    private Boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}

package com.rt_chatApp.Thymeleaf.Controller;

import com.rt_chatApp.Thymeleaf.Service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService service;

    @GetMapping("/")
    public String redirectToIndex(
            Model model,
            Authentication authentication
    ) {
        if (isAuthenticated(authentication)) {
            model.addAttribute("dName", service.getAuthenticatedUserName());
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
            return "groups";
        }
        return "redirect:/login";
    }

    @GetMapping("/friends")
    public String redirectToFriends(
            Model model,
            Authentication authentication
    ) {
        if (isAuthenticated(authentication)) {
            return "friends";
        }
        return "redirect:/login";
    }

    @GetMapping("/settings")
    public String redirectToSettings(
            Model model,
            Authentication authentication
    ) {
        if (isAuthenticated(authentication)) {
            return "settings";
        }
        return "redirect:/login";
    }

    private Boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}

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
    public String redirectToChat(
            Model model,
            Authentication authentication
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("fName", service.getAuthenticatedUserName());
            return "index";
        }
        return "login.html";
    }

}

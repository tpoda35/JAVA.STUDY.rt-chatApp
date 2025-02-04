package com.rt_chatApp.Thymeleaf.Controller;

import com.rt_chatApp.Thymeleaf.Service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService service;

    @GetMapping("/chat")
    public String viewChatPage(Model model){
        model.addAttribute("fName", service.getAuthenticatedUserName());
        return "index";
    }

}

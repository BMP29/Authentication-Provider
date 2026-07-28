package com.github.BMP29.oauth2_auth_provider.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientPageController {
    @GetMapping("/register")
    public String registerPage() {
        return "RegisterClientPage";
    }
}

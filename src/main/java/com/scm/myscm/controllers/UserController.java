package com.scm.myscm.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.myscm.services.UserService;
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    

    // user dashboard page
    @RequestMapping(value="/dashboard")
    public String userDashboard() {
        System.out.println("User Dashboard");
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value="/profile")
    public String userProfile(Model model, Authentication authentication) {

        // System.out.println("User profile");
        return "user/profile";
    }
    

    // user add contacts page

    // user view contacts

    // user edit contact

    // user delete contact

    // user search contact
}

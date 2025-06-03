package com.scm.myscm.controllers;

// import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.myscm.entities.User;
import com.scm.myscm.forms.UserForm;
import com.scm.myscm.helpers.Message;
import com.scm.myscm.helpers.MessageType;
import com.scm.myscm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home page handler");

        //sending data to view
        model.addAttribute("name", "Keshav Pal");
        model.addAttribute("role", "Software Developer");
        model.addAttribute("githubRepo", "https://github.com/Keshavpal22");
        return "home";
    }

    // about route
    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("About page loading");
        return "about";
    }

    // services
    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("Services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }


    // this is for login
    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    

    // registration page
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        // we can also use default data
        // userForm.setName("Keshav");
        // userForm.setAbout("This is Deafulat data for testing.");
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // Processing register
    
    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println("Processing registration");
        // fetch the form data
        // Userform
        System.out.println(userForm);
        // validate form data

        if(rBindingResult.hasErrors()){
            return "register";
        }

        // TODO - Validate userForm
        // save to database
        // userService

        // UserForm --> User
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg");

        User savedUser = userService.saveUser(user);
        System.out.println("user saved :");


        // message = "Registration successful"

        // add the message:
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();

        session.setAttribute("message", message);



        // redirect to login page
        return "redirect:/register";
    }
    // processing login
    

}

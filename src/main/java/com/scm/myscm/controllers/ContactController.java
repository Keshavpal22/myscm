package com.scm.myscm.controllers;

import java.util.*;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.scm.myscm.config.AppConfig;
import com.scm.myscm.config.OAuthAuthenticationSuccessHandler;
import com.scm.myscm.entities.Contact;
import com.scm.myscm.entities.User;
import com.scm.myscm.forms.ContactForm;
import com.scm.myscm.forms.ContactSearchForm;
import com.scm.myscm.helpers.AppConstants;
import com.scm.myscm.helpers.Message;
import com.scm.myscm.helpers.MessageType;
import com.scm.myscm.helpers.helper;
import com.scm.myscm.services.ContactService;
import com.scm.myscm.services.ImageService;
import com.scm.myscm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final AppConfig appConfig;

    private final OAuthAuthenticationSuccessHandler OAuthAuthenticationSuccessHandler;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;
    
    @Autowired
    private UserService userService;

    ContactController(OAuthAuthenticationSuccessHandler OAuthAuthenticationSuccessHandler, AppConfig appConfig) {
        this.OAuthAuthenticationSuccessHandler = OAuthAuthenticationSuccessHandler;
        this.appConfig = appConfig;
    }

    @RequestMapping("/add")
    // add contact page handler
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        // contactForm.setName("Keshav Pal");
        contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result , Authentication authentication, HttpSession session){

        // process the form data

        // 1. validate form
        if (result.hasErrors()){

            result.getAllErrors().forEach(error -> logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
                .content("Please correct the following errors")
                .type(MessageType.red)
                .build());
            return "user/add_contact";
        }
        


        String username = helper.getEmailOfLoggedInUser(authentication);
        // form ---> contact
        
        User user = userService.getUserByEmail(username);

        // 2.process the contact picture
        // image process
        // logger.info("file information : {}", contactForm.getContactImage().getOriginalFilename());

        String filename = UUID.randomUUID().toString();

        String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);
        contactService.save(contact);
        System.out.println(contactForm);

        // 3. set the contact picture url

        // 4. set message to be displayed on the view 

        session.setAttribute("message", Message.builder()
        .content("Your have successfully added a new contact")
        .type(MessageType.green)
        .build());
        
        ;
        return "redirect:/user/contacts/add";

    }


    // view contacts
    @RequestMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,Model model,Authentication authentication){

        // laod all the user contacts 
        String username = helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact =  contactService.getByUser(user, page, size, sortBy, direction);
        
        
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }


    // search handler
    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction, 
        Model model, Authentication authentication
        ){

        logger.info("Field {} and keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        var user = userService.getUserByEmail(helper.getEmailOfLoggedInUser(authentication));


        Page<Contact> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }
        else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }
        else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }
        
        logger.info("Page contact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);


        return "user/search";
    }


}

package com.scm.myscm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.myscm.entities.Contact;
import com.scm.myscm.services.ContactService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class ApiController {

    // get contact
    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getById(contactId);

}
}

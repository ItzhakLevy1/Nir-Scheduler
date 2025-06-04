package com.nirSchedular.nirSchedularMongo.controllers;

import com.nirSchedular.nirSchedularMongo.service.interfac.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final IEmailService emailService;

    @Autowired
    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(
            @RequestParam String to,        // @RequestParam tells Spring to extract values from the query string of the URL, not from the request body
            @RequestParam String subject,   // @RequestParam tells Spring to extract values from the query string of the URL, not from the request body
            @RequestParam String body) {    // @RequestParam tells Spring to extract values from the query string of the URL, not from the request body
        emailService.sendEmail(to, subject, body);
        return "Email sent successfully to " + to;
    }
}

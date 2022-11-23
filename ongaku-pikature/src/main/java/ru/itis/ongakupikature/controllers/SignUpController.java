package ru.itis.ongakupikature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.ongakupikature.dto.SignUpDto;
import ru.itis.ongakupikature.service.SignUpService;

@Controller
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService service;

    @GetMapping("/signUp")
    public String getSignUpPage() {
        return "sign_up";
    }

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(path = "/signUp", produces = "application/text; charset=UTF-8")
    public String signUp(SignUpDto form) {

        service.signUp(form);
        return "ok";
    }
}
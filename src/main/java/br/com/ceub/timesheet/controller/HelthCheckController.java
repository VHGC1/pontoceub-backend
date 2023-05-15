package br.com.ceub.timesheet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/helth")
public class HelthCheckController {

    @GetMapping
    public String healtCheck() {
        return "ok";
    }
}

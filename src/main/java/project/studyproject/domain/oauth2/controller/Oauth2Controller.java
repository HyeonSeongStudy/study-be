package project.studyproject.domain.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Oauth2Controller {

    @GetMapping("/main")
    public String main(){
        return "main";
    }
}
package com.example.interviewPrep.quiz;

import com.example.interviewPrep.quiz.aop.Timer;
import com.example.interviewPrep.quiz.member.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class IndexController {

    private final HttpSession httpSession;

    @Timer
    @GetMapping("/")
    public String index(Model model) {


        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }
}

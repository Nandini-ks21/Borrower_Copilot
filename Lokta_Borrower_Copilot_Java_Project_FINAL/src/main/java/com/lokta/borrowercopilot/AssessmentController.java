package com.lokta.borrowercopilot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class AssessmentController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("page", "form");
        return "index";
    }

    @PostMapping("/assess")
    public String assess(@RequestParam Map<String,String> params, Model model) {
        Map<String,Object> data = new HashMap<>(params);
        LoanRules.Result result = LoanRules.assess(data);
        model.addAttribute("page", "result");
        model.addAttribute("a", params);
        model.addAttribute("r", result);
        return "index";
    }
}

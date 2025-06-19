package com.flow.extensionBlocker.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExtensionBlockerViewController {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("name", "evan");
        return "hello";
    }
}

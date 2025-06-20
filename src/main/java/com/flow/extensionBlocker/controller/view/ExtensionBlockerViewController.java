package com.flow.extensionBlocker.controller.view;

import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.service.ExtensionBlockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ExtensionBlockerViewController {
    @Autowired
    ExtensionBlockerService service;

    @GetMapping("extension/blocker")
    public String hello(Model model) {
        List<ExtensionBlockerResponseDTO> customExtensionList = service.selectAllCustomExtensionWithBanned();
        List<ExtensionBlockerResponseDTO> fixedExtensionList = service.selectAllFixedExtensionWithBanned();
        model.addAttribute("customExtensionList", customExtensionList);
        model.addAttribute("fixedExtensionList", fixedExtensionList);
        return "index";
    }
}

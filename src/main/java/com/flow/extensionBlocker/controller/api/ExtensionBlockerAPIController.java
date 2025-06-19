package com.flow.extensionBlocker.controller.api;

import com.flow.extensionBlocker.common.baseResponse.BaseResponse;
import com.flow.extensionBlocker.dto.ExtensionBlockerRequestDTO;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.service.ExtensionBlockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/extension/")
public class ExtensionBlockerAPIController {
    @Autowired
    ExtensionBlockerService service;

    @PostMapping("/custom")
    public BaseResponse<Object> createExtension(@RequestBody ExtensionBlockerRequestDTO extensionRequestDTO) {
        ExtensionBlockerResponseDTO extension = service.createExtension(extensionRequestDTO);
        return new BaseResponse<>(extension);
    }
}

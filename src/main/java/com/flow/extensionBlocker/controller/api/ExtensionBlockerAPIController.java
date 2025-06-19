package com.flow.extensionBlocker.controller.api;

import com.flow.extensionBlocker.common.baseResponse.BaseResponse;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import com.flow.extensionBlocker.dto.ExtensionBlockerRequestDTO;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.service.ExtensionBlockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/custom/{name}")
    public BaseResponse<Object> deleteExtension(@PathVariable String name) {
        System.out.println("컨트롤러에 들어옴");
        service.deleteExtension(name);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}

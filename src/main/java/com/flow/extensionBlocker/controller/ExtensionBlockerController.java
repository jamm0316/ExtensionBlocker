package com.flow.extensionBlocker.controller;

import com.flow.extensionBlocker.common.baseResponse.BaseResponse;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import com.flow.extensionBlocker.dto.ExtensionBlockerRequestDTO;
import com.flow.extensionBlocker.service.ExtensionBlockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/extension/")
public class ExtensionBlockerController {
    @Autowired
    ExtensionBlockerService extensionBlockerService;

    @PostMapping("custom")
    public BaseResponse<Object> createExtension(@RequestBody ExtensionBlockerRequestDTO extensionRequestDTO) {
        return new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, "확장자 등록을 구현하세요");
    }
}

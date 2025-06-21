package com.flow.extensionBlocker.controller.api;

import com.flow.extensionBlocker.common.baseResponse.BaseResponse;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import com.flow.extensionBlocker.dto.ExtensionBlockerDTO;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.dto.RequestDTO;
import com.flow.extensionBlocker.service.ExtensionBlockerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/extension/")
@AllArgsConstructor
public class ExtensionBlockerAPIController {
    private final ExtensionBlockerService service;

    @PostMapping("/custom")
    public BaseResponse<Object> registerExtension(@RequestBody RequestDTO requestDTO) {
        ExtensionBlockerDTO extensionBlockerDTO = ExtensionBlockerDTO.builder()
                .name(requestDTO.getName())
                .build();
        ExtensionBlockerResponseDTO extension = service.registerExtension(extensionBlockerDTO);
        return new BaseResponse<>(extension);
    }

    @DeleteMapping("/custom/{name}")
    public BaseResponse<Object> deleteExtension(@PathVariable String name) {
        service.deleteExtension(name);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @PatchMapping("/{name}/ban/toggle")
    public BaseResponse<Object> toggleExtensionBan(@PathVariable String name) {
        ExtensionBlockerResponseDTO toggledExtensionBan = service.toggleExtensionBan(name);
        return new BaseResponse<>(toggledExtensionBan);
    }
}

package com.flow.extensionBlocker.dto;

import com.flow.extensionBlocker.domain.ExtensionType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionBlockerDTO {
    @Size(min = 1, max = 20, message = "확장자 명은 최소 1자, 최대 20자 이내여야합니다.")
    private String name;
    @Builder.Default
    private ExtensionType type = ExtensionType.CUSTOM;
    @Builder.Default
    private boolean isBanned = true;
}

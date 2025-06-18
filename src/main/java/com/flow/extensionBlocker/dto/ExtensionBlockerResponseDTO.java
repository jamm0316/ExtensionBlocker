package com.flow.extensionBlocker.dto;

import com.flow.extensionBlocker.domain.ExtensionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExtensionBlockerResponseDTO {
    private final String name;
    private final ExtensionType type;
    private final boolean isBanned;
}

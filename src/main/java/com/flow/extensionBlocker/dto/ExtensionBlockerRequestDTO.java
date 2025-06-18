package com.flow.extensionBlocker.dto;

import com.flow.extensionBlocker.domain.ExtensionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionBlockerRequestDTO {
    private Long id;
    private String name;
    private ExtensionType type;
    @Builder.Default
    private boolean isBanned = true;
}

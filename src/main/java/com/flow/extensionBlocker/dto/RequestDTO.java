package com.flow.extensionBlocker.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @Size(min = 1, max = 20, message = "확장자 명은 최소 1자, 최대 20자 이내여야합니다.")
    private String name;
}

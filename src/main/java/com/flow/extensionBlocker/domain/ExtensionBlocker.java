package com.flow.extensionBlocker.domain;

import com.flow.extensionBlocker.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class ExtensionBlocker extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "확장자 명은 필수 입니다.")
    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @NotNull(message = "확장자 타입은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExtensionType type;

    @NotNull(message = "차단 여부는 필수입니다.")
    @Column(nullable = false)
    private boolean isBanned = true;
}

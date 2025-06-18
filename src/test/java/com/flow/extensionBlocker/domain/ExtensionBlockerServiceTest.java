package com.flow.extensionBlocker.domain;

import com.flow.extensionBlocker.dto.ExtensionBlockerRequestDTO;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.repository.ExtensionBlockerRepository;
import com.flow.extensionBlocker.service.ExtensionBlockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ExtensionBlockerServiceTest {
    @Autowired
    ExtensionBlockerService service;

    @Autowired
    ExtensionBlockerRepository repository;

    ExtensionBlockerRequestDTO exeDto;

    @BeforeEach
    public void setUp() {
        exeDto = ExtensionBlockerRequestDTO.builder()
                .name("exe")
                .type(ExtensionType.CUSTOM)
                .build();
    }

    @Test
    @DisplayName("extension등록 시 isBanned은 true로 저장됨")
    public void createExtension() throws Exception {
        ExtensionBlockerResponseDTO extension = service.createExtension(exeDto);

        //then
        assertThat(extension.getName()).isEqualTo(exeDto.getName());
        assertThat(extension.getType()).isEqualTo(extension.getType());
        assertThat(extension.isBanned()).isTrue();
    }

    @Test
    @DisplayName("확장자 명은 무조건 소문자로 저장함")
    public void extensionNameToLowerCase() throws Exception {
        //given
        exeDto = ExtensionBlockerRequestDTO.builder()
                .name("ExE")
                .type(ExtensionType.CUSTOM)
                .build();

        //when
        ExtensionBlockerResponseDTO extension = service.createExtension(exeDto);

        //then
        assertThat(extension.getName()).isEqualTo("exe");
    }

    @Test
    @DisplayName("이미 등록한 확장자를 재등록 하는 경우 isBanned를 true로 수정")
    public void registeredExtensionChangeIsBannedToTrue() throws Exception {

        // given
        exeDto.setBanned(false);
        service.createExtension(exeDto);

        // when
        ExtensionBlockerResponseDTO result = service.createExtension(exeDto);

        // then
        assertThat(result.getName()).isEqualTo("exe");
        assertThat(result.isBanned()).isTrue();
    }
}

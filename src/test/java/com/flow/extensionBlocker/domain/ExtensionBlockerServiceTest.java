package com.flow.extensionBlocker.domain;

import com.flow.extensionBlocker.common.baseException.BaseException;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
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

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    
    @Test
    @DisplayName("isBanned가 200개면 더 이상 등록안됨")
    public void registered200ExtensionsTheyAreNoLongerRegistered() throws Exception {
        //given
        IntStream.range(0, 200).forEach(i -> {
            ExtensionBlockerRequestDTO build = ExtensionBlockerRequestDTO.builder()
                    .name("exe" + i)
                    .type(ExtensionType.CUSTOM)
                    .build();
            service.createExtension(build);
        });

        //when
        ExtensionBlockerRequestDTO overflow = ExtensionBlockerRequestDTO.builder()
                .name("overflow")
                .type(ExtensionType.CUSTOM)
                .build();

        //then
        assertThatThrownBy(() -> service.createExtension(overflow))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_LIMIT_EXCEEDED.getMessage());
    }
    
    @Test
    @DisplayName("확장자 이름은 최대 20자리까지 가능.")
    public void extensionNameCanBeUpTo20Characters() throws Exception {
        //given
        ExtensionBlockerRequestDTO build = ExtensionBlockerRequestDTO.builder()
                .name("asdfgasdfgasdfgasdfga")
                .build();

        //then
        assertThatThrownBy(() -> service.createExtension(build))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_NAME_LENGTH_EXCEEDED.getMessage());
    }
    
    @Test
    @DisplayName("확장자를 업데이트하면 이름 또는 삭제여부를 바꿀 수 있다.")
    public void canChangeTheNameOrDeleteIt() throws Exception {
        //given
        service.createExtension(exeDto);

        ExtensionBlocker entity = repository.findByName(exeDto.getName());

        ExtensionBlockerRequestDTO newDto = ExtensionBlockerRequestDTO.builder()
                .id(entity.getId())
                .name("avi")
                .type(entity.getType())
                .isBanned(false)
                .build();

        //when
        ExtensionBlockerResponseDTO extensionBlockerResponseDTO = service.updateExtension(newDto);

        //then
        assertThat(extensionBlockerResponseDTO.getName()).isEqualTo("avi");
        assertThat(extensionBlockerResponseDTO.isBanned()).isFalse();
    }

    @Test
    @DisplayName("대문자의 수정된 확장자명이 들어오면 소문자로 바꿔준다.")
    public void uppercaseLettersConvertedToLowercaseLetters() throws Exception {
        //given
        service.createExtension(exeDto);

        ExtensionBlocker entity = repository.findByName(exeDto.getName());

        ExtensionBlockerRequestDTO newDto = ExtensionBlockerRequestDTO.builder()
                .id(entity.getId())
                .name("AVI")
                .type(entity.getType())
                .isBanned(false)
                .build();

        //when
        ExtensionBlockerResponseDTO extensionBlockerResponseDTO = service.updateExtension(newDto);

        //then
        assertThat(extensionBlockerResponseDTO.getName()).isEqualTo("avi");
        assertThat(extensionBlockerResponseDTO.isBanned()).isFalse();

    }

    @Test
    @DisplayName("수정하려는 확장자가 이미 존재하는 확장자라면 확장자 중복 예외를 던진다.")
    public void extensionDuplicateExceptionIsThrown() throws Exception {
        //given
        ExtensionBlockerRequestDTO aviDto = ExtensionBlockerRequestDTO.builder()
                .name("avi")
                .type(ExtensionType.CUSTOM)
                .isBanned(false)
                .build();
        service.createExtension(exeDto);
        service.createExtension(aviDto);

        ExtensionBlocker entity = repository.findByName(exeDto.getName());

        ExtensionBlockerRequestDTO newDto = ExtensionBlockerRequestDTO.builder()
                .id(entity.getId())
                .name("avi")
                .type(entity.getType())
                .isBanned(false)
                .build();

        //then
        assertThatThrownBy(() -> service.updateExtension(newDto))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_NAME_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("확장자 삭제를 누르면 isBanned가 false가 된다.")
    public void deleteExtension() throws Exception {
        //given
        service.createExtension(exeDto);

        //when
        service.deleteExtension(exeDto.getName());

        //then
        ExtensionBlocker entity = repository.findByName(exeDto.getName());
        assertThat(entity.isBanned()).isFalse();
    }

    @Test
    @DisplayName("확장자 영구 삭제를 누르면 확장자가 db에서 삭제 된다.")
    public void deleteForceExtension() throws Exception {
        //given
        service.createExtension(exeDto);

        //when
        service.deleteForceExtension(exeDto.getName());

        //then
        assertThat(repository.findByName(exeDto.getName())).isNull();
    }

    @Test
    @DisplayName("모든 커스텀 extensionList 조회")
    public void findAllCustomExtensionBlocker() throws Exception {
        //given
        IntStream.range(0, 10).forEach(i -> {
            ExtensionBlockerRequestDTO build = ExtensionBlockerRequestDTO.builder()
                    .name("exe" + i)
                    .type(ExtensionType.CUSTOM)
                    .build();
            service.createExtension(build);
        });

        //when
        List<ExtensionBlockerResponseDTO> extensionList = service.selectAllCustomExtension();

        //then
        assertThat(extensionList.size()).isEqualTo(10);
    }
}

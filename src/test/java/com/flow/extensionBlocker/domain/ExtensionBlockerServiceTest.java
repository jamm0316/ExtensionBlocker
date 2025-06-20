package com.flow.extensionBlocker.domain;

import com.flow.extensionBlocker.common.baseException.BaseException;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import com.flow.extensionBlocker.dto.ExtensionBlockerDTO;
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

    ExtensionBlockerDTO exeDto;

    @BeforeEach
    public void setUp() {
        exeDto = ExtensionBlockerDTO.builder()
                .name("uniquedata")
                .type(ExtensionType.CUSTOM)
                .build();
    }

    @Test
    @DisplayName("extension등록 시 isBanned=true, type=CUSTOM으로 저장됨")
    public void createExtension() throws Exception {
        ExtensionBlockerResponseDTO extension = service.registerExtension(exeDto);

        //then
        assertThat(extension.getType()).isEqualTo(ExtensionType.CUSTOM);
        assertThat(extension.isBanned()).isTrue();
    }

    @Test
    @DisplayName("확장자 명은 무조건 소문자로 저장함")
    public void extensionNameToLowerCase() throws Exception {
        //given
        exeDto = ExtensionBlockerDTO.builder()
                .name("UNIquedata")
                .type(ExtensionType.CUSTOM)
                .build();

        //when
        ExtensionBlockerResponseDTO extension = service.registerExtension(exeDto);

        //then
        assertThat(extension.getName()).isEqualTo("uniquedata");
    }

    @Test
    @DisplayName("이미 등록한 확장자를 재등록 하는 경우 isBanned를 true로 수정")
    public void registeredExtensionChangeIsBannedToTrue() throws Exception {

        // given
        exeDto.setBanned(false);
        service.registerExtension(exeDto);

        // when
        ExtensionBlockerResponseDTO result = service.registerExtension(exeDto);

        // then
        assertThat(result.getName()).isEqualTo("uniquedata");
        assertThat(result.isBanned()).isTrue();
    }
    
    @Test
    @DisplayName("customExtension.isBanned >= true가 200개면 더 이상 등록안됨")
    public void registered200ExtensionsTheyAreNoLongerRegistered() throws Exception {
        //given
        int curSize = service.selectAllCustomExtensionWithBanned().size();
        IntStream.range(0, 200 - curSize).forEach(i -> {
            ExtensionBlockerDTO build = ExtensionBlockerDTO.builder()
                    .name("exe" + i)
                    .type(ExtensionType.CUSTOM)
                    .build();
            service.registerExtension(build);
        });

        //when
        ExtensionBlockerDTO overflow = ExtensionBlockerDTO.builder()
                .name("overflow")
                .type(ExtensionType.CUSTOM)
                .build();

        //then
        assertThatThrownBy(() -> service.registerExtension(overflow))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_LIMIT_EXCEEDED.getMessage());
    }
    
    @Test
    @DisplayName("확장자 이름은 최대 20자리까지 가능.")
    public void extensionNameCanBeUpTo20Characters() throws Exception {
        //given
        ExtensionBlockerDTO build = ExtensionBlockerDTO.builder()
                .name("asdfgasdfgasdfgasdfga")
                .build();

        //then
        assertThatThrownBy(() -> service.registerExtension(build))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_NAME_LENGTH_EXCEEDED.getMessage());
    }

    @Test
    @DisplayName("등록한 확장자기 이미 등록된 커스텀 확장자라면 중복예외를 던짐")
    public void customExtensionDuplicateExceptionIsThrown() throws Exception {
        //given
        ExtensionBlockerDTO aviDto = ExtensionBlockerDTO.builder()
                .name("avi")
                .build();
        service.registerExtension(aviDto);

        //then
        assertThatThrownBy(() -> service.registerExtension(aviDto))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_NAME_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("등록한 확장자기 이미 등록된 고정 확장자라면 중복예외를 던짐")
    public void fixedExtensionDuplicateExceptionIsThrown() throws Exception {
        //given
        ExtensionBlockerDTO aviDto = ExtensionBlockerDTO.builder()
                .name("avi")
                .type(ExtensionType.FIXED)
                .build();
        service.registerExtension(aviDto);

        //then
        assertThatThrownBy(() -> service.registerExtension(aviDto))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.EXTENSION_NAME_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("확장자 삭제를 누르면 isBanned가 false가 된다.")
    public void deleteExtension() throws Exception {
        //given
        service.registerExtension(exeDto);

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
        service.registerExtension(exeDto);

        //when
        service.deleteForceExtension(exeDto.getName());

        //then
        assertThat(repository.findByName(exeDto.getName())).isNull();
    }

    @Test
    @DisplayName("모든 커스텀 extensionList 조회")
    public void findAllCustomExtensionBlocker() throws Exception {
        //given
        int beforeSize = service.selectAllCustomExtensionWithBanned().size();
        IntStream.range(0, 10).forEach(i -> {
            ExtensionBlockerDTO build = ExtensionBlockerDTO.builder()
                    .name("exe" + i)
                    .type(ExtensionType.CUSTOM)
                    .build();
            service.registerExtension(build);
        });

        //when
        List<ExtensionBlockerResponseDTO> extensionList = service.selectAllCustomExtensionWithBanned();

        //then
        assertThat(extensionList.size()).isEqualTo(beforeSize + 10);
    }

    @Test
    @DisplayName("고정 확장자의 수정 요청이 들어오면 토글형식으로 isbanned을 바꾼다.")
    public void toggleExtensionBan() throws Exception {
        //given
        ExtensionBlockerDTO fixedExt = ExtensionBlockerDTO.builder()
                .name("smpleFixedExt")
                .type(ExtensionType.FIXED)
                .build();
        ExtensionBlockerResponseDTO extension = service.registerExtension(fixedExt);

        //when
        ExtensionBlockerResponseDTO result = service.toggleExtensionBan(extension.getName());

        //then
        assertThat(result.isBanned()).isFalse();
    }

    @Test
    @DisplayName("커스텀 확장자가 toggleExtensionBan로 들어오면 예외를 던진다.")
    public void toggleCustomExtensionBanException() throws Exception {
        //given
        ExtensionBlockerDTO fixedExt = ExtensionBlockerDTO.builder()
                .name("smpleFixedExt")
                .build();
        ExtensionBlockerResponseDTO extension = service.registerExtension(fixedExt);

        //when
        assertThatThrownBy(() -> service.toggleExtensionBan(extension.getName()))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.NOT_FIXED_EXTENSION.getMessage());
    }
}

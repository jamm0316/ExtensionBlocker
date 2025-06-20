package com.flow.extensionBlocker.service;

import com.flow.extensionBlocker.common.baseException.BaseException;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import com.flow.extensionBlocker.domain.ExtensionBlocker;
import com.flow.extensionBlocker.domain.ExtensionType;
import com.flow.extensionBlocker.dto.ExtensionBlockerDTO;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.repository.ExtensionBlockerRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Log4j2
public class ExtensionBlockerService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExtensionBlockerRepository repository;

    @Transactional
    public ExtensionBlockerResponseDTO registerExtension(ExtensionBlockerDTO extensionRequestDTO) {
        String lowerCaseName = extensionRequestDTO.getName().toLowerCase(Locale.ROOT);
        extensionRequestDTO.setName(lowerCaseName);

        if (isPreviouslyDeleted(lowerCaseName)) {
            return restoreExtension(lowerCaseName);
        } else {
            return createNewExtension(extensionRequestDTO);
        }
    }

    private boolean isPreviouslyDeleted(String name) {
        ExtensionBlocker existing = repository.findByName(name);
        if (existing != null) {
            if (!existing.isBanned()) {  //이미 존재하는데 논리삭제 됐으면 true 반환
                return true;
            } else {  //이미 존재하며, 등록되어있는 상태라면 중복예외 반환
                throw new BaseException(BaseResponseStatus.EXTENSION_NAME_DUPLICATED);
            }
        }
        return false;
    }

    @Transactional
    public ExtensionBlockerResponseDTO restoreExtension(String name) {
        ExtensionBlocker entity = repository.findByName(name);
        entity.setBanned(true);
        return entityToDto(entity);
    }

    @Transactional
    public ExtensionBlockerResponseDTO createNewExtension(ExtensionBlockerDTO extensionBlockerDTO) {
        if (extensionBlockerDTO.getName().length() > 20) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NAME_LENGTH_EXCEEDED);
        }

        if (repository.findAllCustomExtensionWithBanned().size() >= 200) {
            throw new BaseException(BaseResponseStatus.EXTENSION_LIMIT_EXCEEDED);
        }

        ExtensionBlocker savedEntity = repository.save(dtoToEntity(extensionBlockerDTO));
        return entityToDto(savedEntity);
    }

    @Transactional
    public ExtensionBlockerResponseDTO toggleExtensionBan(String name) {
        ExtensionBlocker entity = repository.findByName(name);
        if (entity.getType() != ExtensionType.FIXED) throw new BaseException(BaseResponseStatus.NOT_FIXED_EXTENSION);
        entity.setBanned(!entity.isBanned());
        return entityToDto(entity);
    }

    @Transactional
    public void deleteExtension(String name) {
        ExtensionBlocker entity = repository.findByName(name);
        if (entity == null) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NOT_FOUND);
        }
        entity.setBanned(false);
    }

    public void deleteForceExtension(String name) {
        ExtensionBlocker entity = repository.findByName(name);
        if (entity == null) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NOT_FOUND);
        }
        repository.delete(entity);
    }

    public List<ExtensionBlockerResponseDTO> selectAllCustomExtensionWithBanned() {
        return repository.findAllCustomExtensionWithBanned();
    }

    public List<ExtensionBlockerResponseDTO> selectAllFixedExtensionWithBanned() {
        return repository.findAllFixedExtensionWithBanned();
    }

    private ExtensionBlocker dtoToEntity(ExtensionBlockerDTO requestDTO) {
        return modelMapper.map(requestDTO, ExtensionBlocker.class);
    }

    private ExtensionBlockerResponseDTO entityToDto(ExtensionBlocker entity) {
        return new ExtensionBlockerResponseDTO(entity.getName(), entity.getType(), entity.isBanned());
    }
}

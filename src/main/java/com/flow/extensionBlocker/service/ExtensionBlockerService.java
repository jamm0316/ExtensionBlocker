package com.flow.extensionBlocker.service;

import com.flow.extensionBlocker.common.baseException.BaseException;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import com.flow.extensionBlocker.domain.ExtensionBlocker;
import com.flow.extensionBlocker.dto.ExtensionBlockerRequestDTO;
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
    public ExtensionBlockerResponseDTO createExtension(ExtensionBlockerRequestDTO extensionRequestDTO) {
        String lowerCaseName = extensionRequestDTO.getName().toLowerCase(Locale.ROOT);
        extensionRequestDTO.setName(lowerCaseName);

        ExtensionBlocker existing = repository.findByName(lowerCaseName);

        if (extensionRequestDTO.getName().length() > 20) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NAME_LENGTH_EXCEEDED);
        }

        if (existing != null && !existing.isBanned()) {
            existing.setBanned(true);
            return entityToDto(existing);
        }

        if (repository.countByIsBannedTrue() == 200) {
            throw new BaseException(BaseResponseStatus.EXTENSION_LIMIT_EXCEEDED);
        }

        ExtensionBlocker entity = repository.save(dtoToEntity(extensionRequestDTO));
        return entityToDto(entity);
    }

    public ExtensionBlockerResponseDTO updateExtension(ExtensionBlockerRequestDTO extensionRequestDTO) {
        ExtensionBlocker entity = repository.findById(extensionRequestDTO.getId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.EXTENSION_NOT_FOUND));

        String newName = extensionRequestDTO.getName().toLowerCase(Locale.ROOT);
        ExtensionBlocker duplicate = repository.findByName(newName);
        if (duplicate != null && duplicate.getId() != entity.getId()) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NAME_DUPLICATED);
        }

        entity.setName(extensionRequestDTO.getName().toLowerCase(Locale.ROOT));
        entity.setBanned(extensionRequestDTO.isBanned());
        return entityToDto(entity);
    }

    @Transactional
    public ExtensionBlockerResponseDTO toggleExtensionBan(String name) {
        ExtensionBlocker entity = repository.findByName(name);
        if (entity.isBanned()) entity.setBanned(false);
        else entity.setBanned(true);
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

    public List<ExtensionBlockerResponseDTO> selectAllCustomExtension() {
        List<ExtensionBlockerResponseDTO> extensionList = repository.findAllCustomExtension();
        if (extensionList.size() == 0) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NOT_FOUND);
        }
        return extensionList;
    }

    public List<ExtensionBlockerResponseDTO> selectAllFixedExtensionWithBanned() {
        List<ExtensionBlockerResponseDTO> extensionList = repository.findAllFixedExtension();
        if (extensionList.size() == 0) {
            throw new BaseException(BaseResponseStatus.EXTENSION_NOT_FOUND);
        }
        return extensionList;
    }

    private ExtensionBlocker dtoToEntity(ExtensionBlockerRequestDTO requestDTO) {
        return modelMapper.map(requestDTO, ExtensionBlocker.class);
    }

    private ExtensionBlockerResponseDTO entityToDto(ExtensionBlocker entity) {
        return new ExtensionBlockerResponseDTO(entity.getName(), entity.getType(), entity.isBanned());
    }
}

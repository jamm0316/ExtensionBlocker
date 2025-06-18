package com.flow.extensionBlocker.service;

import com.flow.extensionBlocker.domain.ExtensionBlocker;
import com.flow.extensionBlocker.dto.ExtensionBlockerRequestDTO;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import com.flow.extensionBlocker.repository.ExtensionBlockerRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Log4j2
public class ExtensionBlockerService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExtensionBlockerRepository repository;

    public ExtensionBlockerResponseDTO createExtension(ExtensionBlockerRequestDTO extensionRequestDTO) {
        String lowerCaseName = extensionRequestDTO.getName().toLowerCase(Locale.ROOT);
        extensionRequestDTO.setName(lowerCaseName);

        ExtensionBlocker existing = repository.findByName(lowerCaseName);
        if (existing != null) {
            existing.setBanned(true);
            return entityToDto(existing);
        }

        ExtensionBlocker entity = repository.save(dtoToEntity(extensionRequestDTO));
        return entityToDto(entity);
    }

    public ExtensionBlocker dtoToEntity(ExtensionBlockerRequestDTO requestDTO) {
        return modelMapper.map(requestDTO, ExtensionBlocker.class);
    }

    public ExtensionBlockerResponseDTO entityToDto(ExtensionBlocker entity) {
        return new ExtensionBlockerResponseDTO(entity.getName(), entity.getType(), entity.isBanned());
    }
}

package com.flow.extensionBlocker.repository;

import com.flow.extensionBlocker.domain.ExtensionBlocker;
import com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtensionBlockerRepository extends JpaRepository<ExtensionBlocker, Long> {
    ExtensionBlocker findByName(String name);

    @Query("""
            select new com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO (
                  e.name, e.type, e.isBanned
                )
            from ExtensionBlocker e
            where e.type = com.flow.extensionBlocker.domain.ExtensionType.CUSTOM
                and e.isBanned = true
            """)
    List<ExtensionBlockerResponseDTO> findAllCustomExtension();

    @Query("""
            select new com.flow.extensionBlocker.dto.ExtensionBlockerResponseDTO (
                  e.name, e.type, e.isBanned
                )
            from ExtensionBlocker e
            where e.type = com.flow.extensionBlocker.domain.ExtensionType.FIXED
            order by e.name
            """)
    List<ExtensionBlockerResponseDTO> findAllFixedExtension();

    long countByIsBannedTrue();
}

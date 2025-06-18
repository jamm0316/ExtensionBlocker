package com.flow.extensionBlocker.repository;

import com.flow.extensionBlocker.domain.ExtensionBlocker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtensionBlockerRepository extends JpaRepository<ExtensionBlocker, Long> {
    ExtensionBlocker findByName(String name);
}

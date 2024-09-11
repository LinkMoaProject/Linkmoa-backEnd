package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectoryRepository extends JpaRepository<Directory,Long> {
}

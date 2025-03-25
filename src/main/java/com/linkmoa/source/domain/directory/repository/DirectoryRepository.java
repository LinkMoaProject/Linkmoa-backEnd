package com.linkmoa.source.domain.directory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkmoa.source.domain.directory.entity.Directory;

public interface DirectoryRepository extends JpaRepository<Directory, Long>, DirectoryRepositoryCustom {
}

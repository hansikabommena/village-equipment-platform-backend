package com.villageshare.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.villageshare.backend.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // Get resources of a particular owner
    List<Resource> findByOwnerEmail(String ownerEmail);

    // Find a resource using its name
    Resource findByNameIgnoreCase(String name);
}
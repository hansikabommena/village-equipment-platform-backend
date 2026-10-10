package com.villageshare.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.villageshare.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
    
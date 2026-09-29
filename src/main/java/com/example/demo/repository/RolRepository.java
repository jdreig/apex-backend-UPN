package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}

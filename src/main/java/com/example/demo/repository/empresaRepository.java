package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.empresa;

public interface empresaRepository extends JpaRepository<empresa, Long> {
	
}
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.categoria;

public interface categoryRepository extends JpaRepository<categoria, Long>{

}

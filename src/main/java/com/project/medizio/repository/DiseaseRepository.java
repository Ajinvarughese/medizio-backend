package com.project.medizio.repository;

import com.project.medizio.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findByPatientId(Long id);
}

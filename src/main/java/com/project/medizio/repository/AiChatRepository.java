package com.project.medizio.repository;

import com.project.medizio.entity.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiChatRepository extends JpaRepository<AiChat, Long> {
    List<AiChat> findByPatientId(Long id);
    void deleteByPatientId(Long id);
}

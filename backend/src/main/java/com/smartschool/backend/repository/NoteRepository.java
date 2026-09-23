package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Note;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByEleveId(Long eleveId, Pageable pageable);


}

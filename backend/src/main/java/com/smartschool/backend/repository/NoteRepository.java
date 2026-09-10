package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Note;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByEleveId(Long eleveId);

    List<Note> findByDevoirId(Long devoirId);

    List<Note> findByEleveIdAndDevoirId(Long eleveId, Long devoirId);

}

package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
            SELECT n FROM Notification n
            WHERE n.destinataireId = :destinataireId
              AND (:lue IS NULL OR n.lue = :lue)
              AND (:recherche IS NULL OR :recherche = ''
                   OR LOWER(n.titre) LIKE LOWER(CONCAT('%', :recherche, '%'))
                   OR LOWER(n.message) LIKE LOWER(CONCAT('%', :recherche, '%')))
            ORDER BY n.dateCreation DESC
            """)
    Page<Notification> rechercher(
            @Param("destinataireId") Long destinataireId,
            @Param("recherche") String recherche,
            @Param("lue") Boolean lue,
            Pageable pagination
    );

}

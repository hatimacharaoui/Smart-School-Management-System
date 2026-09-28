package com.smartschool.backend.exception;

import com.smartschool.backend.dto.ErreurApiDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErreurApiDto> gererRessourceIntrouvable(
            ResourceNotFoundException exception
    ) {
        return construireReponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                Collections.emptyMap()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErreurApiDto> gererArgumentInvalide(
            IllegalArgumentException exception
    ) {
        return construireReponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                Collections.emptyMap()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErreurApiDto> gererAccesRefuse(
            AccessDeniedException exception
    ) {
        return construireReponse(
                HttpStatus.FORBIDDEN,
                "Vous n’avez pas l’autorisation d’effectuer cette action.",
                Collections.emptyMap()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurApiDto> gererValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> erreurs = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erreur ->
                        erreurs.put(
                                erreur.getField(),
                                erreur.getDefaultMessage()
                        )
                );

        return construireReponse(
                HttpStatus.BAD_REQUEST,
                "Les données envoyées ne sont pas valides.",
                erreurs
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErreurApiDto> gererContrainteBaseDeDonnees(
            DataIntegrityViolationException exception
    ) {
        return construireReponse(
                HttpStatus.CONFLICT,
                "Cette ressource est utilisée par d’autres données et ne peut pas être supprimée.",
                Collections.emptyMap()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErreurApiDto> gererAuthentification(
            AuthenticationException exception
    ) {
        return construireReponse(
                HttpStatus.UNAUTHORIZED,
                "Email ou mot de passe incorrect.",
                Collections.emptyMap()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurApiDto> gererErreurInterne(
            Exception exception
    ) {
        return construireReponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue.",
                Collections.emptyMap()
        );
    }

    private ResponseEntity<ErreurApiDto> construireReponse(
            HttpStatus statut,
            String message,
            Map<String, String> erreurs
    ) {
        ErreurApiDto reponse = new ErreurApiDto(
                LocalDateTime.now(),
                statut.value(),
                message,
                erreurs
        );

        return ResponseEntity.status(statut).body(reponse);
    }
}
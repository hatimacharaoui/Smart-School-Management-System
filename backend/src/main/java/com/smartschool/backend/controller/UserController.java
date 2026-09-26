package com.smartschool.backend.controller;
import com.smartschool.backend.dto.UserDto;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> rechercher(
            @RequestParam Role role,
            @RequestParam(required = false) String recherche,
            Pageable pagination
    ) {
        return ResponseEntity.ok(userService.rechercher(role, recherche, pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> chercher(@PathVariable Long id) {
        return ResponseEntity.ok(userService.chercherParId(id));
    }

    @PutMapping("/{id}/profil")
    public ResponseEntity<UserDto> modifierProfil(
            @PathVariable Long id,
            @Valid @RequestBody UserDto dto
    ) {
        return ResponseEntity.ok(userService.modifierProfil(id, dto));
    }
}

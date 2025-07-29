package com.rflwgnr.cabeleleilaleila.controllers;

import com.rflwgnr.cabeleleilaleila.data.dto.user.CreateUserDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.user.UserResponseDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.user.UsuarioAtualizadoDTO;
import com.rflwgnr.cabeleleilaleila.model.User;
import com.rflwgnr.cabeleleilaleila.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarTodosUsuarios() {
        List<User> usuarios = service.buscarTodosUsuarios();
        List<UserResponseDTO> response = usuarios.stream()
                .map(service::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> buscarPorUsername(@PathVariable String username) {
        User usuario = service.buscarUsuarioPorUsername(username);
        UserResponseDTO response = service.toResponseDTO(usuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO dto) {
        User user = service.createUser(dto);
        UserResponseDTO userResponseDTO = service.toResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PutMapping("/permissions")
    public ResponseEntity<UserResponseDTO> atualizarPermissoes(@RequestBody UsuarioAtualizadoDTO dto) {
        User user = service.atualizarPermissoesUsuarios(dto);
        UserResponseDTO response = service.toResponseDTO(user);
        return ResponseEntity.ok(response);
    }
}

package com.rflwgnr.cabeleleilaleila.services;

import com.rflwgnr.cabeleleilaleila.data.dto.user.CreateUserDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.user.UserResponseDTO;
import com.rflwgnr.cabeleleilaleila.data.dto.user.UsuarioAtualizadoDTO;
import com.rflwgnr.cabeleleilaleila.exception.AccessDeniedException;
import com.rflwgnr.cabeleleilaleila.exception.BadRequestException;
import com.rflwgnr.cabeleleilaleila.exception.ResourceNotFoundException;
import com.rflwgnr.cabeleleilaleila.model.Permission;
import com.rflwgnr.cabeleleilaleila.model.User;
import com.rflwgnr.cabeleleilaleila.repository.PermissionRepository;
import com.rflwgnr.cabeleleilaleila.repository.UserRepository;
import com.rflwgnr.cabeleleilaleila.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    public UserService(UserRepository repository, PermissionRepository permissionRepository) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não encontrado"));
    }

    public List<User> buscarTodosUsuarios() {
        boolean isAdmin = SecurityUtils.isAdmin();

        if (!isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para listar todos os usuários.");
        }

        List<User> usuarios = repository.findAll();

        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum agendamento encontrado.");
        }

        return usuarios;
    }

    public User buscarUsuarioPorUsername(String username) {
        String usernameLogado = SecurityUtils.getUsuarioLogadoUsername();
        boolean isAdmin = SecurityUtils.isAdmin();

        if (!isAdmin && !username.equals(usernameLogado)) {
            throw new AccessDeniedException("Você não tem permissão para acessar os dados deste usuário.");
        }

        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    @Transactional
    public User atualizarPermissoesUsuarios(UsuarioAtualizadoDTO dto) {
        boolean isAdmin = SecurityUtils.isAdmin();

        if (!isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para listar todos os usuários.");
        }

        if (dto.getPermissoesIds() == null || dto.getPermissoesIds().isEmpty()) {
            throw new BadRequestException("Usuário ID " + dto.getUserId() + " não pode ficar sem permissão.");
        }

        Set<Long> permissoesIds = new HashSet<>(dto.getPermissoesIds());

        Long permissionAdmin = 1L;
        Long permissionCommonUser = 2L;
        if (permissoesIds.contains(permissionAdmin) && !permissoesIds.contains(permissionCommonUser)) {
            permissoesIds.add(permissionCommonUser);
        }

        User user = repository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + dto.getUserId()));

        List<Permission> novasPermissoes = permissionRepository.findAllById(permissoesIds);

        if (novasPermissoes.isEmpty()) {
            throw new BadRequestException("Nenhuma permissão válida encontrada para o usuário ID " + dto.getUserId());
        }

        user.setPermissions(novasPermissoes);
        user.setEnabled(dto.getEnabled());
        return repository.save(user);
    }

    public User createUser(CreateUserDTO dto) {
        if (repository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já em uso.");
        }

        Permission defaultPermission = permissionRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Permissão padrão não encontrada no banco."));

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setPermissions(List.of(defaultPermission));

        return repository.save(user);
    }

    public UserResponseDTO toResponseDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEnabled(),
                user.getPermissions()
        );
    }

}

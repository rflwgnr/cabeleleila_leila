package com.rflwgnr.cabeleleilaleila.data.dto.user;

import com.rflwgnr.cabeleleilaleila.model.Permission;

import java.util.List;

public class UserResponseDTO {
    private Long id;
    private String username;
    private Boolean enabled;
    private List<Permission> permissions;

    public UserResponseDTO(Long id, String username, Boolean enabled, List<Permission> permissions) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}

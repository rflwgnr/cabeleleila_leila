package com.rflwgnr.cabeleleilaleila.data.dto.user;

import java.util.List;

public class UsuarioAtualizadoDTO {
    private Long userId;
    private Boolean enabled;
    private List<Long> permissoesIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Long> getPermissoesIds() {
        return permissoesIds;
    }

    public void setPermissoesIds(List<Long> permissoesIds) {
        this.permissoesIds = permissoesIds;
    }
}

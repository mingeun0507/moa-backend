package com.hanamja.moa.api.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_FRESHMEN, ROLE_SENIOR, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

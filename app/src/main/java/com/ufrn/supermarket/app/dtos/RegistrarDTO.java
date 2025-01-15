package com.ufrn.supermarket.app.dtos;

import com.ufrn.supermarket.app.enums.RoleEnum;

public record RegistrarDTO(
                String login,
                String password,
                RoleEnum role) {
}

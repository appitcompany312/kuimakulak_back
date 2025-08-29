package org.appitcompany.kuimakulak.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AppleLoginRequest(
        @NotBlank
        String idToken,
        String firstName,
        String lastName
) {
}

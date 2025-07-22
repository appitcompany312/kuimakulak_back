package org.appitcompany.kuimakulak.dto.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenRefreshRequest(
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken) {
}

package org.appitcompany.kuimakulak.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record AdminPasswordChangeRequest(
        @NotBlank(message = "Old password is required")
        String oldPassword,

        @NotBlank(message = "New password is required")
        String newPassword
) {
}

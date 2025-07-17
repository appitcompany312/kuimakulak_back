package org.appitcompany.kuimakulak.dto.contributorDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.appitcompany.kuimakulak.enums.ContributorRole;
@Getter
@Setter
public class ContributorRequest {
    @NotEmpty(message = "the full name field must not be empty")
    @Size(max = 500 , message = "максимум 500 символов  ")
    private String fullName;
    @NotEmpty(message = "the role field must not be empty")
    @Size(max = 50 , message = "max 50 characters ")
    private ContributorRole role;
}

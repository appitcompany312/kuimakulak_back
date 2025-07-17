package org.appitcompany.kuimakulak.dto.contributorDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.appitcompany.kuimakulak.enums.ContributorRole;
@Getter
@Setter
public class ContributorRequest {
    @NotEmpty(message = "поле full name не должен быть пустым")
    @Size(max = 500 , message = "максимум 500 символов  ")
    private String fullName;
    @NotEmpty(message = "поле role  не должен быть пустым")
    @Size(max = 50 , message = "максимум 50 символов  ")
    private ContributorRole role;
}

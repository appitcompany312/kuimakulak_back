package org.appitcompany.kuimakulak.dto.genreDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreRequest {
    @NotEmpty(message = "the genre name field must not be empty")
    @Size(max = 255 , message = "max 255 characters ")
    private String genreName;
}

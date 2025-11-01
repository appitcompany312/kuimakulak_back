package org.appitcompany.kuimakulak.dto.genreDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class GenresNameResponse {
    List<String> genres;
}

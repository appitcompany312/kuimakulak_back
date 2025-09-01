package org.appitcompany.kuimakulak.dto.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages,
                              boolean last) implements Serializable {

    public static <T> PageResponse<T> fromSpringPage(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}


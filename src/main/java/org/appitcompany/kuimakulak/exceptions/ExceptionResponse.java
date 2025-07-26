package org.appitcompany.kuimakulak.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(HttpStatus httpStatus,  String message) {
}

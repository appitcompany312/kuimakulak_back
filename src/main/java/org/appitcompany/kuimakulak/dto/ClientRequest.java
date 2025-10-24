package org.appitcompany.kuimakulak.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class ClientRequest {
    private String keyword;
    private List<@NotBlank String> author = new ArrayList<>();
    private List<@NotBlank String> gender = new ArrayList<>();

    public void setAuthor(List<String> author) {
        if (author != null) {
            this.author = author.stream()
                    .filter(a -> a != null && !a.isBlank())
                    .toList();
        } else {
            this.author = new ArrayList<>();
        }
    }

    public void setGender(List<String> gender) {
        if (gender != null) {
            this.gender = gender.stream()
                    .filter(g -> g != null && !g.isBlank())
                    .toList();
        } else {
            this.gender = new ArrayList<>();
        }
    }
}

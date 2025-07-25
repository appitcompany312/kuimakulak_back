package org.appitcompany.kuimakulak.dto.podcastDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatedPodcastRequest {
    private String description;
    @NotEmpty(message = "the podcastName field must not be empty")
    private String podcastName;
    @NotEmpty(message = "the audioUrl field must not be empty")
    private String audioUrl;
    private String bannerUrl;
}

package org.appitcompany.kuimakulak.dto.podcastDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PodcastResponse {
    private Long id;
    private String podcastName;
    private String audioUrl;
    private String bannerUrl;
    private String channelName;
    private String channelAuthor;
}

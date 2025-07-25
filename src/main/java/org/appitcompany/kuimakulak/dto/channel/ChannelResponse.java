package org.appitcompany.kuimakulak.dto.channel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelResponse {
    private Long id;
    private String channelName;
    private String channelAuthor;
    private int countPodcasts;

}

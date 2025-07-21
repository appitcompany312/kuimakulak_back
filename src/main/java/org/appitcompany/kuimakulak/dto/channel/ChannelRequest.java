package org.appitcompany.kuimakulak.dto.channel;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelRequest {
    @NotEmpty(message = "the channelName field must not be empty")
    private String channelName;
    @NotEmpty(message = "the channelAuthor field must not be empty")
    private String channelAuthor;
}

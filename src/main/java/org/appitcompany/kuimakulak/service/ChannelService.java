package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.springframework.http.ResponseEntity;

public interface ChannelService {
    ResponseEntity<?> save(ChannelRequest channelRequest);
}

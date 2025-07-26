package org.appitcompany.kuimakulak.service;

import jakarta.validation.Valid;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.appitcompany.kuimakulak.dto.channel.ChannelResponse;
import org.springframework.http.ResponseEntity;

public interface ChannelService {
    ResponseEntity<?> save(ChannelRequest channelRequest);

    PaginationResponse<ChannelResponse> findAllChannel(int pageSize, int pageNumber);

    ResponseEntity<?> deletedCannel(Long channelId);

    ResponseEntity<?> updatedCannel(@Valid ChannelRequest channelRequest, Long channelId);
}

package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.appitcompany.kuimakulak.entity.Channel;
import org.appitcompany.kuimakulak.exceptions.CustomAlreadyExistsException;
import org.appitcompany.kuimakulak.repository.ChannelRepo;
import org.appitcompany.kuimakulak.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepo channelRepo;
    @Override
    public ResponseEntity<?> save(ChannelRequest channelRequest) {
        Channel channel = channelRepo.findByChannelName(channelRequest.getChannelName())
                .stream().findFirst().orElse(null);
        if (channel != null) {
           throw new CustomAlreadyExistsException("Channel already exists "  + channelRequest.getChannelName());
        }
        Channel newChannel = new Channel();
        newChannel.setChannelName(channelRequest.getChannelName());
        newChannel.setChannelAuthor(channelRequest.getChannelAuthor());
        channelRepo.save(newChannel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Channel created successfully");
    }
}

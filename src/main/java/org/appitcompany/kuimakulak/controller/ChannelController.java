package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.appitcompany.kuimakulak.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    @Secured("ADMIN")
    @Operation(summary = "save channel",description = "only admins can add books")
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody ChannelRequest channelRequest){
       return channelService.save(channelRequest);
    }
}

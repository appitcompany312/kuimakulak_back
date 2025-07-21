package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.appitcompany.kuimakulak.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    //@Secured("ADMIN")
    @Operation(summary = "save book",description = "only admins can add books")
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ChannelRequest channelRequest){
       return channelService.save(channelRequest);
    }
}

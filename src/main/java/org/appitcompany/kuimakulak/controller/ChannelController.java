package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.appitcompany.kuimakulak.dto.channel.ChannelResponse;
import org.appitcompany.kuimakulak.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    @Secured("ADMIN")
    @Operation(summary = "save channel(\"ADMIN\")",description = "only admins can add channel")
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody ChannelRequest channelRequest){
       return channelService.save(channelRequest);
    }
    @Secured("ADMIN")
    @Operation(summary = "find all channel(\"ADMIN\")",description = "only admins can find all channel")
    @GetMapping("/findAllChannel")
    public PaginationResponse<ChannelResponse> findAllChannel(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return channelService.findAllChannel(pageSize,pageNumber);
    }
    @Secured("ADMIN")
    @Operation(summary = "updated channel(\"ADMIN\")",description = "only admins can updated channel")
    @PostMapping("/updatedCannel")
    public ResponseEntity<?> updatedCannel(@Valid @RequestBody ChannelRequest channelRequest,
                                           @RequestParam Long channelId){
        return channelService.updatedCannel(channelRequest,channelId);
    }

    @Secured("ADMIN")
    @Operation(summary = "deleted channel(\"ADMIN\")",description = "only admins can deleted channel")
    @PostMapping("/deletedCannel")
    public ResponseEntity<?> deletedCannel(@RequestParam Long channelId){
        return channelService.deletedCannel(channelId);
    }
}

package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.channel.ChannelRequest;
import org.appitcompany.kuimakulak.dto.channel.ChannelResponse;
import org.appitcompany.kuimakulak.entity.Channel;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.exceptions.CustomAlreadyExistsException;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.repository.ChannelRepo;
import org.appitcompany.kuimakulak.repository.PodcastDocRepo;
import org.appitcompany.kuimakulak.repository.PodcastRepo;
import org.appitcompany.kuimakulak.service.ChannelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepo channelRepo;
    private final PodcastRepo podcastRepo;
    private final PodcastDocRepo podcastDocRepo;

    @Override
    public ResponseEntity<?> save(ChannelRequest channelRequest) {
        Channel channel = channelRepo.findByChannelName(channelRequest.getChannelName())
                .stream().findFirst().orElse(null);
        if (channel != null) {
            throw new CustomAlreadyExistsException("Channel already exists " + channelRequest.getChannelName());
        }
        Channel newChannel = new Channel();
        newChannel.setChannelName(channelRequest.getChannelName());
        newChannel.setChannelAuthor(channelRequest.getChannelAuthor());
        channelRepo.save(newChannel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Channel created successfully");
    }

    @Override
    public PaginationResponse<ChannelResponse> findAllChannel(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Channel> channels = channelRepo.findAll(pageable);
        List<ChannelResponse> list = channels.stream().map(c -> {
            ChannelResponse response = new ChannelResponse();
            response.setChannelName(c.getChannelName());
            response.setChannelAuthor(c.getChannelAuthor());
            response.setId(c.getId());
            response.setCountPodcasts(podcastRepo.findByChannel(c).size());
            return response;
        }).toList();
        return PaginationResponse.<ChannelResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(channels.getTotalElements())
                .totalPages(channels.getTotalPages())
                .content(list)
                .build();
    }

    @Override
    public ResponseEntity<?> deletedCannel(Long channelId) {
        Channel channel = channelRepo.findById(channelId)
                .orElseThrow(() -> new NotFoundException("Channel not found"));
        List<PodcastDocument> documents = podcastDocRepo.findByChannelName(channel.getChannelName());
        List<Podcast> podcasts = podcastRepo.findByChannel(channel);

        podcastRepo.deleteAll(podcasts);

        podcastDocRepo.deleteAll(documents);

        channelRepo.delete(channel);

        return ResponseEntity.ok("Channel and related podcasts deleted successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatedCannel(ChannelRequest channelRequest, Long channelId) {
        Channel channel = channelRepo.findById(channelId)
                .orElseThrow(() -> new NotFoundException("Channel not found"));

        List<PodcastDocument> oldDocuments = podcastDocRepo.findByChannelName(channel.getChannelName());
        List<Podcast> oldPodcasts = podcastRepo.findByChannel(channel);

        channel.setChannelAuthor(channelRequest.getChannelAuthor());
        channel.setChannelName(channelRequest.getChannelName());
        channelRepo.save(channel);

        List<Podcast> updatedPodcasts = oldPodcasts.stream()
                .peek(p -> p.setChannel(channel))
                .collect(Collectors.toList());
        podcastRepo.saveAll(updatedPodcasts);


        List<PodcastDocument> updatedDocuments = oldDocuments.stream()
                .peek(doc -> {
                    doc.setChannelName(channel.getChannelName());
                    doc.setChannelAuthor(channel.getChannelAuthor());
                })
                .collect(Collectors.toList());
        podcastDocRepo.saveAll(updatedDocuments);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Channel updated successfully");
    }
}

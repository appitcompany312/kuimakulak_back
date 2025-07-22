package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastRequest;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastResponse;
import org.appitcompany.kuimakulak.entity.Channel;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.exceptions.CustomAlreadyExistsException;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.PodcastMapper;
import org.appitcompany.kuimakulak.repository.ChannelRepo;
import org.appitcompany.kuimakulak.repository.PodcastDocRepo;
import org.appitcompany.kuimakulak.repository.PodcastRepo;
import org.appitcompany.kuimakulak.service.PodcastService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PodcastServiceImpl implements PodcastService {
    private final PodcastRepo podcastRepo;
    private final ChannelRepo channelRepo;
    private final PodcastDocRepo podcastDocRepo;
    @Override
    @Transactional
    public ResponseEntity<?> save(PodcastRequest podcastRequest) {
        Podcast podcast = podcastRepo.findByPodcastName(podcastRequest.getPodcastName())
                .stream().findFirst().orElse(null);
        if (podcast != null) {
            throw new CustomAlreadyExistsException("Podcast with name " + podcastRequest.getPodcastName() + " already exists");
        }
        Channel channel = channelRepo.findByChannelName(podcastRequest.getChannelName())
                .stream()
                .findFirst()
                .orElse(null);
        if (channel == null) {
            throw new NotFoundException("channel not found! Add channel first!!! " + channel); }
        Podcast newPodcast = new Podcast();
        newPodcast.setPodcastName(podcastRequest.getPodcastName());
        newPodcast.setDescription(podcastRequest.getDescription());
        newPodcast.setChannel(channel);
        newPodcast.setAudioUrl(podcastRequest.getAudioUrl());
        newPodcast.setPublicationDate(LocalDate.now());
        newPodcast.setBannerUrl(podcastRequest.getBannerUrl());
        Podcast save = podcastRepo.save(newPodcast);
        PodcastDocument podcastDocument = PodcastMapper.toPodcastDocument(save);
        podcastDocRepo.save(podcastDocument);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Podcast has been saved successfully");
    }

    @Override
    public List<PodcastDocument> getAllPodcastDoc(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return podcastDocRepo.findAll(pageable).stream().toList();
    }

    @Override
    public PaginationResponse<PodcastResponse> getPodcastsByChannelName(int pageNumber, int pageSize, String channelName) {
        int offset = (pageNumber-1)*pageSize;
        List<PodcastDocument> byChannelName = podcastDocRepo.findByChannelName(channelName)
                .stream()
                .sorted(Comparator.comparingLong(PodcastDocument::getPublicationDate).reversed())
                .toList();
        int totalElements = byChannelName.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        List<PodcastResponse> list = byChannelName.stream()
                .skip(offset)
                .limit(pageSize)
                .map(doc -> {
                    PodcastResponse repo = new PodcastResponse();
                    repo.setId(doc.getId());
                    repo.setPodcastName(doc.getPodcastName());
                    repo.setBannerUrl(doc.getBannerUrl());
                    repo.setAudioUrl(doc.getAudioUrl());
                    repo.setChannelName(doc.getChannelName());
                    repo.setChannelAuthor(doc.getChannelAuthor());
                    return repo;
                }).toList();
        return PaginationResponse.<PodcastResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(list)
                .build();
    }
}

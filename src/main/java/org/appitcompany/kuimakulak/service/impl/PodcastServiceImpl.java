package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.*;
import org.appitcompany.kuimakulak.entity.Channel;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.CustomAlreadyExistsException;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.PodcastMapper;
import org.appitcompany.kuimakulak.jpaRepository.ChannelRepo;
import org.appitcompany.kuimakulak.elasticRepository.PodcastDocRepo;
import org.appitcompany.kuimakulak.jpaRepository.PodcastRepo;
import org.appitcompany.kuimakulak.jpaRepository.UserRepository;
import org.appitcompany.kuimakulak.service.PodcastService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PodcastServiceImpl implements PodcastService {
    private final PodcastRepo podcastRepo;
    private final ChannelRepo channelRepo;
    private final PodcastDocRepo podcastDocRepo;
    private final UserRepository userRepository;

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
            throw new NotFoundException("channel not found! Add channel first!!! " + podcastRequest.getChannelName());
        }
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
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return podcastDocRepo.findAll(pageable).stream().toList();
    }

    @Override
    public PaginationResponse<PodcastResponse> getPodcastsByChannelName(int pageNumber, int pageSize, String channelName) {
        User user = getCurrentUser();
        int offset = (pageNumber - 1) * pageSize;
        List<PodcastDocument> byChannelName = podcastDocRepo.findByChannelName(channelName)
                .stream()
                .sorted(Comparator.comparing(PodcastDocument::getPublicationDate).reversed())
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
                    repo.setHistory(doc.getUserIds() != null && doc.getUserIds().contains(user.getId()));
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

    @Override
    public PodcastResponseById findById(Long podcastId) {
        User user = getCurrentUser();
        Podcast podcast = podcastRepo.findByIdOrElseThrow(podcastId);
        PodcastDocument podcastDocument = podcastDocRepo.findById(podcastId).orElse(null);
        if (podcastDocument == null) {
            PodcastDocument podcastDoc = PodcastMapper.toPodcastDocument(podcast);
            getPodcastResponseById(podcastDoc, user.getId());
        }
        assert podcastDocument != null;
        return getPodcastResponseById(podcastDocument, user.getId());
    }

    @Override
    public PaginationResponse<AllPodcastResponse> getAllPodcast(int pageNumber, int pageSize) {
        User user = getCurrentUser();
        int offset = (pageNumber - 1) * pageSize;
        Iterable<PodcastDocument> all = podcastDocRepo.findAll();
        List<PodcastDocument> list = StreamSupport
                .stream(all.spliterator(), false)
                .sorted(Comparator.comparing(PodcastDocument::getPublicationDate).reversed())
                .toList();
        int totalElements = list.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        List<AllPodcastResponse> allResponses = list.stream()
                .skip(offset)
                .limit(pageSize)
                .map(doc -> {
                    AllPodcastResponse response = new AllPodcastResponse();
                    response.setId(doc.getId());
                    response.setPodcastName(doc.getPodcastName());
                    response.setBannerUrl(doc.getBannerUrl());
                    response.setAudioUrl(doc.getAudioUrl());
                    response.setChannelName(doc.getChannelName());
                    response.setChannelAuthor(doc.getChannelAuthor());
                    return response;
                })
                .toList();
        return PaginationResponse.<AllPodcastResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(allResponses)
                .build();
    }
    @Override
    public ResponseEntity<?> deleted(Long podcastId) {
        Podcast podcast = podcastRepo.findByIdOrElseThrow(podcastId);
        PodcastDocument doc = podcastDocRepo.findById(podcast.getId())
                .orElseThrow(() -> new NotFoundException("Podcast document not found"));
        String bannerUrl = podcast.getBannerUrl();
        String audioUrl = podcast.getAudioUrl();

        podcastRepo.delete(podcast);
        podcastDocRepo.delete(doc);
        return ResponseEntity.ok().body("Podcast deleted successfully");
    }

    @Override
    public ResponseEntity<?> updated(UpdatedPodcastRequest updatedRequest, Long podcastId) {
        Podcast podcast = podcastRepo.findByIdOrElseThrow(podcastId);
        boolean exists = podcastRepo.existsByPodcastNameAndIdNot(
                updatedRequest.getPodcastName(), podcastId);
        if (exists) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Podcast name already exists");
        }
        PodcastDocument doc = podcastDocRepo.findById(podcast.getId())
                .orElseThrow(() -> new NotFoundException("Podcast document not found"));
        podcast.setPodcastName(updatedRequest.getPodcastName());
        podcast.setBannerUrl(updatedRequest.getBannerUrl());
        podcast.setDescription(updatedRequest.getDescription());
        podcast.setAudioUrl(updatedRequest.getAudioUrl());
        podcastRepo.save(podcast);
        doc.setPodcastName(podcast.getPodcastName());
        doc.setBannerUrl(podcast.getBannerUrl());
        doc.setAudioUrl(podcast.getAudioUrl());
        doc.setDescription(podcast.getDescription());
        podcastDocRepo.save(doc);
        return ResponseEntity.ok("Podcast updated successfully");
    }

    @Override
    public Map<String, List<PodcastResponse>> getPodcastCategory() {
        List<Podcast> podcasts = podcastRepo.findAllWithChannel();
        return podcasts.stream()
                .collect(Collectors.groupingBy(
                        podcast -> podcast.getChannel().getChannelName(),
                        Collectors.mapping(
                                podcast -> {
                                    PodcastResponse response = new PodcastResponse();
                                    response.setId(podcast.getId());
                                    response.setPodcastName(podcast.getPodcastName());
                                    response.setAudioUrl(podcast.getAudioUrl());
                                    response.setBannerUrl(podcast.getBannerUrl());
                                    response.setChannelName(podcast.getChannel().getChannelName());
                                    response.setChannelAuthor(podcast.getChannel().getChannelAuthor());
                                    response.setHistory(!podcast.getHistory().isEmpty());
                                    return response;
                                },
                                Collectors.toList()
                        )
                ));
    }


    private PodcastResponseById getPodcastResponseById(PodcastDocument podcastDocument, Long userId) {
        return PodcastResponseById.builder()
                .id(podcastDocument.getId())
                .podcastName(podcastDocument.getPodcastName())
                .description(podcastDocument.getDescription())
                .bannerUrl(podcastDocument.getBannerUrl())
                .audioUrl(podcastDocument.getAudioUrl())
                .channelName(podcastDocument.getChannelName())
                .channelAuthor(podcastDocument.getChannelAuthor())
                .averageRating(podcastDocument.getAverageRating())
                .isHistory(podcastDocument.getUserIds() != null && podcastDocument.getUserIds().contains(userId))
                .publicationDate(podcastDocument.getPublicationDate())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getUserByEmailOrElseThrow(email);
    }
}

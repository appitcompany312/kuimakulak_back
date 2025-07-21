package org.appitcompany.kuimakulak.mapper;

import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.entity.Rating;

import java.time.ZoneId;

public class PodcastMapper {
    public static PodcastDocument toPodcastDocument(Podcast podcast) {
        PodcastDocument podcastDoc = new PodcastDocument();
     podcastDoc.setPodcastName(podcast.getPodcastName());
     podcastDoc.setDescription(podcast.getDescription());
     podcastDoc.setId(podcast.getId());
     podcastDoc.setPublicationDate(podcast.getPublicationDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
     podcastDoc.setBannerUrl(podcast.getBannerUrl());
     podcastDoc.setAudioUrl(podcast.getAudioUrl());
     podcastDoc.setChannelName(podcast.getChannel().getChannelName());
     podcastDoc.setChannelAuthor(podcast.getChannel().getChannelAuthor());
     podcastDoc.setFavoriteCount(podcast.getFavorites().size());

        if (podcast.getRatings() != null && !podcast.getRatings().isEmpty()) {
            double avg = podcast.getRatings().stream()
                    .filter(r -> r.getRating() != null)
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
            podcastDoc.setAverageRating(avg);
        } else {
            podcastDoc.setAverageRating(0.0);
        }

        return podcastDoc;
    }
}

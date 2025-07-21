package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepo extends JpaRepository<Channel, Long> {
    List<Channel> findByChannelName(String channelName);
}

package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteRepo extends JpaRepository<Favorite, Long> {

}

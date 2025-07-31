package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteRepo extends JpaRepository<Favorite, Long> {

}

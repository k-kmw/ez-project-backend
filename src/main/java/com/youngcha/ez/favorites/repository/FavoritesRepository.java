package com.youngcha.ez.favorites.repository;

import com.youngcha.ez.favorites.dto.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites,Long> {

    List<Favorites> findByReport_ReportId(Long reportId);
    List<Favorites> findByMember_UserId(String userId);
    Favorites findByMember_UserIdAndReport_ReportId(String userId, Long reportId);

}

package com.youngcha.ez.favorites.service;

import com.youngcha.ez.favorites.dto.Favorites;
import com.youngcha.ez.favorites.dto.FavoritesConverter;
import com.youngcha.ez.favorites.repository.FavoritesRepository;
import com.youngcha.ez.member.domain.dto.MemberRequest;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.report.dto.Report;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    public List<Favorites.FavoritesDto> findFavorites(String userId){
        List<Favorites> favoritesList = favoritesRepository.findByMember_UserId(userId);
        return FavoritesConverter.entityListToDtoList(favoritesList);
    }

    public boolean joinFavorites(Member member, Report report){
        Favorites favorites = Favorites
                                .builder()
                                .member(member)
                                .report(report)
                                .build();
        Favorites saved = favoritesRepository.save(favorites);
        if(saved == null || saved.getId() == null) return false;
        return true;
    }

    @Transactional
    public boolean deleteFavorites(Member member, Report report){
        Favorites favorites = favoritesRepository.findByMember_UserIdAndReport_ReportId(member.getUserId(), report.getReportId());
        favoritesRepository.delete(favorites);
        return true;
    }
}

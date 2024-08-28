package com.youngcha.ez.favorites.dto;

import com.youngcha.ez.report.dto.ReportConverter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesConverter {

    public static Favorites.FavoritesDto entityToDto(Favorites favorites){
        return Favorites.FavoritesDto
                .builder()
                .report(ReportConverter.reportToReportDto(favorites.getReport()))
                .build();
    }

    public static List<Favorites.FavoritesDto> entityListToDtoList(List<Favorites> favoritesList){
        List<Favorites.FavoritesDto> list = new ArrayList<>();
        for(Favorites favorites : favoritesList){
            list.add(entityToDto(favorites));
        }
        return list;
    }
}

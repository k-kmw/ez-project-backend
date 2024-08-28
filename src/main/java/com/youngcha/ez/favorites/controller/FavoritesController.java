package com.youngcha.ez.favorites.controller;

import com.youngcha.ez.favorites.dto.DeleteFavoritesReq;
import com.youngcha.ez.favorites.dto.Favorites;
import com.youngcha.ez.favorites.dto.JoinFavoritesReq;
import com.youngcha.ez.favorites.service.FavoritesService;
import com.youngcha.ez.member.domain.dto.MemberRequest;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.infrastructure.MemberService;
import com.youngcha.ez.report.dto.Report;
import com.youngcha.ez.report.dto.ReportDto;
import com.youngcha.ez.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final MemberService memberService;
    private final ReportService reportService;

    @GetMapping()
    public ResponseEntity<?> findFavorites(){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId == null) {
            return new ResponseEntity<>("세션이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        List<Favorites.FavoritesDto> favorites = favoritesService.findFavorites(userId);

        return ResponseEntity.ok(favorites);
    }

    @PostMapping()
    public  ResponseEntity<?> joinFavorites(@RequestBody JoinFavoritesReq joinFavoritesReq){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId == null) {
            return new ResponseEntity<>("세션이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findById(userId);
        Report report = reportService.findReportById(joinFavoritesReq.getReportId());

        if(member == null || report == null){
            return new ResponseEntity<>("등록에 실패 했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boolean result = favoritesService.joinFavorites(member, report);
        if(result)return new ResponseEntity<>("성공적으로 등록 되었습니다.",HttpStatus.OK);
        else return new ResponseEntity<>("등록에 실패 했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFavorites(@RequestBody DeleteFavoritesReq deleteFavoritesReq){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId == null) {
            return new ResponseEntity<>("세션이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findById(userId);
        Report report = reportService.findReportById(deleteFavoritesReq.getReportId());

        if(member == null || report == null){
            return new ResponseEntity<>("등록에 실패 했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        favoritesService.deleteFavorites(member,report);

        return new ResponseEntity<>("성공적으로 삭제 되었습니다.",HttpStatus.OK);
    }

}

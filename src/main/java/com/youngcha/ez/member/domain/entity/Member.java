package com.youngcha.ez.member.domain.entity;

import com.youngcha.ez.member.domain.dto.MemberRequest.UserInfoUpdateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 10)
    private String username;
    @Column(nullable = false, length = 25)
    private String email;

    @Column(nullable = false, length = 10, unique = true)
    private String userId;
    @Column(nullable = false)
    private String password;
    private String role;
    private String profileImageUrl;

    public void updateInfo(UserInfoUpdateDTO updateDTO) throws Exception {

        if(!updateDTO.getPassword().equals(updateDTO.getPasswordCheck())) {
            throw new Exception("비밀번호와 비밀번호 확인이 다릅니다.");
        }

        this.username = updateDTO.getUsername();
        this.password = updateDTO.getPassword();
        this.email = updateDTO.getEmail();
    }
}

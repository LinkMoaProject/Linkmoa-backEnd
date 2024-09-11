package com.linkmoa.source.domain.member.entity;

import com.linkmoa.source.domain.directory.entity.Directory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(name="member_email")
    private String email;

    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;

    @Column(name="member_nickname")
    private String nickname;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    @OneToMany(
            mappedBy = "member"
    )
    @Column(name="directory_id")
    private List<Directory> directory = new ArrayList<>();


    @Builder
    public Member(String email,String password,Role role,String nickname,String provider,String providerId){
        this.email=email;
        this.password=password;
        this.role=role;
        this.nickname = nickname;
        this.provider=provider;
        this.providerId=providerId;
    }

    public void updateMember(Member member) {
        if (member.getEmail() != null) {
            this.email = member.getEmail();
        }

        if (member.getNickname() != null) {
            this.nickname = member.getNickname();
        }

        if (member.getRole() != null) {
            this.role = member.getRole();
        }
    }

}

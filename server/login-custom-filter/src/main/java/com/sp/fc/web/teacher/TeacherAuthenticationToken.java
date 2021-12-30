package com.sp.fc.web.teacher;

import com.sp.fc.web.student.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 해당 도메인의 student를 사용할 수 있는 통행증 (사이트 이용 시 받게 될 통행증)
public class TeacherAuthenticationToken implements Authentication {

    private Teacher principal;
    private String credentials;
    private String details;
    private boolean authenticated;  // 도장을 받을 장소(인증)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // principal은 GrantedAuthority를 가지고 있음. 없다면 생성
        return principal == null ? new HashSet<>() : principal.getRole();
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUser_name();
    }
}

package com.sp.fc.web.student;

// student 통행증 발급 (principal)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    private String id;
    private String user_name;
    // 인증을 하려면 GrantedAuthority() 필요, 사이트에서 student는 ROLE_STUDENT를 가지고 있어야 접근 가능하기 때문
    private Set<GrantedAuthority> role;

}

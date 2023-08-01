package com.indi.project.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserEditInfoDto {

    private String name;
    private String nickName;
    private MultipartFile profileImage;

    public UserEditInfoDto(){}
}

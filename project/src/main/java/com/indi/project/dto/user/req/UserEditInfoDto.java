package com.indi.project.dto.user.req;

import com.indi.project.entity.User;
import lombok.*;
import org.springframework.lang.Nullable;
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

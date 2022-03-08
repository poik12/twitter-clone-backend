package com.jd.twitterclonebackend.mapper.mapstruct;

import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.service.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Mapper(
        componentModel = "spring",
        imports = {UserRole.class}
)
public abstract class NewAuthMapper {

    protected final String DEFAULT_PROFILE_PICTURE_PATH = "src/main/resources/images/default_profile_picture_twitter.png";
    protected final String DEFAULT_BACKGROUND_PICTURE_PATH = "src/main/resources/images/default_background_picture_twitter.png";

    @Autowired
    protected FileService fileService;
    @Autowired
    protected PasswordEncoder passwordEncoder;


    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerRequestDto.getPassword()))")
    @Mapping(target = "userRole", constant = "ROLE_USER")
    @Mapping(target = "enabled", constant = "false")
//    @Mapping(target = "followerNo", expression = "java(0L)")
//    @Mapping(target = "followingNo", expression = "java(0L)")
//    @Mapping(target = "tweetNo", expression = "java(0L)")
//    @Mapping(target = "description", expression = "java(null)")
    @Mapping(target = "profilePicture",
            expression = "java(fileService.convertImagePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH))")
    @Mapping(target = "backgroundPicture",
            expression = "java(fileService.convertImagePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH))")
    public abstract UserEntity mapFromDtoToEntity(RegisterRequestDto registerRequestDto);

}

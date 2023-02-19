package com.txt.mongoredis.services.mapper;

import com.txt.mongoredis.dto.UserInfoDTO;
import com.txt.mongoredis.entities.mongo.UserInfo;
import com.txt.mongoredis.services.generic.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserInfoMapper extends GenericMapper<UserInfo, UserInfoDTO> {
}

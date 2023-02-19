package com.txt.mongoredis.services.impl;

import com.txt.mongoredis.dto.UserInfoDTO;
import com.txt.mongoredis.entities.mongo.UserInfo;
import com.txt.mongoredis.repositories.mongo.UserInfoRepository;
import com.txt.mongoredis.services.UserServiceService;
import com.txt.mongoredis.services.generic.GenericMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceServiceImpl implements UserServiceService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    private GenericMapper<UserInfo, UserInfoDTO> userInfoMapper;

    @Override
    public List<UserInfoDTO> getListUser() {
        List<UserInfo> userInfos = userInfoRepository.findAll();

//        List<UserInfoDTO> dtos = new ArrayList<>();
//        for (UserInfo userInfo : userInfos) {
//            UserInfoDTO userInfoDTO = new UserInfoDTO();
//            BeanUtils.copyProperties(userInfo, userInfoDTO);
//            userInfoDTO.setId(userInfo.getId().toString());
//            dtos.add(userInfoDTO);
//        }

        List<UserInfoDTO> dtos = userInfos.stream().map(item -> userInfoMapper.toDTO(item)).collect(Collectors.toList());
        return dtos;
    }
}
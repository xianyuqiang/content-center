package com.itmuch.contentcenter.service.share;

import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.user.ShareDTO;
import com.itmuch.contentcenter.domain.dto.user.UserDTO;
import com.itmuch.contentcenter.domain.entity.content.Share;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: content-center
 * @description:
 * @author: xianyuqiang
 * @create: 2021-09-03 16:48
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {


    private final ShareMapper shareMapper;
    private final RestTemplate restTemplate;

    public ShareDTO findById(Integer id) {
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        String url = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/users/{id}")
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("当前没有示例！"));

//        log.info("url:{}", url);
        // 获取分享详情
        Share share = shareMapper.selectByPrimaryKey(id);
        UserDTO forObject = restTemplate.getForObject("http://user-center/users/{userId}", UserDTO.class, share.getUserId());

        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(forObject.getWxNickname());

        return shareDTO;
    }


}

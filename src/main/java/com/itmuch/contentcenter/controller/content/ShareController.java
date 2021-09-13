package com.itmuch.contentcenter.controller.content;

import com.itmuch.contentcenter.domain.dto.user.ShareDTO;
import com.itmuch.contentcenter.service.share.ShareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @program: content-center
 * @description:
 * @author: xianyuqiang
 * @create: 2021-09-03 17:15
 **/
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    public ShareDTO findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }

}

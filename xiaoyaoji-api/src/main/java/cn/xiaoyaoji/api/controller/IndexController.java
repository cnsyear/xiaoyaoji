package cn.xiaoyaoji.api.controller;

import cn.xiaoyaoji.service.XyjProperties;
import cn.xiaoyaoji.service.annotations.Ignore;
import cn.xiaoyaoji.service.common.HashMapX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoujingjie
 * @date 2016-07-21
 */
@RestController
@RequestMapping("/")
@Ignore
public class IndexController {
    @Autowired
    private XyjProperties xyjProperties;

    @GetMapping
    public Object index() {
        return new HashMapX<>()
                .append("v", xyjProperties.getVersion());
    }
}

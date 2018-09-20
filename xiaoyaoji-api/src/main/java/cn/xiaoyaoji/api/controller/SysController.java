package cn.xiaoyaoji.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhoujingjie
 * @Date: 17/4/2
 */
@RestController
@RequestMapping("/sys")
public class SysController {

   /* @Ignore
    @PostMapping("/update")
    public String update(){
        int rs = UpdateManager.getInstance().update(ConfigUtils.getProperty("xyj.version"));
        return "升级系统成功,更新条数:"+rs;
    }

    @Ignore
    @GetMapping("/version")
    public String version(){
        return ConfigUtils.getProperty("xyj.version");
    }

    @Ignore
    @PostMapping("/generateSiteMap")
    public Object post(@RequestParam String key){
        if(key.equals(ConfigUtils.getProperty("plugin.config.key"))){
            SiteMapTask.manualRunTask();
            return "success";
        }
        return "error";
    }*/
}

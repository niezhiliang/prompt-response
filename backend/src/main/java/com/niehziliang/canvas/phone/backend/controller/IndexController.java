package com.niehziliang.canvas.phone.backend.controller;

import com.niehziliang.canvas.phone.backend.entity.UploadMo;
import com.niehziliang.canvas.phone.backend.socket.SocketServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/12/18 上午11:28
 */
@RestController
public class IndexController {

    @RequestMapping(value = "upload")
    public Object upload(@RequestBody UploadMo uploadMo) {
       if (uploadMo.getBase64() != null && uploadMo.getUserid() != null) {
           SocketServer.sendMessage(uploadMo.getBase64(),uploadMo.getUserid());
       }
        Map<String,String> map  = new HashMap<String,String>();
        map.put("status","20");
        return map;
    }
}

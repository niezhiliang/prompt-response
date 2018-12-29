package com.niehziliang.canvas.phone.backend.controller;

import com.niehziliang.canvas.phone.backend.ImgUtils;
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
           //把base64文件逗号前面的截掉  图片旋转不能有文件前缀
           int index = uploadMo.getBase64().lastIndexOf(",");
           String imgPfefix = uploadMo.getBase64().substring(0,index+1);

           //得到要旋转的base64串
           String base64 = uploadMo.getBase64().replace(imgPfefix,"");
           //得到旋转后的图片base64 并加上前缀返回前端
           base64 = imgPfefix + ImgUtils.imgRoate(base64);
           System.out.println(base64);
           //websocket 发送给前端
           SocketServer.sendMessage(base64,uploadMo.getUserid());
       }
        Map<String,String> map  = new HashMap<String,String>();
        map.put("status","20");
        return map;
    }
}

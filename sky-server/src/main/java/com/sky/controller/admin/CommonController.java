package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "通用接口")
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {

        //没用上传云服务只是做个示例
        //正确的返回值应该是阿里云对象存储的url
        return Result.success("https://imgs.699pic.com/images/500/651/943.jpg!list1x.v2");

    }


}

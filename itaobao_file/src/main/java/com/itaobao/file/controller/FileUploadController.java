package com.itaobao.file.controller;

import entity.Result;
import entity.StatusCode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.IdWorker;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassNameFileUploadController
 * @Description
 * @Author ANGLE0
 * @Date2019/12/6 13:51
 * @Version V1.0
 **/
@CrossOrigin
@Controller
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private IdWorker idWorker;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result UploadImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        String fileLocation = uploadFile(request,  file);
        if (fileLocation != null && !fileLocation.equals("")){
            return new Result(true, StatusCode.OK, "文件上传成功", fileLocation);
        }else {
            return new Result(true, StatusCode.ERROR, "图片上传失败");
        }
    }

    public String uploadFile(HttpServletRequest request, MultipartFile file) throws IOException{
        //文件夹名
        String folderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ServletContext sc = request.getSession().getServletContext();
        String dir = sc.getRealPath("/data/files/"+folderName);
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1, file.getOriginalFilename().length());

        //文件名操作
        String newFileName = idWorker.nextId()+"."+type;

        //创建新文件
        FileUtils.writeByteArrayToFile(new File(dir, newFileName), file.getBytes());
        return "/data/files/"+folderName+"/"+newFileName;
    }
}

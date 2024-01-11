package com.blog.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class ImgUploader {

    private final Path uploadDir = Paths.get("src/main/resources/static/tempImg");

    /**
     * 로컬 경로에 저장
     */
    public String savdImg(MultipartFile multipartFile){
        String imageName = UUID.randomUUID().toString()+".png";
        Path imagePath = uploadDir.resolve(imageName);

        try {
            Files.copy(multipartFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        return "tempImg/"+imageName;
    }

    /**
     * 로컬에 저장된 파일을 지운다
     */
    private void removeFile(File targetFile){
        if(targetFile.delete()){
            log.info("파일 삭제 성공");
            return;
        }
        log.info("파일 삭제 실패");
    }

}

package com.blog.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class ImgUploader {

    /**
     * 로컬 경로에 저장
     */
    public String uploadFile(MultipartFile multipartFile){
        String dirPath = System.getProperty("user.dir")+"/"+UUID.randomUUID();

        try{
            convert(multipartFile,dirPath)
                    .orElseThrow(()-> new IllegalArgumentException("[error]: MultipartFile -> 파일 변환 실패"));
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return dirPath;
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


    /**
     * 로컬에 파일 업로드 및 변환
     */
    private Optional<File> convert(MultipartFile file,String dirPath) throws IOException{
        // 로컬에서 저장할 파일 경로 = dirPath
        File convertFile = new File(dirPath);

        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}

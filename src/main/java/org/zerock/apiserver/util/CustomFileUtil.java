package org.zerock.apiserver.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component//필요할 때마다 다운, 업로드
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    //썸네일 없을 경우 파일의 용량이 커져서 안됨
    //생성자 대신 초기화시킬때 많이 사용
    @PostConstruct
    public void init(){
        File tempFolder = new File(uploadPath);

        //만약 폴더 경로가 존재하지 않는다면 만들어 둬야한다
        if(tempFolder.exists() == false){

            tempFolder.mkdir();

        }

        uploadPath = tempFolder.getAbsolutePath();
        log.info("----------------------");
        log.info(uploadPath);


    }

    public List<String> saveFiles(List<MultipartFile> files) throws  RuntimeException{

        if(files == null || files.size() == 0){
            return null;
        }

        //실제로 업로드된 파일들
        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file : files){
            //같은 파일의 이름이 있을 수 있다 UUID사용
            String saveName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            //새로운 파일의 전체 파일 경로 가져오기
            Path savePath = Paths.get(uploadPath, saveName);

            //파일의 input stream을 저장 경로에 저장
            try {
                Files.copy(file.getInputStream(), savePath); //원본파일 업로드
                //이미지 인 경우 썸네일 만들기
                String contentType = file.getContentType(); // Mime type
                //이미지 파일이라면
                if(contentType != null || contentType.startsWith("image")){
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

                    Thumbnails.of(savePath.toFile()).size(200,200).toFile(thumbnailPath.toFile());
                }


                uploadNames.add(saveName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


       return uploadNames;
    }

    //파일 하나 조회
    public ResponseEntity<Resource> getFile(String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        if(!resource.isReadable()){

            resource = new FileSystemResource(uploadPath + File.separator + "32646eb9-744e-4896-8eba-125c32ee8fa2_전시 포스터_A2 (1).jpg");

        }

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);

    }


    //파일 삭제
    public void deleteFiles(List<String> fileNames){

        if(fileNames == null || fileNames.isEmpty()){
            return;
        }
        fileNames.forEach(fileName ->{

            //썸네일 삭제
            String thumbnailFileName = "s_" + fileName;

            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);

            //원본파일의 이름
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

        });


    }
}

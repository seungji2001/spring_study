package org.zerock.apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductDTO {

    //db 에 파일 저장하면 안됨
    //정말 중요한 파일 제외

    private Long pno;
    private String pnmae;
    private int price;
    private String pdesc;
    private boolean delFlag;

    //업로드할 파일들
    @Builder.Default
     private List<MultipartFile> files = new ArrayList<>();

    //이미 업로드 도힌파일 이름들
    @Builder.Default
    private List<String> uploadedFileNames = new ArrayList<>();

}

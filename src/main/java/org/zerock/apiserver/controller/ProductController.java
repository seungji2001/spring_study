package org.zerock.apiserver.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.service.ProductService;
import org.zerock.apiserver.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    //final로 하여 외부에서 불렀을 때 자동 주입으로 만든다.
    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName){
        return  fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDto<ProductDTO> list(PageRequestDto pageRequestDto){
        return productService.getList(pageRequestDto);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) throws InterruptedException {
        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadedFileNames(uploadFileNames);

        log.info(uploadFileNames);

        long pno = productService.register(productDTO);

        //시간 느리게 하게
        Thread.sleep(2000);

        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno){
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable Long pno, ProductDTO productDTO){

        productDTO.setPno(pno);
        //기존 파일
        ProductDTO oldProductDTO = productService.get(pno);
        //새로운 파일 업로드
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //keep files ->  파일 세개 중 한개만 삭제하고 나머지는 유지되고 있는 경우
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();

        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()){

            uploadedFileNames.addAll(currentUploadFileNames);

        }

        productService.modify(productDTO);

        //이전의 파일 지워야함
        List<String> oldFileNames = oldProductDTO.getUploadedFileNames();
        if(oldFileNames != null && !oldFileNames.isEmpty()){
            List<String> removeFiles = oldFileNames.stream().filter(
                    filename -> uploadedFileNames.indexOf(filename) == -1 //이전 파일들 중 업로드한 파일들(이전 + 새로운 파일)에 없는 경우

            ).collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("Result", "Success");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable Long pno){
        List<String> oldFileNames = productService.get(pno).getUploadedFileNames();
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);
        return Map.of("Result", "Success");
    }
}

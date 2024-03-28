package org.zerock.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDTO;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testList(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        PageResponseDto<ProductDTO> responseDto = productService.getList(pageRequestDto);

        log.info(responseDto.getDtoList());

    }

    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
                .pnmae("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(1000)
                .build();

        productDTO.setUploadedFileNames(
                List.of(UUID.randomUUID() + "_" + "Test1.jpg",
                        UUID.randomUUID() + "_" + "Test2.jpg")
        );

        productService.register(productDTO);
    }
}

package org.zerock.apiserver.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.PageRequestDto;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert(){
        Product product = Product.builder()
                .pname("test ")
                .pdesc("Test desc")
                .price(1000)
                .build();

        product.addImageString(UUID.randomUUID() + "_" + "Image1.jpg");
        product.addImageString(UUID.randomUUID() + "_" + "Image2.jpg");

        productRepository.save(product);
    }

    @Test
    @Transactional
    public void testRead(){
        Long pno = 1L;

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());
        //데이터 베이스 연결이 두번 일어진다 -> transactional이 필요하다 -> or join해서 사용
    }

    @Test
    public void testRead2(){
        Long pno = 1L;

        //한번의 쿼리만 일어난다
        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());
        //데이터 베이스 연결이 두번 일어진다 -> transactional이 필요하다 -> or join해서 사용
    }

    @Transactional
    @Commit
    @Test
    public void testDelete(){
        Long pno = 2L;
        productRepository.updateToDelete(2L, true);
    }

    // many to one보다 수월하다
    //한번에 같이 변경되는 경우는 elementCollection이 좋을 것 같다
    @Test
    public void testUpdate(){
        Product product = productRepository.selectOne(1L).get();
        product.changePrice(3000);
        product.clearList();

        product.addImageString(UUID.randomUUID() + "_"+"PIMAGE1.jpg");
        product.addImageString(UUID.randomUUID() + "_"+"PIMAGE2.jpg");
        product.addImageString(UUID.randomUUID() + "_"+"PIMAGE3.jpg");

        productRepository.save(product);
    }

    @Test
    public void testInsert10(){
        for(int i = 0; i<10; i++){
            Product product = Product.builder()
                    .pname("test " + i)
                    .pdesc("Test desc" + i)
                    .price(1000)
                    .build();

            product.addImageString(UUID.randomUUID() + "_" + "Image1.jpg");
            product.addImageString(UUID.randomUUID() + "_" + "Image2.jpg");

            productRepository.save(product);
        }
    }

    @Test
    public void testList(){
        Pageable pageable =  PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);


        System.out.println(result.getContent());

//        result.getContent().forEach(
//               r->{
//                   log.info(r.toString());
//               }
//        );


    }

    @Test
    public void testSearch(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        productRepository.searchList(pageRequestDto);
    }
}

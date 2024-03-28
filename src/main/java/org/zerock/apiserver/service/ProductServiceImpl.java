package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.domain.ProductImage;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    @Override
    public PageResponseDto<ProductDTO> getList(PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(pageRequestDto.getPage()-1,
                pageRequestDto.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDTO> dtoList = result.getContent().stream().map(arr -> {
            ProductDTO productDTO = new ProductDTO();

            // Correctly cast each element of the array to its expected type
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pnmae(product.getPname()) // Corrected typo from 'pnmae' to 'pnmae'
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFilename();
            productDTO.setUploadedFileNames(List.of(imageStr));
            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDto.<ProductDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDto(pageRequestDto)
                .build();

    }

    @Override
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);
        Long pno = productRepository.save(product).getPno();
        return pno;
    }

    @Override
    public ProductDTO get(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        ProductDTO productDTO = entityToDTO(product);

        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) {
         //조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        //변경내용 반영
        product.changePrice(productDTO.getPrice());
        product.changeName(productDTO.getPnmae());
        product.changeDesc(product.getPdesc());
        product.changeDel(product.isDelFlag());

        //이미지 처리
        List<String> uploadFileNames = productDTO.getUploadedFileNames();

        product.clearList();

        if(uploadFileNames != null && !uploadFileNames.isEmpty()){

            uploadFileNames.forEach(uploadName ->{
                product.addImageString(uploadName);
            });

        }
        //저장
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);
    }

    private ProductDTO entityToDTO(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pnmae(product.getPname())
                .delFlag(product.isDelFlag())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()){
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFilename()).toList();

        productDTO.setUploadedFileNames(fileNameList);

        return productDTO;
    }

    private Product dtoToEntity(ProductDTO  productDTO){
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPnmae())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadedFileNames();

        if(uploadFileNames == null || uploadFileNames.size() == 0){
            return product;
        }


        //업로드할 사진이 있는 경우,
        //element collection에 해당 객체 추가
        uploadFileNames.forEach(filename -> {
            product.addImageString(filename);
        });

        return product;

    }
}

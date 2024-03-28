package org.zerock.apiserver.service;

import jakarta.transaction.Transactional;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDTO;

@Transactional
public interface ProductService {
    PageResponseDto<ProductDTO> getList(PageRequestDto pageRequestDto);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);
}

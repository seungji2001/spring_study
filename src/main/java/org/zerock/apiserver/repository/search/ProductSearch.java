package org.zerock.apiserver.repository.search;

import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDto<ProductDTO> searchList(PageRequestDto pageRequestDto);

}

package org.zerock.apiserver.repository.search;

import org.springframework.data.domain.Page;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDto;

public interface TodoSearch {

    //query dsl을 쓰는 이유는 동적 쿼리를 날리기 위함이다
    //가령 전체 조회시 페이징 처리가 아닌, 조건(연령별, 색상별, 사이즈별)을 주었을 경우 전체 쿼리를 날리는것보다
    //query dsl을 사용하여 조건에 따라 페이징 처리를 다르게 하는것이 중요하다
    Page<Todo> search1(PageRequestDto pageRequestDto);
}

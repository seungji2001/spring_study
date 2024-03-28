package org.zerock.apiserver.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter;
import org.zerock.apiserver.domain.QTodo;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDto;

import java.util.List;

@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDto pageRequestDto) {

        log.info("search1...................");

        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = from(todo);

//        query.where(todo.title.contains("1")); //predicate -> true라 예상
        //pageRequestDto에 검색 조건이 있는 경우

        //스프링부트 3점대

        Pageable pageable = PageRequest.of(pageRequestDto.getPage() -1, pageRequestDto.getSize(), Sort.by("tno").descending());

        this.getQuerydsl().applyPagination(pageable,query);

        List<Todo> list = query.fetch(); //목록 데이터

        long total = query.fetchCount();

        return new PageImpl<>(list,pageable, total);
    }
}

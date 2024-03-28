package org.zerock.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.TodoDto;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;
    //자동 proxy생성 aop

    @Test
    public void testGet(){
        Long tno = 50L;

        log.info(todoService.get(tno));
    }
    @Test
    public void testRegister(){
        TodoDto todoDto = TodoDto.builder()
                .title("Title.....")
                .dueDate(LocalDate.of(2023,12,31))
                .content("content.......")
                .build();

        log.info(todoService.register(todoDto));
    }


    @Test
    public void testGetList(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        log.info(todoService.getList(pageRequestDto));

    }

}

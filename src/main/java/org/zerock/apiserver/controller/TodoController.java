package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.TodoDto;
import org.zerock.apiserver.service.TodoService;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDto get(@PathVariable("tno")Long tno){
        return todoService.get(tno);
    }

    //pathvariable -> /api/todo/33 전세계 동일하여 항상성을 유지
    //queryString -> list?page=3 -> 상황에 따라 다르게 보임
    @GetMapping("/list")
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto){
        log.info("List........" +  pageRequestDto);

        return todoService.getList(pageRequestDto);

        //http://localhost:8080/list/size=undefined
        //이경우 에러가나는데 이러한 경우를 위해서 @RestControllerAdvice가 필요하ㅊ
    }

    @PostMapping("/")
    public Map<String,Long> register(@RequestBody TodoDto dto){
        log.info("todoDto: " + dto);
        Long tno = todoService.register(dto);

        return Map.of("TNO",tno);
    }

    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno,
                                      @RequestBody TodoDto todoDto){
        todoDto.setTno(tno);
        todoService.modify(todoDto);

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable Long tno){
        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");
    }

//Cors설정 -> 외부에서 api 사용하기 때문에 설정을 해주어야한다





}

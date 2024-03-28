package org.zerock.apiserver.service;

import jakarta.transaction.Transactional;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.TodoDto;

@Transactional
public interface TodoService {
    //Service를 interface로 우선 적용하는 이유는 이후에 Aop적용시 유용

    // dto는 public
    TodoDto get(Long tno);

    //등록
    //-새로 등록된 정보 자체를 리턴
    //-pk가 몇번인지 반환
    Long register(TodoDto dto);

    void modify(TodoDto dto);

    void remove(Long tno);

    PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto);

    //java8 부터는 default라는 기능이나 메소드를 서비스에 정의 가능하다
    //entity를 dto로 전환하는 것 여기다 쓰면 일관성이 생겨서 좋다
    //인터페이스를 구현하는 클래스에서 해당 메서드를 오버라이드하지 않아도 되므로 인터페이스를
    // 확장하거나 수정하지 않고도 새로운 기능을 추가할 수 있습니다. 이것이 default 메서드의 장점 중 하나입니다.
    default TodoDto entityToDTO(Todo todo){
        return TodoDto.builder()
                        .tno(todo.getTno())
                        .title(todo.getTitle())
                        .complete(todo.isComplete())
                        .content(todo.getContent())
                        .dueDate(todo.getDueDate())
                        .build();
    }

    default Todo dtoToEntity(TodoDto todoDto){
        return Todo.builder()
                .tno(todoDto.getTno())
                .title(todoDto.getTitle())
                .complete(todoDto.isComplete())
                .content(todoDto.getContent())
                .dueDate(todoDto.getDueDate())
                .build();
    }
}

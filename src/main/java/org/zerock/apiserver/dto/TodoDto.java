package org.zerock.apiserver.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor //json변환 위한 비어있는 생성자 필요
public class TodoDto {
    private Long tno;
    private String title;
    private String content;
    private boolean complete;
    private LocalDate dueDate;
}

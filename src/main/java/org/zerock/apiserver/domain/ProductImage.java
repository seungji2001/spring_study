package org.zerock.apiserver.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String filename;
    private int ord; //상품의 순번
    public  void setOrd(int ord){
        this.ord = ord;
    }
}

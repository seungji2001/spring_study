package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void changePrice(int price) {
        this.price = price;
    }
    public void changeDesc(String desc){
        this.pdesc = desc;
    }
    public void changeName(String name){
        this.pname = name;
    }

    public void changeDel(boolean delFlag){
       this.delFlag = delFlag;
    }


    //상품 이미지 추가
    //트랜잭션 처리 없이 image까지 product_img로 저장이 된다
    public void addImage(ProductImage image) {
        //순번값
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }
    public void addImageString(String fileName){
        ProductImage productImage = ProductImage.builder()
                .filename(fileName)
                .build();
        addImage(productImage);
    }
    public void clearList() {
        this.imageList.clear();
    }

}

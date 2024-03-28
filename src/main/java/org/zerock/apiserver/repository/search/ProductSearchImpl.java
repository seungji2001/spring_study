package org.zerock.apiserver.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.domain.QProduct;
import org.zerock.apiserver.domain.QProductImage;
import org.zerock.apiserver.dto.PageRequestDto;
import org.zerock.apiserver.dto.PageResponseDto;
import org.zerock.apiserver.dto.ProductDTO;

import java.util.List;
import java.util.Objects;


@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDto<ProductDTO> searchList(PageRequestDto pageRequestDto) {
        log.info("------------------ search list --------------------");

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);

        //join으로 해도 좋으나, 상품인데 이미지가 없는 경우를 고려
        //productImage는 entity가 아님
        //product.imageList를 productImage로 간주
        query.leftJoin(product.imageList, productImage);

        query.where(productImage.ord.eq(0));

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);



        List<Tuple> productList = query.select(product, productImage).fetch();

        long count = query.fetchCount();

        log.info("===================================");
        log.info(productList);

        return null;
    }
}

package com.example.cafeproject.domain.product.service;

import com.example.cafeproject.domain.product.consts.ProductStatus;
import com.example.cafeproject.domain.product.dto.GetProductResponse;
import com.example.cafeproject.domain.product.dto.PopularMenuDto;
import com.example.cafeproject.domain.product.dto.PopularProductResponse;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.infrastructure.redis.PopularMenuRedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PopularMenuRedisService popularMenuRedisService;

    @Test
    @DisplayName("전체 메뉴 목록을 반환한다")
    void getAllProducts_returns_all() {
        List<Product> products = List.of(
                new Product("아메리카노", new BigDecimal("4500"), 10L, ProductStatus.ON_SALE),
                new Product("딸기 스무디", new BigDecimal("6500"), 0L, ProductStatus.SOLD_OUT)
        );
        given(productRepository.findAll()).willReturn(products);

        List<GetProductResponse> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("아메리카노");
        assertThat(result.get(1).getName()).isEqualTo("딸기 스무디");
    }

    @Test
    @DisplayName("Redis에 인기 메뉴 데이터가 없으면 빈 리스트를 반환한다")
    void getPopularProducts_empty_when_no_redis_data() {
        given(popularMenuRedisService.getTopProducts(3)).willReturn(List.of());

        List<PopularProductResponse> result = productService.getPopularProducts();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("인기 메뉴를 순위 순서대로 반환한다")
    void getPopularProducts_returns_ranked() {
        Product americano = new Product("아메리카노", new BigDecimal("4500"), 10L, ProductStatus.ON_SALE);
        Product latte = new Product("카페라떼", new BigDecimal("5000"), 8L, ProductStatus.ON_SALE);
        Product cappuccino = new Product("카푸치노", new BigDecimal("5000"), 6L, ProductStatus.ON_SALE);

        // 리플렉션으로 ID 설정
        setId(americano, 1L);
        setId(latte, 2L);
        setId(cappuccino, 3L);

        List<PopularMenuDto> topMenus = List.of(
                new PopularMenuDto(1L, 100L),
                new PopularMenuDto(2L, 80L),
                new PopularMenuDto(3L, 60L)
        );

        given(popularMenuRedisService.getTopProducts(3)).willReturn(topMenus);
        given(productRepository.findAllById(List.of(1L, 2L, 3L)))
                .willReturn(List.of(americano, latte, cappuccino));

        List<PopularProductResponse> result = productService.getPopularProducts();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getRank()).isEqualTo(1);
        assertThat(result.get(0).getProductName()).isEqualTo("아메리카노");
        assertThat(result.get(0).getOrderCount()).isEqualTo(100L);
        assertThat(result.get(1).getRank()).isEqualTo(2);
        assertThat(result.get(2).getRank()).isEqualTo(3);
    }

    @Test
    @DisplayName("Redis에는 있지만 DB에 없는 상품은 결과에서 제외된다")
    void getPopularProducts_skips_missing_product() {
        Product americano = new Product("아메리카노", new BigDecimal("4500"), 10L, ProductStatus.ON_SALE);
        setId(americano, 1L);

        List<PopularMenuDto> topMenus = List.of(
                new PopularMenuDto(1L, 100L),
                new PopularMenuDto(999L, 80L)  // DB에 없는 상품
        );

        given(popularMenuRedisService.getTopProducts(3)).willReturn(topMenus);
        given(productRepository.findAllById(List.of(1L, 999L)))
                .willReturn(List.of(americano)); // 999L은 없음

        List<PopularProductResponse> result = productService.getPopularProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName()).isEqualTo("아메리카노");
    }

    private void setId(Product product, Long id) {
        try {
            var field = Product.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
        }
        try {
            var field = Product.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(product, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
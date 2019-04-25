package cz.muni.fi.pv260.productfilter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceLessThanFilterTest {

    @Test
    void testPasses() {
        new PriceLessThanFilter(BigDecimal.valueOf(100));
        Product product = new Product(1, "Test product", Color.RED, BigDecimal.valueOf(100));
        assertThat(new PriceLessThanFilter(BigDecimal.valueOf(50)).passes(product)).isFalse();
        assertThat(new PriceLessThanFilter(BigDecimal.valueOf(100)).passes(product)).isTrue();
        assertThat(new PriceLessThanFilter(BigDecimal.valueOf(200)).passes(product)).isTrue();
    }
}
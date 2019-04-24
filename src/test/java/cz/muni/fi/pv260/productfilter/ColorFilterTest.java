package cz.muni.fi.pv260.productfilter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ColorFilterTest {

    @Test
    void testPasses() {
        Filter filter = new ColorFilter(Color.RED);

        assertThat(filter.passes(new Product(1, "Name", Color.RED, BigDecimal.ONE))).isTrue();
        assertThat(filter.passes(new Product(1, "Name", Color.BLACK, BigDecimal.ONE))).isFalse();

    }
}
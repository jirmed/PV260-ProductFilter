package cz.muni.fi.pv260.productfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtLeastNOfFilterTest {

    @Mock
    private Filter<Object> filter1;

    @Mock
    private Filter<Object> filter2;

    @Mock
    private Filter<Object> filter3;

    private AtLeastNOfFilter<Object> atLeastNOfFilter;

    @Test
    void testConstructor() {
        atLeastNOfFilter = new AtLeastNOfFilter<>(1, filter1, filter2, filter3);

        assertThat(atLeastNOfFilter).isNotNull();
    }

    @Test
    void testConstructorThrowsExceptionOnZeroRequiredFilters() {
        assertThatThrownBy(() ->
                atLeastNOfFilter = new AtLeastNOfFilter<>(0, filter1, filter2, filter3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testConstructorThrowsExceptionOnNegativeRequiredFilters() {
        assertThatThrownBy(() ->
                atLeastNOfFilter = new AtLeastNOfFilter<>(-1, filter1, filter2, filter3))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void testConstructorThrowsExceptionOnTooManyRequiredFilters() {
        assertThatThrownBy(() ->
                atLeastNOfFilter = new AtLeastNOfFilter<>(4, filter1, filter2, filter3))
                .isInstanceOf(FilterNeverSucceeds.class);
    }

    @Test
    void testPassesIfExactlyNTestPasses() {
        Object objectToFilter = new Object();
        mockFilters(objectToFilter);

        atLeastNOfFilter = new AtLeastNOfFilter<>(2, filter1, filter2, filter3);
        assertThat(atLeastNOfFilter.passes(objectToFilter)).isTrue();

    }

    @Test
    void testPassesIfMoreThanNTestPasses() {
        Object objectToFilter = new Object();
        mockFilters(objectToFilter);

        atLeastNOfFilter = new AtLeastNOfFilter<>(1, filter1, filter2, filter3);
        assertThat(atLeastNOfFilter.passes(objectToFilter)).isTrue();

    }

    @Test
    void testNotPassesIfLessThanNTestPasses() {
        Object objectToFilter = new Object();
        mockFilters(objectToFilter);

        atLeastNOfFilter = new AtLeastNOfFilter<>(3, filter1, filter2, filter3);
        assertThat(atLeastNOfFilter.passes(objectToFilter)).isFalse();

    }


    private void mockFilters(Object objectToFilter) {
        when(filter1.passes(objectToFilter)).thenReturn(true);
        when(filter2.passes(objectToFilter)).thenReturn(false);
        when(filter3.passes(objectToFilter)).thenReturn(true);
    }
}
package cz.muni.fi.pv260.productfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

    private Input inputMock;

    @Mock
    private Output outputMock;

    @Mock
    private Logger loggerMock;

    @Mock
    private Filter<Product> filterMock;

    private Product product1;
    private Product product2;
    private Product product3;

    @Test
    void testNullInputThrowsException() {
        assertThatThrownBy(() ->
                new Controller(null, outputMock, loggerMock)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testNullOutputThrowsException() {
        assertThatThrownBy(() ->
                new Controller(inputMock, null, loggerMock)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testNullLoggerThrowsException() {
        assertThatThrownBy(() ->
                new Controller(inputMock, outputMock, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testSelect() {

        initMocks();
        Controller controller = new Controller(inputMock, outputMock, loggerMock);

        controller.select(filterMock);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(outputMock).postSelectedProducts(captor.capture());
        assertThat(captor.getValue()).containsExactly(product1, product2);
        verify(loggerMock).setLevel("INFO");
        verify(loggerMock)
                .log(Controller.TAG_CONTROLLER, "Successfully selected 2 out of 3 available products.");
    }

    @Test
    void testSelectInputException() {
        ObtainFailedException inputException = new ObtainFailedException();
        inputMock = () -> {
            throw inputException;
        };
        Controller controller = new Controller(inputMock, outputMock, loggerMock);
        controller.select(filterMock);
        verify(outputMock, never()).postSelectedProducts(any());
        verify(loggerMock).setLevel("ERROR");
        verify(loggerMock)
                .log(Controller.TAG_CONTROLLER, "Filter procedure failed with exception: "
                        + inputException);
    }

    private void initMocks() {
        product1 = new Product(1, "Black product", Color.BLACK, BigDecimal.valueOf(123));
        product2 = new Product(1, "Blue product", Color.BLUE, BigDecimal.valueOf(456));
        product3 = new Product(1, "Red product", Color.RED, BigDecimal.valueOf(999));
        inputMock = () -> Arrays.asList(product1, product2, product3);
        doReturn(true).when(filterMock).passes(product1);
        doReturn(true).when(filterMock).passes(product2);
        doReturn(false).when(filterMock).passes(product3);
    }
}
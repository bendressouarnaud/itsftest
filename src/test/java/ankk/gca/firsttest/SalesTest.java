package ankk.gca.firsttest;

import ankk.gca.firsttest.beans.*;
import ankk.gca.firsttest.services.Sales;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SalesTest {

    // Attributes
    @InjectMocks
    Sales sales;


    // Methods
    @Test
    void test(){
        BigDecimal bd = BigDecimal.valueOf(12.1560);
        bd = sales.arrondirCents(bd);
        assertEquals(12.2, bd.doubleValue());
    }

    @Test
    void testReceipt(){
        List<Achat> ensemble = new ArrayList<>();

        Food fd = new Food();
        fd.setLibelle("imported box of chocolates");
        fd.setPrix(10.00);
        fd.setImported(true);
        ensemble.add(new Achat(1, fd));

        OtherGoods os = new OtherGoods();
        os.setLibelle("imported bottle of perfume");
        os.setPrix(47.50);
        os.setImported(true);
        ensemble.add(new Achat(1, os));

        //
        Receipt receipt = sales.computePrice(ensemble);
        assertEquals(65.15, receipt.getTotalPrice());
    }

}

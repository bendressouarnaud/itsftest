package ankk.gca.firsttest.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Receipt {
    List<Achat> liste;
    Double totalTax;
    Double totalPrice;
}

package ankk.gca.firsttest.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Achat<T> {
    int quantite;
    @Getter
    T article;

    public Achat(int quantite, T article) {
        this.quantite = quantite;
        this.article = article;
    }
}

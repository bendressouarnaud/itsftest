package ankk.gca.firsttest.services;

import ankk.gca.firsttest.beans.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class Sales{

    // A t t r i b u t e s :
    private static final BigDecimal ROUNDING_FACTOR = new BigDecimal("0.05");
    private static final String IMPORTEDWORD = "imported";

    List<Achat> ensemble = new ArrayList<>();
    Double importDutyTax = 0.05 ;
    Double basicSalesTax = 0.10 ;
    Double totalImportDutyTax = 0.0;
    Double totalBasicSalesTax = 0.0;
    Double totalGoodPrice = 0D;
    BigDecimal bd = BigDecimal.ZERO;


    // Methodes :
    public void process(){
        Scanner scanner = new Scanner(System.in);
        boolean start = true;

        while(start){
            System.out.println();
            System.out.println("Choisir une option:");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Generer la facture");
            System.out.println("Autres. Terminer");

            int option = getChoix(scanner, 0); // scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option){
                case 1:
                    System.out.println();
                    System.out.println("Tout produit importé doit contenir le mot clé 'imported' : ");
                    System.out.println("Choisir le produit : ");
                    System.out.println("1. Livre (BOOK)");
                    System.out.println("2. Nourriture (FOOD)");
                    System.out.println("3. Produit médical (MEDICAL PRODUCTS)");
                    System.out.println("4. Autres (OTHERS GOODS)");
                    int produit = getChoix(scanner, 1);
                    scanner.nextLine();
                    // Libelle :
                    System.out.println("Ajouter le libelle du produit");
                    String libProduit = scanner.nextLine();
                    // Prix
                    System.out.println("Ajouter le prix");
                    Double prixProduit = getArticlePrice(scanner);
                    scanner.nextLine();
                    // Quantite
                    System.out.println("Ajouter le quantite");
                    int quantiteProduit = scanner.nextInt();
                    scanner.nextLine();

                    switch (produit){
                        case 1:
                            // Livre
                            Book bk = new Book();
                            bk.setLibelle(libProduit);
                            bk.setPrix(prixProduit);
                            bk.setImported(libProduit.toLowerCase().contains(IMPORTEDWORD));
                            ensemble.add(new Achat(quantiteProduit, bk));
                            break;

                        case 2:
                            // Nourriture
                            Food fd = new Food();
                            fd.setLibelle(libProduit);
                            fd.setPrix(prixProduit);
                            fd.setImported(libProduit.toLowerCase().contains(IMPORTEDWORD));
                            ensemble.add(new Achat(quantiteProduit, fd));
                            break;

                        case 3:
                            // Produit medical
                            MedicalProduct mt = new MedicalProduct();
                            mt.setLibelle(libProduit);
                            mt.setPrix(prixProduit);
                            mt.setImported(libProduit.toLowerCase().contains(IMPORTEDWORD));
                            ensemble.add(new Achat(quantiteProduit, mt));
                            break;

                        case 4:
                            // Other product
                            OtherGoods os = new OtherGoods();
                            os.setLibelle(libProduit);
                            os.setPrix(prixProduit);
                            os.setImported(libProduit.toLowerCase().contains(IMPORTEDWORD));
                            ensemble.add(new Achat(quantiteProduit, os));
                            break;

                        default:
                            System.out.println("Aucun produit choisi !");
                            break;
                    }
                    break;

                case 2:
                    // Compute :
                    System.out.println("");
                    System.out.println("");
                    System.out.println("*** FACTURE ***");
                    Receipt receipt = computePrice(ensemble);
                    for(Achat at : receipt.getListe()){
                        log.info("{} {} {}", at.getQuantite(), ((Goods) at.getArticle()).getLibelle(),
                                ((Goods) at.getArticle()).getPrix());
                    }
                    log.info("Sales Taxes : {}  Total : {} ", receipt.getTotalTax(),
                            receipt.getTotalPrice());
                    // Reset in case people want to start a new purchase :
                    cleanList();
                    break;

                default:
                    start = false;
                    break;
            }
        }
    }

    public void cleanList(){
        ensemble.clear();
    }

    public BigDecimal arrondirCents(BigDecimal amount) {
        return amount.divide(ROUNDING_FACTOR, 0, RoundingMode.UP).multiply(ROUNDING_FACTOR);
    }

    public Receipt computePrice(List<Achat> articleachetes){
        // Set PARAM :
        Receipt r = new Receipt();
        totalBasicSalesTax = 0D;
        totalImportDutyTax = 0D;
        totalGoodPrice = 0D;

        articleachetes.forEach(
            d -> {
                // On EXCEPTION :
                double taxes = 0d;
                if(
                        d.getArticle() instanceof OtherGoods
                ){
                    taxes = (((Goods) d.getArticle()).getPrix() * basicSalesTax) * d.getQuantite();
                    totalBasicSalesTax += taxes;
                }
                boolean imported = ((Goods) d.getArticle()).getImported();
                if(imported){
                    // Apply IMPORT DUTY :
                    taxes += (((Goods) d.getArticle()).getPrix() * importDutyTax) * d.getQuantite();
                    totalImportDutyTax += (((Goods) d.getArticle()).getPrix() * importDutyTax) * d.getQuantite();
                }

                // From there, compute GOOD Price :
                BigDecimal productPrice = BigDecimal.valueOf(  (((Goods) d.getArticle()).getPrix() +taxes) * d.getQuantite() );
                // Update the product price :
                ((Goods) d.getArticle()).setPrix(arrondirCents(productPrice).doubleValue());
                totalGoodPrice += arrondirCents(productPrice).doubleValue();
            }
        );

        // Feed receipt : Quantite + Nom articel + Prix
        r.setListe(articleachetes);
        BigDecimal taxAmount = BigDecimal.valueOf( totalBasicSalesTax + totalImportDutyTax );
        r.setTotalTax(arrondirCents(taxAmount).doubleValue());
        r.setTotalPrice(totalGoodPrice);
        return r;
    }

    private int getChoix(Scanner scanner, int etape){
        int tp = 0;
        try{
            tp = scanner.nextInt();
        }
        catch (Exception exc){
            System.out.println();
            System.out.println("Vous avez renseigner une valeur inattendue !");
            switch (etape){
                case 0:
                    tp = 1; // Set it default
                    System.out.println("On constitue par défaut notre liste d'achat !");
                    break;

                case 1:
                    // Pour sélectionner le produit :
                    tp = 5; // Set it default
                    System.out.println("Le type de produit par défaut est AUTRES PRODUITS !");
                    break;
            }
        }
        return tp;
    }

    private Double getArticlePrice(Scanner scanner){
        Double tp = 0D;
        try{
            tp = scanner.nextDouble();
        }
        catch (Exception exc){
            tp = 0D;
            System.out.println();
            System.out.println("Vous avez renseigner une valeur inattendue !");
            System.out.println("Le prix par défaut est 0 !");
            System.out.println();
        }
        return tp;
    }

}

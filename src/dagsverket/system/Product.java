package dagsverket.system;

/**
 * Created by ArneChristian on 21.03.14.
 */
public class Product {

    private int productNumber;
    private String productName;
    private int stock;
    private double cost;


    public Product(int productNumber, String productName, int stock, double cost) {
        this.productNumber = productNumber;
        setProductName(productName);
        setStock(stock);
        setCost(cost);
    }

    public int getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName != null){
            this.productName = productName;
        } else {
            this.productName = "";
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock >= 0){
            this.stock = stock;
        } else {
            throw new IllegalArgumentException("Varebeholdning kan ikke være mindre enn 0");
        }
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        if (cost >= 0){
            this.cost = cost;
        }
    }
}

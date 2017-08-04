package in.pjitsol.zapnabit.Entity;

/**
 * Created by Bhawna on 5/12/2017.
 */

public class ProductAtrributeOption extends  SaleAbleItem{
    public Product product;
    public ProductAtrribute productAttribute;
    public String productAttributeName;
    public boolean optionSelected=false;
    public int categoryId;
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProductAtrributeOption) {
            return ((ProductAtrributeOption) o).id == this.id;
        }
        return false;
    }
}

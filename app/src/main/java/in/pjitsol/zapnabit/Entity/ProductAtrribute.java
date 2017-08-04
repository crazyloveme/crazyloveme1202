package in.pjitsol.zapnabit.Entity;

/**
 * Created by Bhawna on 5/12/2017.
 */

public class ProductAtrribute extends  SaleAbleItem{

    public Product product;
    public boolean hintAdded=false;
    public String attr_condition;
    public boolean conditionMatched2 ;
    public int optionCount;
    public String display_order;
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
        if (o instanceof ProductAtrribute) {
            return ((ProductAtrribute) o).id == this.id;
        }
        return false;
    }
}

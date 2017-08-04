package in.pjitsol.zapnabit.Entity;


import java.util.ArrayList;
import java.util.List;

public class Item implements Cloneable {

	// db generated
	public int _id;

	public int id;

	// for offline
	public static int ITEM_ID = 10000;
	public String productImage;
	public String productSliderImage;
	public String productDescription;
	public String status;
	
	public String name;
	public int productId;
	public String OrderType;
	public String service="";
	public float netamount;
	public float grossamount;
	public float Price;
	public String OrderHistory;
	public boolean isSelect=false;
	public float TotalOrderPrice;
	public float addonTotalPrice=0;
	public boolean hasAddon=false;
	public List<ProductAddOn> productAddOns= new ArrayList<ProductAddOn>() ;
	public List<ProductAtrribute> productAttributes= new ArrayList<ProductAtrribute>() ;
	public List<ProductAtrributeOption> productAttributesOptions= new ArrayList<ProductAtrributeOption>() ;
	public List<ProductAtrribute> selectedAttributes= new ArrayList<ProductAtrribute>() ;
	public List<ProductAtrributeOption> selectedAttributeOptions= new ArrayList<ProductAtrributeOption>() ;
	public List<Item> mealPRoducts= new ArrayList<Item>() ;
	
	public List<ExtraNote> extraNote = new ArrayList<ExtraNote>();
	public String vat;
	public boolean Update=false;
	public static class ExtraNote extends SaleAbleItem {
		public Product product;
		public Order order;
		public boolean isPrint=true;
		public int categoryId;

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ExtraNote) {
				return (id == ((ExtraNote) o).id);
			}
			return false;
		}
	}
}

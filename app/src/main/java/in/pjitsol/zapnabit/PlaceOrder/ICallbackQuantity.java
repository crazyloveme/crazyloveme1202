package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;

public interface ICallbackQuantity {
	
	public void increaseQuantity(Item item);
	public void decreaseQuantity(Item item);

}

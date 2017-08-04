package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.Entity.Item;

import java.util.List;

public interface IMenuFetch {
	public void addedMenuItem(List<Item> result, int parentId, boolean trytomenu);
}


package in.pjitsol.zapnabit.Entity;

import java.util.List;

public class Category extends Item {
	public static final int TOP_LEVEL_CAT_PARENT_ID = 0;

	public boolean hasSubCategory;

	public boolean hasProducts;

	// 0 for top level category
	public int parentId;

	// null if no sub category
	public List<Category> subCategories;

	// null if no product
	public List<Product> products;

	public  String catId = "";
	public  String flag = "";

}

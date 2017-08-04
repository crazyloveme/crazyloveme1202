package in.pjitsol.zapnabit.Entity;

import java.util.ArrayList;

/**
 * * @author Divyesh Sharma This class holds the data for Product Items and
 * their categories/sub categories
 */

public class ProductItem {

	private String productId;
	private String productMainCategory;
	private String productSecondaryCategory;
	private String productThirdCategory;
	private String productNo;
	private String productNetPrice;
	private String productVatPrice;
	private String productGrossPrice;
	private String productMsg;
	private boolean isProductSelected;
	private String productShortname;
	private String Cn_MainCategoryName;
	private String Cn_SubCategoryName;
	private String Cn_ThirdCategoryName;
	private String Cn_ProductShortName;
	private int prodCategoryId;
	private Product product;
	private String product_Qty;

	public String getProduct_Qty() {
		return product_Qty;
	}

	public void setProduct_Qty(String product_Qty) {
		this.product_Qty = product_Qty;
	}

	private ArrayList<Category> catArray = new ArrayList<Category>();

	public ArrayList<Category> getCatArray() {
		return catArray;
	}

	public void setCatArray(ArrayList<Category> catArray) {
		this.catArray = catArray;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getProdCategoryId() {
		return prodCategoryId;
	}

	public void setProdCategoryId(int prodCategoryId) {
		this.prodCategoryId = prodCategoryId;
	}

	private ArrayList<String> cateogryName;

	private int prodHashMap;
	private int catHasmap;

	public int getProdHashMap() {
		return prodHashMap;
	}

	public void setProdHashMap(int prodHashMap) {
		this.prodHashMap = prodHashMap;
	}

	public int getCatHasmap() {
		return catHasmap;
	}

	public void setCatHasmap(int catHasmap) {
		this.catHasmap = catHasmap;
	}

	public ArrayList<String> getCateogryName() {
		return cateogryName;
	}

	public void setCateogryName(ArrayList<String> cateogryName) {
		this.cateogryName = cateogryName;
	}

	public String getCn_MainCategoryName() {
		return Cn_MainCategoryName;
	}

	public void setCn_MainCategoryName(String cn_MainCategoryName) {
		Cn_MainCategoryName = cn_MainCategoryName;
	}

	public String getCn_SubCategoryName() {
		return Cn_SubCategoryName;
	}

	public void setCn_SubCategoryName(String cn_SubCategoryName) {
		Cn_SubCategoryName = cn_SubCategoryName;
	}

	public String getCn_ThirdCategoryName() {
		return Cn_ThirdCategoryName;
	}

	public void setCn_ThirdCategoryName(String cn_ThirdCategoryName) {
		Cn_ThirdCategoryName = cn_ThirdCategoryName;
	}

	public String getCn_ProductShortName() {
		return Cn_ProductShortName;
	}

	public void setCn_ProductShortName(String cn_ProductShortName) {
		Cn_ProductShortName = cn_ProductShortName;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductMainCategory(String productMainCategory) {
		this.productMainCategory = productMainCategory;
	}

	public String getProductMainCategory() {
		return productMainCategory;
	}

	public void setProductSecondaryCategory(String productSecondaryCategory) {
		this.productSecondaryCategory = productSecondaryCategory;
	}

	public String getProductSecondaryCategory() {
		return productSecondaryCategory;
	}

	public void setProductThirdCategory(String productThirdCategory) {
		this.productThirdCategory = productThirdCategory;
	}

	public String getProductThirdCategory() {
		return productThirdCategory;
	}

	public void setProductNetPrice(String productNetPrice) {
		this.productNetPrice = productNetPrice;
	}

	public String getProductNetPrice() {
		return productNetPrice;
	}

	public void setProductVatPrice(String productVatPrice) {
		this.productVatPrice = productVatPrice;
	}

	public String getProductVatPrice() {
		return productVatPrice;
	}

	public void setProductGrossPrice(String productGrossPrice) {
		this.productGrossPrice = productGrossPrice;
	}

	public String getProductGrossPrice() {
		return productGrossPrice;
	}

	public boolean isProductSelected() {
		return isProductSelected;
	}

	public void setProductSelected(boolean isProductSelected) {
		this.isProductSelected = isProductSelected;
	}

	public String getProductMsg() {
		return productMsg;
	}

	public void setProductMsg(String productMsg) {
		this.productMsg = productMsg;
	}

	public String getProductShortname() {
		return productShortname;
	}

	public void setProductShortname(String productShortname) {
		this.productShortname = productShortname;
	}
}

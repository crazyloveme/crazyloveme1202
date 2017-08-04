package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.Db.TakeOrderDB;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.Merchant.MerchantMenuFragment;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

public class GetMenuFROMDbTask extends AsyncTask<Integer, Void, List<Item>>{
	private  MerchantMenuFragment fragmentMerchant;
	WeakReference<Fragment> framWeakReference ;
	private IMenuFetch iMenuItemAdd;
	private int parentId;
	Activity context;
	boolean tryFromWeb;
	MenuFragment fragment;
	ProgressBar progressbar;
	public GetMenuFROMDbTask(int parentId,boolean tryFromWeb,MenuFragment fragment/*,ProgressBar progressbar*/) {
		this.parentId = parentId;
		this.tryFromWeb = tryFromWeb;
		context = fragment.getActivity();
		this.iMenuItemAdd = (IMenuFetch)fragment;
	    this.fragment = (MenuFragment) fragment;
	    this.framWeakReference = new WeakReference<Fragment>(fragment);
	    //this.progressbar = progressbar;
	}
	public GetMenuFROMDbTask(int parentId, boolean tryFromWeb, MerchantMenuFragment fragment/*,ProgressBar progressbar*/) {
		this.parentId = parentId;
		this.tryFromWeb = tryFromWeb;
		context = fragment.getActivity();
		this.iMenuItemAdd = (IMenuFetch)fragment;
		this.fragmentMerchant = (MerchantMenuFragment) fragment;
		this.framWeakReference = new WeakReference<Fragment>(fragment);
		//this.progressbar = progressbar;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		/*if(progressbar!=null && progressbar.getVisibility()==View.GONE)
			progressbar.setVisibility(View.VISIBLE);*/
		
	}

	@SuppressWarnings("unused")
	@Override
	protected List<Item> doInBackground(Integer... params) {
		List<Item> mealItems = null;
		List<Item> catItems = TakeOrderDB.getCategory(context,parentId);
		List<Item> productItems = TakeOrderDB.getProducts(context,parentId);
		
		if(productItems!=null ){
			for(int i=0;i<productItems.size();i++){

				if(((Product)productItems.get(i)).productType.equalsIgnoreCase("product")){
					List<ProductAddOn> productAddOns = TakeOrderDB.getAddOn(
							context,(Product) productItems.get(i));
					if(productAddOns!=null){
						productItems.get(i).productAddOns=productAddOns;
						productItems.get(i).hasAddon=true;
					}
				}
				else{
					List<ProductAtrribute> productAddOns = TakeOrderDB.getATTRibutes(
							context,(Product) productItems.get(i));
					productItems.get(i).productAttributes=productAddOns;
					if(productItems.get(i).productAttributes!=null){
						if(productItems.get(i).productAttributes!=null && productItems.get(i).productAttributes.size()>0){
							for(int k=0;k<productItems.get(i).productAttributes.size();k++){
								List<ProductAtrributeOption> productAttOptions = TakeOrderDB.getATTRibutesOptions(
										context, productItems.get(i).productAttributes.get(k));
								(productItems.get(i).productAttributes.get(k)).productAttributesOptions=productAttOptions;
							}
						}


					}
				}
			}
		}
		
		

		if (catItems != null && productItems != null && mealItems != null) {
			mealItems.addAll(catItems);
			mealItems.addAll(productItems);
			//catItems.addAll(productItems);
			return mealItems;
		} 
		else if (catItems != null && mealItems != null) {
			mealItems.addAll(catItems);
			return mealItems;
		}
		else if (productItems != null && mealItems != null) {
			mealItems.addAll(productItems);
			return mealItems;
		}
		else if (catItems != null && productItems != null) {
			catItems.addAll(productItems);
			return catItems;
		}
		else if (mealItems != null) {
			return mealItems;
		}
		else if (catItems != null) {
			return catItems;
		} else if (productItems != null) {
			return productItems;
		}
		return null;

	}

	@Override
	protected void onPostExecute(List<Item> result) {
		super.onPostExecute(result);
		if(framWeakReference.get()!=null && framWeakReference.get().isVisible())
			iMenuItemAdd.addedMenuItem(result,parentId,tryFromWeb);
		}
		
}

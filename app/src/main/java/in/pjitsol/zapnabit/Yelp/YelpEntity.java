package in.pjitsol.zapnabit.Yelp;

import in.pjitsol.zapnabit.Constants.StaticConstants;

public class YelpEntity {
	
	public  String Color=StaticConstants.RED;
	public String id;
	public String Name;
	public String Lat;
	public String Long;
	public String image_url;
	public String Rating;
	public String Distance;
	public String Phone;
	public int ItemTYpe;
	public String display_address;
	public String priceRange;
	public String sdk_tag;
	public boolean status;
	public boolean userFav=false;
	public String Merchant_Id;
	public String hours;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Name;
	}
	
}

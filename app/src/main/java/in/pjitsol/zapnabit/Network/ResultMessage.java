package in.pjitsol.zapnabit.Network;

import java.util.ArrayList;

import in.pjitsol.zapnabit.BarCode.ProductInfo;
import in.pjitsol.zapnabit.Entity.ScanProductInfo;
import in.pjitsol.zapnabit.NearbySearch.Buiseness;
import in.pjitsol.zapnabit.NearbySearch.RestoItem;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

public class ResultMessage {
	public int STATUS;
	public String RESPONSE;
	public String ERRORMESSAGE;
	public String TYPE;
	public String Error;
	public RestoItem restoItem;
	public ArrayList<YelpEntity> businessNames;
	public Buiseness business;
	public ScanProductInfo scanProductInfo;
	public int Position;
}

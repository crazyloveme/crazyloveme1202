package in.pjitsol.zapnabit.Merchant;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PlaceDetailAdapter extends BaseAdapter{

	private LayoutInflater li;
	private Context context;
	public ArrayList<YelpEntity> placeList=new ArrayList<>();
	private ImageLoader imageLoder;
	public PlaceDetailAdapter(Context con,ArrayList<YelpEntity> placelist2)
	{
		this.context=con;
		this.placeList=placelist2;
		li=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoder=new ImageLoader(context);


	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return placeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return placeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView=li.inflate(R.layout.place_detail_item, parent,false);
			holder=new ViewHolder();
			holder.text_placename = (TextView)convertView.findViewById(R.id.text_placename);
			holder.text_address = (TextView)convertView.findViewById(R.id.text_address);
			holder.rating = (RatingBar)convertView.findViewById(R.id.rating);
			holder.text_distance = (TextView)convertView.findViewById(R.id.text_distance);
			holder.img_resto = (ImageView)convertView.findViewById(R.id.img_resto);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text_placename.setText(placeList.get(position).Name);
		holder.text_address.setText(placeList.get(position).display_address);
		
		imageLoder.DisplayImage(placeList.get(position).image_url, holder.img_resto);
		holder.rating.setRating(Float.valueOf(placeList.get(position).Rating));
		if(placeList.get(position).Distance!=null)
			holder.text_distance.setText(Util.numberfornmat(Float.valueOf(placeList.get(position).Distance))+"Meters");

		return convertView;
	}


	static class ViewHolder {
		TextView text_placename,text_address,text_distance;
		ImageView img_resto;
		RatingBar rating;
	}
}


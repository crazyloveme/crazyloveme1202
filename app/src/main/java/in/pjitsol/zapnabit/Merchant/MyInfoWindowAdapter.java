package in.pjitsol.zapnabit.Merchant;

import in.pjitsol.zapnabit.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements InfoWindowAdapter{

	private final View myContentsView;
	private LayoutInflater li;

	public MyInfoWindowAdapter(Context context){
		li=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myContentsView = li.inflate(R.layout.marker_info, null);
	}

	@Override
	public View getInfoContents(Marker marker) {

		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		TextView text_plcename = ((TextView)myContentsView.findViewById(R.id.text_plcename));
		TextView text_address = ((TextView)myContentsView.findViewById(R.id.text_address));
		TextView text_distance = ((TextView)myContentsView.findViewById(R.id.text_distance));
		ImageView img_arrow= ((ImageView) myContentsView.findViewById(R.id.img_arrow));
		RelativeLayout rel_markerinfo= ((RelativeLayout) myContentsView.findViewById(R.id.rel_markerinfo));
		RelativeLayout rel_markerIamhere= ((RelativeLayout) myContentsView.findViewById(R.id.rel_markerIamhere));

		if(marker.getTitle()!=null){
			if(marker.getTitle().equalsIgnoreCase("I am here")){
				rel_markerIamhere.setVisibility(View.VISIBLE);
				rel_markerinfo.setVisibility(View.GONE);

			}
			else{
				rel_markerIamhere.setVisibility(View.GONE);
				rel_markerinfo.setVisibility(View.VISIBLE);
				text_plcename.setText(marker.getTitle().split(":")[0]);
				text_address.setText(marker.getTitle().split(":")[1].trim());
				text_distance.setText(marker.getTitle().split(":")[3]+"miles");
				img_arrow.setVisibility(View.VISIBLE);
			}

		}
		else{
			text_plcename.setText("I am here");
			text_address.setText("");
			text_distance.setText("");
			img_arrow.setVisibility(View.INVISIBLE);
		}
		
		return myContentsView;
	}

}

package in.pjitsol.zapnabit.NearbySearch;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserPlaceDetailAdapter extends BaseAdapter implements Filterable {

    private final IFavCallBack callback;
    private LayoutInflater li;
    private Context context;
    public ArrayList<YelpEntity> placeList = new ArrayList<>();
    protected ArrayList<YelpEntity> mOriginalValues;
    private ImageLoader imageLoder;

    public UserPlaceDetailAdapter(Context con, ArrayList<YelpEntity> placelist2, IFavCallBack callBack) {
        this.context = con;
        this.placeList = placelist2;
        li = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoder = new ImageLoader(context);
        this.callback = callBack;

    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return (placeList.get(position).ItemTYpe);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = li.inflate(R.layout.user_search_layout, parent, false);
            holder = new ViewHolder();
            holder.text_placename = (TextView) convertView.findViewById(R.id.text_placenameuser);
            holder.text_restoStatus = (TextView) convertView.findViewById(R.id.text_restoStatus);
            holder.text_address = (TextView) convertView.findViewById(R.id.text_addressuser);
            holder.text_distance = (TextView) convertView.findViewById(R.id.text_distanceuser);
            holder.text_pricerangevalue = (TextView) convertView.findViewById(R.id.text_pricerangevalue);
            holder.text_placenameyelp = (TextView) convertView.findViewById(R.id.text_placename);
            holder.text_addressyelp = (TextView) convertView.findViewById(R.id.text_address);
            holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
            holder.text_distanceyelp = (TextView) convertView.findViewById(R.id.text_distance);
            holder.text_noneavailable = (TextView) convertView.findViewById(R.id.text_noneavailable);
            holder.img_resto = (ImageView) convertView.findViewById(R.id.img_resto);
            holder.img_yelp = (ImageView) convertView.findViewById(R.id.img_yelp);
            holder.zap_merchant = (ImageView) convertView.findViewById(R.id.zap_merchant);
            holder.zap_nonmerchant = (ImageView) convertView.findViewById(R.id.zap_nonmerchant);
            holder.img_userfav = (ImageView) convertView.findViewById(R.id.img_userfav);
            holder.img_restouser = (ImageView) convertView.findViewById(R.id.img_restouser);
            holder.rel_zapitem = (RelativeLayout) convertView.findViewById(R.id.rel_zapitem);
            holder.rel_yelpitem = (RelativeLayout) convertView.findViewById(R.id.rel_yelpitem);
            holder.rel_restotype = (RelativeLayout) convertView.findViewById(R.id.rel_restotype);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img_userfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.FavCallback(placeList.get(position));

                if (placeList.get(position).userFav) {
                    placeList.get(position).userFav = false;
                } else {
                    placeList.get(position).userFav = true;
                }
                notifyDataSetChanged();

            }
        });

        if (placeList.get(position).userFav)
            holder.img_userfav.setImageResource(R.drawable.heart_selected_small);
        else
            holder.img_userfav.setImageResource(R.drawable.heart_small);

        if (type == 0) {
            holder.text_placename.setText(placeList.get(position).Name);
            holder.text_address.setText(placeList.get(position).display_address);
            if (placeList.get(position).Distance != null)
                holder.text_distance.setText(Util.numberfornmat
                        (Float.valueOf(placeList.get(position).Distance)) + " Miles");

            boolean status = Util.checkRestostatus((placeList.get(position).hours));
            if (status) {
                holder.text_restoStatus.setText("Open");
                holder.text_restoStatus.setTextColor(context.getResources().getColor(R.color.darkgreen));
            } else {
                holder.text_restoStatus.setText("Closed");
                holder.text_restoStatus.setTextColor(context.getResources().getColor(R.color.red));
            }
            holder.text_pricerangevalue.setText(placeList.get(position).priceRange);
            imageLoder.DisplayImage(placeList.get(position).image_url, holder.img_restouser);
            holder.rel_zapitem.setVisibility(View.VISIBLE);
            holder.rel_yelpitem.setVisibility(View.GONE);
            holder.rel_restotype.setVisibility(View.GONE);
            holder.rel_restotype.setVisibility(View.GONE);
            holder.text_noneavailable.setVisibility(View.GONE);
            holder.zap_merchant.setVisibility(View.VISIBLE);
            holder.zap_nonmerchant.setVisibility(View.INVISIBLE);
        } else if (type == 1) {
            holder.text_placenameyelp.setText(placeList.get(position).Name);
            holder.text_addressyelp.setText(placeList.get(position).display_address);
            holder.rel_restotype.setVisibility(View.GONE);
            imageLoder.DisplayImage(placeList.get(position).image_url, holder.img_resto);
            holder.rating.setRating(Float.valueOf(placeList.get(position).Rating));
            if (placeList.get(position).Distance != null)
                holder.text_distanceyelp.setText(Util.numberfornmat(Float.valueOf(placeList.get(position).Distance)) + "Meters");
            holder.rel_zapitem.setVisibility(View.GONE);
            holder.rel_yelpitem.setVisibility(View.VISIBLE);
            holder.text_noneavailable.setVisibility(View.GONE);
            holder.zap_merchant.setVisibility(View.INVISIBLE);
            holder.zap_nonmerchant.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            holder.rel_zapitem.setVisibility(View.GONE);
            holder.rel_yelpitem.setVisibility(View.GONE);
            holder.rel_restotype.setVisibility(View.VISIBLE);
            holder.img_yelp.setVisibility(View.GONE);
            holder.text_noneavailable.setVisibility(View.GONE);
            holder.zap_merchant.setVisibility(View.VISIBLE);
            holder.zap_nonmerchant.setVisibility(View.INVISIBLE);
        } else if (type == 3) {
            holder.rel_zapitem.setVisibility(View.GONE);
            holder.rel_yelpitem.setVisibility(View.GONE);
            holder.rel_restotype.setVisibility(View.VISIBLE);
            holder.img_yelp.setVisibility(View.VISIBLE);
            holder.text_noneavailable.setVisibility(View.GONE);
            holder.zap_merchant.setVisibility(View.INVISIBLE);
            holder.zap_nonmerchant.setVisibility(View.VISIBLE);
        } else {
            holder.rel_zapitem.setVisibility(View.GONE);
            holder.rel_yelpitem.setVisibility(View.GONE);
            holder.rel_restotype.setVisibility(View.GONE);
            holder.text_noneavailable.setVisibility(View.VISIBLE);
        }


        return convertView;
    }


    static class ViewHolder {
        TextView text_placename, text_address, text_distance, text_pricerangevalue,text_restoStatus;
        TextView text_placenameyelp, text_addressyelp, text_distanceyelp, text_searchType, text_noneavailable;
        ImageView img_resto, img_restouser, img_yelp, zap_merchant, zap_nonmerchant;
        RatingBar rating;
        RelativeLayout rel_zapitem, rel_yelpitem, rel_restotype;
        ImageView img_userfav;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                placeList = (ArrayList<YelpEntity>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<YelpEntity> FilteredArrList = new ArrayList<YelpEntity>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<YelpEntity>(placeList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        YelpEntity data = mOriginalValues.get(i);

                        if (data.Name.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                        /*else if(data.message_subject.toLowerCase().contains(constraint.toString())){
                            FilteredArrList.add(data);
						}
						else if(data.message_body.toLowerCase().contains(constraint.toString())){
							FilteredArrList.add(data);
						}*/
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();

    }
}

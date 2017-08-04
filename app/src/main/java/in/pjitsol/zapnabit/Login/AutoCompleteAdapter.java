package in.pjitsol.zapnabit.Login;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;




/*public class AutoCompleteAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<PlaceInfo> activityList;
	private LayoutInflater li;

	public AutoCompleteAdapter(Context con,ArrayList<PlaceInfo> menuList)
	{
		this.context=con;
		this.activityList=menuList;
		li=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return activityList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return activityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}
	
	
	
}*/

public class AutoCompleteAdapter extends ArrayAdapter<PlaceInfo> {

	private ArrayList<PlaceInfo> fullList =  new ArrayList<PlaceInfo>();
    Context con;

    public AutoCompleteAdapter(Context context, int resource, int textViewResourceId, List<PlaceInfo> objects) {

        super(context, resource, textViewResourceId, objects);
        con=context;
        fullList = (ArrayList<PlaceInfo>) objects;

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public PlaceInfo getItem(int position) {
        return fullList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        return super.getView(position, convertView, parent);
    }
}

    /*   @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<PlaceInfo>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<PlaceInfo> list = new ArrayList<PlaceInfo>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<PlaceInfo> values = mOriginalValues;
                int count = values.size();

                ArrayList<PlaceInfo> newValues = new ArrayList<PlaceInfo>(count);

                for (int i = 0; i < count; i++) {
                	PlaceInfo item = values.get(i);
                	
                	if(item.placeName.toLowerCase().contains(prefixString)){
                		
                     	newValues.add(item);
                	}
                	if(item instanceof Meal){
                		
                		 if (Util.GetNameValue(item.name,con).toLowerCase().contains(prefixString)) {
                         	item.name =Util.GetNameValue(item.name,con);
                         	newValues.add(item);
                         }
                	}
                	else{
                    if (Util.GetNameValue(item.name,con).toLowerCase().contains(prefixString)) {
                    	item.name =Util.GetNameValue(item.name,con);
                    	newValues.add(item);
                    }
                    if (((Product)item).productNum.toLowerCase().contains(prefixString)) {
                    	item.name =Util.GetNameValue(item.name,con);
                    	newValues.add(item);
                    }
                	}
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        if(results.values!=null){
        fullList = (ArrayList<PlaceInfo>) results.values;
        }else{
            fullList = new ArrayList<PlaceInfo>();
        }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
    
}*/


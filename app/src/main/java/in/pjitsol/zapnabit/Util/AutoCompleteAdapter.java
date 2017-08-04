package in.pjitsol.zapnabit.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import in.pjitsol.zapnabit.Entity.CategoryProductinfo;

/**
 * Created by websnoox android on 12/27/2016.
 */

public class AutoCompleteAdapter extends ArrayAdapter<CategoryProductinfo> implements Filterable {

    private ArrayList<CategoryProductinfo> fullList =  new ArrayList<CategoryProductinfo>();
    private ArrayList<CategoryProductinfo> mOriginalValues = new ArrayList<CategoryProductinfo>();
    private ArrayFilter mFilter;
    Context con;

    public AutoCompleteAdapter(Context context, int resource, int textViewResourceId, List<CategoryProductinfo> objects) {

        super(context, resource, textViewResourceId, objects);
        con=context;
        fullList = (ArrayList<CategoryProductinfo>) objects;
        mOriginalValues = new ArrayList<CategoryProductinfo>(fullList);

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

   /* @Override
    public String getItem(int position) {
        return fullList.get(position).name;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }

    @Override
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
                    mOriginalValues = new ArrayList<CategoryProductinfo>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<CategoryProductinfo> list = new ArrayList<CategoryProductinfo>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();
                ArrayList<CategoryProductinfo> values = mOriginalValues;
                int count = values.size();
                ArrayList<String> newValues = new ArrayList<String>(count);
                for (int i = 0; i < count; i++) {
                    String item = values.get(i).name;
                    newValues.add(item);
                    /*if (item.toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                    }*/
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
                fullList = (ArrayList<CategoryProductinfo>) results.values;
            }else{
                fullList = new ArrayList<CategoryProductinfo>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}

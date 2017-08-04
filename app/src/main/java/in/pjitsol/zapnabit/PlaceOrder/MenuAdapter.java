package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Ui.ProductImageDialog;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.util.ArrayList;
import java.util.List;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter implements OnBackPressListener {

    private final IcallBackImage callback;
    private LayoutInflater li;
    private Context context;
    public ArrayList<YelpEntity> placeList = new ArrayList<>();
    private ArrayList<Item> items;
    private ImageLoader imageLoader;

    public MenuAdapter(Context con, List<Item> result,IcallBackImage callback) {
        this.context = con;
        this.items = (ArrayList<Item>) result;
        li = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback=callback;
        imageLoader = new ImageLoader(context);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = li.inflate(R.layout.menu_list_item, parent, false);
            holder = new ViewHolder();
            holder.text_categoryname = (TextView) convertView.findViewById(R.id.text_categoryname);
            holder.text_itemname = (TextView) convertView.findViewById(R.id.text_itemname);
            holder.text_itemPrice = (TextView) convertView.findViewById(R.id.text_itemPrice);
            holder.text_pricelabel = (TextView) convertView.findViewById(R.id.text_pricelabel);

            holder.img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);
            holder.img_productimg = (ImageView) convertView.findViewById(R.id.img_productimg);
            holder.linear_image = (LinearLayout) convertView.findViewById(R.id.linear_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_itemname.setText(items.get(position).name);
        holder.text_categoryname.setText(items.get(position).name);
        imageLoader.DisplayImage(items.get(position).productImage, holder.img_productimg);
        if (items.get(position) instanceof Product) {
            holder.text_pricelabel.setVisibility(View.VISIBLE);
            holder.text_itemPrice.setVisibility(View.VISIBLE);
            holder.text_categoryname.setVisibility(View.GONE);
            holder.text_itemname.setVisibility(View.VISIBLE);
            if (((Product) items.get(position)).quantity == 0)
                holder.text_itemPrice.setText(StaticConstants.DOLLAR+Util.numberfornmat(
                        ((Product) items.get(position)).netamount));
            else
                holder.text_itemPrice.setText(StaticConstants.DOLLAR+Util.numberfornmat(Util.calculateItemTotalPrice(((Product) items.get(position)).quantity,
                        ((Product) items.get(position)).netamount)));

        } else {
            holder.text_pricelabel.setVisibility(View.GONE);
            holder.text_categoryname.setVisibility(View.VISIBLE);
            holder.text_itemname.setVisibility(View.GONE);
            holder.text_itemPrice.setVisibility(View.GONE);
            holder.img_arrow.setVisibility(View.VISIBLE);

        }



        return convertView;
    }


    static class ViewHolder {
        TextView text_itemname,text_itemPrice,text_categoryname,text_pricelabel;

        ImageView img_arrow, img_productimg;
        LinearLayout linear_image;
    }


    @Override
    public boolean onBackPressed() {
        // TODO Auto-generated method stub
        return true;
    }
}

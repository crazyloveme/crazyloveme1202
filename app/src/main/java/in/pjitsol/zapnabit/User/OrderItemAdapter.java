package in.pjitsol.zapnabit.User;

import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.Product.ExtraNote;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Entity.SaleAbleItem;
import in.pjitsol.zapnabit.Util.Logger;
import in.pjitsol.zapnabit.Util.Util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class OrderItemAdapter extends BaseAdapter {
    public Order order;
    private Context context;
    List<SaleAbleItem> items = new ArrayList<SaleAbleItem>();
    private int count;
    public static final int PRODUCT_TAG = -1;
    static final String ARROW = "\u2192";
    static final String ruppe = "\u20B9 ";


    int lastPositionSelected = -1;
    private int clickedChildPosition = -1;
    ArrayList<Integer> checked;
    private int tag = -1;

    public OrderItemAdapter(Context context, ArrayList<SaleAbleItem> items
    ) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.items = items;
        checked = new ArrayList<Integer>();
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return super.isEnabled(position);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return count;
    }

    public void clearDataSet() {
        items.clear();
        tag = -1;
        this.clickedChildPosition = -1;
        checked.clear();

    }

    @Override
    public SaleAbleItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // int itemtype = getItemViewType(position);
        Logger.i("ordermanager", "get view " + position);

        if (convertView == null) {
            ViewHolder holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.product_item, null);
            holder.quantity = (TextView) convertView
                    .findViewById(R.id.text_productqty);
            holder.name = (TextView) convertView
                    .findViewById(R.id.text_productname);
            holder.price = (TextView) convertView
                    .findViewById(R.id.text_productprice);

            convertView.setTag(holder);
        }

        ViewHolder hold = (ViewHolder) convertView.getTag();
        SaleAbleItem item = items.get(position);
        // either product or addon
        Product pr = null;
        if (item instanceof Product) {

            hold.name.setText(item.name);
            pr = (Product) item;
            hold.quantity.setText("x" + String.valueOf(pr.quantity));
            double x = item.netamount * item.quantity;
            hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(x));

            convertView.setTag(PRODUCT_TAG, pr);
        } else if (item instanceof ProductAddOn) {
            ProductAddOn pAd = (ProductAddOn) item;
            hold.quantity.setText("x" + String.valueOf(pAd.quantity));
            hold.name.setText(ARROW + " " + item.name);
            double x = item.netamount * item.quantity;
            hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(x));
            convertView.setTag(PRODUCT_TAG, pAd);
        } else if (item instanceof ExtraNote) {

            ExtraNote note = (ExtraNote) item;
            if (((ExtraNote) item).product == null)
                hold.name.setText(item.name);
            else

                hold.name.setText(ARROW + " " + item.name);

            hold.quantity.setText("x" + String.valueOf(note.quantity));
            hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(item.netamount));

            convertView.setTag(PRODUCT_TAG, note);
        }
        else if (item instanceof ProductAtrributeOption) {
            ProductAtrributeOption pAd = (ProductAtrributeOption) item;
            hold.quantity.setText("x" + String.valueOf(pAd.quantity));
            hold.name.setText(ARROW+pAd.name+"("+pAd.productAttributeName+")");
            double x = pAd.netamount * pAd.quantity;
            hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(x));
            convertView.setTag(PRODUCT_TAG, pAd);
        }


        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        float totalPric = 0.00f;
        float totalgross = 0.00f;
        count = 0;
        items.clear();
        items.clear();
        if (order != null) {
            for (int i = 0; i < order.item.size(); i++) {
                if (order.item.get(i) instanceof Product) {
                    Product p = (Product) order.item.get(i);
                    if (p.productType.equalsIgnoreCase("routes")) {
                        totalPric = totalPric + (p.netamount * p.quantity);
                        totalgross = totalgross + (p.grossamount * p.quantity);
                        items.add(p);
                        count++;
                        for (ProductAddOn ad : p.selectedAddon) {
                            totalPric = totalPric + (ad.quantity * ad.netamount);
                            totalgross = totalgross
                                    + (ad.grossamount * ad.quantity);
                            items.add(ad);
                            count++;
                        }
                        if (p.extraNote != null) {
                            for (ExtraNote note : p.extraNote) {
                                totalPric = totalPric + (note.netamount);
                                totalgross = totalgross + (note.netamount);
                                count++;
                                items.add(note);
                            }
                        }
                    } else {
                        Product p1 = (Product) order.item.get(i);
                        items.add(p1);
                        count++;
                        for (ProductAtrributeOption ad : p1.selectedAttributeOptions) {
                            totalPric = totalPric + (p1.quantity * ad.netamount);
                            totalgross = totalgross
                                    + (p1.grossamount * ad.quantity);
                            items.add(ad);
                            count++;
                        }
                    }
                } else if (order.item.get(i) instanceof ExtraNote) {
                    ExtraNote note = (ExtraNote) order.item.get(i);
                    totalPric = totalPric + (note.netamount);
                    totalgross = totalgross + (note.netamount);
                    count++;
                    items.add(note);
                }

            }
        }

        super.notifyDataSetChanged();
        if (order != null) {
            order.netprice = totalPric;
            order.TotalPrice = totalPric;
            order.grossprice = totalgross;
        }
    }

    public void setClickedChildPosition(int newClickedChildPosition) {
        this.clickedChildPosition = newClickedChildPosition;
    }

    public void setServices(ArrayList<Integer> checkedPositon, int tag) {
        this.checked = checkedPositon;
        this.tag = tag;
    }

    static class ViewHolder {
        TextView name, price, quantity;

    }

}


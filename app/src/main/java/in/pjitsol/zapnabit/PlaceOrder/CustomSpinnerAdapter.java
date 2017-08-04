package in.pjitsol.zapnabit.PlaceOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

/**
 * Created by Bhawna on 5/16/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements OnBackPressListener {

    private final Item item;
    private final IQuantityCallback callback;
    private LayoutInflater li;
    private Context context;
    private ArrayList<ProductAtrributeOption> items;
    private ImageLoader imageLoader;
    ProductAtrribute currentAttribute;
    private boolean isFromView = false;

    public CustomSpinnerAdapter(Context con, List<ProductAtrributeOption> result,
                                ProductAtrribute currentAttribute, Item item, IQuantityCallback callback) {
        this.context = con;
        this.items = (ArrayList<ProductAtrributeOption>) result;
        li = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentAttribute = currentAttribute;
        this.item = item;
        this.callback = callback;

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
            convertView = li.inflate(R.layout.spinner_item, parent, false);
            holder = new ViewHolder();
            holder.text_optionname = (TextView) convertView.findViewById(R.id.text_optionname);
            holder.text_optionprice = (TextView) convertView.findViewById(R.id.text_optionprice);
            holder.image_add = (ImageView) convertView.findViewById(R.id.image_add);
            holder.image_remove = (ImageView) convertView.findViewById(R.id.image_remove);
            holder.rel_itemlayout = (RelativeLayout) convertView.findViewById(R.id.rel_itemlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_optionname.setText(items.get(position).name);
        if (items.get(position).name.contains("Select")) {
            holder.text_optionprice.setText("");
            holder.text_optionname.setTextColor(context.getResources().getColor(R.color.textfonts
            ));
            holder.image_add.setVisibility(View.GONE);
            holder.image_remove.setVisibility(View.GONE);
        } else {
            if (items.get(position).optionSelected) {
                holder.text_optionname.setTextColor(context.getResources().getColor(R.color.darkgreen
                ));
                holder.text_optionprice.setTextColor(context.getResources().getColor(R.color.darkgreen
                ));
                holder.image_add.setVisibility(View.INVISIBLE);
                holder.image_remove.setVisibility(View.VISIBLE);
            } else {
                holder.text_optionname.setTextColor(context.getResources().getColor(R.color.black_transparent_color
                ));
                holder.text_optionprice.setTextColor(context.getResources().getColor(R.color.black_transparent_color
                ));
                holder.image_add.setVisibility(View.VISIBLE);
                holder.image_remove.setVisibility(View.INVISIBLE);
            }

            holder.text_optionprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(items.get(position).netamount));
        }
        holder.image_add.setTag(position);
        holder.image_remove.setTag(position);
        holder.image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((Product) item).quantity == 0)
                    callback.QuantityCallback();


                ProductAtrributeOption option = items.get(position);
                if (!option.name.contains("Select")) {
                    if (currentAttribute.attr_condition.equalsIgnoreCase("1")) {

                        if (currentAttribute.optionSelected) {
                            for (int i = 0; i < currentAttribute.productAttributesOptions.size(); i++) {
                                currentAttribute.productAttributesOptions.get(i).optionSelected = false;
                            }
                            option.optionSelected = true;
                            currentAttribute.optionCount = 1;
                            option.productAttribute = currentAttribute;
                            item.selectedAttributeOptions.add(option);
                            notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();
                        } else {
                            currentAttribute.optionSelected = true;
                            option.optionSelected = true;
                            currentAttribute.optionCount = 1;
                            item.selectedAttributeOptions.add(option);
                            notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();
                        }
                    } else if (currentAttribute.attr_condition.equalsIgnoreCase("2")) {
                        currentAttribute.optionSelected = true;
                        if (option.optionSelected) {
                            if (currentAttribute.optionCount > 1) {
                                option.optionSelected = false;
                                item.selectedAttributeOptions.remove(option);
                                currentAttribute.optionCount--;
                            } else
                                Toast.makeText(context,
                                        "Choose atleast one add ons", Toast.LENGTH_SHORT).show();
                        } else {
                            option.optionSelected = true;
                            item.selectedAttributeOptions.add(option);
                            currentAttribute.optionCount++;
                        }
                        option.productAttribute = currentAttribute;
                        notifyDataSetChanged();
                        ((BaseFragmentActivity) context).ShowCArtItem();

                    } else {
                        currentAttribute.optionSelected = true;
                        if (option.optionSelected) {
                            option.optionSelected = false;
                            item.selectedAttributeOptions.remove(option);
                        } else {
                            option.optionSelected = true;
                            item.selectedAttributeOptions.add(option);
                        }

                        option.productAttribute = currentAttribute;
                        notifyDataSetChanged();
                        ((BaseFragmentActivity) context).ShowCArtItem();
                    }

                }
                /*else
                    Toast.makeText(context, "Please select the quantity of an item first", Toast.LENGTH_SHORT).show();*/
            }
        });


        holder.image_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((Product) item).quantity == 0)
                    callback.QuantityCallback();

                ProductAtrributeOption option = items.get(position);
                if (!option.name.contains("Select")) {
                    if (currentAttribute.attr_condition.equalsIgnoreCase("1")) {

                        if (currentAttribute.optionSelected) {
                            for (int i = 0; i < currentAttribute.productAttributesOptions.size(); i++) {
                                currentAttribute.productAttributesOptions.get(i).optionSelected = false;
                            }
                            option.optionSelected = true;
                            currentAttribute.optionCount = 1;
                            option.productAttribute = currentAttribute;
                            item.selectedAttributeOptions.add(option);
                            notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();
                        } else {
                            currentAttribute.optionSelected = true;
                            option.optionSelected = true;
                            currentAttribute.optionCount = 1;
                            item.selectedAttributeOptions.add(option);
                            notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();
                        }
                    } else if (currentAttribute.attr_condition.equalsIgnoreCase("2")) {
                        currentAttribute.optionSelected = true;
                        if (option.optionSelected) {
                            if (currentAttribute.optionCount > 1) {
                                option.optionSelected = false;
                                item.selectedAttributeOptions.remove(option);
                                currentAttribute.optionCount--;
                            } else
                                Toast.makeText(context,
                                        "Choose atleast one add ons", Toast.LENGTH_SHORT).show();
                        } else {
                            option.optionSelected = true;
                            item.selectedAttributeOptions.add(option);
                            currentAttribute.optionCount++;
                        }
                        option.productAttribute = currentAttribute;
                        notifyDataSetChanged();
                        ((BaseFragmentActivity) context).ShowCArtItem();

                    } else {
                        currentAttribute.optionSelected = true;
                        if (option.optionSelected) {
                            option.optionSelected = false;
                            item.selectedAttributeOptions.remove(option);
                        } else {
                            option.optionSelected = true;
                            item.selectedAttributeOptions.add(option);
                        }

                        option.productAttribute = currentAttribute;
                        notifyDataSetChanged();
                        ((BaseFragmentActivity) context).ShowCArtItem();
                    }

                }

            }
        });

       /* holder.rel_itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Product) item).quantity > 0) {
                    ProductAtrributeOption option = items.get(position);
                    if (!option.name.contains("Select")) {
                        if (currentAttribute.attr_condition.equalsIgnoreCase("1")) {

                            if (currentAttribute.optionSelected) {
                                for (int i = 0; i < currentAttribute.productAttributesOptions.size(); i++) {
                                    currentAttribute.productAttributesOptions.get(i).optionSelected = false;
                                }
                                option.optionSelected = true;
                                currentAttribute.optionCount = 1;
                                option.productAttribute = currentAttribute;
                                item.selectedAttributeOptions.add(option);
                                notifyDataSetChanged();
                                ((BaseFragmentActivity) context).ShowCArtItem();
                            } else {
                                currentAttribute.optionSelected = true;
                                option.optionSelected = true;
                                currentAttribute.optionCount = 1;
                                item.selectedAttributeOptions.add(option);
                                notifyDataSetChanged();
                                ((BaseFragmentActivity) context).ShowCArtItem();
                            }
                        } else if (currentAttribute.attr_condition.equalsIgnoreCase("2")) {
                            currentAttribute.optionSelected = true;
                            if (option.optionSelected) {
                                if (currentAttribute.optionCount > 2) {
                                    option.optionSelected = false;
                                    item.selectedAttributeOptions.remove(option);
                                    currentAttribute.optionCount--;
                                } else
                                    Toast.makeText(context,
                                            "Choose atleast two add ons", Toast.LENGTH_SHORT).show();
                            } else {
                                option.optionSelected = true;
                                item.selectedAttributeOptions.add(option);
                                currentAttribute.optionCount++;
                            }
                            option.productAttribute = currentAttribute;
                            notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();

                        } else {
                            currentAttribute.optionSelected = true;
                            if (option.optionSelected) {
                                option.optionSelected = false;
                                item.selectedAttributeOptions.remove(option);
                            } else {
                                option.optionSelected = true;
                                item.selectedAttributeOptions.add(option);
                            }

                            option.productAttribute = currentAttribute;
                            notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();
                        }

                    }
                } else
                    Toast.makeText(context, "Please select the quantity of an item first", Toast.LENGTH_SHORT).show();
            }
        });*/


        return convertView;
    }


    static class ViewHolder {
        TextView text_optionname, text_optionprice;
        ImageView image_add, image_remove;
        RelativeLayout rel_itemlayout;
    }


    @Override
    public boolean onBackPressed() {
        // TODO Auto-generated method stub
        return true;
    }
}

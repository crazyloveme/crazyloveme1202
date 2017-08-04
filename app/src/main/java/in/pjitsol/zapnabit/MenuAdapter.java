package in.pjitsol.zapnabit;

import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter for displaying menu drawer
 *
 * @author Bhawna Verma
 */
public class MenuAdapter extends BaseAdapter {


    private LayoutInflater li;
    private Context context;
    ArrayList<String> menuList = new ArrayList<>();

    public MenuAdapter(Context con, ArrayList<String> menuList) {
        this.context = con;
        this.menuList = menuList;
        li = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return menuList.get(position);
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
            convertView = li.inflate(R.layout.drawer_list_item, parent, false);
            holder = new ViewHolder();
            holder.text_module = (TextView) convertView.findViewById(R.id.text_module);
            holder.text_moduleinfo = (TextView) convertView.findViewById(R.id.text_moduleinfo);
            holder.text_secondtype = (TextView) convertView.findViewById(R.id.text_secondtype);
            holder.img_secondtype = (ImageView) convertView.findViewById(R.id.img_secondtype);
            holder.img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);
            holder.img_customerviewGrey = (ImageView) convertView.findViewById(R.id.img_customerviewGrey);
            holder.img_menu = (ImageView) convertView.findViewById(R.id.img_menu);
            holder.rel_background = (RelativeLayout) convertView.findViewById(R.id.rel_background);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text_module.setText(menuList.get(position));
        if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_CUSTOMERVIEW)) {
            holder.rel_background.setBackgroundColor(context.getResources().getColor(R.color.redsepratorline));
            holder.img_arrow.setVisibility(View.INVISIBLE);
            holder.text_moduleinfo.setVisibility(View.VISIBLE);
            holder.text_module.setVisibility(View.VISIBLE);
            holder.img_secondtype.setVisibility(View.INVISIBLE);
            holder.text_secondtype.setVisibility(View.INVISIBLE);
            if (PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_STATUS).equalsIgnoreCase("0")) {
                holder.text_module.setText("Customer View: OFF");
                holder.text_moduleinfo.setText("customer cannot view & order");
                holder.img_customerviewGrey.setVisibility(View.VISIBLE);
                holder.img_menu.setVisibility(View.INVISIBLE);
            } else {
                holder.img_menu.setVisibility(View.VISIBLE);
                holder.text_module.setText("Customer View: ON");
                holder.text_moduleinfo.setText("customer can view & order");
                holder.img_customerviewGrey.setVisibility(View.INVISIBLE);
                holder.img_menu.setImageResource(R.drawable.googlemap);
            }
        } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_TAKE_PICTURE) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_SCAN_BARCODE) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_ABOUT_US) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_PRIVACY_POLICY) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_FAQ) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_PROJECTWEFUNDER) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_PROJECT_DRONE) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_RELATIONS) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_TERMS) ||
                menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_REFUND)) {
            holder.rel_background.setBackgroundColor(context.getResources().getColor(R.color.red_dark));
            holder.img_arrow.setVisibility(View.INVISIBLE);
            holder.img_menu.setVisibility(View.GONE);
            holder.text_moduleinfo.setVisibility(View.GONE);
            holder.img_customerviewGrey.setVisibility(View.INVISIBLE);
            holder.img_secondtype.setVisibility(View.VISIBLE);
            holder.text_secondtype.setVisibility(View.VISIBLE);
            holder.text_module.setVisibility(View.INVISIBLE);
            holder.text_secondtype.setText(menuList.get(position));
            if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_TAKE_PICTURE)) {
                holder.img_secondtype.setImageResource(R.drawable.camera);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_SCAN_BARCODE)) {
                holder.img_secondtype.setImageResource(R.drawable.scanner);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_ABOUT_US)) {
                holder.img_secondtype.setImageResource(R.drawable.about_us);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_PRIVACY_POLICY)) {
                holder.img_secondtype.setImageResource(R.drawable.privacy_new);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_FAQ)) {
                holder.img_secondtype.setImageResource(R.drawable.faq);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_PROJECTWEFUNDER)) {
                holder.img_secondtype.setImageResource(R.drawable.wefunder);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_PROJECT_DRONE)) {
                holder.img_secondtype.setImageResource(R.drawable.drone);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_RELATIONS)) {
                holder.img_secondtype.setImageResource(R.drawable.investor);
            }
            else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_REFUND)) {
                holder.img_secondtype.setImageResource(R.drawable.investor);
            }
            else
                holder.img_secondtype.setImageResource(R.drawable.privacy);


        } else {
            holder.rel_background.setBackgroundColor(context.getResources().getColor(R.color.redsepratorline));
            holder.img_menu.setVisibility(View.VISIBLE);
            holder.text_module.setVisibility(View.VISIBLE);
            holder.img_customerviewGrey.setVisibility(View.INVISIBLE);
            holder.img_secondtype.setVisibility(View.INVISIBLE);
            holder.text_secondtype.setVisibility(View.INVISIBLE);
            holder.text_moduleinfo.setVisibility(View.GONE);

            if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_SEARCH_RESTO)) {
                holder.img_menu.setImageResource(R.drawable.search_menu);
                holder.img_arrow.setVisibility(View.INVISIBLE);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_MERCHANT_LINK)) {
                holder.img_menu.setImageResource(R.drawable.investor);
                holder.img_arrow.setVisibility(View.INVISIBLE);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_MY_ORDERS)) {
                holder.img_menu.setImageResource(R.drawable.myorders);
                holder.img_arrow.setVisibility(View.INVISIBLE);
            }
            else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_MY_MENU)) {
                holder.img_menu.setImageResource(R.drawable.menu_mer);
                holder.img_arrow.setVisibility(View.INVISIBLE);
            }
            else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_GENERALINFO)) {
                holder.img_menu.setImageResource(R.drawable.general);
                holder.img_arrow.setVisibility(View.VISIBLE);
                if (menuList.contains(StaticConstants.MENU_ABOUT_US))
                    holder.img_arrow.setImageResource(R.drawable.arrow_up);
                else
                    holder.img_arrow.setImageResource(R.drawable.arrow_white);
            } else if (menuList.get(position).equalsIgnoreCase(StaticConstants.MENU_TOOLS)) {
                holder.img_menu.setImageResource(R.drawable.tools);
                holder.img_arrow.setVisibility(View.VISIBLE);
                if (menuList.contains(StaticConstants.MENU_TAKE_PICTURE))
                    holder.img_arrow.setImageResource(R.drawable.arrow_up);
                else
                    holder.img_arrow.setImageResource(R.drawable.arrow_white);

            } else {
                holder.img_arrow.setVisibility(View.INVISIBLE);
                holder.img_menu.setImageResource(R.drawable.logout);
            }

        }
        /*Animation myRotation = AnimationUtils.loadAnimation(context, R.anim.shake);
		img_moduleicon.startAnimation(myRotation);*/
        return convertView;
    }

    public static class ViewHolder {
        RelativeLayout rel_background;
        TextView text_module, text_moduleinfo, text_secondtype;
        ImageView img_customerviewGrey, img_secondtype, img_menu, img_arrow;
    }
}

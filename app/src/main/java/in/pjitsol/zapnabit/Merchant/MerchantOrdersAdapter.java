package in.pjitsol.zapnabit.Merchant;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.User.HistoryItem;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MerchantOrdersAdapter extends  BaseAdapter{

	private LayoutInflater li;
	private Context context;
	public ArrayList<YelpEntity> placeList=new ArrayList<>();
	private ArrayList<HistoryItem> historyitems;
	public MerchantOrdersAdapter(Context con, ArrayList<HistoryItem> historyitems)
	{
		this.context=con;
		this.historyitems=historyitems;
		li=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return historyitems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return historyitems.get(position);
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
			convertView=li.inflate(R.layout.my_order_item_merchant, parent,false);
			holder=new ViewHolder();
			holder.txt_orderPaidStatus = (TextView) convertView.findViewById(R.id.txt_orderPaidStatus);
			holder.txt_merchantname = (TextView)convertView.findViewById(R.id.txt_merchantname);
			holder.txt_orderid = (TextView)convertView.findViewById(R.id.txt_orderid);
			holder.txt_orderamount = (TextView)convertView.findViewById(R.id.txt_orderamount);
			holder.text_orderdate = (TextView)convertView.findViewById(R.id.text_orderdate);
			holder.text_cancelStatus = (TextView)convertView.findViewById(R.id.text_cancelStatus);
			holder.txt_orderstatus = (TextView)convertView.findViewById(R.id.txt_orderstatus);
			holder.linear_rodone = (LinearLayout)convertView.findViewById(R.id.linear_rodone);
			
			holder.linear_rodtwo = (LinearLayout)convertView.findViewById(R.id.linear_rodtwo);
			holder.linearAcceptStatus = (LinearLayout)convertView.findViewById(R.id.linearAcceptStatus);
			holder.linearReadyStatus = (LinearLayout)convertView.findViewById(R.id.linearReadyStatus);
			holder.linearDoneStatus = (LinearLayout)convertView.findViewById(R.id.linearDoneStatus);
			holder.relativeStatusRelated = (RelativeLayout)convertView.findViewById(R.id.relativeStatusRelated);
			holder.relativeCancel = (RelativeLayout)convertView.findViewById(R.id.relativeCancel);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		if (historyitems.get(position).paid_status.equalsIgnoreCase("2")){
			holder.txt_orderPaidStatus.setText("Not Paid");
			holder.txt_orderPaidStatus.setTextColor(context.getResources().getColor(R.color.red));
		}

		else{
			holder.txt_orderPaidStatus.setText("Paid");
			holder.txt_orderPaidStatus.setTextColor(context.getResources().getColor(R.color.darkgreen));
		}

		holder.txt_merchantname.setText(historyitems.get(position).MerchantName);
		holder.txt_orderid.setText("Order Id: #"+Util.
				getDisplayOrderId(historyitems.get(position).orderId));
		holder.txt_orderamount.setText(StaticConstants.DOLLAR+historyitems.get(position).Total);
		holder.text_orderdate.setText(Util.changeDAteFormat(historyitems.get(position).OrderDate));
		
		if(historyitems.get(position).PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_3)){
			holder.relativeStatusRelated.setVisibility(View.GONE);
			holder.relativeCancel.setVisibility(View.VISIBLE);
			holder.text_cancelStatus.setText("Order cancelled by the customer");
		}
		else if(historyitems.get(position).PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_1)){
			holder.relativeStatusRelated.setVisibility(View.VISIBLE);
			holder.relativeCancel.setVisibility(View.GONE);
			Util.setStatusDarkGreenColour(holder.linear_rodone, context);
			Util.setStatusDarkGreenColour(holder.linear_rodtwo, context);
			holder.linearAcceptStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.accepted));
			holder.linearReadyStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.ready));
			holder.linearDoneStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.completed));
		}
		else if(historyitems.get(position).PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_10)){
			holder.relativeStatusRelated.setVisibility(View.VISIBLE);
			holder.relativeCancel.setVisibility(View.GONE);
			Util.setStatusLightGreenColour(holder.linear_rodone, context);
			Util.setStatusLightGreenColour(holder.linear_rodtwo, context);
			holder.linearAcceptStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.accepted));
			holder.linearReadyStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.ready_disabled));
			holder.linearDoneStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.completed_disabled));
		}
		else{
			holder.relativeStatusRelated.setVisibility(View.VISIBLE);
			holder.relativeCancel.setVisibility(View.GONE);
			Util.setStatusDarkGreenColour(holder.linear_rodone, context);
			Util.setStatusLightGreenColour(holder.linear_rodtwo, context);
			holder.linearAcceptStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.accepted));
			holder.linearReadyStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.ready));
			holder.linearDoneStatus.setBackgroundDrawable
			(context.getResources().getDrawable(R.drawable.completed_disabled));
		}
		
		return convertView;
	}


	static class ViewHolder {
		TextView txt_merchantname,txt_orderid,txt_orderamount,text_orderdate,
		txt_orderstatus,text_cancelStatus,txt_orderPaidStatus;
		RelativeLayout relativeStatusRelated,relativeCancel;
		LinearLayout linear_rodone,linear_rodtwo,linearAcceptStatus,linearReadyStatus,linearDoneStatus;
	}
}



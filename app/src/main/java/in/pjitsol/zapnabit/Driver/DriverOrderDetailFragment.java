package in.pjitsol.zapnabit.Driver;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Driver;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Entity.SaleAbleItem;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.User.HistoryItem;
import in.pjitsol.zapnabit.User.OrderItemAdapter;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;

public class DriverOrderDetailFragment extends Fragment implements
        OnBackPressListener, OnClickListener, IAsyncTaskRunner, OnCancelListener,AdapterView.OnItemSelectedListener {

    private LayoutInflater inflater;
    private Activity context;
    private int ITEM_POSITION;
    private HistoryItem historyItem;
    private Order historyOrder;
    private TextView txt_orderid;
    private TextView txt_orderdate;
    private TextView text_taxes;
    private TextView text_subtotal;
    private TextView text_comission;
    private TextView text_total;
    private RelativeLayout relativeStatusRelated;
    private RelativeLayout relativeCancel;
    private LinearLayout linear_rodone;
    private LinearLayout linearAcceptStatus;
    private LinearLayout linear_rodtwo;
    private LinearLayout linearReadyStatus;
    private LinearLayout linearDoneStatus;
    private ArrayList<SaleAbleItem> array;
    private TextView text_getDirections;
    private ListView list_itemdetails;
    private OrderItemAdapter adapter;
    private ProgressHUD progressDialog;
    private TextView text_username;
    private TextView txt_phno;
    private TextView text_specialInstru;
    private TextView text_fee;
    private TextView text_staticfee;
    private RelativeLayout rel_orderadress;
    private TextView text_address;
    private TextView text_city;
    private TextView text_name;
    private String driverId;
    private RelativeLayout rel_driverInfo;
    private TrackGPS gps;
    private double longitude;
    private double latitude;
    private TextView text_state;
    private TextView text_paid;
    private TextView text_staticpaid;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.driver_detail,
                container, false);
        ITEM_POSITION = getArguments().getInt(StaticConstants.HISTORY_ITEM_POSITION);
        this.inflater = inflater;
        historyItem = StaticConstants.historyItems.get(ITEM_POSITION);
        historyOrder = Util.getOrder(context,
                historyItem);
        initView(view);
        return view;
    }

    private void initView(View view) {
        GetLocation();
        list_itemdetails = (ListView) view.findViewById(R.id.list_itemdetails);
        View footerview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.driver_order_bottomview, null, false);
        View headerview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.order_headerview, null, false);
        list_itemdetails.addHeaderView(headerview);
        list_itemdetails.addFooterView(footerview);
        txt_orderid = (TextView) view.findViewById(R.id.txt_orderid);
        txt_orderdate = (TextView) view.findViewById(R.id.txt_orderdate);
        text_subtotal = (TextView) view.findViewById(R.id.text_subtotal);
        text_taxes = (TextView) view.findViewById(R.id.text_taxes);
        text_comission = (TextView) view.findViewById(R.id.text_comission);
        text_total = (TextView) view.findViewById(R.id.text_total);
        text_username = (TextView) view.findViewById(R.id.text_username);
        text_specialInstru = (TextView) view.findViewById(R.id.text_specialInstru);
        txt_phno = (TextView) view.findViewById(R.id.txt_phno);
        relativeStatusRelated = (RelativeLayout) view.findViewById(R.id.relativeStatusRelated);
        relativeCancel = (RelativeLayout) view.findViewById(R.id.relativeCancel);
        linear_rodone = (LinearLayout) view.findViewById(R.id.linear_rodone);
        linear_rodtwo = (LinearLayout) view.findViewById(R.id.linear_rodtwo);
        linearAcceptStatus = (LinearLayout) view.findViewById(R.id.linearAcceptStatus);
        linearReadyStatus = (LinearLayout) view.findViewById(R.id.linearReadyStatus);
        linearDoneStatus = (LinearLayout) view.findViewById(R.id.linearDoneStatus);
        text_fee = (TextView) view.findViewById(R.id.text_fee);
        text_staticfee = (TextView) view.findViewById(R.id.text_staticfee);
        rel_orderadress = (RelativeLayout) view.findViewById(R.id.rel_orderadress);
        rel_driverInfo = (RelativeLayout) view.findViewById(R.id.rel_driverInfo);
        text_address = (TextView) view.findViewById(R.id.text_address);
        text_city = (TextView) view.findViewById(R.id.text_city);
        text_state = (TextView) view.findViewById(R.id.text_state);
        text_name = (TextView) view.findViewById(R.id.text_name);
        text_getDirections= (TextView) view.findViewById(R.id.text_getDirections);
        text_paid = (TextView) view.findViewById(R.id.text_paid);
        text_staticpaid = (TextView) view.findViewById(R.id.text_staticpaid);
        text_getDirections.setOnClickListener(this);

        array = new ArrayList<SaleAbleItem>();

        adapter = new OrderItemAdapter(context, array);
        list_itemdetails.setAdapter(adapter);
        adapter.order = historyOrder;
        adapter.notifyDataSetChanged();


        UpdateStatus();




        txt_orderid.setText("#" + Util.
                getDisplayOrderId(StaticConstants.historyItems.get(ITEM_POSITION).orderId));
        text_subtotal.setText(StaticConstants.DOLLAR + StaticConstants.historyItems.get(ITEM_POSITION).SubTotal);
        txt_orderdate.setText(Util.changeDAteFormat(StaticConstants.historyItems
                .get(ITEM_POSITION).OrderDate));
        text_username.setText(StaticConstants.historyItems
                .get(ITEM_POSITION).MerchantName);
        txt_phno.setText(StaticConstants.historyItems
                .get(ITEM_POSITION).userPhone);
        text_specialInstru.setText(Util.decodeString(StaticConstants.historyItems.get(ITEM_POSITION)
                .SpecialInstruction));
        adapter.order.comission = Util.calculateComission(adapter.order.netprice, context);
        adapter.order.taxes = Util.calculateTaxes(adapter.order.netprice, context);
        text_comission.setText(StaticConstants.DOLLAR
                + adapter.order.comission);
        text_taxes.setText(StaticConstants.DOLLAR
                + adapter.order.taxes);
        text_total.setText(StaticConstants.DOLLAR +
                Util.CalculateTotalAmount(adapter.order));
        if (Float.valueOf(adapter.order.fee) != 0) {
            text_fee.setText(StaticConstants.DOLLAR
                    + adapter.order.fee);
            text_fee.setVisibility(View.VISIBLE);
            text_staticfee.setVisibility(View.VISIBLE);
        } else {
            text_fee.setVisibility(View.GONE);
            text_staticfee.setVisibility(View.GONE);
        }


        if (historyItem.orderType.equalsIgnoreCase("delivery")) {
            rel_orderadress.setVisibility(View.VISIBLE);
            text_address.setText(historyItem.delivery_address);
            text_city.setText(historyItem.delivery_city);
            text_state.setText(historyItem.delivery_state);
            text_name.setText(historyItem.delivery_address_name);
        } else {
            rel_orderadress.setVisibility(View.GONE);
        }

        if (historyItem.paid_status.equalsIgnoreCase("1")){
            text_staticpaid.setVisibility(View.VISIBLE);
            text_paid.setVisibility(View.VISIBLE);
            text_paid.setText(StaticConstants.DOLLAR +
                    Util.CalculateTotalAmount(adapter.order));
        }
        else{
            text_staticpaid.setVisibility(View.GONE);
            text_paid.setVisibility(View.GONE);
        }
    }
    private void GetLocation() {
        gps = new TrackGPS(context);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
        } else {
            gps.showSettingsAlert();
        }
    }
    private void UpdateStatus() {

        if (historyItem.PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_3)) {
            relativeStatusRelated.setVisibility(View.INVISIBLE);
            relativeCancel.setVisibility(View.VISIBLE);
        } else if (historyItem.PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_1)) {
            relativeStatusRelated.setVisibility(View.VISIBLE);
            relativeCancel.setVisibility(View.GONE);

            Util.setStatusDarkGreenColour(linear_rodone, context);
            Util.setStatusDarkGreenColour(linear_rodtwo, context);
            linearAcceptStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.accepted));
            linearReadyStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.ready));
            linearDoneStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.completed));

        } else if (historyItem.PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_10)) {
            relativeStatusRelated.setVisibility(View.VISIBLE);
            relativeCancel.setVisibility(View.GONE);
            Util.setStatusLightGreenColour(linear_rodone, context);
            Util.setStatusLightGreenColour(linear_rodtwo, context);
            linearAcceptStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.accepted));
            linearReadyStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.ready_disabled));
            linearDoneStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.completed_disabled));

        } else {
            relativeStatusRelated.setVisibility(View.VISIBLE);
            relativeCancel.setVisibility(View.GONE);
            Util.setStatusDarkGreenColour(linear_rodone, context);
            Util.setStatusLightGreenColour(linear_rodtwo, context);
            linearAcceptStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.accepted));
            linearReadyStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.ready));
            linearDoneStatus.setBackgroundDrawable
                    (context.getResources().getDrawable(R.drawable.completed_disabled));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_savedriver:
                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put(StaticConstants.JSON_MERCHANT_DRIVER_ID, driverId);
                par2.put(StaticConstants.JSON_DRIVER_MERCHANT_ID, PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CUSTOMER_MERCHANT_ID));
                par2.put(StaticConstants.JSON_TAG_ORDER_ID, historyOrder.orderId);
                progressDialog = ProgressHUD.show(context,
                        getResources().getString(R.string.label_loading_refresh), true, true, this);
                AuthCommanTask<HashMap<String, String>, ResultMessage> task2 = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                        context, DriverOrderDetailFragment.this,
                        BaseNetwork.obj().KEY_ASSIGN_DRIVER, progressDialog, StaticConstants.POST_METHOD);
                task2.execute(par2);
                break;
            case R.id.text_getDirections:

                String uri = "http://maps.google.com/maps?f=d&hl=en" +
                        "&saddr=" + historyItem.delivery_address+" ,"+
                        historyItem.delivery_city +" ,"+
                         historyItem.delivery_state +
                        "&daddr=" + Util.getAddress(latitude,longitude,context);

               /* String uri = "http://maps.google.com/maps?f=d&hl=en" +
                        "&saddr=" + historyItem.delivery_address + "," +  historyItem.delivery_city + "&daddr=" + Double.valueOf(
                        PrefHelper.getStoredString
                                (context, PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_LAT))
                        + "," + Double.valueOf(
                        PrefHelper.getStoredString
                                (context, PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_LONG));*/
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));
                break;
            default:
                break;
        }


    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnDriverMyOrderFragment();
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskStarting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskCompleted(Object result) {

        ResultMessage object = (ResultMessage) result;

        if (object.TYPE.equalsIgnoreCase(StaticConstants.MERCHANT_HISTORY)) {

            historyItem = StaticConstants.historyItems.get(ITEM_POSITION);
            historyOrder = Util.getOrder(context,
                    historyItem);
            UpdateStatus();
        }
        else if (object.TYPE.equalsIgnoreCase(StaticConstants.ASSIGN_DRIVER)) {
            rel_driverInfo.setVisibility(View.VISIBLE);

        }
        else {
            onBackPressed();
        }


    }

    @Override
    public void taskProgress(String urlString, Object progress) {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskErrorMessage(Object result) {
        // TODO Auto-generated method stub

    }

    @Override
    public Context getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPhoneNo() {
        return StaticConstants.historyItems.get(ITEM_POSITION).userPhone;
    }

    public void FetchHistory() {
        HashMap<String, String> par = new HashMap<String, String>();
        par.put(StaticConstants.JSON_MERCHANT_DRIVER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.DRIVER_ID));
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, DriverOrderDetailFragment.this,
                BaseNetwork.obj().KEY_DRIVER_HISTORY, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        driverId=((Driver)parent.getItemAtPosition(position)).driver_id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

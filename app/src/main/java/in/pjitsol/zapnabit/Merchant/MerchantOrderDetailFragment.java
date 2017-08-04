package in.pjitsol.zapnabit.Merchant;

import java.util.ArrayList;
import java.util.HashMap;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Db.DriverDb;
import in.pjitsol.zapnabit.Entity.CategoryProductinfo;
import in.pjitsol.zapnabit.Entity.Driver;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Entity.SaleAbleItem;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.AssignDriverDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.User.HistoryItem;
import in.pjitsol.zapnabit.User.OrderItemAdapter;
import in.pjitsol.zapnabit.User.UserOrderDetailFragment;
import in.pjitsol.zapnabit.Util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MerchantOrderDetailFragment extends Fragment implements
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
    private TextView text_closed;
    private TextView text_ready;
    private ProgressHUD progressDialog;
    private TextView text_username;
    private TextView txt_phno;
    private TextView text_specialInstru;
    private TextView text_fee;
    private TextView text_staticfee;
    private Spinner spinnerDriver;
    private RelativeLayout rel_orderadress;
    private RelativeLayout rel_assignaddress;
    private TextView text_address;
    private TextView text_city;
    private TextView text_name;
    private TextView text_savedriver;
    private String driverId;
    private RelativeLayout rel_driverInfo;
    private TextView text_DriverName;
    private String driverName;
    private TextView text_state;
    private TextView txt_orderType;
    private TextView text_paid;
    private TextView text_staticpaid;
    private ArrayList<Driver> driverList=new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.order_detail,
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
        list_itemdetails = (ListView) view.findViewById(R.id.list_itemdetails);
        View footerview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.order_bottomview, null, false);
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
        text_closed = (TextView) view.findViewById(R.id.text_closed);
        text_username = (TextView) view.findViewById(R.id.text_username);
        text_ready = (TextView) view.findViewById(R.id.text_readybottom);
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
        spinnerDriver = (Spinner) view.findViewById(R.id.spinnerDriver);
        rel_orderadress = (RelativeLayout) view.findViewById(R.id.rel_orderadress);
        rel_assignaddress = (RelativeLayout) view.findViewById(R.id.rel_assignaddress);
        rel_driverInfo = (RelativeLayout) view.findViewById(R.id.rel_driverInfo);
        text_address = (TextView) view.findViewById(R.id.text_address);
        text_city = (TextView) view.findViewById(R.id.text_city);
        text_name = (TextView) view.findViewById(R.id.text_name);
        text_state= (TextView) view.findViewById(R.id.text_state);
        text_savedriver= (TextView) view.findViewById(R.id.text_savedriver);
        text_DriverName= (TextView) view.findViewById(R.id.text_DriverName);
        txt_orderType= (TextView) view.findViewById(R.id.txt_orderType);
        text_paid = (TextView) view.findViewById(R.id.text_paid);
        text_staticpaid = (TextView) view.findViewById(R.id.text_staticpaid);
        //driverList= DriverDb.obj().getAllPrefrences(context);


        ArrayAdapter<Driver> dataAdapter = new
                ArrayAdapter<Driver>(context, android.R.layout.simple_spinner_item,
                StaticConstants.driversList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDriver.setAdapter(dataAdapter);
        spinnerDriver.setOnItemSelectedListener(this);

        array = new ArrayList<SaleAbleItem>();

        adapter = new OrderItemAdapter(context, array);
        list_itemdetails.setAdapter(adapter);
        adapter.order = historyOrder;
        adapter.notifyDataSetChanged();


        UpdateStatus();


        text_closed.setOnClickListener(this);
        text_ready.setOnClickListener(this);
        text_savedriver.setOnClickListener(this);


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

       /* if (Float.valueOf(adapter.order.fee) != 0) {
            text_fee.setText(StaticConstants.DOLLAR
                    + adapter.order.fee);
            text_fee.setVisibility(View.VISIBLE);
            text_staticfee.setVisibility(View.VISIBLE);
        } else {
            text_fee.setVisibility(View.GONE);
            text_staticfee.setVisibility(View.GONE);
        }*/


        if (historyItem.orderType.equalsIgnoreCase("delivery")) {
            txt_orderType.setText("DELIVERY");
            if (adapter.order.fee.equalsIgnoreCase("0"))
                text_fee.setText("Free");
            else
                text_fee.setText(StaticConstants.DOLLAR
                        + adapter.order.fee);

            text_fee.setVisibility(View.VISIBLE);
            text_staticfee.setVisibility(View.VISIBLE);
            if(historyItem.order_assigned){
                rel_assignaddress.setVisibility(View.GONE);
                rel_orderadress.setVisibility(View.VISIBLE);
                rel_driverInfo.setVisibility(View.VISIBLE);
            }
            else{

                new AssignDriverDialog(context).show();
                rel_assignaddress.setVisibility(View.VISIBLE);
                rel_orderadress.setVisibility(View.VISIBLE);
            }

            text_address.setText(historyItem.delivery_address);
            text_city.setText(historyItem.delivery_city);
            text_name.setText(historyItem.delivery_address_name);
            text_state.setText(historyItem.delivery_state);
            text_DriverName.setText(historyItem.assigned_driver);
        } else {
            txt_orderType.setText("PICK-UP");
            rel_assignaddress.setVisibility(View.GONE);
            rel_orderadress.setVisibility(View.GONE);
            text_fee.setVisibility(View.GONE);
            text_staticfee.setVisibility(View.GONE);
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
            case R.id.text_closed:
                HashMap<String, String> par = new HashMap<String, String>();
                par.put(StaticConstants.JSON_USER_ID, StaticConstants.historyItems
                        .get(ITEM_POSITION).merchantId);
                par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CUSTOMER_MERCHANT_ID));
                par.put(StaticConstants.JSON_TAG_ORDER_ID, historyOrder.orderId);
                par.put(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS, StaticConstants.PAYMENT_STATUS_1);
                progressDialog = ProgressHUD.show(context,
                        getResources().getString(R.string.label_loading_refresh), true, true, this);
                AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                        context, MerchantOrderDetailFragment.this,
                        BaseNetwork.obj().KEY_USER_ORDER_UPDATE, progressDialog, StaticConstants.POST_METHOD);
                task.execute(par);
                break;

            case R.id.text_readybottom:
                HashMap<String, String> par1 = new HashMap<String, String>();
                par1.put(StaticConstants.JSON_USER_ID, StaticConstants.historyItems
                        .get(ITEM_POSITION).merchantId);
                par1.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CUSTOMER_MERCHANT_ID));
                par1.put(StaticConstants.JSON_TAG_ORDER_ID, historyOrder.orderId);
                par1.put(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS, StaticConstants.PAYMENT_STATUS_2);
                progressDialog = ProgressHUD.show(context,
                        getResources().getString(R.string.label_loading_refresh), true, true, this);
                AuthCommanTask<HashMap<String, String>, ResultMessage> task1 = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                        context, MerchantOrderDetailFragment.this,
                        BaseNetwork.obj().KEY_USER_ORDER_UPDATE, progressDialog, StaticConstants.POST_METHOD);
                task1.execute(par1);
                break;
            case R.id.text_savedriver:
                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put(StaticConstants.JSON_MERCHANT_DRIVER_ID, driverId);
                par2.put(StaticConstants.JSON_DRIVER_MERCHANT_ID, PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CUSTOMER_MERCHANT_ID));
                par2.put("order_id", historyOrder.orderId);
                progressDialog = ProgressHUD.show(context,
                        getResources().getString(R.string.label_loading_refresh), true, true, this);
                AuthCommanTask<HashMap<String, String>, ResultMessage> task2 = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                        context, MerchantOrderDetailFragment.this,
                        BaseNetwork.obj().KEY_ASSIGN_DRIVER, progressDialog, StaticConstants.POST_METHOD);
                task2.execute(par2);
                break;
            default:
                break;
        }


    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnMerchantMyOrderFragment();
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
            rel_assignaddress.setVisibility(View.GONE);
            rel_driverInfo.setVisibility(View.VISIBLE);
            text_DriverName.setText(driverName);

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
        par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID));
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, MerchantOrderDetailFragment.this,
                BaseNetwork.obj().KEY_MERCHANT_HISTORY, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        driverId=((Driver)parent.getItemAtPosition(position)).driver_id;
        driverName=((Driver)parent.getItemAtPosition(position)).driver_name;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

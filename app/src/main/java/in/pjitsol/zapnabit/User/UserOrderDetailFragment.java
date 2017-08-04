package in.pjitsol.zapnabit.User;

import in.pjitsol.zapnabit.BaseFragmentActivity;
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
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserOrderDetailFragment extends Fragment implements
        OnBackPressListener, OnClickListener, IAsyncTaskRunner, OnCancelListener {

    private LayoutInflater inflater;
    private Activity context;
    private TextView txt_orderid;
    private TextView txt_orderdate;
    private TextView text_subtotal;
    private TextView text_taxes;
    private TextView text_comission;
    private TextView text_total;
    private TextView text_getDirections;
    private LinearLayout linear_itemdetails;
    private int ITEM_POSITION;
    private ListView list_itemdetails;
    private ArrayList<SaleAbleItem> array;
    private OrderItemAdapter adapter;
    private Order historyOrder;
    private TextView text_cancel;
    private TextView text_Reorder;
    private ProgressHUD progressDialog;
    private RelativeLayout relativeStatusRelated;
    private RelativeLayout relativeCancel;
    private HistoryItem historyItem;
    private LinearLayout linear_rodone;
    private LinearLayout linear_rodtwo;
    private LinearLayout linearAcceptStatus;
    private LinearLayout linearReadyStatus;
    private LinearLayout linearDoneStatus;
    private TextView text_specialInstru;
    private TextView text_username;
    private TrackGPS gps;
    private double longitude;
    private double latitude;
    private TextView text_fee;
    private TextView text_staticfee;
    private RelativeLayout rel_orderadress;
    private TextView text_orderType;
    private TextView text_name;
    private TextView text_address;
    private TextView text_city;
    private TextView text_state;
    private TextView txt_orderType;
    private TextView text_paid;
    private TextView text_staticpaid;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.user_order_detail,
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
        checkPermission(context);
        //GetLocation();
        list_itemdetails = (ListView) view.findViewById(R.id.list_itemdetails);
        View footerview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.userorder_bottomview, null, false);
        View headerview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.userorder_headerview, null, false);
        list_itemdetails.addHeaderView(headerview);
        list_itemdetails.addFooterView(footerview);
        text_username = (TextView) view.findViewById(R.id.text_username);
        text_specialInstru = (TextView) view.findViewById(R.id.text_specialInstru);
        txt_orderid = (TextView) view.findViewById(R.id.txt_orderid);
        txt_orderdate = (TextView) view.findViewById(R.id.txt_orderdate);
        text_subtotal = (TextView) view.findViewById(R.id.text_subtotal);
        text_taxes = (TextView) view.findViewById(R.id.text_taxes);
        text_comission = (TextView) view.findViewById(R.id.text_comission);
        text_total = (TextView) view.findViewById(R.id.text_total);
        text_cancel = (TextView) view.findViewById(R.id.text_cancel);
        text_fee = (TextView) view.findViewById(R.id.text_fee);
        text_staticfee = (TextView) view.findViewById(R.id.text_staticfee);
        text_Reorder = (TextView) view.findViewById(R.id.text_Reorder);
        relativeStatusRelated = (RelativeLayout) view.findViewById(R.id.relativeStatusRelated);
        relativeCancel = (RelativeLayout) view.findViewById(R.id.relativeCancel);
        linear_rodone = (LinearLayout) view.findViewById(R.id.linear_rodone);
        linear_rodtwo = (LinearLayout) view.findViewById(R.id.linear_rodtwo);
        linearAcceptStatus = (LinearLayout) view.findViewById(R.id.linearAcceptStatus);
        linearReadyStatus = (LinearLayout) view.findViewById(R.id.linearReadyStatus);
        linearDoneStatus = (LinearLayout) view.findViewById(R.id.linearDoneStatus);
        rel_orderadress = (RelativeLayout) view.findViewById(R.id.rel_orderadress);
        text_orderType = (TextView) view.findViewById(R.id.text_orderType);
        text_name = (TextView) view.findViewById(R.id.text_name);
        text_address = (TextView) view.findViewById(R.id.text_address);
        text_city = (TextView) view.findViewById(R.id.text_city);
        text_state = (TextView) view.findViewById(R.id.text_state);
        txt_orderType = (TextView) view.findViewById(R.id.txt_orderType);
        text_paid = (TextView) view.findViewById(R.id.text_paid);
        text_staticpaid = (TextView) view.findViewById(R.id.text_staticpaid);



        array = new ArrayList<SaleAbleItem>();
        text_getDirections = (TextView) view.findViewById(R.id.text_getDirections);
        adapter = new OrderItemAdapter(context, array);
        list_itemdetails.setAdapter(adapter);
        adapter.order = historyOrder;
        adapter.notifyDataSetChanged();
        text_getDirections.setOnClickListener(this);

        UpdateStatus();

        text_cancel.setOnClickListener(this);
        text_Reorder.setOnClickListener(this);
        text_username.setText(StaticConstants.historyItems.get(ITEM_POSITION).MerchantName);
        txt_orderid.setText("#" + Util.
                getDisplayOrderId(StaticConstants.historyItems.get(ITEM_POSITION).orderId));
        text_subtotal.setText(StaticConstants.DOLLAR + StaticConstants.historyItems.get(ITEM_POSITION).SubTotal);
        txt_orderdate.setText(Util
                .changeDAteFormat(StaticConstants.historyItems.get(ITEM_POSITION).OrderDate));

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


     /*   if (Float.valueOf(adapter.order.fee) != 0) {
            text_fee.setText(StaticConstants.DOLLAR
                    + adapter.order.fee);
            text_fee.setVisibility(View.VISIBLE);
            text_staticfee.setVisibility(View.VISIBLE);
        } else {
            text_fee.setVisibility(View.GONE);
            text_staticfee.setVisibility(View.GONE);
        }*/

        if (historyItem.orderType.equalsIgnoreCase("delivery")) {

            if (adapter.order.fee.equalsIgnoreCase("0"))
                text_fee.setText("Free");
            else
                text_fee.setText(StaticConstants.DOLLAR
                        + adapter.order.fee);

            text_fee.setVisibility(View.VISIBLE);
            text_staticfee.setVisibility(View.VISIBLE);

            rel_orderadress.setVisibility(View.VISIBLE);
            text_orderType.setText("DELIVERY");
            txt_orderType.setText("DELIVERY");
        } else {
            rel_orderadress.setVisibility(View.GONE);
            text_orderType.setText("PICK-UP");
            txt_orderType.setText("PICK-UP");
            text_fee.setVisibility(View.GONE);
            text_staticfee.setVisibility(View.GONE);
        }


        text_address.setText(historyItem.delivery_address);
        text_city.setText(historyItem.delivery_city);
        text_state.setText(historyItem.delivery_state);
        text_name.setText(historyItem.delivery_address_name);
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
        //txt_orderType.setText(historyItem.orderType);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_Reorder:
                if (historyItem.PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_1)
                        ) {
                    StaticConstants.CURRENT_ORDER = historyOrder;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(StaticConstants.BACKPRESSED, true);
                    ((BaseFragmentActivity) context).OnCartFragment(bundle);
                } else
                    Toast.makeText(context, "This order is not closed yet", Toast.LENGTH_LONG).show();

                break;
            case R.id.text_cancel:
                if (historyItem.PaymentStatus.equalsIgnoreCase(StaticConstants.PAYMENT_STATUS_10)) {
                    HashMap<String, String> par = new HashMap<String, String>();
                    par.put(StaticConstants.JSON_USER_ID, PrefHelper.
                            getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                    StaticConstants.USER_ID));
                    par.put(StaticConstants.JSON_MERCHANT_ID, historyItem.merchantId);
                    par.put(StaticConstants.JSON_TAG_ORDER_ID, historyOrder.orderId);
                    par.put(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS, StaticConstants.PAYMENT_STATUS_3);
                    progressDialog = ProgressHUD.show(context,
                            getResources().getString(R.string.label_loading_refresh), true, true, this);
                    AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                            context, UserOrderDetailFragment.this,
                            BaseNetwork.obj().KEY_USER_ORDER_UPDATE, progressDialog, StaticConstants.POST_METHOD);
                    task.execute(par);
                } else
                    Toast.makeText(context, "Order is in progress", Toast.LENGTH_LONG).show();
                break;
            case R.id.text_getDirections:

                String uri = "http://maps.google.com/maps?f=d&hl=en" +
                        "&saddr=" + latitude + "," + longitude + "&daddr=" + Double.valueOf(
                        PrefHelper.getStoredString
                                (context, PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_LAT))
                        + "," + Double.valueOf(
                        PrefHelper.getStoredString
                                (context, PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_LONG));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));

                break;
            default:
                break;
        }

    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnUserMyOrderFragment();
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
        if (object.TYPE.equalsIgnoreCase(StaticConstants.USER_HISTORY)) {
            historyItem = StaticConstants.historyItems.get(ITEM_POSITION);
            historyOrder = Util.getOrder(context,
                    historyItem);
            UpdateStatus();


        } else {
            onBackPressed();
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


    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;

    public void checkPermission(final Context context) {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Location");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        GetLocation();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted
                    GetLocation();
                } else {
                    // Permission Denied
                    Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void FetchHistory() {
        HashMap<String, String> par = new HashMap<String, String>();
        par.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ID));
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, UserOrderDetailFragment.this,
                BaseNetwork.obj().KEY_USER_HISTORY, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);

    }
}

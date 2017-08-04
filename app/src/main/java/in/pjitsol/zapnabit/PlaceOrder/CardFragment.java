package in.pjitsol.zapnabit.PlaceOrder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.CardActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.AppWebViewClients;
import in.pjitsol.zapnabit.Ui.IcallBackWebView;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.StripeWebViewClient;
import in.pjitsol.zapnabit.Util.Util;

/**
 * Created by Bhawna on 6/9/2017.
 */

public class CardFragment extends Fragment implements
        OnBackPressListener, View.OnClickListener, IcallBackWebView,
        DialogInterface.OnCancelListener,IAsyncTaskRunner {
    private LayoutInflater inflater;
    private Activity context;
    private RelativeLayout rel_cardDetails;
    private RelativeLayout rel_webview;
    private TextView text_submit;
    private WebView mWebView;
    private ProgressBar progress_bar;
    private EditText et_name;
    private EditText et_month;
    private EditText et_year;
    private EditText et_cvv;
    private ProgressHUD progressDialog;
    Stripe stripe;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.card_details,
                container, false);
        this.inflater = inflater;
         stripe = new Stripe(context,"pk_test_OqtoAqrcb9Hqzwi2QCBCo46N");
        initview(view);
        return view;
    }

    private void initview(View view) {
        rel_cardDetails = (RelativeLayout) view.findViewById(R.id.rel_cardDetails);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_month = (EditText) view.findViewById(R.id.et_month);
        et_year = (EditText) view.findViewById(R.id.et_year);
        et_cvv = (EditText) view.findViewById(R.id.et_cvv);
        rel_webview = (RelativeLayout) view.findViewById(R.id.rel_webview);
        mWebView = (WebView) view.findViewById(R.id.webview);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        text_submit = (TextView) view.findViewById(R.id.text_submit);
        text_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_submit:
                progressDialog = ProgressHUD.show(context,
                        getResources().getString(R.string.label_loading_refresh), true, true, this);
                Card card = new Card(et_name.getText().toString().trim()
                        , Integer.valueOf(et_month.getText().toString()),
                        Integer.valueOf( et_year.getText().toString()),
                        et_cvv.getText().toString());

                if(card.validateCard()){
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    progressDialog.dismiss();
                                   SetToken(token);

                                }
                                public void onError(Exception error) {
                                }
                            }
                    );
                }else{
                    Toast.makeText(context,"Invalid Card. Please check your card details.",Toast.LENGTH_SHORT).show();
                }


                //http://dev.ikkapower.com/zapnabit/admin/merchant/payment?card_number=4000056655665556&cvv=123&month=05&year=2018
               /* String RESTO_URL = "http://dev.ikkapower.com/zapnabit/admin/payment/payment?card_number="
                        + et_name.getText().toString() + "&cvv=" + et_cvv.getText().toString() + "&month="
                        + et_month.getText().toString() + "&year=" + et_year.getText().toString()
                        + "&amount=" +StaticConstants.CURRENT_ORDER.netprice;
                rel_cardDetails.setVisibility(View.GONE);
                rel_webview.setVisibility(View.VISIBLE);
                mWebView.loadUrl(RESTO_URL);
                // Enable Javascript
                WebSettings webSettings = mWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                // Force links and redirects to open in the WebView instead of in a browser
                mWebView.setWebViewClient(new StripeWebViewClient(progress_bar, CardFragment.this));*/
                break;
        }

    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnCartFragment(new Bundle());
        return true;
    }

    @Override
    public void callbackWebView(boolean result) {
        if(result)
        OrderPlacedASync(StaticConstants.CURRENT_ORDER);
        else{
            Toast.makeText(context, "Transaction failed! Please check your card details.", Toast.LENGTH_SHORT).show();
            rel_cardDetails.setVisibility(View.VISIBLE);
            rel_webview.setVisibility(View.GONE);
        }

    }



    private void SetToken(Token token){
        HashMap<String, String> par = new HashMap<String, String>();
        par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.CUSTOMER_MERCHANT_ID));
        par.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ID));
        par.put(StaticConstants.JSON_TAG_AMOUNT, Util.CalculateTotalAmount(StaticConstants.CURRENT_ORDER)
        );
        par.put(StaticConstants.JSON_TAG_STRIPE_TOKEN,token.getId()
        );
        par.put(StaticConstants.JSON_TAG_COMISSION,StaticConstants.CURRENT_ORDER.comission
        );
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, CardFragment.this,
                BaseNetwork.obj().KEY_PAYMENT, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);
    }

    private void OrderPlacedASync(Order order) {


        if (order.fee.equalsIgnoreCase("-1") || order.fee.equalsIgnoreCase("-2"))
            order.fee = "0";

        HashMap<String, String> par = new HashMap<String, String>();
        par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.CUSTOMER_MERCHANT_ID));
        par.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ID));
        par.put(StaticConstants.JSON_TAG_CUD_NETPRICE,
                String.valueOf(order.netprice));
        if (order.order_type.equalsIgnoreCase("delivery"))
            par.put(StaticConstants.JSON_TAG_CUD_DELIVERYFEES,
                    String.valueOf(order.fee));
        else
            par.put(StaticConstants.JSON_TAG_CUD_DELIVERYFEES,
                    "0");
        par.put(StaticConstants.JSON_TAG_CUD_TOTALAMOUNT, Util.CalculateTotalAmount(order)
        );
        par.put(StaticConstants.JSON_TAG_CUD_SPECIAL,
                Util.encodeString( order.specialInstru));
        PrefHelper.storeString(context,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.JSON_TAG_CUD_SPECIAL, order.specialInstru);

        par.put(StaticConstants.JSON_TAG_CUD_REQUESTTYPE,
                "");
        par.put(StaticConstants.JSON_TAG_CUD_DEVICE_TYPE,
                StaticConstants.ANDROID);

        par.put(StaticConstants.JSON_TAG_CUD_DEVICEID,
                PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
        par.put(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS,
                StaticConstants.PAYMENT_STATUS_10);
        par.put(StaticConstants.JSON_TAG_CUD_PAYMENT_TYPE,
                "Card");
        par.put(StaticConstants.JSON_TAG_CUD_PAID_STATUS,
                "1");
        par.put(StaticConstants.JSON_TAG_CUD_ORDER_TYPE,
                order.order_type);
        if (order.order_type.equalsIgnoreCase("delivery")) {
            par.put(StaticConstants.RES_JSON_USER_DELIVERY_ID,
                    PrefHelper.
                            getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.RES_JSON_USER_DELIVERY_ID));
        }
        try {
            JSONArray products = (JSONArray) Util.getorderproduct(order);
            par.put(StaticConstants.JSON_TAG_CUD_CUSTOMERORDERHISTORY,
                    products.toString());
            order.orderJson = products.toString();

        } catch (JSONException je) {
            je.fillInStackTrace();
        }

        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, CardFragment.this,
                BaseNetwork.obj().KEY_PLACE_ORDER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void taskStarting() {

    }

    @Override
    public void taskCompleted(Object o) {
        ResultMessage message= (ResultMessage) o;
        if(message.TYPE.equalsIgnoreCase(StaticConstants.PAYMENT_SUCCESSFUL)){
            OrderPlacedASync(StaticConstants.CURRENT_ORDER);
        }
        else{
            StaticConstants.CURRENT_ORDER = new Order();
            ((BaseFragmentActivity) context).HideCArtItem();
            ((BaseFragmentActivity) context).OnUserMyOrderFragment();
        }

    }

    @Override
    public void taskProgress(String urlString, Object o) {

    }

    @Override
    public void taskErrorMessage(Object o) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= (Activity) context;
    }
}

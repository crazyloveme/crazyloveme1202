package in.pjitsol.zapnabit;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BarCode.ProductInfo;
import in.pjitsol.zapnabit.BarCode.ScanBarCodeFragment;
import in.pjitsol.zapnabit.BarCode.TakePicture;
import in.pjitsol.zapnabit.BarCode.TakePictureInfo;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Db.DriverDb;
import in.pjitsol.zapnabit.Driver.DriverOrderDetailFragment;
import in.pjitsol.zapnabit.Driver.DriverOrdersFragment;
import in.pjitsol.zapnabit.Entity.Driver;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Login.LoginSocialActivity;
import in.pjitsol.zapnabit.Merchant.LinkRestaurant;
import in.pjitsol.zapnabit.Merchant.MerchantMenuFragment;
import in.pjitsol.zapnabit.Merchant.MerchantOrderDetailFragment;
import in.pjitsol.zapnabit.Merchant.MerchantOrdersFragment;
import in.pjitsol.zapnabit.Merchant.MerchantVerifyFragment;
import in.pjitsol.zapnabit.NearbySearch.NearbySearchFragment;
import in.pjitsol.zapnabit.NearbySearch.RestoDetailFragment;
import in.pjitsol.zapnabit.NearbySearch.WebViewFragment;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.PlaceOrder.CardFragment;
import in.pjitsol.zapnabit.PlaceOrder.CartFragment;
import in.pjitsol.zapnabit.PlaceOrder.MenuFragment;
import in.pjitsol.zapnabit.PrivacyPolicy.AboutusFragment;
import in.pjitsol.zapnabit.PrivacyPolicy.PrivacyFragment;
import in.pjitsol.zapnabit.Ui.CongratulationDialog;
import in.pjitsol.zapnabit.Ui.DeleteConfirmationDialog;
import in.pjitsol.zapnabit.Ui.IDeleteConfirmation;
import in.pjitsol.zapnabit.Ui.IOverLayClicked;
import in.pjitsol.zapnabit.Ui.IcallBackBarcode;
import in.pjitsol.zapnabit.Ui.LogoutDialog;
import in.pjitsol.zapnabit.Ui.MenuDrawer;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.ScanMoreDialog;
import in.pjitsol.zapnabit.Ui.SearchInfoDialog;
import in.pjitsol.zapnabit.Ui.ZapnabitWebViewFragment;
import in.pjitsol.zapnabit.User.EditProfileFragment;
import in.pjitsol.zapnabit.User.UserOrderDetailFragment;
import in.pjitsol.zapnabit.User.UserOrdersFragment;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Util.WalletUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;

public class BaseFragmentActivity extends FragmentActivity implements OnClickListener,
        IOverLayClicked, OnCancelListener, IAsyncTaskRunner, OnItemClickListener,
        IDeleteConfirmation, IcallBackBarcode, GoogleApiClient.OnConnectionFailedListener {
    public static final int PAYPAL_REQUEST_CODE = StaticConstants.PAYPAL_REQUEST;
    OnBackPressListener currentBackListener;
    private RelativeLayout baseFooter;
    private MenuDrawer slide_me;
    private ListView list_drawer;
    private ArrayList<String> modulelist = new ArrayList<>();
    private MenuAdapter adapter_drawer;
    private ImageView text_menu;
    private TextView text_send;
    private ProgressHUD progressDialog;
    private ImageView img_sdktype;
    private TextView text_linkstore;
    private ImageView img_back;
    private boolean YELP_REGISTERED;
    private TextView txt_user_name;
    TextView text_cartPrice;
    private ImageView img_call;
    private TextView text_restoname;
    private ImageView img_search;
    private EditText edit_search;
    private String USER_TYPE;
    private ImageView img_info;
    private ImageView img_cross;
    private ImageView img_usericon;
    private ImageLoader imageLoader;
    private ImageView img_refresh;
    private ImageView img_refreshdetail;
    private String callFromNoti;
    private TextView txt_editprofile;
    private String fburl;
    private ImageView img_deletecart;
    private ImageView img_favourite;
    private boolean Favourite = false;
    private static PayPalConfiguration config;
    private RelativeLayout paymentOptionLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basefragment);
        Intent intent = getIntent();
        callFromNoti = intent.getStringExtra(StaticConstants.NOTIFICATION_CALL);
        USER_TYPE = getIntent().getStringExtra(StaticConstants.USER_TYPE);
        YELP_REGISTERED = PrefHelper.getStoredBoolean(this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED);
        if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)) {
            modulelist.add(StaticConstants.MENU_SEARCH_RESTO);
            modulelist.add(StaticConstants.MENU_MY_ORDERS);
            modulelist.add(StaticConstants.MENU_GENERALINFO);
            modulelist.add(StaticConstants.MENU_LOGOUT);
        } else if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT)) {
            if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_TYPE).equalsIgnoreCase("0")) {
                if (!PrefHelper.getStoredBoolean(this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                    modulelist.add(StaticConstants.MENU_MERCHANT_LINK);
                else
                    modulelist.add(StaticConstants.MENU_CUSTOMERVIEW);
                modulelist.add(StaticConstants.MENU_MY_ORDERS);
                modulelist.add(StaticConstants.MENU_MY_MENU);
                modulelist.add(StaticConstants.MENU_TOOLS);
                modulelist.add(StaticConstants.MENU_GENERALINFO);
                modulelist.add(StaticConstants.MENU_LOGOUT);
            } else {
                modulelist.add(StaticConstants.MENU_TOOLS);
                modulelist.add(StaticConstants.MENU_GENERALINFO);
                modulelist.add(StaticConstants.MENU_LOGOUT);
            }


        } else {
            modulelist.add(StaticConstants.MENU_MY_ORDERS);
            modulelist.add(StaticConstants.MENU_GENERALINFO);
            modulelist.add(StaticConstants.MENU_LOGOUT);
        }
        initview();
        config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        Intent intentpaypal = new Intent(this, PayPalService.class);
        intentpaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intentpaypal);

    }

    private void initview() {

        imageLoader = new ImageLoader(BaseFragmentActivity.this);
        text_menu = (ImageView) findViewById(R.id.text_menu);
        text_cartPrice = (TextView) findViewById(R.id.text_cartPrice);
        text_linkstore = (TextView) findViewById(R.id.text_linkstore);
        text_restoname = (TextView) findViewById(R.id.text_restoname);
        baseFooter = (RelativeLayout) findViewById(R.id.baseFooter);
        img_sdktype = (ImageView) findViewById(R.id.img_sdktype);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_cross = (ImageView) findViewById(R.id.img_cross);
        img_refresh = (ImageView) findViewById(R.id.img_refresh);
        img_refreshdetail = (ImageView) findViewById(R.id.img_refreshdetail);
        img_favourite = (ImageView) findViewById(R.id.img_favourite);
        text_send = (TextView) findViewById(R.id.text_send);
        img_call = (ImageView) findViewById(R.id.img_call);
        img_info = (ImageView) findViewById(R.id.img_info);
        img_deletecart = (ImageView) findViewById(R.id.img_deletecart);
        edit_search = (EditText) findViewById(R.id.edit_search);
        paymentOptionLayout = (RelativeLayout) findViewById(R.id.paymentOptionLayout);
        text_menu.setOnClickListener(this);
        text_send.setOnClickListener(this);
        img_back.setOnClickListener(this);
        img_call.setOnClickListener(this);
        img_info.setOnClickListener(this);
        img_deletecart.setOnClickListener(this);
        img_refresh.setOnClickListener(this);
        text_cartPrice.setOnClickListener(this);
        img_refreshdetail.setOnClickListener(this);
        img_favourite.setOnClickListener(this);
        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.drawer_header_layout, null, false);
        slide_me = new MenuDrawer(this, this);
        slide_me.setLeftBehindContentView(R.layout.right_menu_drawer);
        list_drawer = (ListView) slide_me.findViewById(R.id.list_drawer);

        list_drawer.addHeaderView(headerView);
        txt_user_name = (TextView) slide_me.findViewById(R.id.txt_user_name);
        adapter_drawer = new MenuAdapter(this, modulelist);
        list_drawer.setAdapter(adapter_drawer);
        list_drawer.setOnItemClickListener(this);
        img_search.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        edit_search.setOnEditorActionListener(onEditChangeListener());
        img_usericon = (ImageView) slide_me.findViewById(R.id.img_usericon);
        txt_editprofile = (TextView) slide_me.findViewById(R.id.txt_editprofile);
        txt_editprofile.setOnClickListener(this);
        txt_user_name.setOnClickListener(this);
        imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_IMAGE), img_usericon);
        if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)) {
            txt_editprofile.setVisibility(View.VISIBLE);
            if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY).equalsIgnoreCase(StaticConstants.ZAPNABIT)) {
                imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE), img_usericon);
            } else if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY).equalsIgnoreCase(StaticConstants.FB)) {
                String fbid = PrefHelper.getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.FB_ID);
                fburl = "https://graph.facebook.com/" + fbid + "/picture?type=large";

                imageLoader.DisplayImage(fburl, img_usericon);
              /* imageLoader.DisplayImage(PrefHelper.getStoredString(this,PrefHelper.PREF_FILE_NAME,
                       StaticConstants.FB_IMAGE_URL), img_usericon);*/
            } else {
                imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE), img_usericon);
            }
            txt_user_name.setText(PrefHelper.
                    getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_NAME));
            if (callFromNoti != null)
                OnUserMyOrderFragment();
            else
                OnNearBySearchFragment();
        } else if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT)) {
            txt_editprofile.setVisibility(View.GONE);
            imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_IMAGE), img_usericon);
            txt_user_name.setText(PrefHelper.
                    getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_NAME));
            if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_TYPE).equalsIgnoreCase("0")) {


                if (callFromNoti != null)
                    OnMerchantMyOrderFragment();
                else {
                    if (YELP_REGISTERED)
                        OnMerchantMyOrderFragment();
                    else
                        OnLinkFragment();
                }
            } else
                OnAboutusFragment();
        } else {
            txt_editprofile.setVisibility(View.GONE);
            img_usericon.setVisibility(View.INVISIBLE);
            txt_user_name.setText(PrefHelper.
                    getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.DRIVER_NAME));
            OnDriverMyOrderFragment();
        }

        img_usericon.setOnClickListener(this);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int errorCode = -1;
        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            StaticConstants.BITMAP = photo;
            OnDisplayPhotoAndNameFragment();

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                    R.id.baseContainer);
            if (fragmentcreate.getTag().toString()
                    .equalsIgnoreCase(StaticConstants.EDITPROFILE_TAG)) {
                EditProfileFragment frgament = (EditProfileFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.baseContainer);
                frgament.UpdateImage(data);

            }

        } else if (requestCode == PAYPAL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            //Getting the payment confirmation
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            //if confirmation is not null
            if (confirm != null) {
                try {
                    //Getting the payment details
                    String paymentDetails = confirm.toJSONObject().toString(4);
                    Log.i("paymentExample", paymentDetails);
                    OrderPlacedASync(StaticConstants.CURRENT_ORDER, "Paypal");
                    /*//Starting a new activity for the payment details and also putting the payment details with intent
                    startActivity(new Intent(this, ConfirmationActivity.class)
                            .putExtra("PaymentDetails", paymentDetails)
                            .putExtra("PaymentAmount", paymentAmount));*/

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_MASKED_WALLET) {
            if (data != null) {
                errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
            }
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null) {
                        maskedWallet =
                                data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        confirm_button.setVisibility(View.VISIBLE);
                        confirm_button.performClick();
                        Toast.makeText(this, "inside masked token", Toast.LENGTH_LONG).show();
                        //requestFullWallet();
                    }
                    break;
                case WalletConstants.RESULT_ERROR:
                    handleError(errorCode);
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    handleError(errorCode);
                    break;
            }
        } else if (requestCode == REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET) {
            if (data != null) {
                errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
            }
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null && data.hasExtra(WalletConstants.EXTRA_FULL_WALLET)) {
                        FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        // the full wallet can now be used to process the customer's payment
                        // send the wallet info up to server to process, and to get the result
                        // for sending a transaction status
                        fetchTransactionStatus(fullWallet);
                    } else if (data != null && data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {

                        Toast.makeText(this, "inside final masked token", Toast.LENGTH_LONG).show();
                        // re-launch the activity with new masked wallet information
                           /* mMaskedWallet =  data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                            mActivityLaunchIntent.putExtra(Constants.EXTRA_MASKED_WALLET,
                                    mMaskedWallet);
                            startActivity(mActivityLaunchIntent);*/
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    // nothing to do here
                    break;
                default:
                    handleError(errorCode);
                    break;
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    //  Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
                } else {

                    Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                            R.id.baseContainer);
                    if (fragmentcreate.getTag().toString()
                            .equalsIgnoreCase(StaticConstants.SCANBARCODE_TAG)) {
                        ScanBarCodeFragment frgament = (ScanBarCodeFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.baseContainer);
                        int value = frgament.scanOrpublish();
                        if (value == 1)
                            PerFormScanning(result.getContents());
                        else
                            save_qrcode_info_in_temp(result.getContents());
                    }

                }
            }
        }


    }

    protected void save_qrcode_info_in_temp(String barcode) {
        progressDialog = ProgressHUD.show(this,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_QRCODE, barcode
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(this, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                this, this,
                BaseNetwork.obj().KEY_SAVE_QRCODE_INFO_IN_TEMP, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    protected void PerFormScanning(String barcode) {
        progressDialog = ProgressHUD.show(this,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_QRCODE, barcode
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(this, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                this, this,
                BaseNetwork.obj().KEY_SCAN_QRCODE, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    private OnEditorActionListener onEditChangeListener() {
        // TODO Auto-generated method stub
        return new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    PrefHelper.storeString(BaseFragmentActivity.this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM, edit_search.getText().toString().trim());
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    NearbySearchFragment nearBy = (NearbySearchFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    nearBy.PerformZabNabitSearch(edit_search.getText().toString().trim());
                    edit_search.requestFocus();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        };
    }

    @Override
    public void onClick(View v) {
        Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                R.id.baseContainer);
        switch (v.getId()) {
            case R.id.text_menu:
                slide_me.toggleLeftDrawer();
                break;

            case R.id.confirm_button:
                requestFullWallet();
                break;
            case R.id.img_favourite:

                if (Favourite) {
                    img_favourite.setImageResource(R.drawable.heart);
                    Favourite = false;
                } else {
                    img_favourite.setImageResource(R.drawable.heart_selected);
                    Favourite = true;
                }
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.NEARBYSEARCH_TAG)) {
                    NearbySearchFragment frgament = (NearbySearchFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FilterCurrentList(Favourite);

                }
                break;
            case R.id.img_deletecart:
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.CART_TAG)) {
                    CartFragment frgament = (CartFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.DeleteCartItems();
                }
                break;
            case R.id.img_back:
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.MERCHANTORDERDETAIL_TAG)) {
                    OnMerchantMyOrderFragment();

                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.MENU_TAG)) {
                    MenuFragment frgament = (MenuFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);

                    if (frgament.catStack.size() == 1) {
                        String merchantId = frgament.getMerchantID();

                        Bundle bundle = new Bundle();
                        bundle.putString(StaticConstants.MERCHANT_ID, merchantId);
                        bundle.putString(StaticConstants.MERCHANT_NAME,
                                PrefHelper.getStoredString(this,
                                        PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_NAME));
                        bundle.putString(StaticConstants.MERCHANT_DISTANCE,
                                PrefHelper.getStoredString(this,
                                        PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_DISTANCE));

                        OnRestoInfoFragment(bundle);
                    } else
                        frgament.onBackPressed();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.WEBVIEW_TAG)) {
                    WebViewFragment frgament = (WebViewFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    OnRestoInfoFragment(frgament.getBundle());
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.CART_TAG)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticConstants.MERCHANT_ID, PrefHelper.
                            getStoredString(this, PrefHelper.PREF_FILE_NAME,
                                    StaticConstants.CUSTOMER_MERCHANT_ID));
                    OnMenuFragment(bundle);
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.RESTOINFO_TAG)) {
                    OnNearBySearchFragment();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.USERORDERDETAIL_TAG)) {
                    OnUserMyOrderFragment();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.TAKEPICTUREINFO_TAG)) {
                    OnTakePictureFragment();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.SCANPRODUCTINFO_TAG)) {
                    OnScanBArcodeFragment();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.MERCHANT_MENU_TAG)) {
                    OnMerchantMyOrderFragment();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.DRIVERORDERDETAIL_TAG)) {
                    OnDriverMyOrderFragment();
                }
                break;
            case R.id.text_send:
                break;
            case R.id.img_usericon:
                if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER))
                   /* if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY).equalsIgnoreCase(StaticConstants.ZAPNABIT))*/
                    OnEditProfileFragment();

                slide_me.toggleLeftDrawer();
                break;
            case R.id.img_info:
                new SearchInfoDialog(BaseFragmentActivity.this).show();
                break;
            case R.id.text_cartPrice:
                if (Float.valueOf(Util.getCurrentOrderPrice()) > 0)
                    OnCartFragment(new Bundle());
                else
                    Toast.makeText(this, "Order amount should be greater than 0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_call:
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.USERORDERDETAIL_TAG)) {
                    UserOrderDetailFragment orderDetail = (UserOrderDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    String phone = orderDetail.getPhoneNo();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.MERCHANTORDERDETAIL_TAG)) {
                    MerchantOrderDetailFragment orderDetail = (MerchantOrderDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    String phone = orderDetail.getPhoneNo();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } else {
                    DriverOrderDetailFragment orderDetail = (DriverOrderDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    String phone = orderDetail.getPhoneNo();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                }


                break;
            case R.id.img_search:
                edit_search.setText("");
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                edit_search.setVisibility(View.VISIBLE);
                edit_search.requestFocus();
                img_cross.setVisibility(View.VISIBLE);
                img_search.setVisibility(View.INVISIBLE);
                break;
            case R.id.img_cross:
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                img_cross.setVisibility(View.INVISIBLE);
                img_search.setVisibility(View.VISIBLE);
                NearbySearchFragment nearBy = (NearbySearchFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.baseContainer);
                nearBy.PerformZabNabit();
                final InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(v.getWindowToken(), 0);
                edit_search.setVisibility(View.GONE);
                break;
            case R.id.img_refresh:
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.MERCHANTORDERS_TAG)) {
                    MerchantOrdersFragment frgament = (MerchantOrdersFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FetchHistory();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.USERORDERS_TAG)) {
                    UserOrdersFragment frgament = (UserOrdersFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FetchHistory();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.DRIVERSORDERS_TAG)) {
                    DriverOrdersFragment frgament = (DriverOrdersFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FetchHistory();
                }


                break;
            case R.id.img_refreshdetail:
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.MERCHANTORDERDETAIL_TAG)) {
                    MerchantOrderDetailFragment frgament = (MerchantOrderDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FetchHistory();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.USERORDERDETAIL_TAG)) {
                    UserOrderDetailFragment frgament = (UserOrderDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FetchHistory();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.DRIVERORDERDETAIL_TAG)) {
                    DriverOrderDetailFragment frgament = (DriverOrderDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.FetchHistory();
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.NEARBYSEARCH_TAG)) {
                    NearbySearchFragment frgament = (NearbySearchFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    if (TextUtils.isEmpty(PrefHelper.getStoredString(BaseFragmentActivity.this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM)))
                        frgament.PerformZabNabit();
                    else
                        frgament.PerformZabNabitSearch(PrefHelper.getStoredString(BaseFragmentActivity.this,
                                PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM));
                } else if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.LINKRESTAURANT_TAG)) {
                    LinkRestaurant frgament = (LinkRestaurant) getSupportFragmentManager()
                            .findFragmentById(R.id.baseContainer);
                    frgament.PerformSearchFunction();
                }

                break;
            case R.id.txt_editprofile:
                OnEditProfileFragment();
                slide_me.toggleLeftDrawer();
                break;
            case R.id.txt_user_name:
                if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER))
                    /*if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY).equalsIgnoreCase(StaticConstants.ZAPNABIT))*/
                    OnEditProfileFragment();
                slide_me.toggleLeftDrawer();
                break;
            default:
                break;
        }
    }


    public void OnNearBySearchFragment() {
        StaticConstants.CURRENT_ORDER = new Order();
        HideCArtItem();
        if (TextUtils.isEmpty(PrefHelper.getStoredString(BaseFragmentActivity.this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM))) {
            edit_search.setVisibility(View.INVISIBLE);
            img_search.setVisibility(View.VISIBLE);
            img_cross.setVisibility(View.INVISIBLE);
        } else {
            edit_search.setVisibility(View.VISIBLE);
            img_search.setVisibility(View.INVISIBLE);
            img_cross.setVisibility(View.VISIBLE);
            edit_search.setText(PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM));
        }

        img_favourite.setVisibility(View.VISIBLE);
        img_favourite.setImageResource(R.drawable.heart);
        Favourite = false;
        text_restoname.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.VISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.NEARBY);
        baseFooter.setVisibility(View.GONE);
        text_send.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NearbySearchFragment login = new NearbySearchFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.NEARBYSEARCH_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnLinkFragment() {
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_info.setVisibility(View.VISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        HideCArtItem();
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.LINK_YOUR_STORE);
        HideSendButton(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LinkRestaurant login = new LinkRestaurant();
        ft.replace(R.id.baseContainer, login, StaticConstants.LINKRESTAURANT_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnMerchantVerifyFragment() {
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_info.setVisibility(View.VISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        HideCArtItem();
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.LINK_YOUR_STORE);
        HideSendButton(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MerchantVerifyFragment login = new MerchantVerifyFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.VERIFYMERCHANT_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnMerchantMyOrderFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MerchantOrdersFragment login = new MerchantOrdersFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.MERCHANTORDERS_TAG);
        currentBackListener = login;
        ft.commit();
    }


    public void OnDriverMyOrderFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DriverOrdersFragment login = new DriverOrdersFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.DRIVERSORDERS_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnGEnralInfoFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.MENU_GENERALINFO);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MerchantOrdersFragment login = new MerchantOrdersFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.MERCHANTORDERS_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnMerchantOrderDEtailFragment(Bundle bundle) {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.VISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.VISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.ORDER_DETAIL);
        img_cross.setVisibility(View.INVISIBLE);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MerchantOrderDetailFragment login = new MerchantOrderDetailFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.MERCHANTORDERDETAIL_TAG);
        currentBackListener = login;
        ft.commit();
    }


    public void OnDriverOrderDEtailFragment(Bundle bundle) {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.VISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.VISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.ORDER_DETAIL);
        img_cross.setVisibility(View.INVISIBLE);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DriverOrderDetailFragment login = new DriverOrderDetailFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.DRIVERORDERDETAIL_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnUserMyOrderFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserOrdersFragment login = new UserOrdersFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.USERORDERS_TAG);
        currentBackListener = login;
        ft.commit();
    }


    public void OnPrivacyFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.VISIBLE);
        text_restoname.setText("PRIVACY POLICY");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PrivacyFragment login = new PrivacyFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.PRIVACY_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnCardFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.VISIBLE);
        text_restoname.setText("Place Order");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CardFragment login = new CardFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.PRIVACY_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnEditProfileFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.VISIBLE);
        text_restoname.setText("Edit Profile");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EditProfileFragment login = new EditProfileFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.EDITPROFILE_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnAboutusFragment() {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        SetHeaderText(StaticConstants.MY_ORDERS);
        img_sdktype.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        text_restoname.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        text_restoname.setText("ABOUT US");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AboutusFragment login = new AboutusFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.ABOUTUS_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnWebViewFragment(Bundle bundle) {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_restoname.setVisibility(View.VISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.VISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        text_restoname.setText(bundle.getString(StaticConstants.RESTO_NAME));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        WebViewFragment login = new WebViewFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.WEBVIEW_TAG);
        login.setArguments(bundle);
        currentBackListener = login;
        ft.commit();
    }

    public void OnZApanbitWebViewFragment(Bundle bundle) {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_restoname.setVisibility(View.VISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        text_restoname.setText(bundle.getString(StaticConstants.RESTO_NAME));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ZapnabitWebViewFragment login = new ZapnabitWebViewFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.ZAPNABITWEBVIEW_TAG);
        login.setArguments(bundle);
        currentBackListener = login;
        ft.commit();
    }

    public void OnUserOrderDEtailFragment(Bundle bundle) {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.VISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.VISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.ORDER_DETAIL);
        img_sdktype.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        HideSendButton(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserOrderDetailFragment login = new UserOrderDetailFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.USERORDERDETAIL_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnRestoInfoFragment(Bundle bundle) {
        HideCArtItem();
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        StaticConstants.CURRENT_ORDER = new Order();
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_restoname.setVisibility(View.VISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.VISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.INVISIBLE);
        text_restoname.setText(bundle.getString(StaticConstants.MERCHANT_NAME));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RestoDetailFragment login = new RestoDetailFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.RESTOINFO_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnCartFragment(Bundle bundle) {
        PrefHelper.storeString(BaseFragmentActivity.this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                "");
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.VISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        img_call.setVisibility(View.INVISIBLE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.ORDER_LIST);
        img_back.setVisibility(View.VISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        img_info.setVisibility(View.INVISIBLE);
        text_restoname.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CartFragment login = new CartFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.CART_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnScanBArcodeFragment() {
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_restoname.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText("Scan Bar Code");
        // text_restoname.setText(bundle.getString(StaticConstants.MERCHANT_NAME));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ScanBarCodeFragment login = new ScanBarCodeFragment();
        ft.replace(R.id.baseContainer, login, StaticConstants.SCANBARCODE_TAG);
        currentBackListener = login;
        ft.commitAllowingStateLoss();
    }

    public void OnTakePictureFragment() {
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_restoname.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_back.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.VISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText("Take Picture");
        // text_restoname.setText(bundle.getString(StaticConstants.MERCHANT_NAME));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TakePicture login = new TakePicture();
        ft.replace(R.id.baseContainer, login, StaticConstants.TAKEPICTURE_TAG);
        currentBackListener = login;
        ft.commitAllowingStateLoss();
    }

    public void OnMenuFragment(Bundle bundle) {
        img_deletecart.setVisibility(View.INVISIBLE);
        PrefHelper.storeString(BaseFragmentActivity.this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                "");
        img_favourite.setVisibility(View.GONE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.MENU);
        img_back.setVisibility(View.VISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_restoname.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MenuFragment login = new MenuFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.MENU_TAG);
        currentBackListener = login;
        ft.commit();
    }


    public void OnMerchantMenuFragment(Bundle bundle) {
        img_deletecart.setVisibility(View.INVISIBLE);
        PrefHelper.storeString(BaseFragmentActivity.this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                "");
        img_favourite.setVisibility(View.GONE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText(StaticConstants.MENU);
        img_back.setVisibility(View.VISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_restoname.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MerchantMenuFragment login = new MerchantMenuFragment();
        login.setArguments(bundle);
        ft.replace(R.id.baseContainer, login, StaticConstants.MERCHANT_MENU_TAG);
        currentBackListener = login;
        ft.commit();
    }


    public void OnScanProductInfoFragment() {
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText("Go Back & Scan Again");
        img_back.setVisibility(View.VISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_restoname.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProductInfo login = new ProductInfo();
        ft.replace(R.id.baseContainer, login, StaticConstants.SCANPRODUCTINFO_TAG);
        currentBackListener = login;
        ft.commit();
    }

    public void OnDisplayPhotoAndNameFragment() {
        img_favourite.setVisibility(View.GONE);
        img_deletecart.setVisibility(View.INVISIBLE);
        img_refresh.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.INVISIBLE);
        edit_search.setVisibility(View.GONE);
        text_linkstore.setVisibility(View.VISIBLE);
        SetHeaderText("Go Back & Take Picture Again");
        img_back.setVisibility(View.VISIBLE);
        img_refreshdetail.setVisibility(View.INVISIBLE);
        text_menu.setVisibility(View.INVISIBLE);
        baseFooter.setVisibility(View.GONE);
        text_restoname.setVisibility(View.INVISIBLE);
        img_cross.setVisibility(View.INVISIBLE);
        img_info.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TakePictureInfo login = new TakePictureInfo();
        ft.replace(R.id.baseContainer, login, StaticConstants.TAKEPICTUREINFO_TAG);
        currentBackListener = login;
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

        if (currentBackListener != null
                && currentBackListener.onBackPressed() == true) {
            return;
        } else if (((FragmentActivity) BaseFragmentActivity.this).
                getSupportFragmentManager().findFragmentByTag(StaticConstants.MENU_TAG) != null) {
            Bundle bundle = new Bundle();
            bundle.putString(StaticConstants.MERCHANT_ID, PrefHelper.getStoredString(this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.CUSTOMER_MERCHANT_ID));
            bundle.putString(StaticConstants.MERCHANT_NAME,
                    PrefHelper.getStoredString(this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_NAME));
            bundle.putString(StaticConstants.MERCHANT_DISTANCE,
                    PrefHelper.getStoredString(this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_DISTANCE));

            OnRestoInfoFragment(bundle);
        } else if (((FragmentActivity) BaseFragmentActivity.this).
                getSupportFragmentManager().findFragmentByTag(StaticConstants.MERCHANT_MENU_TAG) != null) {
            OnMerchantMyOrderFragment();
        } else {
            finish();
        }
    }

    @Override
    public void OverLayClicked() {
        // TODO Auto-generated method stub

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
        if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER))
            imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE), img_usericon);
        else
            imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_IMAGE), img_usericon);
        ResultMessage message = (ResultMessage) result;
        if (message.TYPE.equalsIgnoreCase(StaticConstants.UPDATE_MERCHANT_STATUS)) {
            adapter_drawer.notifyDataSetChanged();
        } else if (message.TYPE.equalsIgnoreCase(StaticConstants.SCANBARCODE_TAG)) {
            OnScanProductInfoFragment();
        } else if (message.TYPE.equalsIgnoreCase(StaticConstants.SAVE_QRCODE_IN_TEMP)) {
            new ScanMoreDialog(this).show();
        } else if (message.TYPE.equalsIgnoreCase(StaticConstants.PLACE_ORDER)) {
            StaticConstants.CURRENT_ORDER = new Order();
            HideCArtItem();
            OnUserMyOrderFragment();
        } else
            new CongratulationDialog(this).show();

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

	/*public  void changeSdkTypeIcon(String serachType){
        img_sdktype.setVisibility(View.VISIBLE);
		if(serachType.equalsIgnoreCase(StaticConstants.GOOGLE_SEARCH))
			img_sdktype.setBackgroundDrawable(getResources().getDrawable(R.drawable.googlesearch));
		else if(serachType.equalsIgnoreCase(StaticConstants.YELP_SEARCH))
			img_sdktype.setBackgroundDrawable(getResources().getDrawable(R.drawable.yelp));
		else if(serachType.equalsIgnoreCase(StaticConstants.FOURSQUARE_SEARCH))
			img_sdktype.setBackgroundDrawable(getResources().getDrawable(R.drawable.foursquare));
		else if(serachType.equalsIgnoreCase(StaticConstants.YELLOWPAGES_SEARCH))
			img_sdktype.setBackgroundDrawable(getResources().getDrawable(R.drawable.yellowpages));
		else 
			img_sdktype.setBackgroundDrawable(getResources().getDrawable(R.drawable.zapnabit));
	}*/

    public void HideSendButton(int visibility) {
        text_send.setVisibility(visibility);
    }

    public void SetHeaderText(String text) {
        text_linkstore.setText(text);
        /*if(text.equalsIgnoreCase(StaticConstants.LINK_YOUR_RESTAURANT)){
            text_menu.setVisibility(View.INVISIBLE);
			img_back.setVisibility(View.VISIBLE);

		}
		else{
			text_menu.setVisibility(View.VISIBLE);
			img_back.setVisibility(View.INVISIBLE);
		}*/
    }

    public void LogoutUser() {
        PrefHelper.storeString(BaseFragmentActivity.this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                "");
        PrefHelper.storeString(this,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.MERCHANT_ID, "");
        PrefHelper.storeString(this,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.USER_ID, "");
        PrefHelper.storeString(this,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.DRIVER_ID, "");
        PrefHelper.storeString(this,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.JSON_MERCHANT_TYPE, "");
        DriverDb.obj().deleteAllData(this);
        StaticConstants.historyItems.clear();
        StaticConstants.driversList.clear();
        startActivity(new Intent(BaseFragmentActivity.this, LoginSocialActivity.class));
        BaseFragmentActivity.this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (position > 0) {
            MenuAdapter.ViewHolder vh = (MenuAdapter.ViewHolder) view.getTag();
            String cutmerviewinfo = vh.text_module.getText().toString();
            String module = (String) adapter_drawer.getItem(position - 1); //position-1 beacuse header view is taking first position
            if (module.equalsIgnoreCase(StaticConstants.MENU_LOGOUT)) {

                if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)) {
                    LogoutUser();
                    PrefHelper.storeString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.LOGOUTTYPE,
                            StaticConstants.DRIVER);
                } else if (USER_TYPE.equalsIgnoreCase(StaticConstants.ASSIGNED_DRIVER)) {
                    PrefHelper.storeString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.LOGOUTTYPE,
                            StaticConstants.ASSIGNED_DRIVER);
                    LogoutUser();
                } else {
                    PrefHelper.storeString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.LOGOUTTYPE,
                            StaticConstants.MERCHANT);
                    if (!PrefHelper.getStoredBoolean(this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                        LogoutUser();
                    else {
                        if (PrefHelper.getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_STATUS)
                                .equalsIgnoreCase("0"))
                            new LogoutDialog(this, this).show();
                        else
                            LogoutUser();
                    }

                }
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_MY_ORDERS)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                if (PrefHelper.getStoredString
                        (this, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_TYPE)
                        .equalsIgnoreCase(StaticConstants.MERCHANT)) {
                    if (PrefHelper.getStoredBoolean(this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                        OnMerchantMyOrderFragment();
                    else
                        Toast.makeText(this, "Please verify your business address", Toast.LENGTH_LONG).show();
                } else if (PrefHelper.getStoredString
                        (this, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_TYPE)
                        .equalsIgnoreCase(StaticConstants.DRIVER))
                    OnUserMyOrderFragment();
                else
                    OnDriverMyOrderFragment();


                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_MERCHANT_LINK)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                if (!PrefHelper.getStoredBoolean(this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED)) {
                    Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                            R.id.baseContainer);
                    if (!fragmentcreate.getTag().toString()
                            .equalsIgnoreCase(StaticConstants.LINKRESTAURANT_TAG)) {
                        OnLinkFragment();
                    }
                } else
                    Toast.makeText(BaseFragmentActivity.this, "You are already verified", Toast.LENGTH_LONG).show();
                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_SEARCH_RESTO)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                        R.id.baseContainer);
                if (!fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.NEARBYSEARCH_TAG)) {
                    OnNearBySearchFragment();
                }
                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_DELETE_BUSINESS_LINK)
                    ) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                DeleteConfirmationDialog dialog = new
                        DeleteConfirmationDialog(BaseFragmentActivity.this, BaseFragmentActivity.this);
                dialog.show();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_ACTIVATE_BUSINESS_LINK)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                ChangeMerchantStatus(1);

            } else if (module.equalsIgnoreCase(StaticConstants.MENU_PRIVACY_POLICY)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_PRIVACY_POLICY);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://zapnabit.com/privacy.html");
                OnZApanbitWebViewFragment(bundle1);


                slide_me.toggleLeftDrawer();
               /* OnPrivacyFragment();
                slide_me.toggleLeftDrawer();*/
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_ABOUT_US)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_ABOUT_US);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://zapnabit.com/about.html");
                OnZApanbitWebViewFragment(bundle1);

                slide_me.toggleLeftDrawer();
               /* OnAboutusFragment();
                slide_me.toggleLeftDrawer();*/
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_CUSTOMERVIEW)) {
                PrefHelper.storeString(BaseFragmentActivity.this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM,
                        "");

                if (cutmerviewinfo.equalsIgnoreCase("Customer View: OFF"))
                    ChangeMerchantStatus(1);
                else
                    ChangeMerchantStatus(0);
                // slide_me.toggleLeftDrawer();

            } else if (module.equalsIgnoreCase(StaticConstants.MENU_TOOLS)) {
                if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT)) {
                    if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                            PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_TYPE).equalsIgnoreCase("0")) {
                        if (modulelist.contains(StaticConstants.MENU_TAKE_PICTURE)) {
                            modulelist.clear();
                            if (!PrefHelper.getStoredBoolean(this,
                                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                                modulelist.add(StaticConstants.MENU_MERCHANT_LINK);
                            else
                                modulelist.add(StaticConstants.MENU_CUSTOMERVIEW);

                            modulelist.add(StaticConstants.MENU_MY_ORDERS);
                            modulelist.add(StaticConstants.MENU_MY_MENU);
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                            adapter_drawer.notifyDataSetChanged();
                        } else {
                            modulelist.clear();
                            if (!PrefHelper.getStoredBoolean(this,
                                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                                modulelist.add(StaticConstants.MENU_MERCHANT_LINK);
                            else
                                modulelist.add(StaticConstants.MENU_CUSTOMERVIEW);
                            modulelist.add(StaticConstants.MENU_MY_ORDERS);
                            modulelist.add(StaticConstants.MENU_MY_MENU);
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_TAKE_PICTURE);
                            modulelist.add(StaticConstants.MENU_SCAN_BARCODE);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                            adapter_drawer.notifyDataSetChanged();
                        }
                    } else {
                        if (modulelist.contains(StaticConstants.MENU_TAKE_PICTURE)) {
                            modulelist.clear();
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                            adapter_drawer.notifyDataSetChanged();
                        } else {
                            modulelist.clear();
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_TAKE_PICTURE);
                            modulelist.add(StaticConstants.MENU_SCAN_BARCODE);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                            adapter_drawer.notifyDataSetChanged();
                        }
                    }
                }

                // slide_me.toggleLeftDrawer();

            } else if (module.equalsIgnoreCase(StaticConstants.MENU_SCAN_BARCODE)) {

                OnScanBArcodeFragment();
                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_TAKE_PICTURE)) {

                OnTakePictureFragment();
                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_GENERALINFO)) {

                if (modulelist.contains(StaticConstants.MENU_ABOUT_US)) {
                    modulelist.clear();
                    if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)) {
                        modulelist.add(StaticConstants.MENU_SEARCH_RESTO);
                        modulelist.add(StaticConstants.MENU_MY_ORDERS);
                        modulelist.add(StaticConstants.MENU_GENERALINFO);
                        modulelist.add(StaticConstants.MENU_LOGOUT);
                        //modulelist.add(StaticConstants.MENU_EDIT_PROFILE);
                    } else if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT)) {
                        if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_TYPE).equalsIgnoreCase("0")) {
                            if (!PrefHelper.getStoredBoolean(this,
                                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                                modulelist.add(StaticConstants.MENU_MERCHANT_LINK);
                            else
                                modulelist.add(StaticConstants.MENU_CUSTOMERVIEW);

                            modulelist.add(StaticConstants.MENU_MY_ORDERS);
                            modulelist.add(StaticConstants.MENU_MY_MENU);
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                        } else {
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                        }

                    } else {
                        modulelist.add(StaticConstants.MENU_MY_ORDERS);
                        modulelist.add(StaticConstants.MENU_GENERALINFO);
                        modulelist.add(StaticConstants.MENU_LOGOUT);
                    }
                    adapter_drawer.notifyDataSetChanged();
                } else {
                    modulelist.clear();
                    if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)) {
                        modulelist.add(StaticConstants.MENU_SEARCH_RESTO);
                        modulelist.add(StaticConstants.MENU_MY_ORDERS);
                        modulelist.add(StaticConstants.MENU_GENERALINFO);
                        modulelist.add(StaticConstants.MENU_ABOUT_US);
                        modulelist.add(StaticConstants.MENU_PRIVACY_POLICY);
                        modulelist.add(StaticConstants.MENU_FAQ);
                        modulelist.add(StaticConstants.MENU_PROJECTWEFUNDER);
                        modulelist.add(StaticConstants.MENU_PROJECT_DRONE);
                        modulelist.add(StaticConstants.MENU_RELATIONS);
                        modulelist.add(StaticConstants.MENU_TERMS);
                        modulelist.add(StaticConstants.MENU_REFUND);
                        modulelist.add(StaticConstants.MENU_LOGOUT);

                    } else if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT)) {
                        if (PrefHelper.getStoredString(BaseFragmentActivity.this,
                                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_TYPE).equalsIgnoreCase("0")) {
                            if (!PrefHelper.getStoredBoolean(this,
                                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                                modulelist.add(StaticConstants.MENU_MERCHANT_LINK);
                            else
                                modulelist.add(StaticConstants.MENU_CUSTOMERVIEW);

                            modulelist.add(StaticConstants.MENU_MY_ORDERS);
                            modulelist.add(StaticConstants.MENU_MY_MENU);
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_ABOUT_US);
                            modulelist.add(StaticConstants.MENU_PRIVACY_POLICY);
                            modulelist.add(StaticConstants.MENU_FAQ);
                            modulelist.add(StaticConstants.MENU_PROJECTWEFUNDER);
                            modulelist.add(StaticConstants.MENU_PROJECT_DRONE);
                            modulelist.add(StaticConstants.MENU_RELATIONS);
                            modulelist.add(StaticConstants.MENU_TERMS);
                            modulelist.add(StaticConstants.MENU_REFUND);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                        } else {
                            modulelist.add(StaticConstants.MENU_TOOLS);
                            modulelist.add(StaticConstants.MENU_GENERALINFO);
                            modulelist.add(StaticConstants.MENU_ABOUT_US);
                            modulelist.add(StaticConstants.MENU_PRIVACY_POLICY);
                            modulelist.add(StaticConstants.MENU_FAQ);
                            modulelist.add(StaticConstants.MENU_PROJECTWEFUNDER);
                            modulelist.add(StaticConstants.MENU_PROJECT_DRONE);
                            modulelist.add(StaticConstants.MENU_RELATIONS);
                            modulelist.add(StaticConstants.MENU_TERMS);
                            modulelist.add(StaticConstants.MENU_REFUND);
                            modulelist.add(StaticConstants.MENU_LOGOUT);
                        }


                    } else {
                        modulelist.add(StaticConstants.MENU_MY_ORDERS);
                        modulelist.add(StaticConstants.MENU_GENERALINFO);
                        modulelist.add(StaticConstants.MENU_ABOUT_US);
                        modulelist.add(StaticConstants.MENU_PRIVACY_POLICY);
                        modulelist.add(StaticConstants.MENU_FAQ);
                        modulelist.add(StaticConstants.MENU_PROJECTWEFUNDER);
                        modulelist.add(StaticConstants.MENU_PROJECT_DRONE);
                        modulelist.add(StaticConstants.MENU_RELATIONS);
                        modulelist.add(StaticConstants.MENU_TERMS);
                        modulelist.add(StaticConstants.MENU_REFUND);
                        modulelist.add(StaticConstants.MENU_LOGOUT);
                    }
                    adapter_drawer.notifyDataSetChanged();
                }
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_FAQ)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_FAQ);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://www.zapnabit.com");
                OnZApanbitWebViewFragment(bundle1);

                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_PROJECTWEFUNDER)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_PROJECTWEFUNDER);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://www.zapnabit.com");
                OnZApanbitWebViewFragment(bundle1);


                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_PROJECT_DRONE)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_PROJECT_DRONE);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://www.zapnabit.com");
                OnZApanbitWebViewFragment(bundle1);


                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_RELATIONS)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_RELATIONS);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "https://www.f6s.com/zapnabit?follow=1");
                OnZApanbitWebViewFragment(bundle1);

                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_TERMS)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_TERMS);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://zapnabit.com/terms.html");
                OnZApanbitWebViewFragment(bundle1);

                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_REFUND)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_NAME, StaticConstants.MENU_REFUND);
                bundle1.putString(StaticConstants.USER_TYPE, USER_TYPE);
                bundle1.putString(StaticConstants.RESTO_URL, "http://zapnabit.com/refund.html");
                OnZApanbitWebViewFragment(bundle1);

                slide_me.toggleLeftDrawer();
            } else if (module.equalsIgnoreCase(StaticConstants.MENU_MY_MENU)) {
                Bundle bundle = new Bundle();
                bundle.putString(StaticConstants.MERCHANT_ID, PrefHelper.getStoredString(this,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_ID));
                OnMerchantMenuFragment(bundle);
                slide_me.toggleLeftDrawer();
            }

        }
    }


    private void ChangeMerchantStatus(int i) {
        progressDialog = ProgressHUD.show(this,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();

        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(this, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID));
        mHashMap.put(StaticConstants.JSON_MERCHANT_STATUS, "" + i);

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                this, BaseFragmentActivity.this,
                BaseNetwork.obj().KEY_ZAPNABIT_MERCHANT_STATUS, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    public void ShowCArtItem() {
        if (StaticConstants.CURRENT_ORDER.item.size() > 0)
            text_cartPrice.setVisibility(View.VISIBLE);
        text_cartPrice.setText("" + Util.getCurrentOrderPrice());
    }

    public void HideCArtItem() {
        text_cartPrice.setVisibility(View.INVISIBLE);
        img_deletecart.setVisibility(View.INVISIBLE);

    }

    public void hideSearchBar() {
        edit_search.setVisibility(View.GONE);
        img_cross.setVisibility(View.INVISIBLE);
        img_search.setVisibility(View.VISIBLE);
    }


    @Override
    public void ideleteConfirmation() {
        ChangeMerchantStatus(0);

    }

    public void UpdateUserInfo() {
        if (PrefHelper.getStoredString(this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY)
                .equalsIgnoreCase(StaticConstants.ZAPNABIT)
                || PrefHelper.getStoredString(this,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY)
                .equalsIgnoreCase(StaticConstants.GOGGLE)) {
            imageLoader.DisplayImage(PrefHelper.getStoredString(BaseFragmentActivity.this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE), img_usericon);
            txt_user_name.setText(PrefHelper.
                    getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_NAME));
        } else
            txt_user_name.setText(PrefHelper.
                    getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_NAME));

    }

    public void UpdateAdapter() {
        modulelist.clear();
        if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)) {
            modulelist.add(StaticConstants.MENU_SEARCH_RESTO);
            //modulelist.add(StaticConstants.MENU_EDIT_PROFILE);
        } else {
            if (!PrefHelper.getStoredBoolean(this,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
                modulelist.add(StaticConstants.MENU_MERCHANT_LINK);
            else
                modulelist.add(StaticConstants.MENU_CUSTOMERVIEW);

        }
        modulelist.add(StaticConstants.MENU_MY_ORDERS);
        if (!USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER))
            modulelist.add(StaticConstants.MENU_TOOLS);
        modulelist.add(StaticConstants.MENU_GENERALINFO);
       /* modulelist.add(StaticConstants.MENU_ABOUT_US);
        modulelist.add(StaticConstants.MENU_PRIVACY_POLICY);*/
        modulelist.add(StaticConstants.MENU_LOGOUT);
        adapter_drawer.notifyDataSetChanged();
    }

    public void ShowUnfavouriteButton() {
        img_favourite.setImageResource(R.drawable.heart);
        Favourite = false;
    }


    @Override
    public void callbackBarcode() {
        LogoutUser();
    }


    public void paypalPayment(String amount) {
        //Getting the amount from editText
        String paymentAmount = amount;

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD",
                PrefHelper.
                        getStoredString(this, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_NAME)/*+"\n"+
                        PrefHelper.
                                getStoredString(this, PrefHelper.PREF_FILE_NAME,
                                        StaticConstants.USER_NAME)*/,
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


  /*  public void CheckFragmentExist(Location location){
        Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                R.id.baseContainer);
        if (fragmentcreate.getTag().toString()
                .equalsIgnoreCase(StaticConstants.NEARBYSEARCH_TAG)) {
            NearbySearchFragment frgament = (NearbySearchFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.baseContainer);
            frgament.updateCurrentMarker(location);
        }
    }*/

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }


    private void OrderPlacedASync(Order order, String orderType) {


        if (order.fee.equalsIgnoreCase("-1") || order.fee.equalsIgnoreCase("-2"))
            order.fee = "0";

        HashMap<String, String> par = new HashMap<String, String>();
        par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(this, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.CUSTOMER_MERCHANT_ID));
        par.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(this, PrefHelper.PREF_FILE_NAME,
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
                Util.encodeString(order.specialInstru));
        PrefHelper.storeString(this,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.JSON_TAG_CUD_SPECIAL, order.specialInstru);

        par.put(StaticConstants.JSON_TAG_CUD_REQUESTTYPE,
                "");
        par.put(StaticConstants.JSON_TAG_CUD_DEVICE_TYPE,
                StaticConstants.ANDROID);

        par.put(StaticConstants.JSON_TAG_CUD_DEVICEID,
                PrefHelper.
                        getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
        par.put(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS,
                StaticConstants.PAYMENT_STATUS_10);
        par.put(StaticConstants.JSON_TAG_CUD_PAYMENT_TYPE,
                orderType);
        par.put(StaticConstants.JSON_TAG_CUD_PAID_STATUS,
                "1");
        par.put(StaticConstants.JSON_TAG_CUD_ORDER_TYPE,
                order.order_type);
        if (order.order_type.equalsIgnoreCase("delivery")) {
            par.put(StaticConstants.RES_JSON_USER_DELIVERY_ID,
                    PrefHelper.
                            getStoredString(this, PrefHelper.PREF_FILE_NAME, StaticConstants.RES_JSON_USER_DELIVERY_ID));
        }
        try {
            JSONArray products = (JSONArray) Util.getorderproduct(order);
            par.put(StaticConstants.JSON_TAG_CUD_CUSTOMERORDERHISTORY,
                    products.toString());
            order.orderJson = products.toString();

        } catch (JSONException je) {
            je.fillInStackTrace();
        }

        progressDialog = ProgressHUD.show(this,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                this, this,
                BaseNetwork.obj().KEY_PLACE_ORDER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);

    }


    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SupportWalletFragment mWalletFragment;
    Order order;
    private static final int REQUEST_CODE_MASKED_WALLET = 1001;
    private MaskedWallet maskedWallet;
    private Button confirm_button;
    public static final int REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET = 1004;

    public void performPaymentFunction() {
        Animation bottomUp = AnimationUtils.loadAnimation(this,R.animator.bottom_up);
        paymentOptionLayout.startAnimation(bottomUp);
        paymentOptionLayout.setVisibility(View.VISIBLE);
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                            .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                            .build())
                    .enableAutoManage(this, BaseFragmentActivity.this)
                    .build();
        }
        confirm_button = (Button) findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(this);
        showProgressDialog();
        Wallet.Payments.isReadyToPay(mGoogleApiClient).setResultCallback(
                new ResultCallback<BooleanResult>() {
                    @Override
                    public void onResult(@NonNull BooleanResult booleanResult) {
                        hideProgressDialog();

                        if (booleanResult.getStatus().isSuccess()) {
                            if (booleanResult.getValue()) {
                                Log.d("Ready to Pay-----", "isReadyToPay:true");
                                createAndAddWalletFragment();
                            }else {
                                Log.d("Ready to Pay","isReadyToPay:false");
                            }
                        }
                    }
                });
    }

    private void createAndAddWalletFragment() {
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                .setBuyButtonAppearance(WalletFragmentStyle.BuyButtonAppearance.ANDROID_PAY_DARK)
                .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_LIGHT)
                .setMode(WalletFragmentMode.BUY_BUTTON)
                .build();
        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
        MaskedWalletRequest maskedWalletRequest = WalletUtil.createMaskedWalletRequest(
                order,
                getString(R.string.public_key));
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(REQUEST_CODE_MASKED_WALLET)
                .setAccountName("Zapnabit Merchant");
        mWalletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
                .commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void requestFullWallet() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        showProgressDialog();

        FullWalletRequest fullWalletRequest = WalletUtil.createFullWalletRequest(order,
                maskedWallet.getGoogleTransactionId());
        Wallet.Payments.loadFullWallet(mGoogleApiClient, fullWalletRequest,
                REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET);
    }

    private void fetchTransactionStatus(FullWallet fullWallet) {
        hideProgressDialog();
        Toast.makeText(this, "inside final full token", Toast.LENGTH_LONG).show();
        OrderPlacedASync(StaticConstants.CURRENT_ORDER, "AndroidPay");
        /*PaymentMethodToken token = fullWallet.getPaymentMethodToken();

        if (token != null) {

           // OrderPlacedASync(order,"AndroidPay");

        }*/
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    protected void handleError(int errorCode) {
        switch (errorCode) {
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                Toast.makeText(this, "Limit Excedded",
                        Toast.LENGTH_LONG).show();
                break;
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                // unrecoverable error
                String errorMessage = "Google wallet unavailable" + "\n" +
                        "Error Code" + errorCode;
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                break;
        }
    }


    public boolean paymentshown() {
        if (paymentOptionLayout.getVisibility() == View.VISIBLE) {
            paymentOptionLayout.setVisibility(View.GONE);
            return true;
        } else
            return false;
    }

}

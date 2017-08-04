package in.pjitsol.zapnabit.NearbySearch;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTaskYelp;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.ChainRestoDialog;
import in.pjitsol.zapnabit.Ui.OutOfrangeDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.RestoOffDialog;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RestoDetailFragment extends Fragment implements OnBackPressListener
        , OnClickListener, OnCancelListener, IAsyncTaskRunner {

    private LayoutInflater inflater;
    private Activity context;
    private String MERCHANT_ID;
    private ProgressHUD progressDialog;
    private TextView text_restoname;
    private TextView text_restoaddress;
    private TextView text_restoDesc;
    private TextView text_price;
    private TextView text_call;
    private TextView text_website;
    private TextView text_viewOrder;
    private TextView text_Reorder;
    private String MERCHANT_NAME;
    private TextView text_hour;
    private TextView text_preorder;
    private TextView text_restodirection;
    private ImageView img_resto;
    private ImageLoader imaageLoder;
    private TextView text_distance;
    private RelativeLayout resto_direction;
    private RestoItem item;
    double mLatitude;
    double mLongitude;
    private RelativeLayout resto_website;
    private RelativeLayout resto_call;
    private RelativeLayout resto_downloadmenu;
    private TextView text_hourstatus;
    private float distanceInMeters;
    private String MERCHANT_DISTANCE;
    private TrackGPS gps;
    private ProgressBar progress_bar;
    private LinearLayout linear_ratingreview;
    private RatingBar rating;
    private TextView text_reviewcount;
    private WebView mWebview;
    private Buiseness buiseness;
    private TextView text_coupon;
    private RelativeLayout linear_special;
    private TextView text_discounts;
    private TextView text_special;
    private LinearLayout linearcoupon;
    private TextView text_deliverytype;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.resto_info,
                container, false);
        MERCHANT_ID = getArguments().getString(StaticConstants.MERCHANT_ID);
        MERCHANT_NAME = getArguments().getString(StaticConstants.MERCHANT_NAME);
        MERCHANT_DISTANCE = getArguments().getString(StaticConstants.MERCHANT_DISTANCE);
        PrefHelper.storeString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.CUSTOMER_MERCHANT_ID,
                MERCHANT_ID);
        PrefHelper.storeString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_NAME,
                MERCHANT_NAME);
        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                StaticConstants.MERCHANT_DISTANCE, MERCHANT_DISTANCE);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        checkPermission(context);
        // GetLocation();
        imaageLoder = new ImageLoader(context);
        text_restoname = (TextView) view.findViewById(R.id.text_restoname);
        text_deliverytype= (TextView) view.findViewById(R.id.text_deliverytype);
        text_restoaddress = (TextView) view.findViewById(R.id.text_restoaddress);
        text_restoDesc = (TextView) view.findViewById(R.id.text_restoDesc);
        text_price = (TextView) view.findViewById(R.id.text_price);
        text_call = (TextView) view.findViewById(R.id.text_call);
        text_website = (TextView) view.findViewById(R.id.text_website);
        text_viewOrder = (TextView) view.findViewById(R.id.text_viewOrder);
        text_Reorder = (TextView) view.findViewById(R.id.text_Reorder);
        text_hour = (TextView) view.findViewById(R.id.text_hour);
        text_hourstatus = (TextView) view.findViewById(R.id.text_hourstatus);
        text_distance = (TextView) view.findViewById(R.id.text_distance);
        text_restodirection = (TextView) view.findViewById(R.id.text_restodirection);
        img_resto = (ImageView) view.findViewById(R.id.img_resto);
        text_preorder = (TextView) view.findViewById(R.id.text_preorder);
        text_coupon = (TextView) view.findViewById(R.id.text_coupon);
        text_discounts = (TextView) view.findViewById(R.id.text_discounts);
        text_special = (TextView) view.findViewById(R.id.text_special);
        linearcoupon = (LinearLayout) view.findViewById(R.id.linearcoupon);
        linear_special = (RelativeLayout) view.findViewById(R.id.linear_special);
        resto_direction = (RelativeLayout) view.findViewById(R.id.resto_direction);
        resto_website = (RelativeLayout) view.findViewById(R.id.resto_website);
        resto_call = (RelativeLayout) view.findViewById(R.id.resto_call);
        resto_downloadmenu = (RelativeLayout) view.findViewById(R.id.resto_downloadmenu);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        linear_ratingreview = (LinearLayout) view.findViewById(R.id.linear_ratingreview);
        rating = (RatingBar) view.findViewById(R.id.rating);
        text_reviewcount = (TextView) view.findViewById(R.id.text_reviewcount);
        text_viewOrder.setOnClickListener(this);
        text_Reorder.setOnClickListener(this);
        resto_direction.setOnClickListener(this);
        resto_website.setOnClickListener(this);
        resto_call.setOnClickListener(this);
        resto_downloadmenu.setOnClickListener(this);
        linear_ratingreview.setOnClickListener(this);
        text_coupon.setOnClickListener(this);
        text_discounts.setOnClickListener(this);
        text_special.setMovementMethod(LinkMovementMethod.getInstance());
        img_resto.setScaleType(ImageView.ScaleType.FIT_XY);
        // linear_special.setOnClickListener(this);
        FetchrestoInfo();
    }

    private void GetLocation() {
        gps = new TrackGPS(context);
        if (gps.canGetLocation()) {
            mLongitude = gps.getLongitude();
            mLatitude = gps.getLatitude();
        } else {
            gps.showSettingsAlert();
        }
    }


    private void FetchrestoInfo() {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ID)
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, MERCHANT_ID);

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, RestoDetailFragment.this,
                BaseNetwork.obj().KEY_RESTO_GENERALSETTING, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_viewOrder:
                if (item.merchant_type.equalsIgnoreCase("0")) {
                    if (Double.valueOf(item.milesRange) > Double.valueOf(MERCHANT_DISTANCE)) {

                        if (item.profileCompleted.equalsIgnoreCase("1")) {
                            if (item.menuCompleted.equalsIgnoreCase("1")) {
                                if (Util.checkRestostatus(item.hours)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(StaticConstants.MERCHANT_ID, MERCHANT_ID);
                                    ((BaseFragmentActivity) context).OnMenuFragment(bundle);
                                } else {
                                    new RestoOffDialog(context).show();

                                }
                            } else {
                                new OutOfrangeDialog(context, "menu").show();
                            }

                        } else {
                            new OutOfrangeDialog(context, "profile").show();
                        }


                    } else
                        new OutOfrangeDialog(context, "distance").show();
                }
                else
                    new ChainRestoDialog(context).show();
                break;
            case R.id.text_Reorder:
                if (item.lastOrder != null) {
                    if (Double.valueOf(item.milesRange) > Double.valueOf(MERCHANT_DISTANCE))
                        if (Util.checkRestostatus(item.hours)) {
                            StaticConstants.CURRENT_ORDER = Util.getOrder(context,
                                    item.lastOrder);
                            ((BaseFragmentActivity) context).OnCartFragment(new Bundle());
                        } else
                            new RestoOffDialog(context).show();
                    else
                        new OutOfrangeDialog(context, "distance").show();
                } else
                    Toast.makeText(context, "Sorry, you don't have any completed order in this retaurant", Toast.LENGTH_SHORT).show();

                break;
            case R.id.resto_direction:
                String uri = "http://maps.google.com/maps?f=d&hl=en" +
                        "&saddr=" + mLatitude + "," + mLongitude + "&daddr=" + item.merchantLat + "," + item.merchantLng;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));
                break;
            case R.id.resto_website:
            /*Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(item.website));
			startActivity(i);*/
                break;
            case R.id.resto_call:
            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+item.merchantPhone));
			startActivity(callIntent);*/
                break;
            case R.id.resto_downloadmenu:
                Util.DownLoadAttachment(item, context);
                break;
            case R.id.linear_ratingreview:
                Bundle bundle = new Bundle();
                bundle.putString(StaticConstants.RESTO_URL, buiseness.url);
                bundle.putString(StaticConstants.RESTO_NAME, buiseness.name);
                bundle.putString(StaticConstants.MERCHANT_ID, MERCHANT_ID);
                bundle.putString(StaticConstants.MERCHANT_NAME, MERCHANT_NAME
                );
                bundle.putString(StaticConstants.MERCHANT_DISTANCE, MERCHANT_DISTANCE);
                ((BaseFragmentActivity) context).OnWebViewFragment(bundle);
                break;
            case R.id.text_coupon:
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.RESTO_URL, item.coupons);
                bundle1.putString(StaticConstants.RESTO_NAME, item.merchantName);
                bundle1.putString(StaticConstants.MERCHANT_ID, MERCHANT_ID);
                bundle1.putString(StaticConstants.MERCHANT_NAME, MERCHANT_NAME
                );
                bundle1.putString(StaticConstants.MERCHANT_DISTANCE, MERCHANT_DISTANCE);
                ((BaseFragmentActivity) context).OnWebViewFragment(bundle1);

                break;
            case R.id.text_discounts:
                Bundle bundle2 = new Bundle();
                bundle2.putString(StaticConstants.RESTO_URL, item.linkDiscounts);
                bundle2.putString(StaticConstants.RESTO_NAME, item.merchantName);
                bundle2.putString(StaticConstants.MERCHANT_ID, MERCHANT_ID);
                bundle2.putString(StaticConstants.MERCHANT_NAME, MERCHANT_NAME
                );
                bundle2.putString(StaticConstants.MERCHANT_DISTANCE, MERCHANT_DISTANCE);
                ((BaseFragmentActivity) context).OnWebViewFragment(bundle2);

                break;
            case R.id.linear_special:

                break;
            default:
                break;
        }

    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnNearBySearchFragment();
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void taskStarting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskCompleted(Object result) {
        ResultMessage resultmsg = (ResultMessage) result;

        if (resultmsg.TYPE.equalsIgnoreCase(StaticConstants.RESTO_GENERAL_SETTING)) {
            item = resultmsg.restoItem;
            //	calculateDistance();
            boolean status = Util.checkRestostatus(item.hours);
            text_restoname.setText(item.merchantName);
            text_restoaddress.setText(item.merchantAddress);
            text_price.setText(item.priceRange);
            text_call.setText(item.merchantPhone);
            text_special.setText(Html.fromHtml(item.specials));
            text_website.setText(Html.fromHtml(item.website));
            text_restoDesc.setText(item.description);
            text_restodirection.setText(item.directions);
            imaageLoder.DisplayImage(item.image, img_resto);

            if (TextUtils.isEmpty(item.coupons) && TextUtils.isEmpty(item.linkDiscounts))
                linearcoupon.setVisibility(View.GONE);
            else {
                linearcoupon.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(item.coupons))
                    text_coupon.setVisibility(View.GONE);
                else
                    text_coupon.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(item.linkDiscounts))
                    text_discounts.setVisibility(View.GONE);
                else
                    text_discounts.setVisibility(View.VISIBLE);

            }


            if (item.restoStatus.equalsIgnoreCase("1")) {
                text_hourstatus.setText("Closed");
                text_hourstatus.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                if (status) {
                    text_hourstatus.setText("Open");
                    text_hourstatus.setTextColor(context.getResources().getColor(R.color.darkgreen));
                } else {
                    text_hourstatus.setText("Closed");
                    text_hourstatus.setTextColor(context.getResources().getColor(R.color.red));
                }
            }

            try {
                text_hour.setText(item.hours.split("-")[0] + " - " + item.hours.split("-")[1]);
            } catch (Exception e) {
                text_hour.setText(item.hours);
            }


            if(item.sdk_name.equalsIgnoreCase("google")){
                text_preorder.setText(context.getResources().getString(R.string.paymentarrival));
            }
            else{
                if (item.preOrder.equalsIgnoreCase("1")) {
                    text_preorder.setText(context.getResources().getString(R.string.paymentarrival));
                } else {
                    text_preorder.setText(context.getResources().getString(R.string.advancepayment));
                }
            }


            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_LAT, item.merchantLat);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_LONG, item.merchantLng);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_COMISSION, item.Commision);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_TAXES, item.Tax);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_TYPE, item.deliveryType);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_VALUE, item.deliveryValue);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.JSON_MERCHANT_DELIVERY_MIN_FEE, item.minorderFee);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_SDK_NAME, item.sdk_name);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_PHONE, item.merchantPhone);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_NAME, item.merchantName);

            if(item.deliveryType.equalsIgnoreCase("0"))
                text_deliverytype.setText("Off");
            else if(item.deliveryType.equalsIgnoreCase("1"))
                text_deliverytype.setText("Yes | Free");
            else if(item.deliveryType.equalsIgnoreCase("2"))
                text_deliverytype.setText("Yes | Free w/min order of "+StaticConstants.DOLLAR+item.minorderFee);
            else if(item.deliveryType.equalsIgnoreCase("4"))
                text_deliverytype.setText("Yes | "+item.deliveryValue+" % of purchase");


            if (item.sdk_name.equalsIgnoreCase("google")) {
                linear_ratingreview.setVisibility(View.GONE);
                progress_bar.setVisibility(View.GONE);
            } else {
                progress_bar.setVisibility(View.VISIBLE);
                HashMap<String, String> mHashMap = new HashMap<String, String>();
                mHashMap.put(StaticConstants.JSON_YELP_TERM, item.sdk_id);
                AuthCommanTaskYelp<HashMap<String, String>, ResultMessage> task = new AuthCommanTaskYelp<HashMap<String, String>, ResultMessage>(
                        context, this,
                        BaseNetwork.obj().KEY_YELP_BUISENESS, progress_bar, StaticConstants.GET_METHOD);
                task.execute(mHashMap);
            }
        } else {
            buiseness = resultmsg.business;
            linear_ratingreview.setVisibility(View.VISIBLE);
            rating.setRating(Float.valueOf(buiseness.rating));
            text_reviewcount.setText(buiseness.review_count + " Reviews");
        }


    }

    private void calculateDistance() {
        Location loc1 = new Location("");
        loc1.setLatitude(mLatitude);
        loc1.setLongitude(mLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(Double.valueOf(item.lat));
        loc2.setLongitude(Double.valueOf(item.lng));
        distanceInMeters = loc1.distanceTo(loc2);
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

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

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

}

package in.pjitsol.zapnabit.NearbySearch;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTaskYelp;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Db.TakeOrderDB;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Merchant.MyInfoWindowAdapter;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.NonZapnabitDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.SearchInfoDialog;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

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
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbySearchFragment extends Fragment implements
        OnBackPressListener, IFavCallBack,
        OnCancelListener, OnItemClickListener, OnClickListener, IAsyncTaskRunner, OnMapReadyCallback {

    GoogleMap mGoogleMap;

    /*double mLatitude=37.786882;
    double mLongitude=-122.4021607;*/
    double mLatitude;
    double mLongitude;
    private LayoutInflater inflater;
    private Activity context;
    private ProgressHUD progressDialog;
    private ArrayList<YelpEntity> recentMarkerList = new ArrayList<>();
    private ListView list_searchresult;
    private UserPlaceDetailAdapter adapter;
    private RelativeLayout rel_listview;
    private SupportMapFragment fragment;
    private ImageView text_go;
    private String currentsearchType;
    private ImageView text_list;
    private ImageView img_cross;
    private View viewBlack;
    private TextView text_query;
    private RelativeLayout rel_header;
    private String SEARCHTERM = "";
    private TrackGPS gps;
    private ImageView img_radar;
    private boolean radarCall = false;
    private ImageView img_radarlink;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.near_by,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {

        checkPermission(context);
        //  GetLocation();
        fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        text_list = (ImageView) view.findViewById(R.id.text_list);
        img_radar = (ImageView) view.findViewById(R.id.img_radar);
        img_radarlink = (ImageView) view.findViewById(R.id.img_radarlink);
        list_searchresult = (ListView) view.findViewById(R.id.list_searchresult);
        text_go = (ImageView) view.findViewById(R.id.text_go);
        text_query = (TextView) view.findViewById(R.id.text_query);
        viewBlack = (View) view.findViewById(R.id.viewBlack);
        img_cross = (ImageView) view.findViewById(R.id.img_cross);
        rel_header = (RelativeLayout) view.findViewById(R.id.rel_header);
        rel_header.setVisibility(View.GONE);
        rel_listview = (RelativeLayout) view.findViewById(R.id.rel_listview);
        adapter = new UserPlaceDetailAdapter(context, recentMarkerList, this);
        list_searchresult.setAdapter(adapter);
        fragment.getMapAsync(this);
        list_searchresult.setOnItemClickListener(this);
        img_cross.setOnClickListener(this);
        text_go.setOnClickListener(this);
        text_list.setOnClickListener(this);
        img_radar.setOnClickListener(this);
        img_radarlink.setOnClickListener(this);
        viewBlack.setOnClickListener(this);
        text_query.setText(SEARCHTERM);
        if (TextUtils.isEmpty(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM))) {
            PerformZabNabit();
            text_query.setText(SEARCHTERM);
        } else {
            SEARCHTERM = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM);
            PerformZabNabitSearch(SEARCHTERM);
            text_query.setText(SEARCHTERM);
        }
        TakeOrderDB.deleteAllData(context);
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


    @Override
    public boolean onBackPressed() {
        rel_header.setVisibility(View.GONE);
        ((BaseFragmentActivity) context).hideSearchBar();
        PerformZabNabit();
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

    }

    public void PerformZabNabit() {
        rel_header.setVisibility(View.GONE);
        text_query.setText(SEARCHTERM);
        SEARCHTERM = "";
        if (TextUtils.isEmpty(PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_ACESSTOKEN))) {
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_YELP_CLIENT_ID, StaticConstants.YELP_CLIENT_ID);
            mHashMap.put(StaticConstants.JSON_YELP_CLIENT_SECRET, StaticConstants.YELP_CLIENT_SECRET);
            AuthCommanTaskYelp<HashMap<String, String>, ResultMessage> task = new AuthCommanTaskYelp<HashMap<String, String>, ResultMessage>(
                    context, this,
                    BaseNetwork.obj().KEY_YELP_TOKEN, progressDialog, StaticConstants.POST_METHOD);
            task.execute(mHashMap);
        } else {
            StaticConstants.zabnabit_resgitred_resto_list.clear();
            YelpEntity yelp = new YelpEntity();
            yelp.ItemTYpe = 2;
            StaticConstants.zabnabit_resgitred_resto_list.add(yelp);
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.
                    getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_ID)
            );
            mHashMap.put(StaticConstants.JSON_LAT, String.valueOf(gps.getLatitude()));
            mHashMap.put(StaticConstants.JSON_LNG, String.valueOf(gps.getLongitude()));

            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                    context, NearbySearchFragment.this,
                    BaseNetwork.obj().KEY_ZAPNABIT_REGISTERED_RESTO_LIST, progressDialog, StaticConstants.POST_METHOD);
            task.execute(mHashMap);
        }


    }

    public void OnRestoListClick() {
        rel_listview.setVisibility(View.VISIBLE);
        img_radar.setVisibility(View.GONE);
        img_radarlink.setVisibility(View.GONE);
        //fragment.getView().setVisibility(View.GONE);
        adapter.placeList = recentMarkerList;
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        YelpEntity item = (YelpEntity) parent.getItemAtPosition(position);

        if (item.ItemTYpe == 1) {

            Uri uri = Uri.parse("smsto:" + item.Phone);
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", context.getResources().getString(R.string.nonzapnabitinfo));
            startActivity(it);
            /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("address"+item.Phone));
			sendIntent.putExtra("sms_body", context.getResources().getString(R.string.nonzapnabitinfo));
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);*/
        } else if (item.ItemTYpe == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(StaticConstants.MERCHANT_ID, item.id);
            bundle.putString(StaticConstants.MERCHANT_NAME, item.Name);
            bundle.putString(StaticConstants.MERCHANT_DISTANCE, item.Distance);
            ((BaseFragmentActivity) context).OnRestoInfoFragment(bundle);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = (Fragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.text_list:
                OnRestoListClick();
                break;
            case R.id.img_radarlink:
                String url = "http://dynastyfootballwarehouse.com/idp-stockwatch-above-the-radar/radar-screen/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
                break;
            case R.id.img_radar:
                if (radarCall) {
                    radarCall = false;
                    img_radar.setImageResource(R.drawable.radar);
                } else {
                    radarCall = true;
                    img_radar.setImageResource(R.drawable.radar_grey);
                }

                if (TextUtils.isEmpty(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM))) {
                    PerformZabNabit();
                    text_query.setText(SEARCHTERM);
                } else {
                    SEARCHTERM = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM);
                    PerformZabNabitSearch(SEARCHTERM);
                    text_query.setText(SEARCHTERM);
                }

                ((BaseFragmentActivity)context).ShowUnfavouriteButton();
                //  PerformZabNabit();
                break;
            case R.id.img_cross:
                rel_listview.setVisibility(View.INVISIBLE);
                break;
            case R.id.viewBlack:
                rel_listview.setVisibility(View.INVISIBLE);
                img_radar.setVisibility(View.VISIBLE);
                img_radarlink.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    @Override
    public void taskStarting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskCompleted(Object result) {
        ResultMessage object = (ResultMessage) result;
        if (object.TYPE.equalsIgnoreCase(BaseNetwork.obj().KEY_YELP_TOKEN)) {
            StaticConstants.zabnabit_resgitred_resto_list.clear();
            YelpEntity yelp = new YelpEntity();
            yelp.ItemTYpe = 2;
            StaticConstants.zabnabit_resgitred_resto_list.add(yelp);
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.
                    getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_ID)
            );
            mHashMap.put(StaticConstants.JSON_LAT, String.valueOf(mLatitude));
            mHashMap.put(StaticConstants.JSON_LNG, String.valueOf(mLongitude));

            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                    context, NearbySearchFragment.this,
                    BaseNetwork.obj().KEY_ZAPNABIT_REGISTERED_RESTO_LIST, progressDialog, StaticConstants.POST_METHOD);
            task.execute(mHashMap);
        } else if (object.TYPE.equalsIgnoreCase(StaticConstants.ZABNABIT_RESGISTERED_RESTO_LIST)) {

            if (radarCall) {

                MarkerOptions markerOptions = new MarkerOptions();
                mGoogleMap.clear();
                getLatlongforMarkers();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                recentMarkerList.clear();
                if (StaticConstants.zabnabit_resgitred_resto_list.size() < 2) {
                    YelpEntity yelp = new YelpEntity();
                    yelp.ItemTYpe = 4;
                    StaticConstants.zabnabit_resgitred_resto_list.add(yelp);
                    YelpEntity yelp1 = new YelpEntity();
                    yelp1.ItemTYpe = 3;
                    for (int i = 0; i < StaticConstants.zabnabit_resgitred_resto_list.size(); i++) {
                        if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat != null) {


                            recentMarkerList.add(StaticConstants.
                                    zabnabit_resgitred_resto_list.get(i));
                            String name = StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Name;
                            String vicinity = StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).display_address;
                            String distance = StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Distance;
                            LatLng latLng = new LatLng(Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Lat), Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list.get(i).Long));
                            markerOptions.position(latLng);
                            markerOptions.title(name + " : " + vicinity + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).id + ":" + distance + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).sdk_tag + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Phone);

                            if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Color.equalsIgnoreCase(StaticConstants.GREEN)) {
                                BitmapDescriptor icon = BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                                markerOptions.icon(icon);
                            } else {
                                BitmapDescriptor icon = BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED);
                                markerOptions.icon(icon);
                            }


                            mGoogleMap.addMarker(markerOptions);
                            builder.include(markerOptions.getPosition());
                        } else {
                            recentMarkerList.add(StaticConstants.
                                    zabnabit_resgitred_resto_list.get(i));
                        }
                    }
                    adapter.placeList = recentMarkerList;
                    adapter.notifyDataSetChanged();
                    LatLng latLngcurrent = new LatLng(mLatitude, mLongitude);
                    markerOptions.position(latLngcurrent);
                    markerOptions.title("I am here");
                    BitmapDescriptor icon = BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                    markerOptions.icon(icon);
                    mGoogleMap.addMarker(markerOptions).showInfoWindow();
                    builder.include(markerOptions.getPosition());


                    if (StaticConstants.zabnabit_resgitred_resto_list != null
                            && StaticConstants.zabnabit_resgitred_resto_list.size() > 0) {
                        LatLngBounds bounds = builder.build();
                        int padding = 20; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mGoogleMap.moveCamera(cu);
                        mGoogleMap.animateCamera(cu);
                    }
                } else {
                    YelpEntity yelp = new YelpEntity();
                    yelp.ItemTYpe = 3;
                    StaticConstants.zabnabit_resgitred_resto_list.add(yelp);

                    for (int i = 0; i < StaticConstants.zabnabit_resgitred_resto_list.size(); i++) {

                        if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat != null) {


                            recentMarkerList.add(StaticConstants.
                                    zabnabit_resgitred_resto_list.get(i));
                            String name = StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Name;
                            String vicinity = StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).display_address;
                            String distance = StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Distance;
                            LatLng latLng = new LatLng(Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Lat), Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list.get(i).Long));
                            markerOptions.position(latLng);
                            markerOptions.title(name + " : " + vicinity + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).id + ":" + distance + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).sdk_tag + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                    .get(i).Phone);

                            if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Color.equalsIgnoreCase(StaticConstants.GREEN)) {
                                BitmapDescriptor icon = BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                                markerOptions.icon(icon);
                            } else {
                                BitmapDescriptor icon = BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED);
                                markerOptions.icon(icon);
                            }


                            mGoogleMap.addMarker(markerOptions);
                            builder.include(markerOptions.getPosition());
                        } else {
                            recentMarkerList.add(StaticConstants.
                                    zabnabit_resgitred_resto_list.get(i));
                        }
                    }
                    LatLng latLngcurrent = new LatLng(mLatitude, mLongitude);
                    markerOptions.position(latLngcurrent);
                    markerOptions.title("I am here");
                    // markerOptions.title("I am here" + " : " + "  " + ":" +"  " + ":" + " " + ":" +" " + ":" + " ");
                    BitmapDescriptor icon = BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                    markerOptions.icon(icon);
                    mGoogleMap.addMarker(markerOptions).showInfoWindow();
                    builder.include(markerOptions.getPosition());

                    adapter.placeList = recentMarkerList;
                    adapter.notifyDataSetChanged();
                    if (StaticConstants.zabnabit_resgitred_resto_list != null
                            && StaticConstants.zabnabit_resgitred_resto_list.size() > 0) {
                        LatLngBounds bounds = builder.build();
                        int padding = 20; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mGoogleMap.moveCamera(cu);
                        mGoogleMap.animateCamera(cu);
                    }
                }
            } else
                PErformYelSearch(SEARCHTERM);

        } else {
            MarkerOptions markerOptions = new MarkerOptions();
            mGoogleMap.clear();
            getLatlongforMarkers();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            recentMarkerList.clear();
            if (StaticConstants.zabnabit_resgitred_resto_list.size() < 2) {
                YelpEntity yelp = new YelpEntity();
                yelp.ItemTYpe = 4;
                StaticConstants.zabnabit_resgitred_resto_list.add(yelp);
                YelpEntity yelp1 = new YelpEntity();
                yelp1.ItemTYpe = 3;
                StaticConstants.zabnabit_resgitred_resto_list.add(yelp1);
                if (object.businessNames != null && object.businessNames.size() > 0)
                    StaticConstants.zabnabit_resgitred_resto_list.addAll(object.businessNames);
                for (int i = 0; i < StaticConstants.zabnabit_resgitred_resto_list.size(); i++) {
                    if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat != null) {


                        recentMarkerList.add(StaticConstants.
                                zabnabit_resgitred_resto_list.get(i));
                        String name = StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Name;
                        String vicinity = StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).display_address;
                        String distance = StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Distance;
                        LatLng latLng = new LatLng(Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Lat), Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list.get(i).Long));
                        markerOptions.position(latLng);
                        markerOptions.title(name + " : " + vicinity + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).id + ":" + distance + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).sdk_tag + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Phone);

                        if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Color.equalsIgnoreCase(StaticConstants.GREEN)) {
                            BitmapDescriptor icon = BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            markerOptions.icon(icon);
                        } else {
                            BitmapDescriptor icon = BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            markerOptions.icon(icon);
                        }


                        mGoogleMap.addMarker(markerOptions);
                        builder.include(markerOptions.getPosition());
                    } else {
                        recentMarkerList.add(StaticConstants.
                                zabnabit_resgitred_resto_list.get(i));
                    }
                }
                adapter.placeList = recentMarkerList;
                adapter.notifyDataSetChanged();
                LatLng latLngcurrent = new LatLng(mLatitude, mLongitude);
                markerOptions.position(latLngcurrent);
                markerOptions.title("I am here");
                BitmapDescriptor icon = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                markerOptions.icon(icon);

                mGoogleMap.addMarker(markerOptions);
                builder.include(markerOptions.getPosition());


                if (StaticConstants.zabnabit_resgitred_resto_list != null
                        && StaticConstants.zabnabit_resgitred_resto_list.size() > 0) {
                    LatLngBounds bounds = builder.build();
                    int padding = 20; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGoogleMap.moveCamera(cu);
                    mGoogleMap.animateCamera(cu);
                }
            } else {
                YelpEntity yelp = new YelpEntity();
                yelp.ItemTYpe = 3;
                StaticConstants.zabnabit_resgitred_resto_list.add(yelp);
                if (object.businessNames != null && object.businessNames.size() > 0)
                    StaticConstants.zabnabit_resgitred_resto_list.addAll(object.businessNames);
                for (int i = 0; i < StaticConstants.zabnabit_resgitred_resto_list.size(); i++) {

                    if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat != null) {


                        recentMarkerList.add(StaticConstants.
                                zabnabit_resgitred_resto_list.get(i));
                        String name = StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Name;
                        String vicinity = StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).display_address;
                        String distance = StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Distance;
                        LatLng latLng = new LatLng(Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Lat), Double.valueOf(StaticConstants.zabnabit_resgitred_resto_list.get(i).Long));
                        markerOptions.position(latLng);
                        markerOptions.title(name + " : " + vicinity + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).id + ":" + distance + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).sdk_tag + ":" + StaticConstants.zabnabit_resgitred_resto_list
                                .get(i).Phone);

                        if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Color.equalsIgnoreCase(StaticConstants.GREEN)) {
                            BitmapDescriptor icon = BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            markerOptions.icon(icon);
                        } else {
                            BitmapDescriptor icon = BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            markerOptions.icon(icon);
                        }


                        mGoogleMap.addMarker(markerOptions);
                        builder.include(markerOptions.getPosition());
                    } else {
                        recentMarkerList.add(StaticConstants.
                                zabnabit_resgitred_resto_list.get(i));
                    }
                }
                LatLng latLngcurrent = new LatLng(mLatitude, mLongitude);
                markerOptions.position(latLngcurrent);
                markerOptions.title("I am here");
                // markerOptions.title("I am here" + " : " + "  " + ":" +"  " + ":" + " " + ":" +" " + ":" + " ");
                BitmapDescriptor icon = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                markerOptions.icon(icon);
                mGoogleMap.addMarker(markerOptions);
                builder.include(markerOptions.getPosition());

                adapter.placeList = recentMarkerList;
                adapter.notifyDataSetChanged();
                if (StaticConstants.zabnabit_resgitred_resto_list != null
                        && StaticConstants.zabnabit_resgitred_resto_list.size() > 0) {
                    LatLngBounds bounds = builder.build();
                    int padding = 20; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGoogleMap.moveCamera(cu);
                    mGoogleMap.animateCamera(cu);
                }
            }
        }
    }

    private void PErformYelSearch(String string) {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_YELP_TERM, string);
        mHashMap.put("limit", "50");
        mHashMap.put(StaticConstants.JSON_YELP_LATITUDE, String.valueOf(mLatitude));
        mHashMap.put(StaticConstants.JSON_YELP_LONGITUDE, String.valueOf(mLongitude));
        mHashMap.put(StaticConstants.JSON_YELP_RADIUS, "6400");
        //mHashMap.put("categories", "[IE,CA,GB,PL,PL,US,NL,TR");
        AuthCommanTaskYelp<HashMap<String, String>, ResultMessage> task = new AuthCommanTaskYelp<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_YELP_SEARCH, progressDialog, StaticConstants.GET_METHOD);
        task.execute(mHashMap);

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

    public void PerformZabNabitSearch(String s) {
        rel_header.setVisibility(View.VISIBLE);
        SEARCHTERM = s;
        text_query.setText(s);
        StaticConstants.zabnabit_resgitred_resto_list.clear();
        YelpEntity yelp = new YelpEntity();
        yelp.ItemTYpe = 2;
        StaticConstants.zabnabit_resgitred_resto_list.add(yelp);
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ID)
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_NAME, s);
        mHashMap.put(StaticConstants.JSON_LAT, String.valueOf(gps.getLatitude()));
        mHashMap.put(StaticConstants.JSON_LNG, String.valueOf(gps.getLongitude()));
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, NearbySearchFragment.this,
                BaseNetwork.obj().KEY_ZAPNABIT_SEARCH_RESTO, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    public void searchRestoZapNabit(String s) {
        PerformZabNabitSearch(s);
    }

    static final float COORDINATE_OFFSET = 0.000003f;
    HashMap<String, String> markerLocation = new HashMap<>();


    private void getLatlongforMarkers() {
        for (int i = 0; i < StaticConstants.zabnabit_resgitred_resto_list.size(); i++) {

            if (StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat != null) {
                String[] locationValues = coordinateForMarker(Float.valueOf(StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat),
                        Float.valueOf(StaticConstants.zabnabit_resgitred_resto_list.get(i).Long));
                markerLocation.put(StaticConstants.zabnabit_resgitred_resto_list.get(i).id, locationValues[0]);

                StaticConstants.zabnabit_resgitred_resto_list.get(i).Lat = locationValues[0];
                StaticConstants.zabnabit_resgitred_resto_list.get(i).Long = locationValues[1];
            }
        }
    }

    private String[] coordinateForMarker(float latitude, float longitude) {

        String[] location = new String[2];

        for (int i = 0; i <= StaticConstants.zabnabit_resgitred_resto_list.size(); i++) {

            if (mapAlreadyHasMarkerForLocation((String.valueOf(latitude + i
                    * COORDINATE_OFFSET)))) {

                // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
                if (i == 0)
                    continue;


                if (mapAlreadyHasMarkerForLocation((String.valueOf(latitude - i
                        * COORDINATE_OFFSET)))) {

                    continue;

                } else {
                    location[0] = latitude - (i * COORDINATE_OFFSET) + "";
                    location[1] = longitude - (i * COORDINATE_OFFSET) + "";
                    break;
                }

            } else {
                location[0] = latitude + (i * COORDINATE_OFFSET) + "";
                location[1] = longitude + (i * COORDINATE_OFFSET) + "";
                break;
            }
        }

        return location;
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(String location) {
        return (markerLocation.containsValue(location));
    }

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;

    public void checkPermission(final Context context) {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Location");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read write");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Photos");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Call");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");

        if (permissionsList.size() > 0) {
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        GetLocation();
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
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        ) {
                    GetLocation();
                    if (TextUtils.isEmpty(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM))) {
                        PerformZabNabit();
                        text_query.setText(SEARCHTERM);
                    } else {
                        SEARCHTERM = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.SEARCHTERM);
                        PerformZabNabitSearch(SEARCHTERM);
                        text_query.setText(SEARCHTERM);
                    }

                } else {
                    checkPermission(context);
                 /*   Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(context));
        mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                if (marker.getTitle() != null && !(marker.getTitle().equalsIgnoreCase("I am here"))) {
                    if (marker.getTitle().split(":")[4].trim().equalsIgnoreCase(StaticConstants.YELP_SEARCH)) {

                        Uri uri = Uri.parse("smsto:" + marker.getTitle().split(":")[5].trim());
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", context.getResources().getString(R.string.nonzapnabitinfo));
                        startActivity(it);
                    } else if (marker.getTitle().split(":")[4].trim().equalsIgnoreCase(StaticConstants.ZAPNABIT_SEARCH)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(StaticConstants.MERCHANT_ID, marker.getTitle().split(":")[2].trim());
                        bundle.putString(StaticConstants.MERCHANT_NAME, marker.getTitle().split(":")[0].trim());
                        bundle.putString(StaticConstants.MERCHANT_DISTANCE, marker.getTitle().split(":")[3].trim());
                        ((BaseFragmentActivity) context).OnRestoInfoFragment(bundle);
                    }
                }


            }
        });
    }

    @Override
    public void FavCallback(YelpEntity item) {

        if (item.userFav) {

            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.
                    getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_ID)
            );
            mHashMap.put(StaticConstants.JSON_MERCHANT_ID, item.Merchant_Id);

            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                    context, NearbySearchFragment.this,
                    BaseNetwork.obj().KEY_USER_UNFAVOURITE, progressDialog, StaticConstants.POST_METHOD);
            task.execute(mHashMap);

        } else {
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.
                    getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_ID)
            );
            mHashMap.put(StaticConstants.JSON_MERCHANT_ID, item.Merchant_Id);

            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                    context, NearbySearchFragment.this,
                    BaseNetwork.obj().KEY_USER_FAVOURITE, progressDialog, StaticConstants.POST_METHOD);
            task.execute(mHashMap);
        }


    }

    public void FilterCurrentList(boolean Favourite) {

        MarkerOptions markerOptions = new MarkerOptions();
        mGoogleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (Favourite) {
            for (int i = recentMarkerList.size() - 1; i > 0; i--) {
                if (recentMarkerList.get(i).ItemTYpe == 0) {
                    if (!recentMarkerList.get(i).userFav) {
                        recentMarkerList.remove(recentMarkerList.get(i));

                    } else {
                        String name = recentMarkerList
                                .get(i).Name;
                        String vicinity = recentMarkerList
                                .get(i).display_address;
                        String distance = recentMarkerList
                                .get(i).Distance;
                        LatLng latLng = new LatLng(Double.valueOf(recentMarkerList
                                .get(i).Lat), Double.valueOf(recentMarkerList.get(i).Long));
                        markerOptions.position(latLng);
                        markerOptions.title(name + " : " + vicinity + ":" + recentMarkerList
                                .get(i).id + ":" + distance + ":" + recentMarkerList
                                .get(i).sdk_tag + ":" + recentMarkerList
                                .get(i).Phone);

                        if (recentMarkerList.get(i).Color.equalsIgnoreCase(StaticConstants.GREEN)) {
                            BitmapDescriptor icon = BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            markerOptions.icon(icon);
                        } else {
                            BitmapDescriptor icon = BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            markerOptions.icon(icon);
                        }
                        mGoogleMap.addMarker(markerOptions);
                        builder.include(markerOptions.getPosition());
                        LatLngBounds bounds = builder.build();
                        int padding = 300; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mGoogleMap.moveCamera(cu);
                        //mGoogleMap.animateCamera(cu);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            PerformZabNabit();
        }

    }


    /*Marker now;
    public void updateCurrentMarker(Location location){
        if(now != null){
            now.remove();

        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        now = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        BitmapDescriptor icon = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        now.setIcon(icon);
        // Showing the current location in Google Map
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }*/
}

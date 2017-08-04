package in.pjitsol.zapnabit.Util;

import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Db.TakeOrderDB;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.Product.ExtraNote;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Entity.SaleAbleItem;
import in.pjitsol.zapnabit.NearbySearch.RestoItem;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.User.HistoryItem;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Util {


    public static Animation BottomToTopAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    public static Animation TopToBottomAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getMilesFromMeter(float meters) {

        double inches = (39.370078 * meters);
        int miles = (int) (inches / 63360);
        return miles;
    }


    public static ArrayList<YelpEntity> getResto(ArrayList<YelpEntity> list, double lat, double lng) {

        ArrayList<YelpEntity> finalList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            float[] distance = new float[2];
            Location.distanceBetween(Double.valueOf(list.get(i).Lat), Double.valueOf(list.get(i).Long),
                    lat, lng, distance);
            if (distance[0] > 5000) {
                // Toast.makeText(getBaseContext(), "Outside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
            } else {
                list.get(i).Distance = numberfornmat(distance[0]);
                //list.get(i).Distance=String.valueOf(getMilesFromMeter(distance[0]));
                finalList.add(list.get(i));
                //Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + mCircle.getRadius() , Toast.LENGTH_LONG).show();
            }
        }

        return finalList;
    }

    public static String numberfornmat(float value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("00.00");
        return df.format(value);
    }

    public static String CheckJsonIsEmpty(JSONObject jsonObject,
                                          String jsonTagname) throws JSONException {
        if (jsonObject.has(jsonTagname))
            return TextUtils.isEmpty(jsonObject.getString(jsonTagname)) ? ""
                    : jsonObject.getString(jsonTagname);
        else
            return "";

    }


    public static JSONObject getSdkInfo(Marker marker) {
        JSONObject jsonObject = new JSONObject();
        try {


            JSONArray sdkinfo = new JSONArray();

            JSONObject jsonSdkinfo = new JSONObject();

            jsonSdkinfo.put(StaticConstants.JSON_SDK_ID, marker.getTitle().
                    split(":")[2]);
            jsonSdkinfo.put(StaticConstants.JSON_SDK_NAME, marker.getTitle().
                    split(":")[1]);
            jsonSdkinfo.put(StaticConstants.JSON_LAT, marker.getPosition().latitude);
            jsonSdkinfo.put(StaticConstants.JSON_LNG, marker.getPosition().longitude);

            sdkinfo.put(jsonSdkinfo);


            jsonObject.put(StaticConstants.JSON_SDK_INFO, sdkinfo);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getSdkInfoItem(YelpEntity marker) {
        JSONObject jsonObject = new JSONObject();
        try {


            JSONArray sdkinfo = new JSONArray();

            JSONObject jsonSdkinfo = new JSONObject();

            jsonSdkinfo.put(StaticConstants.JSON_SDK_ID, marker.id);
            jsonSdkinfo.put(StaticConstants.JSON_SDK_NAME, marker.sdk_tag);
            jsonSdkinfo.put(StaticConstants.JSON_LAT, marker.Lat);
            jsonSdkinfo.put(StaticConstants.JSON_LNG, marker.Long);

            sdkinfo.put(jsonSdkinfo);


            jsonObject.put(StaticConstants.JSON_SDK_INFO, sdkinfo);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getSdkInfoItem(Place marker) {
        JSONObject jsonObject = new JSONObject();
        try {


            JSONArray sdkinfo = new JSONArray();

            JSONObject jsonSdkinfo = new JSONObject();

            jsonSdkinfo.put(StaticConstants.JSON_SDK_ID, marker.getId());
            jsonSdkinfo.put(StaticConstants.JSON_SDK_NAME, "google");
            jsonSdkinfo.put(StaticConstants.JSON_LAT, marker.getLatLng().latitude);
            jsonSdkinfo.put(StaticConstants.JSON_LNG, marker.getLatLng().longitude);

            sdkinfo.put(jsonSdkinfo);


            jsonObject.put(StaticConstants.JSON_SDK_INFO, sdkinfo);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static ArrayList<YelpEntity> getMarkerColour(ArrayList<YelpEntity> recentMarkerList) {

        for (int i = 0; i < StaticConstants.resgitred_resto_list.size(); i++) {
            for (int k = 0; k < recentMarkerList.size(); k++) {
                if (recentMarkerList.get(k).id.equalsIgnoreCase(
                        StaticConstants.resgitred_resto_list.get(i).id)) {
                    recentMarkerList.get(k).Color = StaticConstants.GREEN;
                    break;

                } else
                    recentMarkerList.get(k).Color = StaticConstants.RED;
            }
        }

        return recentMarkerList;
    }


    public static String getcityName(double latitude, double longitude, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        String stateName = "";
        String cityName = "";
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getLocality();
            stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
        } catch (IOException e) {
            cityName = "";
            e.printStackTrace();
        }

        return stateName;
    }

    public static String getAddress(double latitude, double longitude, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        String stateName = "";
        String cityName = "";
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getLocality();
            stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
        } catch (IOException e) {
            cityName = "";
            e.printStackTrace();
        }

        return stateName+" ,"+cityName;
    }

    public static String GetNameValueChinese(String namejson) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject = new JSONObject(namejson);
            return jsonObject.getString("zh");

        } catch (Exception exception) {
            return namejson;
        }
    }

    public static String GetNameValueEnglish(String namejson) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject = new JSONObject(namejson);
            return jsonObject.getString("en");

        } catch (Exception exception) {
            return namejson;
        }
    }


    public static JSONObject nameCreate(String name) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("zh", name);
        jsonObject.put("en", name);
        return jsonObject;

    }

    static public String ConvertLangCodeToLanguage(String langcode) {
        if (langcode.equalsIgnoreCase("en"))
            return "English";
        else if (langcode.equalsIgnoreCase("zh"))
            return "Chinese";
        return "English";
    }

    static public String ConvertLangLanguageToCode(String langcode) {
        if (langcode.equalsIgnoreCase("English"))
            return "en";
        else if (langcode.equalsIgnoreCase("Chinese"))
            return "zh";
        return "en";
    }

    public static float calculateItemTotalPrice(int qty, float netamount) {
        return qty * netamount;
    }

    public static String numberfornmat(Double value) {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.00");
        return df.format(value);
    }

    public static String CurrentDateTimeFormat_24() {
        return DateFormat.format(StaticConstants.REGISTER_DATEFORMATE_24,
                new Date()).toString();
    }

    public static JSONObject getProductJSON(Product p) throws JSONException {
        JSONObject json = new JSONObject();
        byte[] b = p.name.trim().getBytes(Charset.forName("UTF-8"));
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNAME, Base64.encodeToString(b, Base64.DEFAULT).trim());
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID, p.categoryId);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNETPRICE, p.netamount);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTID, p.id);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTQTY, p.quantity);

        return json;
    }

    public static JSONObject getOptionJSON(ProductAtrributeOption p) throws JSONException {

        JSONObject json = new JSONObject();
        byte[] b = p.name.trim().getBytes(Charset.forName("UTF-8"));
        byte[] bAttriname = p.productAttribute.name.trim().getBytes(Charset.forName("UTF-8"));
        json.put(StaticConstants.JSON_TAG_OR_OPTION_ATTRIID, p.productAttribute.id);
        json.put(StaticConstants.JSON_TAG_OR_OPTION_ATTRINAME, Base64.encodeToString(bAttriname, Base64.DEFAULT).trim());
        json.put(StaticConstants.JSON_TAG_OR_OPTION_ATTRI_PARENTID, p.productAttribute.categoryId);
        json.put(StaticConstants.JSON_TAG_OR_OPTIONID, p.id);
        json.put(StaticConstants.JSON_TAG_OR_OPTIONNAME, Base64.encodeToString(b, Base64.DEFAULT).trim());
        json.put(StaticConstants.JSON_TAG_OR_OPTIONNETPRICE, p.netamount);

        return json;
    }

    public static JSONObject getExtraNoteJSON(ExtraNote p, boolean flag)
            throws JSONException {
        JSONObject json = new JSONObject();
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNAME, p.name);
        if (p.product != null)
            json.put(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID, p.product.id);
        else
            json.put(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID, p.id);

        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNETPRICE, p.netamount);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTID, p.id);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTQTY, "1");
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNAME, p.name);
        json.put(StaticConstants.JSON_TAG_OR_GETPRODUCT_SUBCAT_THIRD, p.name);


        return json;
    }

    public static JSONObject getProductJSON(ProductAddOn p)
            throws JSONException {
        JSONObject json = new JSONObject();
        byte[] b = p.name.trim().getBytes(Charset.forName("UTF-8"));
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNAME, Base64.encodeToString(b, Base64.DEFAULT).trim());
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID, p.product.id);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTNETPRICE, p.netamount);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTID, p.id);
        json.put(StaticConstants.JSON_TAG_OR_PRODUCTQTY, p.quantity);
        json.put(StaticConstants.JSON_TAG_OR_GETPRODUCT_SUBCAT_THIRD, Base64.encodeToString(b, Base64.DEFAULT).trim());
        return json;
    }

    public static String encodeString(String string){
        byte[] b = string.trim().getBytes(Charset.forName("UTF-8"));
        return Base64.encodeToString(b, Base64.DEFAULT).trim();
    }

    public static String decodeString(String string){
        byte[] byteName = Base64.decode(string,
                Base64.DEFAULT);
        return new String(byteName);
    }

    public static Object getorderproduct(Order order) throws JSONException {

        JSONArray products = new JSONArray();
        for (int i = 0; i < order.item.size(); i++) {
            SaleAbleItem item = (SaleAbleItem) order.item.get(i);

            if (item instanceof Product) {
                Product p = (Product) item;
                if (p.productType.equalsIgnoreCase("product")) {
                    JSONObject prodJson = getProductJSON(p);
                    prodJson.put(StaticConstants.JSON_TAG_OR_PRODUCTTYPE,
                            StaticConstants.JSON_TAG_OR_ROUTES);
                    JSONArray addOnArray = new JSONArray();

                    if (p.selectedAddon.size() > 0) {
                        for (ProductAddOn ad : p.selectedAddon) {
                            addOnArray.put(getProductJSON(ad));
                        }
                    }
                    if (p.extraNote.size() > 0) {
                        Logger.d("selectedAddon", ":3:" + p.extraNote.size());
                        for (ExtraNote ad : p.extraNote) {
                            addOnArray.put(getExtraNoteJSON(ad, false));
                        }
                        Logger.d("selectedAddon", ":4:" + addOnArray.length());
                    }
                    if (addOnArray.length() > 0) {
                        prodJson.put(StaticConstants.JSON_TAG_OR_THIRDCATEGORY,
                                addOnArray);
                    } else {
                        prodJson.put(StaticConstants.JSON_TAG_OR_THIRDCATEGORY,
                                new JSONArray());
                    }
                    products.put(prodJson);


                } else {
                    JSONObject prodJson = getProductJSON(p);
                    prodJson.put(StaticConstants.JSON_TAG_OR_PRODUCTTYPE,
                            p.productType);
                    JSONArray optionArray = new JSONArray();
                    for (ProductAtrributeOption ad : p.selectedAttributeOptions) {
                        optionArray.put(getOptionJSON(ad));
                    }
                    if (optionArray.length() > 0) {
                        prodJson.put(StaticConstants.JSON_TAG_OR_OPTIONARRAY,
                                optionArray);
                    } else {
                        prodJson.put(StaticConstants.JSON_TAG_OR_OPTIONARRAY,
                                optionArray);
                    }

                    products.put(prodJson);

                    // JSONObject optionJson = getOptionJSON(p);
                }
            }
            if (item instanceof ExtraNote) {
                ExtraNote note = (ExtraNote) item;
                JSONObject extrajson = getExtraNoteJSON(note, true);
                extrajson.put(StaticConstants.JSON_TAG_OR_PRODUCTTYPE,
                        StaticConstants.JSON_TAG_OR_EXTRANOTES);
                extrajson.put(StaticConstants.JSON_TAG_OR_ISPRINTRT,
                        note.isPrint);
                products.put(extrajson);
            }

        }

        return products;
    }

    public static String getDisplayOrderId(String orderId) {
        return orderId.substring(orderId.length() - 8);
    }

    public static Order getOrder(Context context, HistoryItem historyItem) {
        try {
            Order order = new Order();
            order.orderId = historyItem.orderId;
            order.TotalPrice = Float.valueOf(historyItem.Total);
            order.netprice = Float.valueOf(historyItem.SubTotal);
            order.restuarant_id = historyItem.merchantId;
            order.date = historyItem.OrderDate;
            order.comission = historyItem.Commision;
            order.taxes = historyItem.Taxes;
            order.fee = historyItem.deliveryFees;
            order.order_type = historyItem.orderType;

            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_COMISSION, historyItem.Commision);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_TAXES, historyItem.Taxes);
            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_VALUE, historyItem.deliveryFees);


            JSONArray jsonArray = new JSONArray(
                    historyItem.CustomerOrderHistory);
            order.item.clear();
            int enabledItemsFrom = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                if (jObject.has(StaticConstants.JSON_TAG_OR_PRODUCTTYPE)) {

                    if (jObject.getString(
                            StaticConstants.JSON_TAG_OR_PRODUCTTYPE)
                            .equalsIgnoreCase(
                                    StaticConstants.JSON_TAG_OR_ROUTES)) {
                        Product p = getProduct(jObject, context);
                        p.isprint = false;
                        enabledItemsFrom++;
                        fillProductAddOn(
                                p,
                                jObject.getJSONArray(StaticConstants.JSON_TAG_OR_THIRDCATEGORY));
                        enabledItemsFrom += p.selectedAddon.size();
                        order.item.add(p);
                    }
                    else{

                        Product p = getProduct(jObject, context);
                        p.isprint = false;
                        enabledItemsFrom++;
                        fillProductAttriOption(
                                p,
                                jObject.getJSONArray(StaticConstants.JSON_TAG_OR_OPTIONARRAY));
                        enabledItemsFrom += p.selectedAddon.size();
                        order.item.add(p);

                    }
                    if (jObject.getString(
                            StaticConstants.JSON_TAG_OR_PRODUCTTYPE)
                            .equalsIgnoreCase(
                                    StaticConstants.JSON_TAG_OR_EXTRANOTES)) {
                        ExtraNote p = getExtraNote(jObject);
                        p.isPrint = false;
                        enabledItemsFrom++;
                        order.item.add(p);

                    }
                }
            }
            order.itemsEnabledFrom = enabledItemsFrom;

            return order;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private static Product getProduct(JSONObject jsonObject, Context context
    ) throws NumberFormatException, JSONException {
        Product p = new Product();
        p.id = Integer.parseInt(jsonObject
                .getString(StaticConstants.JSON_TAG_OR_PRODUCTID));
        if (jsonObject.has(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID))
            p.categoryId = Integer.parseInt(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID));
        else
            p.categoryId = Integer.parseInt(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_PRODUCTID));
        byte[] byteName = Base64.decode(jsonObject
                        .getString(StaticConstants.JSON_TAG_OR_PRODUCTNAME),
                Base64.DEFAULT);
        String s = new String(byteName);
        p.name = s;

        p.netamount = Float.parseFloat(jsonObject
                .getString(StaticConstants.JSON_TAG_OR_PRODUCTNETPRICE));

        p.quantity = jsonObject.getInt(StaticConstants.JSON_TAG_OR_PRODUCTQTY);
        p.productType = jsonObject.getString(StaticConstants.JSON_TAG_OR_PRODUCTTYPE);

        p.addOns = TakeOrderDB.getAddOn(context, p);

        return p;
    }

    private static void fillProductAddOn(Product pr, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            ProductAddOn p = new ProductAddOn();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            p.id = Integer.parseInt(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_PRODUCTID));
            p.product = pr;
            byte[] byteName = Base64.decode(jsonObject
                            .getString(StaticConstants.JSON_TAG_OR_PRODUCTNAME),
                    Base64.DEFAULT);
            String s = new String(byteName);
            p.name = s;

            p.netamount = Float.parseFloat(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_PRODUCTNETPRICE));

            p.quantity = jsonObject
                    .getInt(StaticConstants.JSON_TAG_OR_PRODUCTQTY);
            pr.selectedAddon.add(p);
        }
    }


    private static void fillProductAttriOption(Product pr, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            ProductAtrributeOption p = new ProductAtrributeOption();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            p.id = Integer.parseInt(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_OPTIONID));
            byte[] byteName = Base64.decode(jsonObject
                            .getString(StaticConstants.JSON_TAG_OR_OPTIONNAME),
                    Base64.DEFAULT);
            String s = new String(byteName);
            p.name = s;

            p.netamount = Float.parseFloat(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_OPTIONNETPRICE));

            p.quantity = pr.quantity;
            byte[] byteName1 = Base64.decode(jsonObject
                            .getString(StaticConstants.JSON_TAG_OR_OPTION_ATTRINAME),
                    Base64.DEFAULT);
            String s1 = new String(byteName1);

            p.productAttributeName=s1;
            pr.selectedAttributeOptions.add(p);
        }
    }


    private static ExtraNote getExtraNote(JSONObject jsonObject) throws NumberFormatException, JSONException {
        ExtraNote p = new ExtraNote();
        p.id = Integer.parseInt(jsonObject
                .getString(StaticConstants.JSON_TAG_OR_PRODUCTID));
        if (jsonObject.has(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID))
            p.categoryId = Integer.parseInt(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_PRODUCTPARENTID));
        else
            p.categoryId = Integer.parseInt(jsonObject
                    .getString(StaticConstants.JSON_TAG_OR_PRODUCTID));

        p.name =
                jsonObject.getString(StaticConstants.JSON_TAG_OR_PRODUCTNAME);

        p.netamount = Float.parseFloat(jsonObject
                .getString(StaticConstants.JSON_TAG_OR_PRODUCTNETPRICE));

        p.quantity = jsonObject.getInt(StaticConstants.JSON_TAG_OR_PRODUCTQTY);

        // p.addOns = TakeOrderDB.getAddOn(getActivity(), p);
        return p;
    }
    /* circle = mGoogleMap.addCircle(new CircleOptions()
    .center(new LatLng(Double.valueOf(result
			.get(i).Lat), Double.valueOf(result.get(i).Long)))
    .radius(50)
    .strokeWidth(10)
    .strokeColor(Color.GREEN)
    .fillColor(Color.argb(128, 255, 0, 0)));*/


    public static String getCurrentOrderPrice() {

        float price = 0;

        for (int i = 0; i < StaticConstants.CURRENT_ORDER.item.size(); i++) {

            if (((Product) StaticConstants.CURRENT_ORDER.item.get(i)).productType.equalsIgnoreCase("product")) {
                price += Util.calculateItemTotalPrice(((Product) StaticConstants.CURRENT_ORDER.item.get(i)).quantity,
                        StaticConstants.CURRENT_ORDER.item.get(i).netamount);

                if (StaticConstants.CURRENT_ORDER.item.get(i).productAddOns != null
                        && StaticConstants.CURRENT_ORDER.item.get(i).productAddOns.size() > 0) {
                    for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.get(i).productAddOns.size(); k++) {
                        price += Util.calculateItemTotalPrice(
                                StaticConstants.CURRENT_ORDER.item.get(i).productAddOns.get(k).quantity,
                                StaticConstants.CURRENT_ORDER.item.get(i).productAddOns.get(k).netamount);
                    }
                }
            } else {
                if (StaticConstants.CURRENT_ORDER.item.get(i).productAttributes != null
                        && StaticConstants.CURRENT_ORDER.item.get(i).productAttributes.size() > 0) {

                    for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.get(i).productAttributes.size(); k++) {

                        if (StaticConstants.CURRENT_ORDER.item.get(i).productAttributes.get(k).productAttributesOptions != null
                                && StaticConstants.CURRENT_ORDER.item.get(i).productAttributes.get(k).productAttributesOptions.size() > 0) {
                            ProductAtrribute attribute = StaticConstants.CURRENT_ORDER.item.get(i).productAttributes.get(k);
                            for (int j = 0; j < attribute.productAttributesOptions.size(); j++) {

                                if (((ProductAtrributeOption) attribute.productAttributesOptions.get(j)).optionSelected)
                                    price += Util.calculateItemTotalPrice(
                                            ((Product) StaticConstants.CURRENT_ORDER.item.get(i)).quantity,
                                            attribute.productAttributesOptions.get(j).netamount);
                            }
                        }

                    }

                }


            }


        }


        return Util.numberfornmat(price);
    }

    public static void setStatusDarkGreenColour(View v, Context context) {
        GradientDrawable bgShape = (GradientDrawable) v
                .getBackground();
        bgShape.setColor(context.getResources().getColor(R.color.darkgreen));
    }

    public static void setStatusLightGreenColour(View v, Context context) {
        GradientDrawable bgShape = (GradientDrawable) v
                .getBackground();
        bgShape.setColor(context.getResources().getColor(R.color.lightgreen));
    }

    public static String changeDAteFormat(String dateSrting) {
        String finaldate = "";
        String finaleTime = "";
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateSrting);
            String dateFormat = "dd-MMM-yyyy";
            SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, new Locale("en_US"));
            String tDate = sdf1.format(date1);
            String[] splitDate = tDate.split("-");
            finaldate = splitDate[1] + "'" + splitDate[0] + " " + splitDate[2];
            String time = dateSrting.split(" ")[1];
            finaleTime = time.split(":")[0] + ":" + time.split(":")[1];
        } catch (Exception e) {
            return dateSrting;
        }
        return finaldate + " " + finaleTime;
    }

    @SuppressWarnings("unchecked")
    public static void DownLoadAttachment(RestoItem item, Context context) {

        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_FILE_NAME, item.merchantName);
        mHashMap.put(StaticConstants.JSON_FILE_URL, item.pdf);
        parseImage task = new parseImage(context);
        task.execute(mHashMap);
    }

    public static String calculateFee(float netprice, Activity context) {
        String deliveryType = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                StaticConstants.MERCHANT_DELIVERY_TYPE);
        if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_FREE)) {
            return "-1";
        } else if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_FREEMIN)) {
            String minOrderValue = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_VALUE);
            if (netprice >= Float.valueOf(minOrderValue))
                return Util.numberfornmat(0);
            else {
                return "-2";
            }
        } else if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_FIXED)) {
            Float feeAmount = Float.valueOf(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_VALUE));
            return Util.numberfornmat(feeAmount);
        } else if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_PERCENT)) {
            Float feevalue = Float.valueOf(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_VALUE));
            float feeamount = (netprice * feevalue / 100);
            return Util.numberfornmat(feeamount);
        } else {
            return "-2";
        }
    }

    public static class parseImage extends AsyncTask<HashMap<String, String>, Object, String>
            implements OnCancelListener {
        Context context;
        ProgressHUD progressDialog;

        public parseImage(Context context) {
            this.context = context;
            progressDialog = ProgressHUD.show(context, "Loading...", true, true, this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            HashMap<String, String> paramsHashmap = new HashMap<>();
            File file = null;
            try {

                paramsHashmap = params[0];
                //URL url=new URL("http://apiv3.eschools.co/v3/storage?file_uid=XRqx9ixwM1");
                URL url = new URL(paramsHashmap.get(StaticConstants.JSON_FILE_URL));
                File imageFileFolder = new File(Environment.getExternalStorageDirectory(), "Zapnabit_App");
                if (!imageFileFolder.exists()) {
                    if (imageFileFolder.mkdir()) {
                        // directory is created;
                    }
                }
                file = new File(imageFileFolder, paramsHashmap.get(StaticConstants.JSON_FILE_NAME));
                long startTime = System.currentTimeMillis();
                HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

                //URLConnection ucon = url.openConnection();

                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
				 */
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.close();
                scanPhoto(file.toString(), context);


            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return file.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();

            File file = new File(result);
            MimeTypeMap map = MimeTypeMap.getSingleton();
            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            String type = map.getMimeTypeFromExtension(ext);
            if (type == null)
                type = "application/pdf";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);
            intent.setDataAndType(data, type);

            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);

            } else {
  /* No PDF reader found on device. Provide user the option to
   * install the Adobe Reader found on Google Play.
   */
                Log.e("Directory", "No PDF viewers!");

                Toast.makeText(context, "No pdf reader", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

    }

    static MediaScannerConnection msConn;

    public static void scanPhoto(final String imageFileName, Context context) {
        msConn = new MediaScannerConnection(context, new MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msConn.scanFile(imageFileName, null);
                Log.i("msClient obj  in Photo Utility", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                msConn.disconnect();
                Log.i("msClient obj in Photo Utility", "scan completed");
            }
        });
        msConn.connect();
    }


    public static String calculateTaxes(float netprice, Context context) {
        Float taxes = Float.valueOf(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                StaticConstants.MERCHANT_TAXES));
        float taxamount = (netprice * taxes / 100);
        return Util.numberfornmat(taxamount);
    }

    public static String calculateComission(float netprice, Context context) {
        Float taxes = Float.valueOf(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                StaticConstants.MERCHANT_COMISSION));
        float taxamount = (netprice * taxes / 100);
        return Util.numberfornmat(taxamount);
    }


    public static String CalculateTotalAmount(Order order) {
        if(order.order_type.equalsIgnoreCase("delivery")){
            if (Float.valueOf(order.fee) != -2 || Float.valueOf(order.fee) != -2)
                return Util.numberfornmat(Float.valueOf(order.netprice) +
                        Float.valueOf(order.comission) + Float.valueOf(order.taxes) + Float.valueOf(order.fee));
            else
                return Util.numberfornmat(Float.valueOf(order.netprice) +
                        Float.valueOf(order.comission) + Float.valueOf(order.taxes));
        }
        else
            return Util.numberfornmat(Float.valueOf(order.netprice) +
                    Float.valueOf(order.comission) + Float.valueOf(order.taxes));

    }


    public static boolean checkRestostatus(String hours) {
        int openTime;
        int closeTime;
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int curentHour = calendar.get(Calendar.HOUR_OF_DAY);// gets hour in 24h format
        String ampm = hours.split("-")[0].substring(hours.split("-")[0].length() - 2);
        String ampmClose = hours.split("-")[1].substring(hours.split("-")[0].length() - 2);
        if (ampm.equalsIgnoreCase("AM")) {
            openTime = Integer.valueOf((hours.split("-")[0]).split("\\.")[0]);
            if (openTime == 12)
                openTime = 0;
        } else {
            openTime = 12 + Integer.valueOf((hours.split("-")[0]).split("\\.")[0]);
        }

        if (ampmClose.equalsIgnoreCase("AM"))
            closeTime = (Integer.valueOf((hours.split("-")[1]).split("\\.")[0]));
        else
            closeTime = (Integer.valueOf((hours.split("-")[1]).split("\\.")[0]) + 12);

        if (openTime <= curentHour && closeTime >= curentHour) {
            return true;
        } else
            return false;
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;

    public boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) /*&&
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
							!= PackageManager.PERMISSION_GRANTED)*/) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Turn on your gps");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        //String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
    /*@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					writeCalendarEvent();
				} else {
//code for deny
				}
				break;
		}
	}*/


    public static Bitmap downloadImage(String stringUrl) {
        URL url;
        Bitmap bm = null;
        try {
            url = new URL(stringUrl);
            URLConnection ucon = url.openConnection();
            InputStream is;
            if (ucon instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) ucon;
                int statusCode = httpConn.getResponseCode();
                if (statusCode == 200) {
                    is = httpConn.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    BufferedInputStream bis = new BufferedInputStream(is, 8192);
                    ByteArrayBuffer baf = new ByteArrayBuffer(1024);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    byte[] rawImage = baf.toByteArray();
                    bm = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
                    bis.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
   /* public void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("in.pjitsol.zapnabit", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }*/
}

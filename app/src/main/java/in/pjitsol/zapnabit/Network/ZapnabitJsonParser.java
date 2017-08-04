package in.pjitsol.zapnabit.Network;

import java.util.ArrayList;
import java.util.HashMap;

import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Db.DBConstants;
import in.pjitsol.zapnabit.Db.DriverDb;
import in.pjitsol.zapnabit.Entity.Category;
import in.pjitsol.zapnabit.Entity.CategoryProductinfo;
import in.pjitsol.zapnabit.Entity.Driver;
import in.pjitsol.zapnabit.Entity.ScanProductInfo;
import in.pjitsol.zapnabit.Exception.SearchException;
import in.pjitsol.zapnabit.NearbySearch.Buiseness;
import in.pjitsol.zapnabit.NearbySearch.RestoItem;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.User.HistoryItem;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


public class ZapnabitJsonParser implements DBConstants{
    public void ParseMerchantLoginInfo(Context context, ResultMessage resultMessage,
                                       String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.MERCHANT_LOGIN;
                    String Merchant_ID = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_MERCHANT_ID);
                    String Merchant_Status = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_MERCHANT_STATUS);
                    boolean yelp_regsitered = jsonuser.getBoolean(
                            StaticConstants.JSON_MERCHANT_YELP_REGISTERED);
                    String merchantName = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_MERCHANT_NAME);
                    String Merchant_TYpe = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_MERCHANT_TYPE);
                    String Commision = jsonuser.getString(StaticConstants.JSON_MERCHANT_COMISSION);
                    String Tax = jsonuser.getString(StaticConstants.JSON_MERCHANT_TAX);
                    String image = jsonuser.getString(StaticConstants.JSON_MERCHANT_IMAGE);
                    String deliveryvalue = jsonuser.getString(StaticConstants.JSON_MERCHANT_DELIVERY_VALUE);
                    String deliveryType = jsonuser.getString(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE);
                    if (jsonuser.has(StaticConstants.JSON_MERCHANT_DRIVERS)) {
                        JSONArray jsonDrivers = jsonuser.getJSONArray(StaticConstants.JSON_MERCHANT_DRIVERS);
                        for (int k = 0; k < jsonDrivers.length(); k++) {
                            ContentValues cv = new ContentValues();
                            Driver item=new Driver();
                            JSONObject object = jsonDrivers.getJSONObject(k);
                            String driver_id = object.getString(StaticConstants.JSON_MERCHANT_DRIVER_ID);
                            item.driver_id=driver_id;
                            cv.put(DRIVER_ID, driver_id);
                            String driver_name = object.getString(StaticConstants.JSON_MERCHANT_DRIVER_NAME);
                            cv.put(DRIVER_NAME, driver_name);
                            item.driver_name=driver_name;
                            String driver_email = object.getString(StaticConstants.JSON_MERCHANT_DRIVER_EMAIL);
                            cv.put(DRIVER_EMAIL, driver_email);
                            item.driver_email=driver_email;
                            DriverDb.obj().savePrefrenceeformation(context,driver_id,cv,resultMessage);
                          StaticConstants.driversList.add(item);
                        }
                    }


                    if (!TextUtils.isEmpty(Merchant_ID)) {
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_ID, Merchant_ID);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_STATUS, Merchant_Status);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_TYPE, StaticConstants.MERCHANT);
                        PrefHelper.storeBoolean(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_MERCHANT_YELP_REGISTERED, yelp_regsitered);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_NAME, merchantName);
                        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_COMISSION, Commision);
                        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_TAXES, Tax);
                        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_DELIVERY_TYPE, deliveryType);
                        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_DELIVERY_VALUE, deliveryvalue);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME, StaticConstants.CUSTOMER_MERCHANT_ID,
                                Merchant_ID);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_IMAGE,
                                image);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_TYPE,
                                Merchant_TYpe);


                    } else {
                        resultMessage.STATUS = SearchException.NO_RESULTS;
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_ID, "");

                    }
                } else {
                    if (jsonuser.getString("message").contains("Login failure")) {
                        resultMessage.STATUS = StaticConstants.ASYN_RESULT_LOGINFAILURE;
                        resultMessage.ERRORMESSAGE = context.getResources().getString(R.string.loginfailure);
                    } else {
                        resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                        resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                    }


                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }


    public void ParseDriverLoginInfo(Context context, ResultMessage resultMessage,
                                     String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.DRIVER_LOGIN;
                    String Driver_ID = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_MERCHANT_DRIVER_ID);
                    String driver_name = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_MERCHANT_DRIVER_NAME);
                    PrefHelper.storeString(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.DRIVER_ID, Driver_ID);
                    PrefHelper.storeString(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.DRIVER_NAME, driver_name);
                    PrefHelper.storeString(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_TYPE, StaticConstants.ASSIGNED_DRIVER);


                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }


    public void ParseUserLoginInfo(Context context, ResultMessage resultMessage,
                                   String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.USER_LOGIN;
                    String User_ID = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_ID);
                    String User_name = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_NAME);
                    String phoneno = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_PHONE);
                    String city = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_CITY);
                    String specailInstruction = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_TAG_CUD_SPECIAL);
                    String deliveryid = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_ID);
                    String deliveryName = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_NAME);
                    String deliveryAddress = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS);
                    String deliverycity = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_CITY);
                    String deliveryState = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_STATE);

                    if (TextUtils.isEmpty(deliveryid))
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, false);
                    else
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, true);


                    String registerBy = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_REGISTERBY);
                    if (registerBy.equalsIgnoreCase(StaticConstants.ZAPNABIT)) {
                        String userImage = Util.CheckJsonIsEmpty(jsonuser,
                                StaticConstants.JSON_USER_IMAGE);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_USER_IMAGE, userImage);
                    }
                    //Log.d("userinfo",jsonStr);


                    if (!TextUtils.isEmpty(User_ID)) {
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_ID, User_ID);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_TYPE, StaticConstants.DRIVER);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_NAME, User_name);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_CITY, city);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_PHONE, phoneno);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_PHONE, phoneno);

                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_USER_REGISTERBY, registerBy);


                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ID, deliveryid);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_NAME, deliveryName);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_CITY, deliverycity);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS, deliveryAddress);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_STATE, deliveryState);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_TAG_CUD_SPECIAL, specailInstruction);

                    } else {
                        resultMessage.STATUS = SearchException.NO_RESULTS;
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_ID, "");

                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseUserEditInfo(Context context, ResultMessage resultMessage,
                                  String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.USER_LOGIN;

                    String User_name = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_NAME);
                    String phoneno = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_PHONE);
                    String city = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_CITY);
                    String userImage = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_IMAGE);
                    //Log.d("userinfo",jsonStr);


                    if (!TextUtils.isEmpty(User_name)) {


                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_NAME, User_name);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_CITY, city);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_PHONE, phoneno);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_USER_IMAGE, userImage);


                    } else {
                        resultMessage.STATUS = SearchException.NO_RESULTS;
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_ID, "");

                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }


    public void ParseYElpToken(Context context, ResultMessage resultMessage,
                               String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_ACESSTOKEN)) {


                resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                resultMessage.TYPE = BaseNetwork.obj().KEY_YELP_TOKEN;
                String access_token = Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_ACESSTOKEN);
                PrefHelper.storeString(context,
                        PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ACESSTOKEN, access_token);
            } else {
                resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                resultMessage.ERRORMESSAGE = jsonuser.getString("message");
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
    }

    public void ParseYElpSearch(Context context, ResultMessage resultMessage,
                                String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_YELP_TOTAL)) {
                String total = Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_YELP_TOTAL);

                if (!total.equalsIgnoreCase("0")) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = BaseNetwork.obj().KEY_YELP_SEARCH;

                    JSONArray businesses = jsonuser.getJSONArray(StaticConstants.JSON_TAG_YELP_BUISENESS);
                    ArrayList<YelpEntity> businessNames = new ArrayList<YelpEntity>(businesses.length());
                    for (int i = 0; i < businesses.length(); i++) {
                        YelpEntity yelp = new YelpEntity();
                        JSONObject business = businesses.getJSONObject(i);
                        yelp.ItemTYpe = 1;
                        yelp.id = business.getString("id");
                        yelp.Name = business.getString("name");
                        yelp.Rating = business.getString("rating");
                        String distance = business.getString("distance");
                        yelp.Distance = Util.numberfornmat(Double.valueOf(distance) / 1609);
                        String location = business.getString("location");
                        JSONObject locationObj = new JSONObject(location);
                        String fullAddres = "";
                        JSONArray jsonFullAdress = locationObj.getJSONArray("display_address");
                        for (int k = 0; k < jsonFullAdress.length(); k++) {
                            String address = (String) jsonFullAdress.get(k);
                            if (TextUtils.isEmpty(fullAddres))
                                fullAddres = address;
                            else
                                fullAddres = fullAddres + "," + "\n" + address;

                        }
                        yelp.display_address = fullAddres;
                        yelp.image_url = business.getString("image_url");
                        ;
                        yelp.Rating = business.getString("rating");
                        ;
                        yelp.Phone = business.getString("phone");

                        JSONObject coordinate = business.getJSONObject("coordinates");
                        if(!TextUtils.isEmpty(coordinate.getString("latitude"))
                                && !coordinate.getString("latitude").equalsIgnoreCase("null")){
                            yelp.Lat = coordinate.getString("latitude");
                            yelp.Long = coordinate.getString("longitude");
                            yelp.sdk_tag = StaticConstants.YELP_SEARCH;
                            businessNames.add(yelp);
                        }


                    }
                    resultMessage.businessNames = businessNames;
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = BaseNetwork.obj().KEY_YELP_SEARCH;
                }
            } else {
                resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                resultMessage.TYPE = BaseNetwork.obj().KEY_YELP_SEARCH;
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
    }

    public void ParseYElpBuiseness(Context context, ResultMessage resultMessage,
                                   String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_PR_CAT_ID)) {
                String url = Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_BUISENESS_URL);
                String rating = Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_BUISENESS_RATING);
                String review = Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_BUISENESS_REVIEW);
                String name = Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_BUISENESS_NAME);


                Buiseness buiseness = new Buiseness();
                buiseness.rating = rating;
                buiseness.review_count = review;
                buiseness.url = url;
                buiseness.name = name;
                resultMessage.business = buiseness;
                resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                resultMessage.TYPE = BaseNetwork.obj().KEY_YELP_BUISENESS;

            } else {
                resultMessage.ERRORMESSAGE = "No Reviews Found";
                resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
    }

    public void ParseRegisterInfo(Context context, ResultMessage resultMessage,
                                  String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.USER_SIGNUP;
                    String User_ID = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_ID);
                    String User_name = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_NAME);
                    String phoneno = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_PHONE);
                    String city = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_CITY);
                    String specailInstruction = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_TAG_CUD_SPECIAL);

                    String registerBy = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_REGISTERBY);
                    String userImage = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.JSON_USER_IMAGE);
                    String deliveryid = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_ID);
                    String deliveryName = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_NAME);
                    String deliveryAddress = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS);
                    String deliverycity = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_CITY);
                    String deliveryState = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_STATE);

                    if (TextUtils.isEmpty(deliveryid))
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, false);
                    else if (deliveryid.equalsIgnoreCase("null"))
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, false);
                    else
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, true);


                    PrefHelper.storeString(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.JSON_USER_IMAGE, userImage);


                    //Log.d("userinfo",jsonStr);


                    if (!TextUtils.isEmpty(User_ID)) {
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_ID, User_ID);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_TYPE, StaticConstants.DRIVER);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_NAME, User_name);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_CITY, city);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_PHONE, phoneno);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_PHONE, phoneno);

                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_USER_REGISTERBY, registerBy);

                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ID, deliveryid);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_NAME, deliveryName);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_CITY, deliverycity);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS, deliveryAddress);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_STATE, deliveryState);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_TAG_CUD_SPECIAL, specailInstruction);

                    } else {
                        resultMessage.STATUS = SearchException.NO_RESULTS;
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_ID, "");

                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseOtpInfo(Context context, ResultMessage resultMessage,
                             String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.SEND_OTP;
                    String merchant_Otp = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.MERCHANT_OTP);


                    if (!TextUtils.isEmpty(merchant_Otp)) {
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_OTP, merchant_Otp);


                    } else {
                        resultMessage.STATUS = SearchException.NO_RESULTS;
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_OTP, "");

                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseAddress(Context context, ResultMessage resultMessage,
                             String jsonStr, HashMap<String, String> paramsHashmap) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.ADD_ADDRESS;
                    String address_id = Util.CheckJsonIsEmpty(jsonuser,
                            StaticConstants.RES_JSON_USER_DELIVERY_ID);


                    if (!TextUtils.isEmpty(address_id)) {
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, true);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ID, address_id);

                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_NAME,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_NAME));
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_CITY,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_CITY));
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_ADDRESS));
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_STATE,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_STATE));


                    } else {
                        resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                        resultMessage.TYPE = StaticConstants.ADD_ADDRESS;
                        PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.ADDRESS_PRESENT, true);
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_NAME,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_NAME));
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_CITY,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_CITY));
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_ADDRESS));
                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.RES_JSON_USER_DELIVERY_STATE,
                                paramsHashmap.get(StaticConstants.JSON_TAG_DELIVERY_STATE));


                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseScanQrcode(Context context, ResultMessage resultMessage,
                                String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_CODE)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_CODE)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_OK)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.SCANBARCODE_TAG;
                    JSONArray jsonItem = jsonuser.getJSONArray("items");
                    JSONObject jsonItemobject = jsonItem.getJSONObject(0);
                    ScanProductInfo info = new ScanProductInfo();
                    info.description = jsonItemobject.getString("description");
                    info.title = jsonItemobject.getString("title");
                    info.lowest_recorded_price = jsonItemobject.getString("lowest_recorded_price");
                    JSONArray jsonImages = jsonItemobject.getJSONArray("images");
                    String image = (String) jsonImages.get(0);

                    info.images = image;

                    if (jsonuser.has("category")) {
                        if (jsonuser.getJSONArray("category") != null) {
                            JSONArray jsonCategories = jsonuser.getJSONArray("category");
                            for (int i = 0; i < jsonCategories.length(); i++) {
                                CategoryProductinfo item = new CategoryProductinfo();
                                item.id = jsonCategories.getJSONObject(i).getString("id");
                                item.name = jsonCategories.getJSONObject(i).getString("name");
                                info.categories.add(item);

                            }
                        }
                    }

                    resultMessage.scanProductInfo = info;

                    StaticConstants.SCANNEDPRODUCTINFO = info;
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseChangePassInfo(Context context, ResultMessage resultMessage,
                                    String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.CHANGE_PASS;

                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
    }

    public void ParseForgetPassInfo(Context context, ResultMessage resultMessage,
                                    String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.FORGOT_PASS;

                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_PARSE_FAILED;
        }
    }

    public void ParseRegistredRestoList(Context context, ResultMessage resultMessage,
                                        String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.RESGISTERED_RESTO_LIST;
                    JSONArray jsonArray = jsonuser.getJSONArray(StaticConstants.JSON_SDK_INFO);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        YelpEntity yelp = new YelpEntity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        yelp.Merchant_Id = jsonObject.getString(StaticConstants.JSON_MERCHANT_ID);
                        yelp.sdk_tag = jsonObject.getString(StaticConstants.JSON_SDK_NAME);
                        yelp.id = jsonObject.getString(StaticConstants.JSON_SDK_ID);
                        yelp.Name = "name";
                        StaticConstants.resgitred_resto_list.add(yelp);

                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseZabnabitRegistredRestoList(Context context, ResultMessage resultMessage,
                                                String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.ZABNABIT_RESGISTERED_RESTO_LIST;
                    JSONArray jsonArray = jsonuser.getJSONArray(StaticConstants.JSON_SDK_INFO);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        YelpEntity yelp = new YelpEntity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //yelp.ItemTYpe=0;
                        yelp.Name = jsonObject.getString(StaticConstants.JSON_ZAPNABITMERCHANT_NAME);
                        yelp.Merchant_Id = jsonObject.getString(StaticConstants.JSON_MERCHANT_ID);
                        yelp.id = jsonObject.getString(StaticConstants.JSON_MERCHANT_ID);
                        yelp.Lat = jsonObject.getString(StaticConstants.JSON_LAT);
                        yelp.Long = jsonObject.getString(StaticConstants.JSON_LNG);
                        yelp.Color = StaticConstants.GREEN;
                        yelp.sdk_tag = StaticConstants.ZAPNABIT_SEARCH;
                        yelp.display_address = jsonObject.getString(StaticConstants.JSON_MERCHANT_ADDRESS);
                        yelp.hours = jsonObject.getString(StaticConstants.JSON_MERCHANT_HOURS);
                        yelp.priceRange = jsonObject.getString(StaticConstants.JSON_MERCHANT_PRICE);
                        yelp.Distance = jsonObject.getString(StaticConstants.JSON_MERCHANT_DISTANCE);
                        yelp.image_url = jsonObject.getString(StaticConstants.JSON_MERCHANT_IMAGE);

                        if (jsonObject.has("userFav"))
                            if (jsonObject.getString("userFav").equalsIgnoreCase("1"))
                                yelp.userFav = true;

                        StaticConstants.zabnabit_resgitred_resto_list.add(yelp);

                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }

    public void ParseRestoGeneralSetting(Context context, ResultMessage resultMessage,
                                         String jsonStr) {
        try {

            JSONObject jsonuser = new JSONObject(jsonStr);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.RESTO_GENERAL_SETTING;
                    RestoItem item = new RestoItem();
                    item.merchantEmail = jsonuser.getString(StaticConstants.JSON_MERCHANT_EMAIL);
                    item.merchantPhone = jsonuser.getString(StaticConstants.JSON_MERCHANT_PHONE);
                    item.merchantAddress = jsonuser.getString(StaticConstants.JSON_MERCHANT_ADDRESS);
                    item.priceRange = jsonuser.getString(StaticConstants.JSON_MERCHANT_PRICE);
                    item.website = jsonuser.getString(StaticConstants.JSON_MERCHANT_WEBSITE);
                    item.currencyCode = jsonuser.getString(StaticConstants.JSON_MERCHANT_CURRENCY);
                    item.merchantName = jsonuser.getString(StaticConstants.JSON_MERCHANT_NAME);
                    item.preOrder = jsonuser.getString(StaticConstants.JSON_MERCHANT_PREORDER);
                    item.description = jsonuser.getString(StaticConstants.JSON_MERCHANT_DESCRIPTION);
                    item.directions = jsonuser.getString(StaticConstants.JSON_MERCHANT_DIRECTIONS);
                    item.hours = jsonuser.getString(StaticConstants.JSON_MERCHANT_HOURS);
                    item.merchantLat = jsonuser.getString(StaticConstants.JSON_LAT);
                    item.merchantLng = jsonuser.getString(StaticConstants.JSON_LNG);
                    item.image = jsonuser.getString(StaticConstants.JSON_MERCHANT_IMAGE);
                    item.pdf = jsonuser.getString(StaticConstants.JSON_MERCHANT_PDF);
                    item.Commision = jsonuser.getString(StaticConstants.JSON_MERCHANT_COMISSION);
                    item.Tax = jsonuser.getString(StaticConstants.JSON_MERCHANT_TAX);
                    item.milesRange = jsonuser.getString(StaticConstants.JSON_MERCHANT_MILESRANGE);
                    item.lat = jsonuser.getString(StaticConstants.JSON_LAT);
                    item.lng = jsonuser.getString(StaticConstants.JSON_LNG);
                    item.restoStatus = jsonuser.getString(StaticConstants.JSON_MERCHANT_RESTOSTATUS);
                    item.sdk_id = jsonuser.getString(StaticConstants.JSON_SDK_ID);
                    item.sdk_name = jsonuser.getString(StaticConstants.JSON_SDK_NAME);
                    item.specials = jsonuser.getString(StaticConstants.JSON_MERCHANT_SPECIAL);
                    item.coupons = jsonuser.getString(StaticConstants.JSON_MERCHANT_COUPONS);
                    item.linkDiscounts = jsonuser.getString(StaticConstants.JSON_MERCHANT_DISCOUNTS);
                    item.deliveryType = jsonuser.getString(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE);
                    item.deliveryValue = jsonuser.getString(StaticConstants.JSON_MERCHANT_DELIVERY_VALUE);
                    item.minorderFee = jsonuser.getString(StaticConstants.JSON_MERCHANT_DELIVERY_MIN_FEE);
                    item.menuCompleted = jsonuser.getString(StaticConstants.JSON_MERCHANT_MENU);
                    item.profileCompleted = jsonuser.getString(StaticConstants.JSON_MERCHANT_PROFILE);
                    item.merchant_type = jsonuser.getString(StaticConstants.JSON_MERCHANT_TYPE);
                    if (jsonuser.has(StaticConstants.HISTORY_DATA)) {
                        JSONArray jsonArray = jsonuser.getJSONArray(StaticConstants.HISTORY_DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HistoryItem historyItem = new HistoryItem();
                            historyItem.orderId = jsonObject.getString(StaticConstants.JSON_TAG_ORDER_ID);
                            historyItem.Commision = jsonObject.getString(StaticConstants.JSON_TAG_CUD_COMISSION);
                            historyItem.CustomerOrderHistory = jsonObject.getString(StaticConstants.JSON_TAG_CUD_CUSTOMERORDERHISTORY);
                            historyItem.merchantId = jsonObject.getString(StaticConstants.JSON_MERCHANT_ID);
                            historyItem.OrderDate = jsonObject.getString(StaticConstants.JSON_TAG_CUD_ORDERDATE);
                            historyItem.PaymentStatus = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS);
                            historyItem.SpecialInstruction = jsonObject.getString(StaticConstants.JSON_TAG_CUD_SPECIAL);
                            historyItem.SubTotal = jsonObject.getString(StaticConstants.JSON_TAG_CUD_NETPRICE);
                            historyItem.Taxes = jsonObject.getString(StaticConstants.JSON_TAG_CUD_TAXES);
                            historyItem.Total = jsonObject.getString(StaticConstants.JSON_TAG_CUD_TOTALAMOUNT);
                            historyItem.MerchantName = jsonObject.getString(StaticConstants.JSON_MERCHANT_NAME);
                            historyItem.userPhone = jsonObject.getString(StaticConstants.JSON_MERCHANT_PHONE);
                            historyItem.deliveryFees = jsonObject.getString(StaticConstants.JSON_MERCHANT_DELIVERY_VALUE);
                            item.lastOrder = historyItem;
                        }
                    }

                    resultMessage.restoItem = item;


                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        }
        // resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
    }


    public void ParsePlaceOrder(Context context, ResultMessage resultMessage,
                                String jsonString) {
        try {

            JSONObject jsonuser = new JSONObject(jsonString);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.PLACE_ORDER;
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        }
    }

    public void ParseUserHistory(Context context, ResultMessage resultMessage,
                                 String jsonString) {
        try {
            StaticConstants.historyItems.clear();
            JSONObject jsonuser = new JSONObject(jsonString);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.USER_HISTORY;
                    JSONArray jsonArray = jsonuser.getJSONArray(StaticConstants.HISTORY_DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HistoryItem item = new HistoryItem();
                        item.orderId = jsonObject.getString(StaticConstants.JSON_TAG_ORDER_ID);
                        item.Commision = jsonObject.getString(StaticConstants.JSON_TAG_CUD_COMISSION);
                        item.CustomerOrderHistory = jsonObject.getString(StaticConstants.JSON_TAG_CUD_CUSTOMERORDERHISTORY);
                        item.merchantId = jsonObject.getString(StaticConstants.JSON_MERCHANT_ID);
                        item.OrderDate = jsonObject.getString(StaticConstants.JSON_TAG_CUD_ORDERDATE);
                        item.PaymentStatus = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS);
                        item.SpecialInstruction = jsonObject.getString(StaticConstants.JSON_TAG_CUD_SPECIAL);
                        item.SubTotal = jsonObject.getString(StaticConstants.JSON_TAG_CUD_NETPRICE);
                        item.Taxes = jsonObject.getString(StaticConstants.JSON_TAG_CUD_TAXES);
                        item.Total = jsonObject.getString(StaticConstants.JSON_TAG_CUD_TOTALAMOUNT);
                        item.MerchantName = jsonObject.getString(StaticConstants.JSON_MERCHANT_NAME);
                        item.orderType = jsonObject.getString(StaticConstants.JSON_TAG_CUD_ORDER_TYPE);
                        item.paid_status = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAID_STATUS);
                        item.payment_type = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAYMENT_TYPE);
                        item.delivery_address = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS) : "";
                        item.delivery_address_name = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_NAME) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_NAME) : "";
                        item.delivery_city = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_CITY) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_CITY) : "";
                        item.delivery_state = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_STATE) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_STATE) : "";

                        item.userPhone = jsonObject.getString(StaticConstants.JSON_MERCHANT_PHONE);
                        item.deliveryFees = jsonObject.getString(StaticConstants.JSON_MERCHANT_DELIVERY_VALUE);
                        String merchantLat = jsonObject.getString(StaticConstants.JSON_LAT);
                        String merchantLng = jsonObject.getString(StaticConstants.JSON_LNG);
                        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_LAT, merchantLat);
                        PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_LONG, merchantLng);
                        StaticConstants.historyItems.add(item);
                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        }
    }


    public void ParseMerchantHistory(Context context, ResultMessage resultMessage,
                                     String jsonString) {
        try {
            StaticConstants.historyItems.clear();
            JSONObject jsonuser = new JSONObject(jsonString);
            if (jsonuser != null
                    && jsonuser.has(StaticConstants.JSON_TAG_AUTHENTICATION)) {

                if (Util.CheckJsonIsEmpty(jsonuser,
                        StaticConstants.JSON_TAG_AUTHENTICATION)
                        .equalsIgnoreCase(StaticConstants.JSON_TAG_AUTH_TRUE)) {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
                    resultMessage.TYPE = StaticConstants.MERCHANT_HISTORY;
                    JSONArray jsonArray = jsonuser.getJSONArray(StaticConstants.HISTORY_DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HistoryItem item = new HistoryItem();
                        item.orderId = jsonObject.getString(StaticConstants.JSON_TAG_ORDER_ID);
                        item.Commision = jsonObject.getString(StaticConstants.JSON_TAG_CUD_COMISSION);
                        item.CustomerOrderHistory = jsonObject.getString(StaticConstants.JSON_TAG_CUD_CUSTOMERORDERHISTORY);
                        item.merchantId = jsonObject.getString(StaticConstants.JSON_USER_ID);
                        item.OrderDate = jsonObject.getString(StaticConstants.JSON_TAG_CUD_ORDERDATE);
                        item.PaymentStatus = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS);
                        item.SpecialInstruction = jsonObject.getString(StaticConstants.JSON_TAG_CUD_SPECIAL);
                        item.SubTotal = jsonObject.getString(StaticConstants.JSON_TAG_CUD_NETPRICE);
                        item.Taxes = jsonObject.getString(StaticConstants.JSON_TAG_CUD_TAXES);
                        item.deliveryFees = jsonObject.getString(StaticConstants.JSON_MERCHANT_DELIVERY_VALUE);
                        item.Total = jsonObject.getString(StaticConstants.JSON_TAG_CUD_TOTALAMOUNT);
                        item.MerchantName = jsonObject.getString(StaticConstants.JSON_USER_NAME);
                        item.userPhone = jsonObject.getString(StaticConstants.JSON_USER_PHONE);
                        item.orderType = jsonObject.getString(StaticConstants.JSON_TAG_CUD_ORDER_TYPE);
                        item.paid_status = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAID_STATUS);
                        item.payment_type = jsonObject.getString(StaticConstants.JSON_TAG_CUD_PAYMENT_TYPE);
                        item.delivery_address = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS) : "";
                        item.delivery_address_name = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_NAME) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_NAME) : "";
                        item.delivery_city = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_CITY) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_CITY) : "";
                        item.delivery_state = jsonObject.has(StaticConstants.RES_JSON_USER_DELIVERY_STATE) ? jsonObject
                                .getString(StaticConstants.RES_JSON_USER_DELIVERY_STATE) : "";

                        item.order_assigned = jsonObject.has(StaticConstants.RES_JSON_ORDER_ASSIGNED) ? jsonObject
                                .getBoolean(StaticConstants.RES_JSON_ORDER_ASSIGNED) : false;
                        item.assigned_driver = jsonObject.has(StaticConstants.RES_JSON_ORDER_ASSIGNED_DRIVER_NAME) ? jsonObject
                                .getString(StaticConstants.RES_JSON_ORDER_ASSIGNED_DRIVER_NAME) : "";
                        StaticConstants.historyItems.add(item);
                    }
                } else {
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_CANCEL;
                    resultMessage.ERRORMESSAGE = jsonuser.getString("message");
                }

            }
        } catch (JSONException exception) {
            exception.printStackTrace();
            resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        }
    }
}

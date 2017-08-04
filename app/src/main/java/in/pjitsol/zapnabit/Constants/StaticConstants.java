package in.pjitsol.zapnabit.Constants;

import android.graphics.Bitmap;

import in.pjitsol.zapnabit.Entity.Driver;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Entity.ScanProductInfo;
import in.pjitsol.zapnabit.Login.PlaceInfo;
import in.pjitsol.zapnabit.User.HistoryItem;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.util.ArrayList;

public class StaticConstants {


    public static final ArrayList<PlaceInfo> PlacesList = new ArrayList<>();
    public static final ArrayList<YelpEntity> resgitred_resto_list = new ArrayList<>();
    public static final ArrayList<YelpEntity> zabnabit_resgitred_resto_list = new ArrayList<>();
    public static final ArrayList<HistoryItem> historyItems = new ArrayList<>();

    public static final ArrayList<Driver> driversList = new ArrayList<>();

    public static final String IS_LOGGED_IN_VIA_GOOGLE = "IS_LOGGED_IN_GOOGLE";
    public static final String IS_LOGGED_IN_VIA_FACEBOOK = "IS_LOGGED_IN_FACEBOOK";
    public static final String JSON_FILE_NAME = "file_name";
    public static final String JSON_FILE_URL = "file_url";

    public static final String REGISTER_DATEFORMATE_24 = "yyyy-MM-dd kk:mm:ss";
    public static final String LOGIN_TAG = "Login";
    public static final String SIGNUP_TAG = "Signup";
    public static final String NEARBYSEARCH_TAG = "NearBySearch";
    public static final String GOOGLE_TAG = "Google";
    public static final String FOURSQUARE_SEARCH = "Foursquare";
    public static final String GOOGLE_SEARCH = "Google";
    public static final String YELP_SEARCH = "Yelp";
    public static final String YELLOWPAGES_SEARCH = "YEllowpages";
    public static final String USER_TYPE = "UserTYpe";
    public static final String DRIVER = "Driver";
    public static final String MERCHANT = "Merchant";
    public static final String LINKRESTAURANT_TAG = "LinkRestaurant";
    public static final String MERRESTOLIST_TAG = "MerchREstoList";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String SEARCH_PERFORMED = "Search_Type";
    public static final String QUERY_STRING = "Query_String";
    public static final String LOGINSOCIAL_TAG = "Login_Social";
    public static final String RESTOINFO_TAG = "Resto_Info";
    public static final String MENU_SEARCH_RESTO = "Search Restaurants,stores etc";
    public static final String MENU_MERCHANT_LINK = "MERCHANT VERIFICATION\nMap link and verify my place of business," +
            " so\ncustomers can locate me.";
    public static final String MENU_MY_ORDERS = "My Orders";
    public static final String MENU_TOOLS = "Add Product Tools";
    public static final String MENU_TAKE_PICTURE= "Take Picture (Free Version)";
    public static final String MENU_SCAN_BARCODE= "Scan Bar Code (Paid Version)";
    public static final String MENU_ABOUT_US = "About Us";
    public static final String MENU_GENERALINFO = "General Info & Projects";
    public static final String MENU_PRIVACY_POLICY = "Privacy Policies";
    public static final String MENU_FAQ = "FAQs";
    public static final String MENU_PROJECTWEFUNDER = "Project: WeFunder";
    public static final String MENU_PROJECT_DRONE = "Project: Drone Delivery Competition";
    public static final String MENU_RELATIONS = "INVESTOR RELATIONS";
    public static final String MENU_REFUND = "Refund";
    public static final String MENU_TERMS = "TERMS OF USE";
    public static final String MENU_LOGOUT = "Logout";
    public static final String MENU_DELETE_BUSINESS_LINK = "Delete My Business Link";
    public static final String MENU_ACTIVATE_BUSINESS_LINK = "Activate My Business Link";
    public static final String MENU_CUSTOMERVIEW = "Customer View:";
    public static final String MENU_EDIT_PROFILE = "Edit Profile";
    public static final int ASYNTASK_STARTING = 1;
    public static final int ASYNTASK_FINISHING = 2;
    public static final int ASYN_NETWORK_FAIL = 0;
    public static final int ASYN_RESULT_OK = 1;
    public static final int ASYN_RESULT_CANCEL = 3;
    public static final int ASYN_RESULT_LOGINFAILURE= 9;
    public static final int ASYN_RESULT_OK_ = 4;
    public static final int ASYN_RESULT_PARSE_FAILED = 5;
    public static final int ASYN_RESULT_DRAFTCANCEL = 6;

    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    public static final String DELETE_METHOD = "DELETE";
    public static final String JSON_TAG_ERROR = "error";
    public static final String JSON_TAG_AUTHENTICATION = "authorization";
    public static final String JSON_TAG_AUTH_TRUE = "true";
    public static final String JSON_TAG_OK = "OK";
    public static final String JSON_TAG_AUTH_FALSE = "false";
    public static final String JSON_TAG_MESSAGE = "message";
    public static final String JSON_TAG_CODE = "code";

    public static final String JSON_MERCHANT_DELIVERY_TYPE = "deliveryType";
    public static final String JSON_MERCHANT_DELIVERY_TYPE_NOTSET ="0";
    public static final String JSON_MERCHANT_DELIVERY_TYPE_FREE = "1";
    public static final String JSON_MERCHANT_DELIVERY_TYPE_FREEMIN = "2";
    public static final String JSON_MERCHANT_DELIVERY_TYPE_FIXED= "3";
    public static final String JSON_MERCHANT_DELIVERY_TYPE_PERCENT= "4";
    public static final String JSON_MERCHANT_DRIVERS= "drivers";
    public static final String JSON_MERCHANT_DRIVER_ID= "driver_id";
    public static final String JSON_MERCHANT_DRIVER_NAME= "driver_name";
    public static final String JSON_MERCHANT_DRIVER_EMAIL= "driver_email";


    public static final String JSON_MERCHANT_DELIVERY_VALUE= "deliveryValue";
    public static final String JSON_MERCHANT_SPECIAL = "specials";
    public static final String JSON_MERCHANT_PROFILE= "profileCompleted";
    public static final String JSON_MERCHANT_MENU = "menuCompleted";
    public static final String JSON_MERCHANT_COUPONS = "coupons";
    public static final String JSON_MERCHANT_DISCOUNTS = "linkDiscounts";
    public static final String JSON_MERCHANT_ID = "merchantId";
    public static final String JSON_DRIVER_MERCHANT_ID = "merchant_id";
    public static final String JSON_MERCHANT_EMAIL = "merchantEmail";
    public static final String JSON_MERCHANT_NAME = "merchantName";
    public static final String JSON_ZAPNABITMERCHANT_NAME = "MerchantName";
    public static final String JSON_MERCHANT_PASSWORD = "merchantPassword";
    public static final String MERCHANT_ID = "Merchant_Id";
    public static final String MERCHANT_PHONE = "Merchant_Phone";
    public static final String MERCHANT_STATUS = "merchant_active";
    public static final String JSON_MERCHANT_PHONE = "merchantPhone";
    public static final String JSON_MERCHANT_ADDRESS = "merchantAddress";
    public static final String JSON_MERCHANT_PRICE = "priceRange";
    public static final String JSON_MERCHANT_WEBSITE = "website";
    public static final String JSON_MERCHANT_CURRENCY = "currencyCode";
    public static final String JSON_MERCHANT_PREORDER = "preOrder";
    public static final String JSON_MERCHANT_YELP_REGISTERED = "yelp_regsitered";
    public static final String JSON_MERCHANT_DISTANCE = "distance";
    public static final String JSON_MERCHANT_STATUS = "merchant_active";
    public static final String JSON_MERCHANT_RESTOSTATUS = "restoStatus";
    public static final String JSON_MERCHANT_TYPE = "merchant_type";

    public static final String JSON_DRIVER_EMAIL = "email";
    public static final String JSON_DRIVER_PASSWORD = "password";

    public static final String JSON_PRODUCTNAME= "productName";
    public static final String JSON_PRODUCTPRICE = "price";
    public static final String JSON_PRODUCTDESCRIPTION = "Description";
    public static final String JSON_PRODUCTCATEGORY = "Category";
    public static final String JSON_PRODUCTIMAGE= "productImage";



    public static final String JSON_TAG_ACESSTOKEN = "access_token";
    public static final String USER_ACESSTOKEN = "access_token";
    public static final String JSON_YELP_CLIENT_ID = "client_id";
    public static final String JSON_YELP_CLIENT_SECRET = "client_secret";
    public static final String YELP_CLIENT_ID = "BqVWUrWV96ZUchSnQ5hYdg";
    public static final String YELP_CLIENT_SECRET = "YEBjWVw0oF0Gc9Ktc5Karlo6KQPU2tMI34mmBjdwRvOHXqwloZsntDIAEeOGLzAY";
    public static final String JSON_YELP_LATITUDE = "latitude";
    public static final String JSON_YELP_LONGITUDE = "longitude";
    public static final String JSON_YELP_RADIUS = "radius";
    public static final String JSON_YELP_TERM = "term";
    public static final String JSON_TAG_YELP_TOTAL = "total";
    public static final String JSON_TAG_YELP_BUISENESS = "businesses";


    public static final String JSON_MERCHANT_DESCRIPTION = "description";
    public static final String JSON_MERCHANT_DIRECTIONS = "directions";
    public static final String JSON_MERCHANT_HOURS = "hours";
    public static final String JSON_MERCHANT_IMAGE = "image";
    public static final String JSON_MERCHANT_PDF = "pdf";
    public static final String JSON_MERCHANT_COMISSION = "Commision";
    public static final String JSON_MERCHANT_TAX = "Taxes";
    public static final String JSON_MERCHANT_MILESRANGE = "milesRange";

    public static final String JSON_USER_ID = "userId";
    public static final String JSON_USER_EMAIL = "userEmail";
    public static final String JSON_USER_PASSWORD = "userPassword";
    public static final String JSON_USER_NAME = "userName";
    public static final String JSON_USER_CITY = "userCity";
    public static final String JSON_USER_IMAGE = "userImage";
    public static final String JSON_USER_REGISTERBY = "registerBy";
    public static final String JSON_USER_DEVICEID = "deviceId";
    public static final String JSON_USER_PHONE = "userPhone";
    public static final String JSON_PHONE = "phone";
    public static final String RES_JSON_USER_PHONE = "phone no";
    public static final String RES_JSON_USER_CITY = "city";
    public static final String RES_JSON_USER_NAME = "name";
    
    public static final String RES_JSON_USER_DELIVERY_ID = "address_id";
    public static final String RES_JSON_USER_DELIVERY_NAME = "delivery_address_name";
    public static final String RES_JSON_USER_DELIVERY_ADDRESS= "delivery_address";
    public static final String RES_JSON_USER_DELIVERY_CITY= "delivery_city";
    public static final String RES_JSON_USER_DELIVERY_STATE= "delivery_state";

    public static final String JSON_USER_OLDPASSWORD = "oldPassword";
    public static final String JSON_USER_NEWPASSWORD = "newPassword";
    public static final String RES_JSON_ORDER_ASSIGNED= "order_assigned";
    public static final String RES_JSON_ORDER_ASSIGNED_DRIVER_NAME= "assigned_driver";
	
	/*public static final String JSON_RESPONSE_MERCHANT_ID = "Merchant_id";

	
	
	public static final String JSON_RESPONSE_USER_ID = "user_id";
	public static final String JSON_RESPONSE_USER_EMAIL = "user_email";
	public static final String JSON_RESPONSE_USER_NAME = "user_name";*/

    public static final String JSON_SDK_INFO = "sdkinfo";
    public static final String JSON_SDK_ID = "sdk_id";
    public static final String JSON_SDK_NAME = "sdk_name";
    public static final String JSON_LAT = "lat";
    public static final String JSON_LNG = "lng";
    public static final String SDK_INFO = "Sdk_info";
    public static final String SDK_NAME = "Sdk_name";
/*	public static final String JSON_LAT_CAPS = "Lat";
	public static final String JSON_LONG_CAPS = "Lng";*/


    public static final String MERCHANT_LOGIN = "Merchant_Login";
    public static final String USER_LOGIN = "User_Login";
    public static final String USER_SIGNUP = "User_Signup";
    public static final String USER_ID = "User_id";
    public static final String RESGISTERED_RESTO_LIST = "Registered_resto_list";
    public static final String MERCHANT_YELP_REGISTERED = "yelp_regsitered";


    public static final String GREEN = "Green";
    public static final String RED = "Red";
    public static final String LINK_YOUR_RESTAURANT = "Link Your Restaurant";
    public static final String LINK_YOUR_STORE = "Link My Business";
    public static final String ZABNABIT_RESGISTERED_RESTO_LIST = "Zabnabit_resgitered_resto";
    public static final String ZAPNABIT_SEARCH = "Zapnabit";
    public static final String USERSEARCH_TAG = "User_search_tag";
    public static final String RESTAURANT = "Restaurant";
    public static final String QUERY_STRING_FORSEARCH = "QueryString";
    public static final String LOGIN = "Login";
    public static final String SIGNUP = "Signup";
    public static final String RESTO_GENERAL_SETTING = "Resto_General_Setting";
    public static final String MERCHANTORDERS_TAG = "Merchant_Orders";
    public static final String MY_ORDERS = "My Orders";
    public static final String MERCHANTORDERDETAIL_TAG = "Merchant_Order_Detail";
    public static final String ORDER_DETAIL = "Order Detail";
    public static final String MERCHANT_NAME = "merchantName";
    public static final String MERCHANT_DISTANCE = "merchantDistance";
    public static final String MENU_TAG = "Menu";


    public static final String JSON_TAG_PR_MESSAGE = "Message";
    public static final String JSON_TAG_PR_AUTH = "Authentication";
    public static final String JSON_TAG_PR_RESTO_UNIQUE_ID = "RestoUniqueId";
    public static final String JSON_TAG_PR_ITEMS = "items";


    // CATEGORY
    public static final String JSON_TAG_PR_CAT_ARRAY = "catArray";
    public static final String JSON_TAG_PR_CAT_ID = "id";
    public static final String JSON_TAG_PR_CAT_PARENT = "parent_id";
    public static final String JSON_TAG_PR_CAT_NAME = "name";
    public static final String JSON_TAG_PR_CATID = "catid";
    public static final String JSON_TAG_PR_FLAG = "flag";
    public static final String JSON_TAG_PR_CAT_STATUS = "status";
    public static final String JSON_TAG_PR_CAT_HAS_SUB_CAT = "hasubcat";
    public static final String JSON_TAG_PR_CAT_HAS_PRD = "hasprod";
    public static final String JSON_TAG_PR_CAT_VAT = "vat";

    // PRODUCT
    public static final String JSON_TAG_PR_PRO_ARRAY = "proArray";
    public static final String JSON_TAG_PR_PRO_ID = "id";
    public static final String JSON_TAG_PR_PRO_NET_PRICE = "price";
    public static final String JSON_TAG_PR_PRO_GROSS_PRICE = "gross";
    public static final String JSON_TAG_PR_PRO_PRODUCT_CODE = "product_code";
    public static final String JSON_TAG_PR_PRO_NAME = "name";
    public static final String JSON_TAG_PR_PRO_TYPE = "type";
    public static final String JSON_TAG_PR_PRO_VAT = "vat";
    public static final String JSON_TAG_PR_PRO_QUANTITY = "Product_Quantity";
    public static final String JSON_TAG_PR_PRO_PARENT = "parent_id";
    public static final String JSON_TAG_PR_PRO_PRODUCT_IMG = "image";
    public static final String JSON_TAG_PR_PRO_PRODUCT_SLIDERIMG = "product_img_slide";
    public static final String JSON_TAG_PR_PRO_DES = "description";
    public static final String JSON_TAG_PR_PRO_PRODUCT_IMGNEW = "product_img";

    public static final String JSON_TAG_PR_PRO_ATTR_CONDITION = "attr_condition";
    public static final String JSON_TAG_PR_PRO_ATTR_DISPLAYORDER= "display_order";


    // PRODUCT ADDONS
    public static final String JSON_TAG_PR_PRO_AD_ARRAY = "addonsArray";
    public static final String CART_TAG = "Cart";
    public static final String SEND_OTP = "Send_otp";
    public static final String RESEND_OTP = "ReSend_otp";

    public static final String MERCHANT_OTP = "merchant_otp";
    public static final String SEARCHTERM = "SEARCHTERM";
    public static final String FCM_TOKEN = "Fcm_Token";
    public static final String NOTIFICATION_CALL = "Notification_Call";
    public static final String EDITPROFILE_TAG = "Edit_Profile_Tag";
    public static final String CHANGE_PASS = "Change_Pass";
    public static final String FORGOT_PASS = "Change_Pass";
    public static final String FB_ID = "Fb_id";
    public static final String VERIFYMERCHANT_TAG = "Verify_Merchant";
    public static final String FROM_GOBACK = "From_goback";
    public static final String JSON_TAG_BUISENESS_URL = "url";
    public static final String JSON_TAG_BUISENESS_RATING = "rating";
    public static final String JSON_TAG_BUISENESS_REVIEW = "review_count";
    public static final String WEBVIEW_TAG = "WebView_Tag";
    public static final String RESTO_URL = "Resto_Url";
    public static final String RESTO_NAME = "name";
    public static final String JSON_TAG_BUISENESS_NAME = "name";
   /* public static final String IS_LOGGED_IN_EARLIER_FB = "Fblogin";
    public static final String IS_LOGGED_IN_EARLIER_GOOGLE = "Glogin";*/


    public static final String CURRENT_LOGIN_TYPE = "current";
    public static final String LAST_LOGIN_TYPE = "last";
    public static final String CHANGE_LOGIN_TYPE_SHOWED_ONCE = "logintype";
    public static final String FB_IMAGE_URL = "ImageUrl";
    public static final String FB_ACESSTOKEN = "fbaccestoken";
    public static final String FB_NAME ="Fbname" ;
    public static final String MERCHANT_SDK_NAME = "sdk_name";
    public static final String GOOGLE ="google" ;
    public static final String LAST_IMAGE_DATA = "encoded_image";
    public static final String SCANBARCODE_TAG = "ScanBArcode_Tag";
    public static final String SCANPRODUCTINFO_TAG = "ScanProduct_Info";
    public static final String JSON_QRCODE = "qrCode";
    public static final String TAKEPICTURE_TAG = "takepicture_tag";
    public static final String TAKEPICTUREINFO_TAG = "takepictureinfo";
    public static final String ZAPNABITWEBVIEW_TAG = "Zapnbit_webview";
    public static final String BACKPRESSED = "backPressed";
    public static final String CALLING_FROM_LOGINMERCHANT = "calling from login merchant";
    public static final String LOGOUTTYPE = "logout_type";
    public static final String HAS_PERMISSIONS = "permission";
    public static final String JSON_TAG_OR_VARIABLEPRODUCT = "variable_product";
    public static final String ADDRESS_PRESENT = "addresspresent";
    public static final String JSON_TAG_DELIVERY_NAME = "name";
    public static final String JSON_TAG_DELIVERY_ADDRESS = "address";
    public static final String JSON_TAG_DELIVERY_CITY = "city";
    public static final String JSON_TAG_DELIVERY_STATE = "state";
    public static final String DRIVER_ID = "driver_id";
    public static final String DRIVERSORDERS_TAG = "driverorder_tag";
    public static final String DRIVERORDERDETAIL_TAG = "driverorderdetail_tag";
    public static final String DRIVER_NAME = "driverNAme";
    public static final String MENU_MY_MENU = "Menu";
    public static final String MERCHANT_MENU_TAG = "Merchant_Menu_Tag";
    public static final String JSON_MERCHANT_DELIVERY_MIN_FEE = "minorderFee";
    public static final int PAYPAL_REQUEST = 143;
    public static final String PAYPAL = "paypal";
    public static final String CARD = "card";
    public static final String ANDROIDPAY = "androidpay";
    public static final String JSON_TAG_COMISSION = "commission";


    public static  Bitmap BITMAP ;

    public static Order CURRENT_ORDER = new Order();

    public static final String JSON_TAG_ORDER_ID = "orderId";
    public static final String JSON_TAG_CUD_ORDER_ID = "OrderId";
    public static final String JSON_TAG_CUD_DEVICE_TYPE = "DeviceType";
    public static final String JSON_TAG_CUD_DISCOUNTPER = "DiscountPer";
    public static final String JSON_TAG_CUD_DISCOUNTTOTAL = "DiscountTotal";
    public static final String JSON_TAG_CUD_TOTALAMOUNT = "Total";
    public static final String JSON_TAG_CUD_NETPRICE = "SubTotal";
    public static final String JSON_TAG_CUD_DELIVERYFEES = "deliveryFees";
    public static final String JSON_TAG_CUD_PAYMENTSTATUS = "PaymentStatus";
    public static final String JSON_TAG_CUD_ORDERDATE = "OrderDate";
    public static final String JSON_TAG_CUD_CUSTOMERORDERHISTORY = "CustomerOrderHistory";
    public static final String JSON_TAG_CUD_TAXES = "Taxes";
    public static final String JSON_TAG_CUD_COMISSION = "Commision";
    public static final String JSON_TAG_CUD_REQUESTTYPE = "RequestType";
    public static final String JSON_TAG_CUD_DEVICEID = "DeviceId";
    public static final String JSON_TAG_CUD_SPECIAL = "SpecialInstruction";
    public static final String ANDROID = "Android";
    public static final String JSON_TAG_CUD_ORDER_TYPE = "orderType";
    public static final String JSON_TAG_CUD_PAID_STATUS = "paid_status";
    public static final String JSON_TAG_CUD_PAYMENT_TYPE= "payment_type";
    public static final String JSON_TAG_AMOUNT = "amount";
    public static final String JSON_TAG_STRIPE_TOKEN = "stripeToken";

    public static final String JSON_TAG_OR_ORDER = "Order";
    public static final String JSON_TAG_OR_ROUTES = "routes";
    public static final String JSON_TAG_OR_PRODUCTTYPE = "ProductType";
    public static final String JSON_TAG_OR_THIRDCATEGORY = "third_cat";
    public static final String JSON_TAG_OR_PRODUCTNAME = "ProductName";
    public static final String JSON_TAG_OR_PRODUCTPARENTID = "ProductParentId";
    public static final String JSON_TAG_OR_PRODUCTID = "ProductId";
    public static final String JSON_TAG_OR_PRODUCTNO = "ProductNo";
    public static final String JSON_TAG_OR_PRODUCTNETPRICE = "ProductNetPrice";
    public static final String JSON_TAG_OR_PRODUCTQTY = "ProductQty";
    public static final String JSON_TAG_OR_TAG_PRODUCTTYPEID = "ProductTypeId";
    public static final String JSON_TAG_OR_PRODUCTGROSSPRICE = "ProductGrossPrice";
    public static final String JSON_TAG_OR_EXTRANOTES = "extraNotes";
    public static final String JSON_TAG_OR_TABLEMOVE = "TableMove";
    public static final String JSON_TAG_OR_GETPRODUCT_SUBCAT_THIRD = "GetProduct_subcat_third";
    public static final String JSON_TAG_OR_SERVICES = "Services";
    public static final String JSON_TAG_OR_ISPRINTRT = "isprinter";
    public static final String CUSTOMER_MERCHANT_ID = "Customer_Merchant_Id";
    public static final String PLACE_ORDER = "Place Order";
    public static final String USERORDERS_TAG = "User_Orders";
    public static final String USERORDERDETAIL_TAG = "User_order_detail";
    public static final String USER_HISTORY = "User_History";
    public static final String HISTORY_DATA = "HistoryData";
    public static final String FETCH_MENU = "Fetch_Menu";
    public static final String HISTORY_ITEM_POSITION = "History_Item_Position";
    public static final String MENU = "Menu";
    public static final String ORDER_LIST = "Order List";
    public static final String NEARBY = "Near By";
    public static final String PAYMENT_STATUS_3 = "3";
    public static final String PAYMENT_STATUS_10 = "10";
    public static final String PAYMENT_STATUS_2 = "2";
    public static final String PAYMENT_STATUS_1 = "1";
    public static final String MERCHANT_HISTORY = "Merchant_history";
    public static final String UPDATE_ORDER = "Update_order";
    public static final String DOLLAR = "$ ";
    public static final String MERCHANT_LAT = "Merchant_Lat";
    public static final String MERCHANT_LONG = "Merchant_Long";
    public static final String MERCHANT_COMISSION = "Merchant_Comission";
    public static final String MERCHANT_TAXES = "Merchant_Taxes";
    public static final String MERCHANT_DELIVERY_TYPE = "deliveryType";
    public static final String MERCHANT_DELIVERY_VALUE = "deliveryValue";


    //Option Json Tag
    public static final String JSON_TAG_OR_OPTIONID = "OptionId";
    public static final String JSON_TAG_OR_OPTIONNAME = "OptionName";
    public static final String JSON_TAG_OR_OPTIONNETPRICE = "OptionNetPrice";
    public static final String JSON_TAG_OR_OPTION_ATTRIID = "OptionAttributeId";
    public static final String JSON_TAG_OR_OPTION_ATTRI_PARENTID = "OptionAttributeParentId";
    public static final String JSON_TAG_OR_OPTION_ATTRINAME = "OptionAttributeName";
    public static final String JSON_TAG_OR_OPTIONARRAY = "options";

    public static final String USER_NAME = "userName";
    public static final String USER_PHONE = "userPhone";
    public static final String USER_CITY = "userCity";
    public static final String FACEBOOK_LOGIN = "Facebook_login";
    public static final String GOOGLE_LOGIN = "Google_login";
    public static final String FB = "fb";
   public static final String GOGGLE = "g";
    public static final String ZAPNABIT = "Zapnabit";

    public static final String JSON_REGISTEREDBY = "register_by";
    public static final String UPDATE_MERCHANT_STATUS = "update_merchant_status";
    public static final String PRIVACY_TAG = "Privacy_Tag";
    public static final String ABOUTUS_TAG = "Aboutus_Tag";
    public static ScanProductInfo SCANNEDPRODUCTINFO;
    public static String SAVE_QRCODE_IN_MENU="saveinmenu";
    public static String SAVE_QRCODE_IN_TEMP="saveintemp";
    public static String USER_FAVOURITE="User_fav";
    public static String USER_UNFAVOURITE="User_UnFav";
    public static String MERCHANT_SDK_REGISTER="Merchant_Sdk_Register";

    public static final String JSON_TAG_PR_PRO_ATTRI_ARRAY = "attributeArray";
    public static final String JSON_TAG_PR_PRO_ATTRIOPTION_ARRAY = "optionArray";
    public static String ADD_ADDRESS="Add_address";
    public static String ASSIGN_DRIVER="Assign_driver";
    public static String DRIVER_LOGIN="Driver_Login";
    public static String ASSIGNED_DRIVER="Assigned_Driver";
    public static String DRIVER_SAVED="driver_saved";
    public static String PAYMENT_SUCCESSFUL="payment_success";
}

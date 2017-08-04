package in.pjitsol.zapnabit.Db;

public interface DBConstants {
	public static final int DB_VERSION = 1;

	public static final String DB_NAME = "pos";

	public static final String _ID = "_id";

	final String CREATE_TABLE_BASE = "CREATE TABLE IF NOT EXISTS ";

	final String CREATE_INDEX_BASE = "CREATE INDEX IF NOT EXISTS ";

	final String ON = " ON ";

	final String PRIMARY_KEY = " PRIMARY KEY";

	final String INTEGER = " Integer";
	final String INTEGERNOTNULL = " Integer NOT NULL DEFAULT";
	final String TEXT = " TEXT";

	final String TEXTNOTNULL = " TEXT NOT NULL DEFAULT ";

	final String BLOB = " BLOB";

	final String AUTO_ICNREMENT = " AUTOINCREMENT";

	final String START_COLUMN = " ( ";

	final String FINISH_COLUMN = " ) ";

	final String COMMA = ",";

	// Category table
	public static final String CATEGORY_TABLE = " category";
	public static final String CATEGORY_ID = "cat_id";
	public static final String CATEGORY_NAME = "cat_name";
	public static final String CATEGORY_NAME_EN = "cat_name_en";
	public static final String CATEGORY_HAS_SUB_CAT = "has_sub_cat";
	public static final String CATEGORY_HAS_PRODUCT = "has_product";
	public static final String CATEGORY_PARENT_CAT_ID = "parent_cat_id";
	public static final String CATEGORY_VAT = "vat";
	public static final String CAT_SYNC = "sync";
	public static final String CAT_DATE_TIME = "date_time";
	public static final String CAT_USER_ID = "UserId";
	public static final String CAT_RESTOUNIQEID = "RestoUniqeId";
	public static final String CATEGORY_STATUS= "status";
	public static final String CATEGORY_IMAGE="product_img";
	public static final String CATEGORY_SLIDER_IMAGE="product_img_slide";
	public static final String CATEGORY_DESC="product_Des";


	
	// Category table ends here

	// Product table
	public static final String PRODUCT_STATUS= "status";
	public static final String PRODUCT_TABLE = " product";
	public static final String PRODUCT_ID = "id";
	public static final String PRODUCT_NAME = "name";
	public static final String PRODUCT_TYPE = "type";

	public static final String PRODUCT_NAME_EN = "name_en";
	public static final String PRODUCT_NAME_CH = "name_ch";
	public static final String PRODUCT_GROSS_PRICE = "gross_price";
	public static final String PRODUCT_NET_PRICE = "net_price";
	public static final String PRODUCT_AMOUNT = "Amount";
	public static final String PRODUCT_CURRENCY = "currency";
	public static final String PRODUCT_CATEGORY_ID = "cat_id";
	public static final String PRODUCT_NUM = "product_num";
	public static final String PRODUCT_VAT = "vat";
	public static final String PRODUCT_QUANTITY = "Product_Quantity";
	public static final String PRODUCT_SYNC = "sync";
	public static final String PRODUCT_DATE_TIME = "date_time";
	public static final String PRODUCT_USER_ID = "USER_ID";
	public static final String PRODUCT_RESTOUNIQEID = "RestoUniqeId";
	public static final String JSON_TAG_PR_PRO_DES = "description";
	public static final String JSON_TAG_PR_PRO_ATTR_CONDITION = "attr_condition";
	public static final String JSON_TAG_PR_PRO_ATTR_DISPLAYORDER = "display_order";

	public static final String PRODUCT_QTY= "product_qty";

	String PRODUCT_HAS_ADD_ON = "has_add_on";

	// index
	public static final String PRODUCT_TABLE_ID_INDEX = "product_id_index";

	final String PRODUCT_TABLE_CAT_ID_INDEX = "product_cat_id_index";

	final String USER_TABLE_ID_INDEX = "user_id_index";

	final String STAFF_TABLE_ID_INDEX = "staff_id_index";

	final String TABLE_TABLE_ID_INDEX = "table_id_index";

	final String CUSTOMERDETAIL_TABLE_ID_INDEX = "Customer_id_index";

	final String PRINTER_TABLE_ID_INDEX = "Printer_id_index";

	final String REPRINTQUEUE_TABLE_ID_INDEX = "RePrintQueue_id_index";

	final String NOTIFICATION_TABLE_ID_INDEX = "Notification_id_index";

	// Product Table Ends here
	// Product Add On Table
	String PRODUCT_ADD_ON_TABLE = " product_add_on";

	String ADD_ON_ID = "id";

	String ADD_ON_PRODUCT_ID = "product_id";

	String ADD_ON_NAME = "name";

	String ADD_ON_NET_PRICE = "net_price";

	String ADD_ON_GROSS_PRICE = "gross_price";

	String ADD_ON_VAT = "vat";

	String ADD_ON_SYNC = "sync";

	String ADD_ON_DATE_TIME = "date_time";

	String ADD_ON_USER_ID = "user_id";

	String ADD_ON_RESTOUNIQEID = "RestoUniqeId";
	String ADD_ON_TYPE = "type";


	// Product Add On Ends here





	String PRODUCT_ATTRIBUTES_TABLE = " product_attributes";

	String ATTRIBUTE_ID = "id";

	String ATTRIBUTE_PRODUCT_ID = "product_id";

	String ATTRIBUTE_NAME = "name";

	String ATTRIBUTE_CONDITION = "condition";

	String ATTRIBUTE_DISPLAYORDER = "displayOrder";



	String PRODUCT_ATTRIBUTESOPTION_TABLE = " product_attributes_option";

	String ATTRIBUTESOPTION_ID = "id";

	String ATTRIBUTESOPTION_PRODUCT_ID = "product_id";

	String ATTRIBUTESOPTION_NAME = "name";

	String ATTRIBUTESOPTION_PRICE = "price";




	// USR_USERTABLE:---->>>

	public static final String USER_TABLE_BASE = "tblUSRUsers";

	String USR_USERID = "UserId";

	String USR_USERNAME = "UserName";

	String USR_USEREMAILID = "UserEmailId";

	String USR_PASSWORD = "UserPassword";

	String USR_USERDESIGNATION = "UserDesignation";

	String USR_USERREGDATE = "UserRegDate";

	String USR_USEREDITDATE = "UserEditDate";

	String USR_USERADDRESS1 = "UserAddress1";

	String USR_USERADDRESS = "UserAddress";

	String USR_TOTALEMPLOYEE = "TotalEmployee";

	String USR_RESTOFULLNAME = "RestoFullName";

	String USR_RESTOUNIQENAME = "RestoUniqeName";

	String USR_USERCOMPANY = "UserCompany";

	String USR_USERPHONE = "UserPhone";

	String USR_LOGINBY = "LoginBy";

	String USR_LANG = "lang";

	String USR_CITY = "City";

	String USR_PROVINCE = "Province";

	String USR_POSTCODE = "PostCode";

	String USR_VAT = "Vat";

	String USR_VAT_PERCENT = "VatPercent";

	String USR_RESTOOPEN = "Resto_open";

	String USR_RESTOCLOSE = "Resto_close";

	String USR_WEBSITE1 = "website1";

	String USR_WEBSITE2 = "website2";

	String USR_WEBSITE3 = "website3";

	String USR_WEBSITE4 = "website4";

	String USR_SERVICES = "Services";

	String USR_PRINTERSETTING = "Printer_Setting";

	String USR_PRINTERFONT = "Printer_Font";

	String USR_COUNTRY = "country";

	String USR_COUNTY = "county";

	String USR_APPTYPE = "AppType";

	String USR_SYNC = "Sync";

	String USR_IMAGE = "Images";

	String USR_SERVICECHARGE = "Service_Charge";

	String USR_IPADDRESS = "IP_Address";

	String USR_DEVICE_ID = "Device_Id";

	String USR_GCM_REGKEY = "GCM_Registration_Key";
	// STAFF MEMBER TABLE:---->>>
	String STAFF_TABLE_BASE = "tblUSRStaff";

	String STF_STAFFID = "StaffId";

	String STF_STAFFUSERID = "UserId";

	String STF_STAFFUSERRESTOUNIQEID = "UserRestoUniqeId";

	String STF_STAFFPOSTION = "StaffPosition";

	String STF_STAFFUSEREMAIL = "StaffUserEmail";

	String STF_STAFFUSERNAME = "StaffUserName";

	String STF_STAFFUSERPASSWORD = "StaffUserPassword";

	String STF_STAFFREGDATE = "StaffRegDate";

	String STF_STAFFEDITDATE = "StaffEditDate";

	String STF_STAFFPAYMENT = "Payment";

	String STF_STAFFORDERHISTORY = "Order_history";

	String STF_STAFFDATAREPORT = "Data_Report";

	String STF_STAFFMANAGEPRODUCT = "Manage_Product";

	String STF_STAFFTAKEORDER = "Take_Order";

	String STF_STAFFADMINTRATIONMANAGEMENT = "Adminstration_management";

	String STF_STAFFPRINTERSETTING = "Printer_Setting";

	String STF_STAFFSYNC = "Sync";

	String STF_STAFF_PRINT_RECEIPT = "Print_Receipt";

	String STF_STAFF_IP_ADDRESS = "IP_Address";
	
	String STF_STAFF_PINCODE = "StaffPincode";
	
	String STF_STAFF_NOTIFICATION_ACTIVATED = "Activate_Notification";

	String STF_STAFF_STATUS = "status";

	String STF_STAFF_SERVERID = "Server_Id";

	// Create table

	public static final String MANAGE_TABLE_TABLENUMBERS = " tblUSRRestoTable ";

	String TABLE_TABLEID = "TableId";
	String TABLE_TABLENUMBER = "TableNo";
	String TABLE_TABLETSTATUS = "TableStatus";
	String TABLE_LOGINNAME = "LoginName";
	String TABLE_RESTTOUNIQUENAME = "RestoUniqeid";
	String TABLE_SYNC = "sync";
	String TABLE_SERVERID = "Server_Id";

	// Create Customer Details Table
	public static String CUSTOMERDETAILS_TABLE_BASE = " tblUSRCustomerDetails ";

	String CUD_ORDER_ID = "OrderId";
	String CUD_GROSSPRICE = "GrossPrice";
	String CUD_DISCOUNTPER = "DiscountPer";
	String CUD_DISCOUNTTOTAL = "DiscountTotal";
	String CUD_TOTALAMOUNT = "TotalAmount";
	String CUD_VATNO = "VatNo";
	String CUD_VATTOTAL = "VatTotal";
	String CUD_NETPRICE = "NetPrice";
	String CUD_LOGINNAME = "LoginName";
	String CUD_RESTOUNIQENAME = "RestoUniqeName";
	String CUD_PAYMENTMODE = "PaymentMode";
	String CUD_PAYMENTSTATUS = "PaymentStatus";
	String CUD_TABLENO = "TableNo";
	String CUD_ORDERDATE = "OrderDate";
	String CUD_CUSTOMERORDERHISTORY = "CustomerOrderHistory";
	String CUD_PAYMENTDATE = "PaymentDate";
	String CUD_STATUS = "Status";
	String CUD_POSTCODE = "PostalCode";
	String CUD_ADDRESS = "Address";
	String CUD_PHONENUMBER = "PhoneNumber";
	String CUD_DISTENCE = "Distence";
	String CUD_USERNAME = "UserName";
	String CUD_TITLE = "Title";
	String CUD_POSTSATUS = "PostStatus";
	String CUD_USERAMOUNT = "UserAmount";
	String CUD_USER_CASHAMOUNT = "CashAmount";
	String CUD_USER_CARDAMOUNT = "CardAmount";
	String CUD_USERCHANGE = "UserChange";
	String CUD_FEE = "Fee";
	String CUD_LEADTIME = "LeadTime";
	String CUD_SERVICE_CHARGE = "Service_Charge";
	String CUD_ORDER_TYPE = "OrderType";
	String CUD_DEVICE_TYPE = "DeviceType";
	String CUD_DECLINE_REASON = "Decline_Reason";
	String CUD_CARD_NUMBER = "CardNumber";
	String CUD_CARD_NAME = "CardName";
	String CUD_SYNC = "sync";
	String CUD_SERVERID = "Server_Id";

	// for sold product db
	public static final String TB_SOLDPRODUCTTABLE = "tbSoldProduct";
	String CUD_SOLDID = "SoldId";
	String CUD_PRODUCTID = "ProductId";
	String CUD_PRODUCTNAME = "ProductName";
	String CUD_PRODUCTPARENTID = "ProductParentId";
	String CUD_PRODUCTNETPRICE = "ProductNetPrice";
	String CUD_PRODUCTGROSSPRICE = "ProductGrossPrice";
	String CUD_PRODUCTQTY = "ProductQty";

	// tbVerions table-------->>>>>
	public static final String TB_VERSION_TABLE = "tbVersion";

	public static final String TB_VERSION_ID = "_id";
	public static final String TB_VERSION_RESTOUNIQENAME = "RestoUniqeName";
	public static final String TB_VERSION_USER_TABLE = "tblUSRUsers";
	public static final String TB_VERSION_STAFF_TABLE = "tblUSRStaff";
	public static final String TB_VERSION_MANAGE_TABLE = "tblUSRRestoTable";
	public static final String TB_VERSION_CUSTOMERDETAILS_TABLE = "tblUSRCustomerDetails";
	public static final String TB_VERSION_MENUPRODUCT = "tblMENUproducT";
	public static final String TB_VERSION_DB_VER = "dbVersion";

	// tbVerions print table-------->>>>>
	public static final String TB_PRINTER_TABLE = "tbUSRPrinter";
	public static final String TB_PRINTER_ID = "_id";
	public static final String TB_PRINTER_TITLE = "PrinterTitle";
	public static final String TB_PRINTER_USERSETTINGS = "UserPrinterSettings";
	public static final String TB_PRINTER_VALUE = "PrinterValue";
	public static final String TB_PRINTER_DRAWEROPEN = "DrawerOpen";
	public static final String TB_PRINTER_OPENDATETIME = "OpenDateTime";
	public static final String TB_PRINTER_CLOSEDATETIME = "CloseDateTime";
	public static final String TB_PRINTER_CURRENTSTATUS = "CurrentStatus";
	public static final String TB_PRINTER_ERROR_MESSAGE = "errorMessage";
	public static final String TB_PRINTER_RESTOUNIQENAME = "RestoUniqeName";

	// tbRePrintQueue RePrint table ------>>>>>
	public static final String TB_REPRINTQUEUE_TABLE = "tbRePrintQueue";
	public static final String TB_REPRINTQUEUE_ID = "_id";
	public static final String TB_REPRINTQUEUE_USER = "RePrintUser";
	public static final String TB_REPRINTQUEUE_VALUE = "RePrintValue";
	public static final String TB_REPRINTQUEUE_STATUS = "RePrintStatus";
	public static final String TB_REPRINTER_RESTOUNIQENAME = "RestoUniqeName";

	// tbNotification Notification table ------>>>>>
	public static final String TB_NOTIFICATION_TABLE = "tbNotification";
	public static final String TB_NOTIFICATION_ID = "ID";
	public static final String TB_NOTIFICATION_MESSAGE = "NotifcationMessage";
	public static final String TB_NOTIFICATION_DETAIL = "NotifcationDetail";
	public static final String TB_NOTIFICATION_DEVICETYPE = "DeviceType";
	public static final String TB_NOTIFICATION_STATUS = "NotificationStatus";
	public static final String TB_NOTIFICATION_DATETIME = "NotificationDateTime";
	public static final String TB_NOTIFICATION_RESTOUNIQENAME = "RestoUniqeName";
	public static final String TB_NOTIFICATION_PHONENUMBER = "PhoneNumber";
	public static final String TB_NOTIFICATION_ORDER_TYPE = "OrderType";
	public static final String TB_NOTIFICATION_ORDER_STATUS = "OrderStatus";

	// tbUSRSyncTable Synchronization Table
	public static final String TB_SYNC_TABLE_BASE = "tbUSRSyncTable";
	public static final String TB_SYNC_ID = "ID";
	public static final String TB_SYNC_TABLENAME = "TableName";
	public static final String TB_SYNC_STATUS = "SyncStatus";
	
	
	
	
	public static final String MEAL_ADDON= "mealaddon";
	public static final String MEAL_ADDONID= "id";
	public static final String MEAL_ADDONNAME = "name";
	public static final String MEAL_ADDONPRICE = "price";
	public static final String MEAL_ADDON_PARENTMEALID = "parentmealid";
	public static final String MEAL_ADDON_PARENTGRID= "parentgroupid";
	
	
	

	// Meal table
	public static final String MEAL_TABLE = " meal";

	public static final String MEAL_ID = "meal_id";
	public static final String MEAL_PARENTID = "meal_parentid";

	public static final String MEAL_PRICE = "meal_price";
	public static final String MEAL_QTY = "meal_qty";

	public static final String MEAL_NAME = "meal_name";

	public static final String MEAL_NAME_EN="meal_name_en";

	public static final String MEAL_DESCRIPTION="meal_description";

	public static final String MEAL_RESTOUNIQEID = "RestoUniqeId";
	public static final String MEAL_IMAGE = "mealimage";
	public static final String MEAL_SLIDER_IMAGE = "mealimage_slide";

	// Meal table ends here

	// Meal GROUP table
	public static final String MEALGROUP_TABLE = " mealgroup";
	public static final String MEALGROUP_ID = "mealgroup_id";
	public static final String MEALGROUP_PARENTID = "mealgroup_parentid";
	public static final String MEALGROUP_NAME = "mealgroup_name";
	public static final String MEALGROUP_CONDITIONID= "mealgroup_conditionid";
	public static final String MEALGROUP_CONDITIONNAME = "mealgroup_conditionname";
	public static final String MEALGROUP_CONDITIONFILTERSTATUS = "mealgroup_conditionfilter";
	public static final String MEALGROUP_CONDITIONMIN = "mealgroup_conditionmin";
	public static final String MEALGROUP_CONDITIONMAX= "mealgroup_conditionmax";
	public static final String MEALGROUP_CONDITIONSELECTALL= "mealgroup_conditionselectall";

	// Meal PRODUCT table ends here
	public static final String MEAL_PRODUCT= "product";
	public static final String MEAL_PRODUCTTABLE= "producttable";
	public static final String MEAL_PRODUCTID = "productid";
	public static final String MEAL_PRODUCTNAME = "product_name";
	public static final String MEAL_PRODUCTQTY = "product_qty";
	public static final String MEAL_PRODUCT_MEALGROUPID= "product_mealgroupid";
	public static final String MEAL_PRODUCT_MEALID = "product_mealid";






	// Meal table
	public static final String CUSTOMER_MEAL_TABLE = " customer_meal";

	public static final String CUSTOMER_MEAL_ID = "customer_meal_id";
	public static final String CUSTOMER_MEAL_PRICE = "customer_meal_price";
	public static final String CUSTOMER_MEAL_QTY = "customer_meal_qty";
	public static final String CUSTOMER_MEAL_NAME = "customer_meal_name";
	public static final String CUSTOMER_MEAL_NAME_EN="customer_meal_name_en";
	public static final String CUSTOMER_MEAL_DESCRIPTION="customer_meal_description";
	public static final String CUSTOMER_MEAL_RESTOUNIQEID = "customer_RestoUniqeId";

	// Meal table ends here

	// Meal GROUP table
	public static final String CUSTOMER_MEALGROUP_TABLE = " customer_mealgroup";
	public static final String CUSTOMER_MEALGROUP_ID = "customer_mealgroup_id";
	public static final String CUSTOMER_MEALGROUP_PARENTID = "customer_mealgroup_parentid";
	public static final String CUSTOMER_MEALGROUP_NAME = "customer_mealgroup_name";
	public static final String CUSTOMER_MEALGROUP_CONDITIONID= "customer_mealgroup_conditionid";
	public static final String CUSTOMER_MEALGROUP_CONDITIONNAME = "customer_mealgroup_conditionname";
	public static final String CUSTOMER_MEALGROUP_CONDITIONFILTERSTATUS = "customer_mealgroup_conditionfilter";
	public static final String CUSTOMER_MEALGROUP_CONDITIONMIN = "customer_mealgroup_conditionmin";
	public static final String CUSTOMER_MEALGROUP_CONDITIONMAX= "customer_mealgroup_conditionmax";
	public static final String CUSTOMER_MEALGROUP_CONDITIONSELECTALL= "customer_mealgroup_conditionselectall";

	// Meal PRODUCT table ends here
	public static final String CUSTOMER_MEAL_PRODUCT= "customer_product";
	public static final String CUSTOMER_MEAL_PRODUCTTABLE= "customer_producttable";
	public static final String CUSTOMER_MEAL_PRODUCTID = "customer_productid";
	public static final String CUSTOMER_MEAL_PRODUCTNAME = "customer_product_name";
	public static final String CUSTOMER_MEAL_PRODUCTQTY = "customer_product_qty";
	public static final String CUSTOMER_MEAL_PRODUCT_MEALGROUPID= "customer_product_mealgroupid";
	public static final String CUSTOMER_MEAL_PRODUCT_MEALID = "customer_product_mealid";



	// Meal custom Addons table
	public static final String CUSTOMER_MEAL_ADDON_TABLE = " customer_meal_addon";
	public static final String CUSTOMER_ORIGINAL_MEAL_ADDON_TABLE = " customer_original_meal_addon";
	public static final String CUSTOMER_MEAL_ADDON_ID = "customer_meal_addon_id";
	public static final String CUSTOMER_MEAL_ADDON__PRICE = "customer_meal_addon_price";
	public static final String CUSTOMER_MEAL_ADDON__QTY = "customer_meal_addon_qty";
	public static final String CUSTOMER_MEAL_ADDON_NAME = "customer_meal_addon_name";
	public static final String CUSTOMER_MEAL_ADDON_MEALID = "customer_meal_addon_mealid";
	public static final String CUSTOMER_MEAL_ADDON_GROUPID = "customer_meal_addon_gorupid";
	public static final String CUSTOMER_MEAL_ADDON_PRODUCTID= "customer_meal_addon_productid";



	
	public static final String CUSTOM_PRODUCT_ADD_ON_TABLE = " custom_product_add_on";
	public static final String CUSTOMER_PRODUCT_TABLE = "customer_product";
	public static final String CUSTOMER_PRODUCT_ADD_ON_TABLE = " customer_addon";
	
	public static final String PRODUCT_IMAGE="product_img";
	public static final String PRODUCT_SLIDER_IMAGE="product_img_slide";
	public static final String PRODUCT_DESC="product_Des";



	public static final String TB_DRIVER = " driver_table";
	public static final String DRIVER_NAME = "driver_name";
	public static final String DRIVER_ID = " driver_id";
	public static final String DRIVER_EMAIL="driver_email";

}

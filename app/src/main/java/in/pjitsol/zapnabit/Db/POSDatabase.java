package in.pjitsol.zapnabit.Db;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Util.Logger;
import in.pjitsol.zapnabit.Util.Util;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class POSDatabase extends SQLiteOpenHelper implements DBConstants {
	private static POSDatabase mDatabase;

	private SQLiteDatabase mDb;

	private static final String TAG = "POSDATABASE";

	private POSDatabase(Context context, int versionCode) {
		super(context, DB_NAME, null, versionCode);

	}

	public static final POSDatabase getInstance(Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			Logger.d("versionCode", ":-//" + pInfo.versionCode);
			mDatabase = new POSDatabase(context, pInfo.versionCode);
			// Logger.d("versionCode", ":-//" + mDatabase.getDatabaseName());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDatabase.getWritableDatabase();
		return mDatabase;
	}

	public static final POSDatabase getInstanceprinter(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceprinterFetch(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstancenotifcation(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceprintqueues(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceprintNotification(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceReprint(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceorder(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getDataHistory(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getCashierInstanceorder(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getCashierFetchInstanceorder(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceMenu(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceLogin(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceSync(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	public static final POSDatabase getInstanceBugReport(Context context) {
		if (mDatabase == null) {
			try {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				mDatabase = new POSDatabase(context, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		Logger.i(TAG, "oncreate tables");
		// create table
		String[] createStatements = getCreatetableStatements();
		int total = createStatements.length;
		for (int i = 0; i < total; i++) {
			Logger.i(TAG, "executing create query " + createStatements[i]);
			arg0.execSQL(createStatements[i]);
		}

		// create index
		String[] indexStatements = getIndexStatementsOnCreate();
		total = indexStatements.length;
		for (int i = 0; i < total; i++) {
			Logger.i(TAG, "executing create index query " + indexStatements[i]);
			arg0.execSQL(indexStatements[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.d("Applicationversion", ":oldVersion:" + oldVersion
				+ ":newVersion:" + newVersion);
		

	}

	private String[] getCreatetableStatements() {
		String[] create = new String[28];
		// category table -> _id , cat_id, cat_name, has_sub_cat , parent_cat_id
		String CATEGORY_TABLE_ST = CREATE_TABLE_BASE + CATEGORY_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + CATEGORY_ID + INTEGER + COMMA + CATEGORY_NAME + TEXT
				+ COMMA + CATEGORY_NAME_EN + TEXT 
				+ COMMA + CATEGORY_IMAGE + TEXT 
				+ COMMA + CATEGORY_SLIDER_IMAGE + TEXT 
				+ COMMA + CATEGORY_DESC + TEXT 
				+ COMMA + CATEGORY_STATUS + TEXT 
				+ COMMA + CATEGORY_HAS_SUB_CAT + INTEGER + " DEFAULT 0 "
				+ COMMA + CATEGORY_PARENT_CAT_ID + INTEGER + " DEFAULT 0"
				+ COMMA + CATEGORY_VAT + INTEGER + COMMA + CAT_DATE_TIME + TEXT
				+ COMMA + CAT_SYNC + TEXT + COMMA + CATEGORY_HAS_PRODUCT
				+ INTEGER + COMMA + CAT_USER_ID + TEXT + COMMA
				+ CAT_RESTOUNIQEID + TEXT + FINISH_COLUMN;
		// category table
		create[0] = CATEGORY_TABLE_ST;
		// product table -> product_id , product_name , price , total_price ,
		// currency , cat_id
		String PRODUCT_TABLE_ST = CREATE_TABLE_BASE + PRODUCT_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + PRODUCT_ID + INTEGER + COMMA + PRODUCT_NAME + TEXT
				+ COMMA + PRODUCT_NAME_EN + TEXT 
				+ COMMA + PRODUCT_DESC + TEXT
				+ COMMA + PRODUCT_STATUS + TEXT
				+ COMMA + PRODUCT_TYPE + TEXT
				+ COMMA + PRODUCT_AMOUNT + TEXT + COMMA + PRODUCT_NET_PRICE
				+ TEXT + COMMA + PRODUCT_GROSS_PRICE + TEXT + COMMA
				+ PRODUCT_CATEGORY_ID + INTEGER + COMMA + PRODUCT_NUM + TEXT
				+ COMMA + PRODUCT_VAT + INTEGER + COMMA + PRODUCT_DATE_TIME
				+ TEXT + COMMA + PRODUCT_SYNC + TEXT + COMMA
				+ PRODUCT_HAS_ADD_ON + INTEGER + COMMA + PRODUCT_USER_ID + TEXT
				+ COMMA + PRODUCT_QTY + TEXT 
				+ COMMA + PRODUCT_IMAGE + TEXT 
				+ COMMA + PRODUCT_SLIDER_IMAGE + TEXT 
				+ COMMA + PRODUCT_RESTOUNIQEID + TEXT + FINISH_COLUMN;
		create[1] = PRODUCT_TABLE_ST;

		// product table ends here

		// product add on -> id , product_id , name, name_json, price,
		// total_price
		String ADD_ON_TABLE_ST = CREATE_TABLE_BASE + PRODUCT_ADD_ON_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + ADD_ON_ID + INTEGER + COMMA + ADD_ON_NAME + TEXT
				+ COMMA + ADD_ON_NET_PRICE + TEXT + COMMA + ADD_ON_GROSS_PRICE
				+ TEXT + COMMA + ADD_ON_VAT + INTEGER + COMMA
				+ ADD_ON_DATE_TIME + TEXT + COMMA + ADD_ON_SYNC + TEXT + COMMA
				+ ADD_ON_PRODUCT_ID + TEXT + COMMA + ADD_ON_USER_ID + TEXT
				+ COMMA + PRODUCT_QTY + TEXT 
				+ COMMA + ADD_ON_TYPE + TEXT
				+ COMMA + ADD_ON_RESTOUNIQEID + TEXT 
				+ FINISH_COLUMN;
		create[2] = ADD_ON_TABLE_ST;

		String POS_USER_TYPE_ST = CREATE_TABLE_BASE + USER_TABLE_BASE
				+ START_COLUMN + USR_USERID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + USR_USERNAME + TEXT + COMMA
				+ USR_USEREMAILID + TEXT + COMMA + USR_PASSWORD + TEXT + COMMA
				+ USR_USERDESIGNATION + TEXT + COMMA + USR_USERREGDATE + TEXT
				+ COMMA + USR_USEREDITDATE + TEXT + COMMA + USR_USERADDRESS1
				+ TEXT + COMMA + USR_USERADDRESS + TEXT + COMMA
				+ USR_TOTALEMPLOYEE + INTEGER + COMMA + USR_RESTOFULLNAME
				+ TEXT + COMMA + USR_RESTOUNIQENAME + TEXT + COMMA
				+ USR_USERCOMPANY + TEXT + COMMA + USR_SERVICECHARGE + INTEGER
				+ COMMA + USR_USERPHONE + TEXT + COMMA + USR_LOGINBY + TEXT
				+ COMMA + USR_LANG + TEXT + COMMA + USR_CITY + TEXT + COMMA
				+ USR_PROVINCE + TEXT + COMMA + USR_COUNTRY + TEXT + COMMA
				+ USR_COUNTY + TEXT + COMMA + USR_POSTCODE + TEXT + COMMA
				+ USR_VAT + TEXT + COMMA + USR_VAT_PERCENT + TEXT + COMMA
				+ USR_RESTOOPEN + TEXT + COMMA + USR_RESTOCLOSE + TEXT + COMMA
				+ USR_WEBSITE1 + TEXT + COMMA + USR_WEBSITE2 + TEXT + COMMA
				+ USR_WEBSITE3 + TEXT + COMMA + USR_WEBSITE4 + TEXT + COMMA
				+ USR_SERVICES + TEXT + COMMA + USR_APPTYPE + TEXT + COMMA
				+ USR_IMAGE + BLOB + COMMA + USR_SYNC + TEXT + COMMA
				+ USR_PRINTERSETTING + TEXT + COMMA + USR_PRINTERFONT + TEXT
				+ COMMA + USR_IPADDRESS + TEXT + COMMA + USR_DEVICE_ID + TEXT
				+ COMMA + USR_GCM_REGKEY + TEXT + FINISH_COLUMN;
		create[3] = POS_USER_TYPE_ST;

		String POS_RESTUARANT_USER_ST = CREATE_TABLE_BASE + STAFF_TABLE_BASE
				+ START_COLUMN + STF_STAFFID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + STF_STAFFUSERID + INTEGER + COMMA
				+ STF_STAFFUSERRESTOUNIQEID + TEXT + COMMA + STF_STAFFPOSTION
				+ TEXT + COMMA + STF_STAFFUSEREMAIL + TEXT + COMMA
				+ STF_STAFFUSERNAME + TEXT + COMMA + STF_STAFFUSERPASSWORD
				+ TEXT + COMMA + STF_STAFFREGDATE + TEXT + COMMA
				+ STF_STAFFEDITDATE + TEXT + COMMA + STF_STAFFPAYMENT + INTEGER
				+ COMMA + STF_STAFFORDERHISTORY + INTEGER + COMMA
				+ STF_STAFFDATAREPORT + TEXT + COMMA + STF_STAFF_PRINT_RECEIPT
				+ TEXT + COMMA + STF_STAFFMANAGEPRODUCT + INTEGER + COMMA
				+ STF_STAFFTAKEORDER + INTEGER + COMMA + STF_STAFF_SERVERID
				+ TEXT + COMMA + STF_STAFFADMINTRATIONMANAGEMENT + INTEGER
				+ COMMA + USR_SYNC + TEXT + COMMA + STF_STAFFPRINTERSETTING
				+ TEXT + COMMA + STF_STAFF_IP_ADDRESS + TEXT + COMMA
				+ TEXT + COMMA + STF_STAFF_PINCODE + TEXT + COMMA
				+ STF_STAFF_STATUS + INTEGER + " DEFAULT 0 " + COMMA
				+ STF_STAFF_NOTIFICATION_ACTIVATED + TEXT + FINISH_COLUMN;

		create[4] = POS_RESTUARANT_USER_ST;
		String MANAGE_TABLE = CREATE_TABLE_BASE + MANAGE_TABLE_TABLENUMBERS
				+ START_COLUMN + TABLE_TABLEID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + TABLE_TABLENUMBER + TEXT + COMMA
				+ TABLE_LOGINNAME + TEXT + COMMA + TABLE_TABLETSTATUS + TEXT
				+ COMMA + TABLE_SERVERID + TEXT + COMMA
				+ TABLE_RESTTOUNIQUENAME + TEXT + COMMA + TABLE_SYNC + TEXT
				+ FINISH_COLUMN;
		create[5] = MANAGE_TABLE;

		String CUSTOMERDETAILS_TABLE = CREATE_TABLE_BASE
				+ CUSTOMERDETAILS_TABLE_BASE + START_COLUMN + CUD_ORDER_ID
				+ TEXT + COMMA + CUD_GROSSPRICE + TEXT + COMMA
				+ CUD_DISCOUNTPER + TEXT + COMMA + CUD_DISCOUNTTOTAL + TEXT
				+ COMMA + CUD_VATNO + TEXT + COMMA + CUD_VATTOTAL + TEXT
				+ COMMA + CUD_NETPRICE + TEXT + COMMA + CUD_TOTALAMOUNT + TEXT
				+ COMMA + CUD_LOGINNAME + TEXT + COMMA + CUD_RESTOUNIQENAME
				+ TEXT + COMMA + CUD_PAYMENTMODE + TEXT + COMMA
				+ CUD_PAYMENTSTATUS + INTEGER + " DEFAULT 0 " + COMMA
				+ CUD_TABLENO + TEXT + COMMA + CUD_ORDERDATE + TEXT + COMMA
				+ CUD_CUSTOMERORDERHISTORY + TEXT + COMMA + CUD_PAYMENTDATE
				+ TEXT + COMMA + CUD_STATUS + INTEGER + " DEFAULT 0 " + COMMA
				+ CUD_POSTCODE + TEXT + COMMA + CUD_ADDRESS + TEXT + COMMA
				+ CUD_PHONENUMBER + TEXT + COMMA + CUD_DISTENCE + TEXT + COMMA
				+ CUD_USERNAME + TEXT + COMMA + CUD_TITLE + TEXT + COMMA
				+ CUD_POSTSATUS + INTEGER + COMMA + CUD_FEE + TEXT + COMMA
				+ CUD_USERAMOUNT + TEXT + COMMA + CUD_USER_CASHAMOUNT + TEXT
				+ COMMA + CUD_USER_CARDAMOUNT + TEXT + COMMA + CUD_USERCHANGE
				+ TEXT + COMMA + CUD_LEADTIME + TEXT + COMMA
				+ CUD_SERVICE_CHARGE + TEXT + COMMA + CUD_DEVICE_TYPE + TEXT
				+ COMMA + CUD_ORDER_TYPE + TEXT + COMMA + CUD_DECLINE_REASON
				+ TEXT + COMMA + CUD_CARD_NUMBER + TEXT + COMMA + CUD_SERVERID
				+ TEXT + COMMA + CUD_CARD_NAME + TEXT + COMMA + CUD_SYNC + TEXT
				+ FINISH_COLUMN;
		create[6] = CUSTOMERDETAILS_TABLE;

		String TB_VERSION_TABLE_COMMAND = CREATE_TABLE_BASE + TB_VERSION_TABLE
				+ START_COLUMN + TB_VERSION_ID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + TB_VERSION_RESTOUNIQENAME + TEXT
				+ COMMA + TB_VERSION_USER_TABLE + TEXT + COMMA
				+ TB_VERSION_STAFF_TABLE + TEXT + COMMA
				+ TB_VERSION_MANAGE_TABLE + TEXT + COMMA
				+ TB_VERSION_CUSTOMERDETAILS_TABLE + TEXT + COMMA
				+ TB_VERSION_MENUPRODUCT + TEXT + COMMA + TB_VERSION_DB_VER
				+ TEXT + FINISH_COLUMN;
		create[7] = TB_VERSION_TABLE_COMMAND;

		String TB_PRINTER_COMMAND = CREATE_TABLE_BASE + TB_PRINTER_TABLE
				+ START_COLUMN + TB_PRINTER_ID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + TB_PRINTER_TITLE + TEXT + COMMA
				+ TB_PRINTER_VALUE + TEXT + COMMA + TB_PRINTER_USERSETTINGS
				+ TEXT + COMMA + TB_PRINTER_DRAWEROPEN + INTEGER + COMMA
				+ TB_PRINTER_OPENDATETIME + TEXT + COMMA
				+ TB_PRINTER_CLOSEDATETIME + TEXT + COMMA
				+ TB_PRINTER_CURRENTSTATUS + INTEGER + COMMA
				+ TB_PRINTER_RESTOUNIQENAME + TEXT + COMMA
				+ TB_PRINTER_ERROR_MESSAGE + TEXT + FINISH_COLUMN;

		create[8] = TB_PRINTER_COMMAND;

		String SOLDPRODUCTTABLE = CREATE_TABLE_BASE + TB_SOLDPRODUCTTABLE
				+ START_COLUMN + CUD_SOLDID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + CUD_PRODUCTID + TEXT + COMMA
				+ CUD_ORDER_ID + TEXT + COMMA + CUD_PRODUCTNAME + TEXT + COMMA
				+ CUD_PRODUCTPARENTID + TEXT + COMMA + CUD_PRODUCTNETPRICE
				+ TEXT + COMMA + CUD_PRODUCTQTY + INTEGER + COMMA
				+ CUD_PRODUCTGROSSPRICE + TEXT + FINISH_COLUMN;

		create[9] = SOLDPRODUCTTABLE;

		String TB_REPRINTER_COMMAND = CREATE_TABLE_BASE + TB_REPRINTQUEUE_TABLE
				+ START_COLUMN + TB_REPRINTQUEUE_ID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + TB_REPRINTQUEUE_USER + TEXT + COMMA
				+ TB_REPRINTQUEUE_VALUE + INTEGER + COMMA
				+ TB_PRINTER_RESTOUNIQENAME + TEXT + COMMA
				+ TB_REPRINTQUEUE_STATUS + INTEGER + FINISH_COLUMN;
		create[10] = TB_REPRINTER_COMMAND;

		String TB_NOTIFICATION_COMMAND = CREATE_TABLE_BASE
				+ TB_NOTIFICATION_TABLE + START_COLUMN + TB_NOTIFICATION_ID
				+ INTEGER + PRIMARY_KEY + AUTO_ICNREMENT + COMMA
				+ TB_NOTIFICATION_MESSAGE + TEXT + COMMA
				+ TB_NOTIFICATION_DETAIL + TEXT + COMMA
				+ TB_NOTIFICATION_RESTOUNIQENAME + TEXT + COMMA
				+ TB_NOTIFICATION_DEVICETYPE + TEXT + COMMA
				+ TB_NOTIFICATION_STATUS + TEXT + COMMA
				+ TB_NOTIFICATION_PHONENUMBER + TEXT + COMMA
				+ TB_NOTIFICATION_ORDER_TYPE + TEXT + COMMA
				+ TB_NOTIFICATION_ORDER_STATUS + TEXT + FINISH_COLUMN;
		create[11] = TB_NOTIFICATION_COMMAND;

		String TB_SYNCHRONIZATION_COMMAND = CREATE_TABLE_BASE
				+ TB_SYNC_TABLE_BASE + START_COLUMN + TB_SYNC_ID + INTEGER
				+ PRIMARY_KEY + AUTO_ICNREMENT + COMMA + TB_SYNC_TABLENAME
				+ TEXT + COMMA + TB_SYNC_STATUS + TEXTNOTNULL + " N "
				+ FINISH_COLUMN;
		create[12] = TB_SYNCHRONIZATION_COMMAND;
		
		String CUSTOM_ADD_ON_TABLE_ST = CREATE_TABLE_BASE + CUSTOM_PRODUCT_ADD_ON_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + ADD_ON_ID + INTEGER + COMMA + ADD_ON_NAME + TEXT
				+ COMMA + ADD_ON_NET_PRICE + TEXT + COMMA + ADD_ON_GROSS_PRICE
				+ TEXT + COMMA + ADD_ON_VAT + INTEGER + COMMA
				+ ADD_ON_DATE_TIME + TEXT + COMMA + ADD_ON_SYNC + TEXT + COMMA
				+ ADD_ON_PRODUCT_ID + TEXT + COMMA + ADD_ON_USER_ID + TEXT
				+ COMMA + PRODUCT_QTY + TEXT 
				+ COMMA + ADD_ON_TYPE + TEXT 
				+ COMMA + ADD_ON_RESTOUNIQEID + TEXT 
				+ FINISH_COLUMN;
		create[13] = CUSTOM_ADD_ON_TABLE_ST;

		String MEAL_TABLE_ST = CREATE_TABLE_BASE + MEAL_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + MEAL_ID + INTEGER + COMMA + MEAL_NAME + TEXT
				+ COMMA + MEAL_PRICE + TEXT + COMMA + MEAL_DESCRIPTION
				+ TEXT + COMMA + MEAL_NAME_EN + TEXT 
				+ COMMA + MEAL_QTY + INTEGER 
				+ COMMA + MEAL_PARENTID + INTEGER 
				+ COMMA + MEAL_RESTOUNIQEID + TEXT
				+ COMMA + MEAL_IMAGE+ TEXT
				+ COMMA + MEAL_SLIDER_IMAGE+ TEXT
				+ FINISH_COLUMN;
		create[14] = MEAL_TABLE_ST;
		String MEAL_GROUPTABLE_ST = CREATE_TABLE_BASE + MEALGROUP_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + MEALGROUP_ID + INTEGER + COMMA + MEALGROUP_NAME + TEXT
				+ COMMA + MEALGROUP_CONDITIONID+ TEXT + COMMA + MEALGROUP_CONDITIONFILTERSTATUS
				+ TEXT + COMMA + MEALGROUP_CONDITIONMIN + TEXT 
				+ COMMA + MEALGROUP_CONDITIONMAX + TEXT 
				+ COMMA + MEALGROUP_CONDITIONNAME + TEXT
				+ COMMA + MEALGROUP_CONDITIONSELECTALL + TEXT
				+ COMMA + MEALGROUP_PARENTID + INTEGER
				+ FINISH_COLUMN;
		create[15] = MEAL_GROUPTABLE_ST;

		String MEAL_PRODUCT_ST = CREATE_TABLE_BASE + MEAL_PRODUCTTABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + MEAL_PRODUCTID + INTEGER 
				+ COMMA + MEAL_PRODUCTNAME + TEXT
				+ COMMA + MEAL_PRODUCTQTY+ INTEGER
				+ COMMA + MEAL_PRODUCT_MEALGROUPID + INTEGER 
				+ COMMA + MEAL_PRODUCT_MEALID + INTEGER 
				+ FINISH_COLUMN;
		create[16] = MEAL_PRODUCT_ST;


		String CUTOMER_MEAL_TABLE_ST = CREATE_TABLE_BASE + CUSTOMER_MEAL_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + CUSTOMER_MEAL_ID + INTEGER + COMMA + CUSTOMER_MEAL_NAME + TEXT
				+ COMMA + CUSTOMER_MEAL_PRICE + TEXT + COMMA + CUSTOMER_MEAL_DESCRIPTION
				+ TEXT + COMMA + CUSTOMER_MEAL_NAME_EN + TEXT 
				+ COMMA + CUSTOMER_MEAL_QTY + INTEGER 
				+ COMMA + CUSTOMER_MEAL_RESTOUNIQEID + TEXT 
				+ FINISH_COLUMN;
		create[17] = CUTOMER_MEAL_TABLE_ST;
		String CUTOMER_MEAL_GROUPTABLE_ST = CREATE_TABLE_BASE + CUSTOMER_MEALGROUP_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + CUSTOMER_MEALGROUP_ID + INTEGER + COMMA + 
				CUSTOMER_MEALGROUP_NAME + TEXT
				+ COMMA + CUSTOMER_MEALGROUP_CONDITIONID+ TEXT +
				COMMA + CUSTOMER_MEALGROUP_CONDITIONFILTERSTATUS
				+ TEXT + COMMA + CUSTOMER_MEALGROUP_CONDITIONMIN + TEXT 
				+ COMMA + CUSTOMER_MEALGROUP_CONDITIONMAX + TEXT 
				+ COMMA + CUSTOMER_MEALGROUP_CONDITIONNAME + TEXT
				+ COMMA + CUSTOMER_MEALGROUP_CONDITIONSELECTALL + TEXT
				+ COMMA + CUSTOMER_MEALGROUP_PARENTID + INTEGER
				+ FINISH_COLUMN;
		create[18] = CUTOMER_MEAL_GROUPTABLE_ST;

		String CUTOMER_MEAL_PRODUCT_ST = CREATE_TABLE_BASE + CUSTOMER_MEAL_PRODUCTTABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + CUSTOMER_MEAL_PRODUCTID + INTEGER 
				+ COMMA + CUSTOMER_MEAL_PRODUCTNAME + TEXT
				+ COMMA + CUSTOMER_MEAL_PRODUCTQTY+ INTEGER
				+ COMMA + CUSTOMER_MEAL_PRODUCT_MEALGROUPID + INTEGER 
				+ COMMA + CUSTOMER_MEAL_PRODUCT_MEALID + INTEGER 
				+ FINISH_COLUMN;
		create[19] = CUTOMER_MEAL_PRODUCT_ST;
		
		
		String MEAL_ADDON_ST = CREATE_TABLE_BASE + CUSTOMER_MEAL_ADDON_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + CUSTOMER_MEAL_ADDON_ID + INTEGER 
				+ COMMA + CUSTOMER_MEAL_ADDON_NAME + TEXT
				+ COMMA + CUSTOMER_MEAL_ADDON__QTY+ INTEGER
				+ COMMA + CUSTOMER_MEAL_ADDON__PRICE + TEXT 
				+ COMMA + CUSTOMER_MEAL_ADDON_MEALID + INTEGER 
				+ FINISH_COLUMN;
		create[20] = MEAL_ADDON_ST;
		
		String MEAL_GROUPADDON_ST = CREATE_TABLE_BASE + MEAL_ADDON
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + MEAL_ADDONID + INTEGER 
				+ COMMA + MEAL_ADDONNAME + TEXT
				+ COMMA + MEAL_ADDON_PARENTGRID+ INTEGER
				+ COMMA + MEAL_ADDON_PARENTMEALID + INTEGER 
				+ COMMA + MEAL_ADDONPRICE + TEXT 
				+ FINISH_COLUMN;
		create[21] = MEAL_GROUPADDON_ST;
		
		String ORIGINAL_MEAL_ADDON_ST = CREATE_TABLE_BASE + CUSTOMER_ORIGINAL_MEAL_ADDON_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + CUSTOMER_MEAL_ADDON_ID + INTEGER 
				+ COMMA + CUSTOMER_MEAL_ADDON_NAME + TEXT
				+ COMMA + CUSTOMER_MEAL_ADDON__QTY+ INTEGER
				+ COMMA + CUSTOMER_MEAL_ADDON__PRICE + TEXT 
				+ COMMA + CUSTOMER_MEAL_ADDON_MEALID + INTEGER 
				+ COMMA + CUSTOMER_MEAL_ADDON_GROUPID + INTEGER
				+ COMMA + CUSTOMER_MEAL_ADDON_PRODUCTID + INTEGER
				+ FINISH_COLUMN;
		create[22] = ORIGINAL_MEAL_ADDON_ST;
		
		String CUSTOMER_PRODUCT_TABLE_ST = CREATE_TABLE_BASE + CUSTOMER_PRODUCT_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + PRODUCT_ID + INTEGER + COMMA + PRODUCT_NAME + TEXT
				+ COMMA + PRODUCT_NAME_EN + TEXT
				+ COMMA + PRODUCT_DESC + TEXT
				+ COMMA + PRODUCT_STATUS + TEXT
				+ COMMA + PRODUCT_AMOUNT + TEXT + COMMA + PRODUCT_NET_PRICE
				+ TEXT + COMMA + PRODUCT_GROSS_PRICE + TEXT + COMMA
				+ PRODUCT_CATEGORY_ID + INTEGER + COMMA + PRODUCT_NUM + TEXT
				+ COMMA + PRODUCT_VAT + INTEGER + COMMA + PRODUCT_DATE_TIME
				+ TEXT + COMMA + PRODUCT_SYNC + TEXT + COMMA
				+ PRODUCT_HAS_ADD_ON + INTEGER + COMMA + PRODUCT_USER_ID + TEXT
				+ COMMA + PRODUCT_QTY + TEXT
				+ COMMA + PRODUCT_IMAGE + TEXT
				+ COMMA + PRODUCT_SLIDER_IMAGE + TEXT
				+ COMMA + TABLE_TABLEID + TEXT
				+ COMMA + PRODUCT_RESTOUNIQEID + TEXT + FINISH_COLUMN;
		create[23] = CUSTOMER_PRODUCT_TABLE_ST;

		// product table ends here

		// product add on -> id , product_id , name, name_json, price,
		// total_price
		String CUSTOMER_ADD_ON_TABLE_ST = CREATE_TABLE_BASE + CUSTOMER_PRODUCT_ADD_ON_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + ADD_ON_ID + INTEGER + COMMA + ADD_ON_NAME + TEXT
				+ COMMA + ADD_ON_NET_PRICE + TEXT + COMMA + ADD_ON_GROSS_PRICE
				+ TEXT + COMMA + ADD_ON_VAT + INTEGER + COMMA
				+ ADD_ON_DATE_TIME + TEXT + COMMA + ADD_ON_SYNC + TEXT + COMMA
				+ ADD_ON_PRODUCT_ID + TEXT + COMMA + ADD_ON_USER_ID + TEXT
				+ COMMA + PRODUCT_QTY + TEXT
				+ COMMA + ADD_ON_TYPE + TEXT
				+ COMMA + TABLE_TABLEID + TEXT
				+ COMMA + ADD_ON_RESTOUNIQEID + TEXT
				+ FINISH_COLUMN;
		create[24] = CUSTOMER_ADD_ON_TABLE_ST;



		String ATTRIBUTE_TABLE_ST = CREATE_TABLE_BASE + PRODUCT_ATTRIBUTES_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + ATTRIBUTE_ID
				+ TEXT + COMMA + ATTRIBUTE_NAME + TEXT
				+ COMMA + ATTRIBUTE_CONDITION
				+ COMMA + ATTRIBUTE_DISPLAYORDER + TEXT
				+ COMMA + ATTRIBUTE_PRODUCT_ID + TEXT
				+ FINISH_COLUMN;
		create[25] = ATTRIBUTE_TABLE_ST;

		String ATTRIBUTE_OPTION_TABLE_ST = CREATE_TABLE_BASE + PRODUCT_ATTRIBUTESOPTION_TABLE
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT
				+ COMMA + ATTRIBUTESOPTION_ID
				+ TEXT + COMMA + ATTRIBUTESOPTION_NAME + TEXT
				+ COMMA + ATTRIBUTESOPTION_PRODUCT_ID + TEXT
				+ COMMA + ATTRIBUTESOPTION_PRICE + TEXT
				+ COMMA + PRODUCT_QTY + TEXT
				+ FINISH_COLUMN;
		create[26] = ATTRIBUTE_OPTION_TABLE_ST;


		String TB_DRIVER_DB= CREATE_TABLE_BASE + TB_DRIVER
				+ START_COLUMN + _ID + INTEGER + PRIMARY_KEY
				+ AUTO_ICNREMENT + COMMA + DRIVER_NAME + TEXT + COMMA
				+ DRIVER_ID + TEXT + COMMA
				+ DRIVER_EMAIL + INTEGER + FINISH_COLUMN;
		create[27] = TB_DRIVER_DB;
		return create;
	}

	private String[] getIndexStatementsOnCreate() {
		String[] index = new String[9];
		// product table index starts here -> product_id and product_cat_id
		// product table index starts here -> product_id and product_cat_id
		String PRODUCT_TABLE_ID_INDEX_ST = CREATE_INDEX_BASE
				+ PRODUCT_TABLE_ID_INDEX + ON + PRODUCT_TABLE + START_COLUMN
				+ PRODUCT_ID + FINISH_COLUMN;
		String PRODUCT_TABLE_CAT_ID_INDEX_ST = CREATE_INDEX_BASE
				+ PRODUCT_TABLE_CAT_ID_INDEX + ON + PRODUCT_TABLE
				+ START_COLUMN + PRODUCT_CATEGORY_ID + FINISH_COLUMN;
		String USER_TABLE_ID_INDEX_ST = CREATE_INDEX_BASE + USER_TABLE_ID_INDEX
				+ ON + USER_TABLE_BASE + START_COLUMN + USR_USERID
				+ FINISH_COLUMN;
		String STAFF_TABLE_CAT_ID_INDEX_ST = CREATE_INDEX_BASE
				+ STAFF_TABLE_ID_INDEX + ON + STAFF_TABLE_BASE + START_COLUMN
				+ STF_STAFFID + FINISH_COLUMN;

		String TABLE_TABLE_TABLE_ID_INDEX_ST = CREATE_INDEX_BASE
				+ TABLE_TABLE_ID_INDEX + ON + MANAGE_TABLE_TABLENUMBERS
				+ START_COLUMN + TABLE_TABLEID + FINISH_COLUMN;

		String CUSTOMERDETAIL_TABLE_ID_INDEX_ST = CREATE_INDEX_BASE
				+ CUSTOMERDETAIL_TABLE_ID_INDEX + ON
				+ CUSTOMERDETAILS_TABLE_BASE + START_COLUMN + CUD_ORDER_ID
				+ FINISH_COLUMN;

		String PRINTER_TABLE_ID_INDEX_ST = CREATE_INDEX_BASE
				+ PRINTER_TABLE_ID_INDEX + ON + TB_PRINTER_TABLE + START_COLUMN
				+ TB_PRINTER_ID + FINISH_COLUMN;

		String REPRINT_TABLE_ID_INDEX_ST = CREATE_INDEX_BASE
				+ REPRINTQUEUE_TABLE_ID_INDEX + ON + TB_REPRINTQUEUE_TABLE
				+ START_COLUMN + TB_REPRINTQUEUE_ID + FINISH_COLUMN;

		String NOTIFICATION_ID_INDEX_ST = CREATE_INDEX_BASE
				+ NOTIFICATION_TABLE_ID_INDEX + ON + TB_NOTIFICATION_TABLE
				+ START_COLUMN + TB_NOTIFICATION_ID + FINISH_COLUMN;

		index[0] = PRODUCT_TABLE_ID_INDEX_ST;
		index[1] = PRODUCT_TABLE_CAT_ID_INDEX_ST;
		index[2] = USER_TABLE_ID_INDEX_ST;
		index[3] = STAFF_TABLE_CAT_ID_INDEX_ST;
		index[4] = TABLE_TABLE_TABLE_ID_INDEX_ST;
		index[5] = CUSTOMERDETAIL_TABLE_ID_INDEX_ST;
		index[6] = PRINTER_TABLE_ID_INDEX_ST;
		index[7] = REPRINT_TABLE_ID_INDEX_ST;
		index[8] = NOTIFICATION_ID_INDEX_ST;
		return index;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {

		return mDb != null ? mDb : (mDb = super.getWritableDatabase());
	}
}

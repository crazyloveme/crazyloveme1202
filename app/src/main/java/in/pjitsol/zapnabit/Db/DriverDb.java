package in.pjitsol.zapnabit.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Driver;
import in.pjitsol.zapnabit.Network.ResultMessage;


/**
 * Created by Bhawna on 5/31/2017.
 */

public class DriverDb implements DBConstants {

    private static DriverDb obj = null;

    public synchronized static DriverDb obj() {
        if (obj == null)
            obj = new DriverDb();
        return obj;
    }

    public void savePrefrenceeformation(Context context, String prefId,
                                      ContentValues cv, ResultMessage message) {

        String userTableQuery = "SELECT * FROM " + TB_DRIVER
                + " WHERE " + DRIVER_ID+ "='" + prefId + "'";
				/*+" AND "+
						DB_USERID+ "='" + userid + "'";*/

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor c = mdb.rawQuery(userTableQuery, null);
            if (c != null && c.getCount() > 0) {
                mdb.update(TB_DRIVER ,cv, DRIVER_ID + "='" + prefId
                        + "'", null);
                message.STATUS = StaticConstants.ASYN_RESULT_OK;
                message.TYPE = StaticConstants.DRIVER_SAVED;
            } else {
                mdb.insert(TB_DRIVER, null, cv);
                message.STATUS = StaticConstants.ASYN_RESULT_OK;
                message.TYPE = StaticConstants.DRIVER_SAVED;
            }
            c.close();
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

    }

    public ArrayList<Driver> getAllPrefrences(Context context){

        ArrayList<Driver> categories=new ArrayList<>();
        String userTableQuery = "SELECT * FROM " + TB_DRIVER;
				/*+ " WHERE " + USER_ID+ "='" + userID + "'";*/
				/*+" AND "+
						DB_USERID+ "='" + userId + "'";*/

        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        try {

            Cursor c = mdb.rawQuery(userTableQuery, null);
            if (c != null && c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        Driver item = new Driver();
                        item.driver_id = c
                                .getString(c
                                        .getColumnIndex(DRIVER_ID));
                        item.driver_name = c
                                .getString(c
                                        .getColumnIndex(DRIVER_NAME));
                        item.driver_email = c
                                .getString(c
                                        .getColumnIndex(DRIVER_EMAIL));
                        categories.add(item);
                    } while (c.moveToNext());
                }
            }
            c.close();
            mdb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mdb.endTransaction();
        }

        return categories;

    }





    public  void deleteAllData(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceLogin(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        mdb.execSQL("DELETE FROM " + TB_DRIVER);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();

    }
}

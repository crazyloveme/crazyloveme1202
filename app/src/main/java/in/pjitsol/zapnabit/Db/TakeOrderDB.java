package in.pjitsol.zapnabit.Db;

import in.pjitsol.zapnabit.Entity.Category;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.Entity.ProductItem;
import in.pjitsol.zapnabit.Util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


public class TakeOrderDB implements DBConstants {

	/*
     * private static TakeOrderDB obj = null;
	 * 
	 * public synchronized static TakeOrderDB obj() { if (obj == null) obj = new
	 * TakeOrderDB(); return obj; }
	 */

    public static boolean saveData(Context context, List<Item> list,
                                   boolean update) {
        int total = list.size();
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        List<ContentValues> categories = new ArrayList<ContentValues>();
        List<ContentValues> products = new ArrayList<ContentValues>();
        List<ContentValues> addOns = new ArrayList<ContentValues>();
        List<ContentValues> attributes = new ArrayList<ContentValues>();
        List<ContentValues> attriOptions = new ArrayList<ContentValues>();
        for (int i = 0; i < total; i++) {
            Item item = list.get(i);
            if (item instanceof Category) {
                Category c = (Category) item;
                addCategory(categories, products, addOns, c, mdb, update, context);
            } else if (item instanceof Product) {
                Product p = (Product) item;
                addProduct(products, addOns, p, mdb, update, context);
            } else if (item instanceof ProductAddOn) {
                ProductAddOn productAddOn = (ProductAddOn) item;
                addProductAddon(addOns, productAddOn, mdb, update);
            } else if (item instanceof ProductAtrribute) {
                ProductAtrribute productAddOn = (ProductAtrribute) item;
                addProductATtributes(attributes, productAddOn, mdb, update);
            } else if (item instanceof ProductAtrributeOption) {
                ProductAtrributeOption productAddOn = (ProductAtrributeOption) item;
                addProductATtributesOptions(attriOptions, productAddOn, mdb, update);
            }

        }
        int count = 0;
        mdb.beginTransaction();
        for (ContentValues cv : categories) {
            if (mdb.insert(CATEGORY_TABLE, null, cv) > 0)
                count++;
        }
        for (ContentValues cv : products) {
            if (mdb.insert(PRODUCT_TABLE, null, cv) > 0)
                count++;
        }
        for (ContentValues cv : addOns) {
            if (mdb.insert(PRODUCT_ADD_ON_TABLE, null, cv) > 0)
                count++;

        }

        for (ContentValues cv : attributes) {
            if (mdb.insert(PRODUCT_ATTRIBUTES_TABLE, null, cv) > 0)
                count++;

        }

        for (ContentValues cv : attriOptions) {
            if (mdb.insert(PRODUCT_ATTRIBUTESOPTION_TABLE, null, cv) > 0)
                count++;

        }

        mdb.setTransactionSuccessful();
        mdb.endTransaction();
        Log.i("db store", ":store:");
        if (count > 0)
            return true;
        else
            return false;
    }

    public static boolean saveData(Context context, List<Item> list) {
        int total = list.size();
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        List<ContentValues> catContentValues = new ArrayList<ContentValues>();
        List<ContentValues> proContentValues = new ArrayList<ContentValues>();
        List<ContentValues> addonContentValues = new ArrayList<ContentValues>();
        for (int i = 0; i < total; i++) {
            Item item = list.get(i);
            if (item instanceof Category) {
                Category c = (Category) item;
                addCategory(catContentValues, c, mdb, context, i);
            } else if (item instanceof Product) {
                Product product = (Product) item;
                addProduct(proContentValues, product, mdb, context);
            } else if (item instanceof ProductAddOn) {
                ProductAddOn productAddOn = (ProductAddOn) item;
                addProductAddon(addonContentValues, productAddOn, mdb, context);
            }
        }
        int count = 0;
        mdb.beginTransaction();
        for (ContentValues cv : catContentValues) {
            if (mdb.insert(CATEGORY_TABLE, null, cv) > 0)
                count++;
        }
        for (ContentValues cv : proContentValues) {
            if (mdb.insert(PRODUCT_TABLE, null, cv) > 0)
                count++;
        }
        for (ContentValues cv : addonContentValues) {
            if (mdb.insert(PRODUCT_ADD_ON_TABLE, null, cv) > 0)
                count++;
        }
        mdb.setTransactionSuccessful();
        mdb.endTransaction();
        if (count > 0)
            return true;
        else
            return false;
    }

    private static void addProductAddon(List<ContentValues> addonContentValues,
                                        ProductAddOn productAddOn, SQLiteDatabase mdb, Context context) {
        if (productAddOn.product != null) {
            Product product = (Product) productAddOn.product;
            ContentValues cvA = new ContentValues();
            cvA.put(ADD_ON_ID, product.id);
            try {
                cvA.put(ADD_ON_NAME, Util.nameCreate(product.name).toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cvA.put(ADD_ON_NET_PRICE, product.netamount);
            cvA.put(ADD_ON_GROSS_PRICE, product.grossamount);
            cvA.put(ADD_ON_PRODUCT_ID, product.categoryId);
            addonContentValues.add(cvA);
        }
    }

    private static void addProduct(List<ContentValues> proContentValues,
                                   Product p, SQLiteDatabase mdb, Context context) {
        ContentValues cv = new ContentValues();
        int increment = getlastAutoIncrement(context,
                DBConstants.CATEGORY_TABLE, mdb);
        cv.put(PRODUCT_CATEGORY_ID, (increment + p.categoryId));
        cv.put(PRODUCT_ID, p.id);
        try {
            cv.put(PRODUCT_NAME, Util.nameCreate(p.name).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cv.put(PRODUCT_NUM, p.productNum);
        cv.put(PRODUCT_NET_PRICE, p.netamount);
        cv.put(PRODUCT_GROSS_PRICE, p.grossamount);
        cv.put(PRODUCT_STATUS, p.status);
        cv.put(PRODUCT_HAS_ADD_ON, p.addOns != null ? 1 : 0);
        proContentValues.add(cv);
    }

    private static void addCategory(List<ContentValues> catContentValues,
                                    Category c, SQLiteDatabase mdb, Context context, int id) {
        ContentValues cv = new ContentValues();
        int increment = getlastAutoIncrement(context,
                DBConstants.CATEGORY_TABLE, mdb);
        cv.put(CATEGORY_ID, (increment + c.id));
        cv.put(CATEGORY_HAS_SUB_CAT, c.hasSubCategory ? 1 : 0);
        cv.put(CATEGORY_HAS_PRODUCT, c.hasProducts ? 1 : 0);
        try {
            cv.put(CATEGORY_NAME, Util.nameCreate(c.name).toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (id == 0)
            cv.put(CATEGORY_PARENT_CAT_ID, c.parentId);
        else
            cv.put(CATEGORY_PARENT_CAT_ID, (increment + c.parentId));
        catContentValues.add(cv);
    }

    public static int getlastAutoIncrement(Context context, String tableName) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        /*
		 * Cursor c =
		 * mdb.rawQuery("SELECT seq from SQLITE_SEQUENCE WHERE name='" +
		 * tableName + "'", null);
		 */
        Cursor c = mdb.rawQuery("Select _id from " + tableName
                + " order by _id DESC limit 1", null);
        if (c != null && c.moveToFirst())
            return Integer.parseInt(c.getString(0));
        else
            return 1;
    }

    public static int getlastAutoIncrement(Context context, String tableName,
                                           SQLiteDatabase mdb) {
        mdb = POSDatabase.getInstanceMenu(context).getWritableDatabase();
        Cursor c = mdb.rawQuery("Select _id from " + tableName
                + " order by _id DESC limit 1", null);
        if (c != null && c.moveToFirst())
            return Integer.parseInt(c.getString(0));
        else
            return 1;
    }

    private static void addCategory(List<ContentValues> categories,
                                    List<ContentValues> products, List<ContentValues> addOns,
                                    Category c, SQLiteDatabase mdb, boolean update, Context context) {
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_ID, c.id);
        cv.put(CATEGORY_HAS_SUB_CAT, c.hasSubCategory ? 1 : 0);
        cv.put(CATEGORY_HAS_PRODUCT, c.hasProducts ? 1 : 0);
        cv.put(CATEGORY_NAME, c.name);
        cv.put(CATEGORY_NAME_EN, c.name);
        cv.put(CATEGORY_PARENT_CAT_ID, c.parentId);
        cv.put(CATEGORY_IMAGE, c.productImage);
        cv.put(CATEGORY_SLIDER_IMAGE, c.productSliderImage);
        cv.put(CATEGORY_DESC, c.productDescription);
        cv.put(CATEGORY_STATUS, c.status);
        // cv.put(PRODUCT_QTY, 0);
        if (update) {
            if (updateCategory(cv, c.id, mdb)) {
                return;
            } else {
                categories.add(cv);
            }

        } else
            categories.add(cv);

        if (c.hasSubCategory && c.subCategories != null) {
            for (Category sub : c.subCategories) {
                addCategory(categories, products, addOns, sub, mdb, update, context);
            }
        }
        if (c.hasProducts && c.products != null) {

            for (Product subP : c.products) {
                addProduct(products, addOns, subP, mdb, update, context);
            }
        }
    }

    private static boolean updateCategory(ContentValues cv, int id,
                                          SQLiteDatabase mdb) {

        if (mdb.update(CATEGORY_TABLE, cv, CATEGORY_ID + " = " + id, null) > 0) {
            return true;
        } else
            return false;

    }

    private static void addProduct(List<ContentValues> products,
                                   List<ContentValues> addOns, Product p, SQLiteDatabase mdb,
                                   boolean update, Context context) {
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_CATEGORY_ID, p.categoryId);
        cv.put(PRODUCT_ID, p.id);
        cv.put(PRODUCT_NAME, p.name);
        cv.put(PRODUCT_NAME_EN, p.name);
        cv.put(PRODUCT_NUM, p.productNum);
        cv.put(PRODUCT_VAT, p.vat);
        cv.put(PRODUCT_NET_PRICE, p.netamount);
        cv.put(PRODUCT_GROSS_PRICE, p.grossamount);
        cv.put(PRODUCT_HAS_ADD_ON, p.addOns != null ? 1 : 0);
        cv.put(PRODUCT_QTY, 0);
        cv.put(PRODUCT_TYPE, p.productType);
        cv.put(PRODUCT_IMAGE, p.productImage);
        cv.put(PRODUCT_SLIDER_IMAGE, p.productSliderImage);
        cv.put(PRODUCT_DESC, p.productDescription);
        cv.put(PRODUCT_STATUS, p.status);

        if (update) {
            if (updateProduct(cv, p.id, mdb))
                return;
            else {
                products.add(cv);
                if (p.addOns != null) {
                    for (ProductAddOn addOn : p.addOns) {
                        ContentValues cvA = new ContentValues();
                        cvA.put(ADD_ON_ID, addOn.id);
                        cvA.put(ADD_ON_NAME, addOn.name);
                        cvA.put(ADD_ON_NET_PRICE, addOn.netamount);
                        cvA.put(ADD_ON_GROSS_PRICE, addOn.grossamount);
                        cvA.put(ADD_ON_PRODUCT_ID, p.id);
                        if (updateProductaddons(cvA, addOn.id, mdb))
                            return;
                        else
                            addOns.add(cvA);
                    }
                }
            }
        } else {
            products.add(cv);
            if (p.addOns != null) {
                for (ProductAddOn addOn : p.addOns) {
                    ContentValues cvA = new ContentValues();
                    cvA.put(ADD_ON_ID, addOn.id);
                    cvA.put(ADD_ON_NAME, addOn.name);
                    cvA.put(ADD_ON_NET_PRICE, addOn.netamount);
                    cvA.put(ADD_ON_GROSS_PRICE, addOn.grossamount);
                    cvA.put(ADD_ON_PRODUCT_ID, p.id);
                    cvA.put(PRODUCT_QTY, 0);
                    if (updateProductaddons(cvA, addOn.id, mdb))
                        return;
                    else
                        addOns.add(cvA);
                }
            }
        }
    }

    private static boolean updateProduct(ContentValues cv, int product_id,
                                         SQLiteDatabase mdb) {
        if (mdb.update(PRODUCT_TABLE, cv, PRODUCT_ID + "=" + product_id, null) > 0)
            return true;
        else
            return false;
    }

    public static void updateProductCustom(Context context, int quanty,
                                           int product_id) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();
        String query = "UPDATE " + PRODUCT_TABLE + " SET " + PRODUCT_QTY + "='"
                + quanty + "'" + " WHERE " + PRODUCT_ID + "='" + product_id
                + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();
    }


    public static void updateProductStatus(Context context, String Status,
                                           int product_id) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String query = "UPDATE " + PRODUCT_TABLE + " SET " + PRODUCT_STATUS + "='"
                + Status + "'" + " WHERE " + PRODUCT_ID + "='" + product_id
                + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();


    }

    public static void updateCategoryStatus(Context context, String Status,
                                            int cat_id) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String query = "UPDATE " + CATEGORY_TABLE + " SET " + CATEGORY_STATUS + "='"
                + Status + "'" + " WHERE " + CATEGORY_ID + "='" + cat_id
                + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();


    }

    private static void addProductAddon(List<ContentValues> productaddons,
                                        ProductAddOn addOn, SQLiteDatabase mdb, boolean update) {
        Product product = (Product) addOn.product;
        ContentValues cvA = new ContentValues();
        cvA.put(ADD_ON_ID, product.id);
        cvA.put(ADD_ON_NAME, product.name);
        cvA.put(ADD_ON_NET_PRICE, product.netamount);
        cvA.put(ADD_ON_GROSS_PRICE, product.grossamount);
        cvA.put(ADD_ON_PRODUCT_ID, product.categoryId);
        cvA.put(ADD_ON_VAT, product.vat);
        cvA.put(ADD_ON_TYPE, " ");
        cvA.put(PRODUCT_QTY, 0);
        if (update) {
            if (updateProductaddons(cvA, product.id, mdb))
                return;
            else
                productaddons.add(cvA);

        } else {

            productaddons.add(cvA);

        }

    }


    private static void addProductATtributes(List<ContentValues> productaddons,
                                             ProductAtrribute addOn, SQLiteDatabase mdb, boolean update) {
        ContentValues cvA = new ContentValues();
        cvA.put(ATTRIBUTE_ID, addOn.id);
        cvA.put(ATTRIBUTE_NAME, addOn.name);
        cvA.put(ATTRIBUTE_CONDITION, addOn.attr_condition);
        cvA.put(ATTRIBUTE_DISPLAYORDER, addOn.display_order);
        cvA.put(ATTRIBUTE_PRODUCT_ID, addOn.categoryId);

        if (update) {
            if (updateProductAttributes(cvA, addOn.id, mdb))
                return;
            else
                productaddons.add(cvA);

        } else {

            productaddons.add(cvA);

        }

    }


    private static void addProductATtributesOptions(List<ContentValues> productaddons,
                                                    ProductAtrributeOption addOn, SQLiteDatabase mdb, boolean update) {
        ContentValues cvA = new ContentValues();
        cvA.put(ATTRIBUTESOPTION_ID, addOn.id);
        cvA.put(ATTRIBUTESOPTION_NAME, addOn.name);
        cvA.put(ATTRIBUTESOPTION_PRICE, addOn.netamount);
        cvA.put(ATTRIBUTESOPTION_PRODUCT_ID, addOn.categoryId);
        cvA.put(PRODUCT_QTY, 0);

        if (update) {
            if (updateProductAttributesOption(cvA, addOn.id, mdb))
                return;
            else
                productaddons.add(cvA);

        } else {

            productaddons.add(cvA);

        }

    }


    private static boolean updateProductaddons(ContentValues cv,
                                               int productaddonId, SQLiteDatabase mdb) {
        if (mdb.update(PRODUCT_ADD_ON_TABLE, cv, ADD_ON_ID + "="
                + productaddonId, null) > 0)
            return true;
        else
            return false;
    }

    private static boolean updateProductAttributes(ContentValues cv,
                                                   int productaddonId, SQLiteDatabase mdb) {
        if (mdb.update(PRODUCT_ATTRIBUTES_TABLE, cv, ATTRIBUTE_ID + "="
                + productaddonId, null) > 0)
            return true;
        else
            return false;
    }

    private static boolean updateProductAttributesOption(ContentValues cv,
                                                         int productaddonId, SQLiteDatabase mdb) {
        if (mdb.update(PRODUCT_ATTRIBUTESOPTION_TABLE, cv, ATTRIBUTESOPTION_ID + "="
                + productaddonId, null) > 0)
            return true;
        else
            return false;
    }


    public static void updateProductaddonsCustom(Context context,
                                                 int productaddonId, int qunatity) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String query = "UPDATE " + PRODUCT_ADD_ON_TABLE + " SET " + PRODUCT_QTY
                + "='" + qunatity + "'" + " WHERE " + ADD_ON_ID + "='"
                + productaddonId + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();
    }


    public static void updateProductCustomAddOns(Context context,
                                                 int productaddonId, int qunatity) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String query = "UPDATE " + CUSTOM_PRODUCT_ADD_ON_TABLE + " SET " + PRODUCT_QTY
                + "='" + qunatity + "'" + " WHERE " + ADD_ON_PRODUCT_ID + "='"
                + productaddonId + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();
    }

    public static void updateQtyOfProducts(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String query = "UPDATE " + PRODUCT_TABLE + " SET " + PRODUCT_QTY + "='"
                + 0 + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();
    }

    public static void updateQtyOfAddons(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.beginTransaction();

        String query = "UPDATE " + PRODUCT_ADD_ON_TABLE + " SET " + PRODUCT_QTY
                + "='" + 0 + "'";
        mdb.execSQL(query);
        mdb.setTransactionSuccessful();
        mdb.endTransaction();
    }

    public static List<Item> getOrderedProducts(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        String query = "SELECT * FROM " + PRODUCT_TABLE + " WHERE "
                + PRODUCT_QTY + " > 0";
        Cursor cursor = mdb.rawQuery(query, null);
        if (cursor != null)
            Log.i("query products", cursor.getCount() + " - count");

        List<Item> list = getproductfromdb(cursor, context);
        return list.size() > 0 ? list : null;

    }


	/*
	 * public static List<Item> getOrderedProducts(Context context) {
	 * SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
	 * .getWritableDatabase();
	 * 
	 * 
	 * String query = "SELECT * FROM " + PRODUCT_TABLE + " WHERE " + PRODUCT_QTY
	 * + " > 0"; Cursor cursor = mdb.rawQuery(query, null); if(cursor!=null)
	 * Log.i("query products", cursor.getCount() + " - count");
	 * 
	 * List<Item> list = getproductfromdb(cursor, context); return list.size() >
	 * 0 ? list : null;
	 * 
	 * }
	 */

    public static List<ProductAddOn> getAddOn(Context context, Product pro) {
        int productId = pro.id;
        SQLiteDatabase db = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = db.query(PRODUCT_ADD_ON_TABLE, new String[]{_ID,
                        ADD_ON_ID, ADD_ON_NAME, ADD_ON_NET_PRICE, ADD_ON_PRODUCT_ID,
                        ADD_ON_GROSS_PRICE, PRODUCT_QTY}, ADD_ON_PRODUCT_ID + " = ? ",
                new String[]{String.valueOf(productId)}, null, null, _ID);
        List<ProductAddOn> list = new ArrayList<ProductAddOn>();
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    ProductAddOn p = new ProductAddOn();
                    p._id = c.getInt(c.getColumnIndex(_ID));
                    p.id = c.getInt(c.getColumnIndex(ADD_ON_ID));

                    p.name = c.getString(c.getColumnIndex(ADD_ON_NAME));
                    p.grossamount = c.getFloat(c
                            .getColumnIndex(ADD_ON_GROSS_PRICE));
                    p.netamount = c
                            .getFloat(c.getColumnIndex(ADD_ON_NET_PRICE));
                    p.product = pro;
                    p.quantity = Integer.valueOf(c.getString(c
                            .getColumnIndex(PRODUCT_QTY)));
                    list.add(p);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list.size() > 0 ? list : null;
    }


    public static List<ProductAtrribute> getATTRibutes(Context context, Product pro) {
        int productId = pro.id;

        SQLiteDatabase db = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = db.query(PRODUCT_ATTRIBUTES_TABLE, new String[]{_ID,
                        ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_CONDITION, ATTRIBUTE_DISPLAYORDER,
                        ATTRIBUTE_PRODUCT_ID}, ATTRIBUTE_PRODUCT_ID + " = ? ",
                new String[]{String.valueOf(productId)}, null, null, ATTRIBUTE_DISPLAYORDER);
        List<ProductAtrribute> list = new ArrayList<ProductAtrribute>();
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    ProductAtrribute p = new ProductAtrribute();
                    p._id = c.getInt(c.getColumnIndex(_ID));
                    p.id = c.getInt(c.getColumnIndex(ATTRIBUTE_ID));

                    p.name = c.getString(c.getColumnIndex(ATTRIBUTE_NAME));

                    p.attr_condition = c.getString(c.getColumnIndex(ATTRIBUTE_CONDITION));
                    p.display_order = c.getString(c.getColumnIndex(ATTRIBUTE_DISPLAYORDER));
                    p.categoryId = c.getInt(c
                            .getColumnIndex(ATTRIBUTE_PRODUCT_ID));

                    list.add(p);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list.size() > 0 ? list : null;
    }

    public static List<ProductAtrributeOption> getATTRibutesOptions(Context context, ProductAtrribute pro) {
        int productId = pro.id;

        SQLiteDatabase db = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = db.query(PRODUCT_ATTRIBUTESOPTION_TABLE, new String[]{_ID,
                        ATTRIBUTESOPTION_ID, ATTRIBUTESOPTION_NAME, ATTRIBUTESOPTION_PRICE, PRODUCT_QTY,
                        ATTRIBUTESOPTION_PRODUCT_ID}, ATTRIBUTESOPTION_PRODUCT_ID + " = ? ",
                new String[]{String.valueOf(productId)}, null, null, _ID);
        List<ProductAtrributeOption> list = new ArrayList<ProductAtrributeOption>();
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    ProductAtrributeOption p = new ProductAtrributeOption();
                    p._id = c.getInt(c.getColumnIndex(_ID));
                    p.id = c.getInt(c.getColumnIndex(ATTRIBUTESOPTION_ID));

                    p.name = c.getString(c.getColumnIndex(ATTRIBUTESOPTION_NAME));

                    p.quantity = Integer.valueOf(c.getString(c
                            .getColumnIndex(PRODUCT_QTY)));
                    p.netamount = c
                            .getFloat(c.getColumnIndex(ATTRIBUTESOPTION_PRICE));
                    p.categoryId = c.getInt(c
                            .getColumnIndex(ATTRIBUTESOPTION_PRODUCT_ID));

                    list.add(p);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list.size() > 0 ? list : null;
    }


    //String item="全部";
    public static List<ProductAddOn> getOrderedAddons(Context context, Product pro) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        String query = "SELECT * FROM " + PRODUCT_ADD_ON_TABLE + " WHERE "
                + PRODUCT_QTY + " > 0" + " AND " + ADD_ON_PRODUCT_ID + " = '" + pro.id + "'";
        Cursor c = mdb.rawQuery(query, null);
        List<ProductAddOn> list = new ArrayList<ProductAddOn>();
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    ProductAddOn p = new ProductAddOn();
                    p._id = c.getInt(c.getColumnIndex(_ID));
                    p.id = c.getInt(c.getColumnIndex(ADD_ON_ID));

                    p.name = c.getString(c.getColumnIndex(ADD_ON_NAME));
                    p.grossamount = c.getFloat(c
                            .getColumnIndex(ADD_ON_GROSS_PRICE));
                    p.netamount = c
                            .getFloat(c.getColumnIndex(ADD_ON_NET_PRICE));
                    p.product = pro;
                    p.quantity = Integer.valueOf(c.getString(c
                            .getColumnIndex(PRODUCT_QTY)));
                    p.addOnType = c.getString(c.getColumnIndex(ADD_ON_TYPE));
                    list.add(p);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list.size() > 0 ? list : null;

    }


    public static List<ProductAddOn> getCutomAddOn(Context context, Product pro) {
        int productId = pro.id;
        SQLiteDatabase db = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = db.query(CUSTOM_PRODUCT_ADD_ON_TABLE, new String[]{_ID, ADD_ON_TYPE,
                        ADD_ON_ID, ADD_ON_NAME, ADD_ON_NET_PRICE, ADD_ON_PRODUCT_ID,
                        ADD_ON_GROSS_PRICE, PRODUCT_QTY}, ADD_ON_PRODUCT_ID + " = ? ",
                new String[]{String.valueOf(productId)}, null, null, _ID);
        List<ProductAddOn> list = new ArrayList<ProductAddOn>();
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    ProductAddOn p = new ProductAddOn();
                    p._id = c.getInt(c.getColumnIndex(_ID));
                    p.id = c.getInt(c.getColumnIndex(ADD_ON_ID));

                    p.name = c.getString(c.getColumnIndex(ADD_ON_NAME));
                    p.grossamount = c.getFloat(c
                            .getColumnIndex(ADD_ON_GROSS_PRICE));
                    p.netamount = c
                            .getFloat(c.getColumnIndex(ADD_ON_NET_PRICE));
                    p.product = pro;

                    p.quantity = Integer.valueOf(c.getString(c
                            .getColumnIndex(PRODUCT_QTY)));
                    p.addOnType = c.getString(c.getColumnIndex(ADD_ON_TYPE));
                    list.add(p);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list.size() > 0 ? list : null;
    }

    public static List<Item> getCategory(Context context, int parent) {
        // List<Item> list = mapOfIdToListItems.get(parent);
        List<Item> list = new ArrayList<Item>();
        // if (list == null) {
        SQLiteDatabase mdb = POSDatabase.getInstance(context)
                .getWritableDatabase();
        Cursor c = mdb.query(CATEGORY_TABLE, new String[]{_ID, CATEGORY_ID,
                        CATEGORY_HAS_PRODUCT, CATEGORY_HAS_SUB_CAT, CATEGORY_NAME,
                        CATEGORY_IMAGE, CATEGORY_DESC, CATEGORY_STATUS,
                        CATEGORY_PARENT_CAT_ID}, CATEGORY_PARENT_CAT_ID + " = ? ",
                new String[]{String.valueOf(parent)}, null, null, CATEGORY_NAME_EN);
        Log.i("query category", c.getCount() + " - count");
        list = new ArrayList<Item>();
        try {
            while (c != null && c.moveToNext()) {
                Category cat = new Category();
                cat._id = c.getInt(c.getColumnIndex(_ID));
                cat.id = c.getInt(c.getColumnIndex(CATEGORY_ID));
                cat.hasProducts = c.getInt(c
                        .getColumnIndex(CATEGORY_HAS_PRODUCT)) == 1;
                cat.hasSubCategory = c.getInt(c
                        .getColumnIndex(CATEGORY_HAS_SUB_CAT)) == 1;
                cat.name = c.getString(c.getColumnIndex(CATEGORY_NAME));
                cat.parentId = c.getInt(c
                        .getColumnIndex(CATEGORY_PARENT_CAT_ID));
                cat.productImage = c.getString(c
                        .getColumnIndex(CATEGORY_IMAGE));
                cat.productDescription = c.getString(c
                        .getColumnIndex(CATEGORY_DESC));
                cat.status = c.getString(c
                        .getColumnIndex(CATEGORY_STATUS));
                list.add(cat);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // mapOfIdToListItems.put(parent, list);
        } finally {
            c.close();
        }
        // }

        return list.size() > 0 ? list : null;
    }

    public static List<Item> getProducts(Context context, int catId) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        Cursor c = mdb.query(PRODUCT_TABLE, new String[]{_ID, PRODUCT_ID,
                        PRODUCT_NAME, PRODUCT_NUM, PRODUCT_VAT, PRODUCT_NET_PRICE,
                        PRODUCT_DESC, PRODUCT_SLIDER_IMAGE, PRODUCT_STATUS, PRODUCT_TYPE,
                        PRODUCT_GROSS_PRICE, PRODUCT_CATEGORY_ID, PRODUCT_QTY, PRODUCT_IMAGE},
                PRODUCT_CATEGORY_ID + " = ? ",
                new String[]{String.valueOf(catId)}, null, null, PRODUCT_NAME_EN);
        if (c != null)
            Log.i("query products", c.getCount() + " - count");

        List<Item> list = getproductfromdb(c, context);
        return list.size() > 0 ? list : null;
    }

    private static List<Item> getproductfromdb(Cursor c, Context context) {
        List<Item> list = new ArrayList<Item>();

        list = new ArrayList<Item>();

        try {
            if (c != null && c.moveToFirst()) {
                do {
                    Product prod = new Product();
                    prod.categoryId = c.getInt(c
                            .getColumnIndex(PRODUCT_CATEGORY_ID));

                    prod._id = c.getInt(c.getColumnIndex(_ID));
                    prod.id = c.getInt(c.getColumnIndex(PRODUCT_ID));
                    prod.name = c.getString(c.getColumnIndex(PRODUCT_NAME));
                    prod.productType = c.getString(c.getColumnIndex(PRODUCT_TYPE));
                    prod.vat = c.getString(c.getColumnIndex(PRODUCT_VAT));
                    prod.productNum = c.getString(c.getColumnIndex(PRODUCT_NUM));
                    prod.status = c.getString(c.getColumnIndex(PRODUCT_STATUS));
                    prod.grossamount = c.getFloat(c
                            .getColumnIndex(PRODUCT_GROSS_PRICE));
                    prod.netamount = c
                            .getFloat(c.getColumnIndex(PRODUCT_NET_PRICE));
                    prod.quantity = Integer.valueOf(c.getString(c.getColumnIndex(PRODUCT_QTY)));
                    prod.productImage = c.getString(c.getColumnIndex(PRODUCT_IMAGE));
                    prod.productDescription = c.getString(c.getColumnIndex(PRODUCT_DESC));
                    prod.productSliderImage = c.getString(c.getColumnIndex(PRODUCT_SLIDER_IMAGE));
                    list.add(prod);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // mapOfIdToListItems.put(catId, list);
        } finally {
            c.close();
        }

        return list;

    }

    public static List<Item> getProducts(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        Cursor c = mdb.query(PRODUCT_TABLE, new String[]{_ID, PRODUCT_ID,
                        PRODUCT_NAME, PRODUCT_NUM, PRODUCT_VAT, PRODUCT_NET_PRICE, PRODUCT_DESC, PRODUCT_SLIDER_IMAGE,
                        PRODUCT_GROSS_PRICE, PRODUCT_STATUS, PRODUCT_CATEGORY_ID, PRODUCT_QTY, PRODUCT_IMAGE}, null, null, null,
                null, _ID);
        Log.i("query", c.getCount() + " - count");
        List<Item> list = getproductfromdb(c, context);

        return list.size() > 0 ? list : null;
    }

    public static List<Item> getmanagePrduct(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = mdb.query(PRODUCT_TABLE, new String[]{_ID, PRODUCT_ID,
                        PRODUCT_NAME, PRODUCT_NUM, PRODUCT_VAT, PRODUCT_NET_PRICE,
                        PRODUCT_GROSS_PRICE, PRODUCT_CATEGORY_ID}, null, null, null,
                null, _ID);
        Log.i("query", c.getCount() + " - count");
        List<Item> itemlist = new ArrayList<Item>();
        List<Item> listProduct = getproductfromdb(c, context);

        itemlist.addAll(listProduct);
        List<Item> listcateItems = null;
        int total = listProduct.size();
        for (int i = 0; i < total; i++) {
            Product product = (Product) listProduct.get(i);
            listcateItems = getCategory(context, product.categoryId);
        }
        if (listcateItems != null)
            itemlist.addAll(listcateItems);

        return itemlist.size() > 0 ? itemlist : null;
    }

    public static ArrayList<ProductItem> getManageProducta(Context context) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = mdb
                .query(CATEGORY_TABLE, new String[]{_ID, CATEGORY_ID,
                                CATEGORY_HAS_PRODUCT, CATEGORY_HAS_SUB_CAT,
                                CATEGORY_NAME, CATEGORY_PARENT_CAT_ID},
                        CATEGORY_PARENT_CAT_ID + " = ? ", new String[]{String
                                .valueOf(Category.TOP_LEVEL_CAT_PARENT_ID)},
                        null, null, _ID);

        ArrayList<Item> list = new ArrayList<Item>();
        try {
            while (c.moveToNext()) {
                Category cat = new Category();
                cat._id = c.getInt(c.getColumnIndex(_ID));
                cat.id = c.getInt(c.getColumnIndex(CATEGORY_ID));
                cat.hasProducts = c.getInt(c
                        .getColumnIndex(CATEGORY_HAS_PRODUCT)) == 1;
                cat.hasSubCategory = c.getInt(c
                        .getColumnIndex(CATEGORY_HAS_SUB_CAT)) == 1;
                cat.name = c.getString(c.getColumnIndex(CATEGORY_NAME));
                cat.parentId = c.getInt(c
                        .getColumnIndex(CATEGORY_PARENT_CAT_ID));
                list.add(cat);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // mapOfIdToListItems.put(parent, list);
        } finally {
            c.close();
        }
        // }

        return null;
    }

    public static ArrayList<ProductItem> getManageProduct(Context context) {
        ArrayList<ProductItem> productItems = new ArrayList<ProductItem>();

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = mdb.query(PRODUCT_TABLE, new String[]{_ID, PRODUCT_ID,
                        PRODUCT_NAME, PRODUCT_NUM, PRODUCT_NET_PRICE,
                        PRODUCT_GROSS_PRICE, PRODUCT_CATEGORY_ID}, null, null, null,
                null, _ID);
        Log.i("query", c.getCount() + " - count");

        List<Item> listProduct = getproductfromdb(c, context);

        if (listProduct.size() > 0) {
            for (int i = 0; i < listProduct.size(); i++) {

                Product prod = (Product) listProduct.get(i);
                ProductItem productItem = new ProductItem();

                productItem.setProductNo(prod.productNum);
                productItem.setProductVatPrice(prod.vat);
                productItem.setProductGrossPrice(Util
                        .numberfornmat(prod.grossamount));

                ArrayList<String> catArray = new ArrayList<String>();
                getCategoryName(context, prod.categoryId, mdb, catArray);
                productItem.setCateogryName(catArray);
                productItem.setProductShortname(prod.name);

                productItem.setProductNetPrice(Util
                        .numberfornmat(prod.netamount));

                productItems.add(productItem);
            }

        }

        return productItems;
    }

    public static ArrayList<ProductItem> getManageProductNew(Context context,
                                                             SQLiteDatabase mdb) {
        ArrayList<ProductItem> productItems = new ArrayList<ProductItem>();

        // mdb = POSDatabase.getInstanceMenu(context).getWritableDatabase();
        Cursor c = mdb.query(PRODUCT_TABLE, new String[]{_ID, PRODUCT_ID,
                        PRODUCT_NAME, PRODUCT_NUM, PRODUCT_VAT, PRODUCT_NET_PRICE,
                        PRODUCT_GROSS_PRICE, PRODUCT_CATEGORY_ID}, null, null, null,
                null, _ID);
        Log.i("query", c.getCount() + " - count");

        List<Item> listProduct = getproductfromdb(c, context);

        if (listProduct.size() > 0) {
            for (int i = 0; i < listProduct.size(); i++) {

                Product prod = (Product) listProduct.get(i);
                ProductItem productItem = new ProductItem();

                productItem.setProductId("" + prod.id);
                productItem.setProductNo(prod.productNum);
                productItem.setProductVatPrice(prod.vat);
                productItem.setProductGrossPrice(Util
                        .numberfornmat(prod.grossamount));

                ArrayList<Category> catArray = new ArrayList<Category>();
                getcategoryfromdb(context, prod.categoryId, catArray, mdb);
                productItem.setCatArray(catArray);

                prod.addOns = getAddOnManageProduct(context, prod);

                if (prod.addOns != null)
                    Log.i("pro addonss", "::size" + prod.addOns.size());

                productItem.setProductShortname(prod.name);
                productItem.setProductNetPrice(Util
                        .numberfornmat(prod.netamount));

                productItem.setProduct(prod);
                productItems.add(productItem);
            }

        }

        return productItems;
    }

    private static void getCategoryName(Context context, int parentId,
                                        SQLiteDatabase mdb2, ArrayList<String> catArray) {

        Cursor c = mdb2.rawQuery("Select " + CATEGORY_ID + " , "
                + CATEGORY_NAME + " , " + CATEGORY_PARENT_CAT_ID + " from "
                + CATEGORY_TABLE + " where " + CATEGORY_ID + "='" + parentId
                + "'", null);

		/*
		 * Cursor c = mdb2.query(CATEGORY_TABLE, new String[] { CATEGORY_ID,
		 * CATEGORY_NAME, CATEGORY_PARENT_CAT_ID }, CATEGORY_ID + " = ? ", new
		 * String[] { String.valueOf(parentId) }, null, null, _ID);
		 */
        if (c.moveToFirst()) {

            catArray.add(c.getString(c.getColumnIndex(CATEGORY_NAME)));

            Log.i("category Name", ":"
                    + catArray.get(catArray.size() - 1).toString());
            getCategoryName(context,
                    c.getInt(c.getColumnIndex(CATEGORY_PARENT_CAT_ID)), mdb2,
                    catArray);

			/*
			 * List<String> getCat = getCategoryName(context,
			 * c.getInt(c.getColumnIndex(CATEGORY_PARENT_CAT_ID)), mdb2);
			 */
            // catArray.addAll(getCat);
        }

    }

	/*
	 * public static HashMap<Integer, ArrayList<Category>> getManage( Context
	 * context) {
	 * 
	 * // List<Item> list = mapOfIdToListItems.get(parent); List<Category> list
	 * = new ArrayList<Category>(); // if (list == null) { SQLiteDatabase mdb =
	 * POSDatabase.getInstanceMenu(context) .getWritableDatabase(); Cursor c =
	 * mdb.query(CATEGORY_TABLE, new String[] { _ID, CATEGORY_ID,
	 * CATEGORY_HAS_PRODUCT, CATEGORY_HAS_SUB_CAT, CATEGORY_NAME,
	 * CATEGORY_PARENT_CAT_ID }, null, null, null, null, _ID); Log.i("query",
	 * c.getCount() + " - count"); HashMap<Integer, ArrayList<Category>> mhasmap
	 * = new HashMap<>();
	 * 
	 * try {
	 * 
	 * ArrayList<Category> catArray = new ArrayList<>(); int i = 0; while (c !=
	 * null && c.moveToNext()) {
	 * 
	 * Category cat = new Category(); cat._id = c.getInt(c.getColumnIndex(_ID));
	 * cat.id = c.getInt(c.getColumnIndex(CATEGORY_ID)); cat.hasProducts =
	 * c.getInt(c .getColumnIndex(CATEGORY_HAS_PRODUCT)) == 1;
	 * cat.hasSubCategory = c.getInt(c .getColumnIndex(CATEGORY_HAS_SUB_CAT)) ==
	 * 1; cat.name = Util.GetNameValue(c.getString(c
	 * .getColumnIndex(CATEGORY_NAME))); cat.parentId = c.getInt(c
	 * .getColumnIndex(CATEGORY_PARENT_CAT_ID)); Log.i("cat.name", ":name:" +
	 * cat.name);
	 * 
	 * if (cat.hasProducts) { cat.products = getproManageProduct(context,
	 * cat.id); catArray.add(cat); mhasmap.put(i, catArray); i++; catArray = new
	 * ArrayList<>(); } else { catArray.add(cat); } if (cat.products != null) {
	 * Log.i("prod", ":prod:" + cat.products.size()); }
	 * 
	 * }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); // mapOfIdToListItems.put(parent, list); } finally {
	 * c.close(); } // }
	 * 
	 * return mhasmap.size() > 0 ? mhasmap : null;
	 * 
	 * }
	 */

	/*
	 * public static List<Product> getproManageProduct(Context context, int
	 * catId) { SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
	 * .getWritableDatabase(); Cursor c = mdb.query(PRODUCT_TABLE, new String[]
	 * { _ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_NUM, PRODUCT_NET_PRICE,
	 * PRODUCT_GROSS_PRICE, PRODUCT_CATEGORY_ID }, PRODUCT_CATEGORY_ID +
	 * " = ? ", new String[] { String.valueOf(catId) }, null, null, _ID);
	 * Log.i("query", c.getCount() + " - count");
	 * 
	 * List<Product> list = getproductfromdbManageProduct(c, context);
	 * 
	 * return list.size() > 0 ? list : null; }
	 */

	/*
	 * public static List<Product> getproductfromdbManageProduct(Cursor c,
	 * Context context) { List<Product> list = new ArrayList<Product>();
	 * 
	 * try { while (c.moveToNext()) { Product prod = new Product();
	 * prod.categoryId = c.getInt(c .getColumnIndex(PRODUCT_CATEGORY_ID));
	 * 
	 * prod._id = c.getInt(c.getColumnIndex(_ID)); prod.id =
	 * c.getInt(c.getColumnIndex(PRODUCT_ID));
	 * 
	 * String lang = POSApplication.getApp().getDataModel()
	 * .getUserInfo().USR_LANG; String code =
	 * Util.ConvertLangLanguageToCode(lang); prod.name =
	 * Util.GetNameValue(c.getString(c .getColumnIndex(PRODUCT_NAME)));
	 * 
	 * prod.productNum = c.getString(c.getColumnIndex(PRODUCT_NUM));
	 * prod.grossamount = c.getFloat(c .getColumnIndex(PRODUCT_GROSS_PRICE));
	 * prod.netamount = c.getFloat(c .getColumnIndex(PRODUCT_GROSS_PRICE));
	 * 
	 * prod.addOns = getAddOnManageProduct(context, prod);
	 * 
	 * if (prod.addOns != null) { Log.i("prod addons ", ":" +
	 * prod.addOns.size()); }
	 * 
	 * list.add(prod); } } catch (Exception e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); // mapOfIdToListItems.put(catId, list); }
	 * finally { c.close(); }
	 * 
	 * return list;
	 * 
	 * }
	 */

    public static List<ProductAddOn> getAddOnManageProduct(Context context,
                                                           Product pro) {
        int productId = pro.id;
        SQLiteDatabase db = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = db.query(PRODUCT_ADD_ON_TABLE, new String[]{_ID,
                        ADD_ON_ID, ADD_ON_NAME, ADD_ON_NET_PRICE, ADD_ON_PRODUCT_ID,
                        ADD_ON_GROSS_PRICE, ADD_ON_VAT}, ADD_ON_PRODUCT_ID + " = ? ",
                new String[]{String.valueOf(productId)}, null, null, _ID);
        List<ProductAddOn> list = new ArrayList<ProductAddOn>();
        try {
            while (c.moveToNext()) {
                ProductAddOn p = new ProductAddOn();
                p._id = c.getInt(c.getColumnIndex(_ID));
                p.id = c.getInt(c.getColumnIndex(ADD_ON_ID));
                p.name = c.getString(c.getColumnIndex(ADD_ON_NAME));
                p.grossamount = c
                        .getFloat(c.getColumnIndex(ADD_ON_GROSS_PRICE));
                p.netamount = c.getFloat(c.getColumnIndex(ADD_ON_NET_PRICE));
                p.vat = c.getString(c.getColumnIndex(ADD_ON_VAT));
                list.add(p);
            }
        } catch (Exception e) {
            // TODO Auto-generated `````catch block
            e.printStackTrace();
        } finally {
            c.close();
        }

        return list.size() > 0 ? list : null;
    }

    public static void getcategoryfromdb(Context context, int parentId,
                                         List<Category> catArray, SQLiteDatabase mdb) {

        Cursor c = mdb.rawQuery("Select " + _ID + " , " + CATEGORY_ID + " , "
                        + CATEGORY_HAS_PRODUCT + " , " + CATEGORY_HAS_SUB_CAT + " , "
                        + CATEGORY_NAME + " , " + CATEGORY_PARENT_CAT_ID + " from "
                        + CATEGORY_TABLE + "	where " + CATEGORY_ID + " = " + parentId,
                null);

		/*
		 * Cursor c = mdb2.query(CATEGORY_TABLE, new String[] { CATEGORY_ID,
		 * CATEGORY_NAME, CATEGORY_PARENT_CAT_ID }, CATEGORY_ID + " = ? ", new
		 * String[] { String.valueOf(parentId) }, null, null, _ID);
		 */
        if (c != null && c.moveToFirst()) {
            Category cat = new Category();
            cat._id = c.getInt(c.getColumnIndex(_ID));
            cat.id = c.getInt(c.getColumnIndex(CATEGORY_ID));
            cat.hasProducts = c.getInt(c.getColumnIndex(CATEGORY_HAS_PRODUCT)) == 1;
            cat.hasSubCategory = c.getInt(c
                    .getColumnIndex(CATEGORY_HAS_SUB_CAT)) == 1;

            cat.name = c.getString(c
                    .getColumnIndex(CATEGORY_NAME));
            cat.parentId = c.getInt(c.getColumnIndex(CATEGORY_PARENT_CAT_ID));
            getcategoryfromdb(context, cat.parentId, catArray, mdb);
            catArray.add(cat);

        }

        // mdb.close();

    }

    // public static Product getProduct(Context context, int productId) {
    // SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
    // .getWritableDatabase();
    // Cursor c = mdb.query(PRODUCT_TABLE, new String[] { _ID, PRODUCT_ID,
    // PRODUCT_NAME, PRODUCT_NAME_JSON, PRODUCT_NUM, PRODUCT_PRICE,
    // PRODUCT_TOTAL_PRICE }, PRODUCT_ID + " = ? ",
    // new String[] { String.valueOf(productId) }, null, null, _ID);
    // }

	/*
	 * public final static void CheckDb(Context context) { SQLiteDatabase mdb =
	 * POSDatabase.getInstanceMenu(context) .getWritableDatabase(); String query
	 * = "SELECT COUNT() FROM sqlite_master WHERE name ='CATEGORY_TABLE'";
	 * mdb.execSQL(query); }
	 */

    public final static void deleteAllData(Context context) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        mdb.execSQL("DELETE FROM " + CATEGORY_TABLE);
        mdb.execSQL("DELETE FROM " + PRODUCT_TABLE);
        mdb.execSQL("DELETE FROM " + PRODUCT_ADD_ON_TABLE);
        mdb.execSQL("DELETE FROM " + PRODUCT_ATTRIBUTES_TABLE);
        mdb.execSQL("DELETE FROM " + PRODUCT_ATTRIBUTESOPTION_TABLE);
    }

    public static List<Item> getCatAndProducts(Integer parent) {
        // return mapOfIdToListItems.get(parent);
        return new ArrayList<Item>();
    }

    public static Integer getParentCat(Context context, Category ca) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = mdb.query(CATEGORY_TABLE,
                new String[]{CATEGORY_PARENT_CAT_ID}, CATEGORY_ID + " = ? ",
                new String[]{String.valueOf(ca.id)}, null, null, _ID);
        Log.i("query", c.getCount() + " - count");
        try {
            while (c.moveToNext()) {

                return c.getInt(c.getColumnIndex(CATEGORY_PARENT_CAT_ID));
            }
        } finally {
            c.close();
        }
        return null;
    }

    public static Integer getParentCat(Context context, Product p) {
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = mdb.query(PRODUCT_TABLE,
                new String[]{PRODUCT_CATEGORY_ID}, PRODUCT_ID + " = ? ",
                new String[]{String.valueOf(p.id)}, null, null, _ID);
        Log.i("query", c.getCount() + " - count");
        try {
            while (c.moveToNext()) {

                return c.getInt(c.getColumnIndex(PRODUCT_CATEGORY_ID));
            }
        } finally {
            c.close();
        }
        return null;
    }

    public static boolean deleteProduct(Context context, String[] id) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        for (int i = 0; i < id.length; i++) {

            String productId = id[i];
            String query = "DELETE FROM " + PRODUCT_TABLE + " WHERE "
                    + PRODUCT_ID + " = " + productId;

            Log.i("query", query);

            if (!TextUtils.isEmpty(productId))
                mdb.execSQL(query);

        }

        return true;

    }

    public static boolean deleteCategory(Context context, String[] id) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        Cursor c = null;

        for (int i = 0; i < id.length; i++) {

            String categoryId = id[i];

            c = mdb.rawQuery("DELETE FROM " + CATEGORY_TABLE + " WHERE "
                    + CATEGORY_ID + " = " + categoryId, null);

        }

        if (c != null && c.getCount() > 0) {
            return true;
        } else
            return false;
    }

    public static void getCategory(Context context, SQLiteDatabase mdb,
                                   List<Item> item) {
        List<Item> list = new ArrayList<Item>();
        System.out.println("ParseProduct.CreateParseMenuItem()");
        // if (list == null) {

        Cursor c = mdb.rawQuery("Select * from " + CATEGORY_TABLE, null);

        try {
            while (c.moveToNext()) {
                Category cat = new Category();
                cat._id = c.getInt(c.getColumnIndex(_ID));
                cat.id = c.getInt(c.getColumnIndex(CATEGORY_ID));
                cat.hasProducts = c.getInt(c
                        .getColumnIndex(CATEGORY_HAS_PRODUCT)) == 1;
                cat.hasSubCategory = c.getInt(c
                        .getColumnIndex(CATEGORY_HAS_SUB_CAT)) == 1;
                cat.name = c.getString(c.getColumnIndex(CATEGORY_NAME));
                cat.parentId = c.getInt(c
                        .getColumnIndex(CATEGORY_PARENT_CAT_ID));
                item.add(cat);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // mapOfIdToListItems.put(parent, list);
        } finally {
            c.close();
        }

    }

    public static void getProduct(Context context, SQLiteDatabase mdb,
                                  List<Item> item) {
        System.out.println("TakeOrderDB.getProduct()");
        String querystr = "Select * from " + PRODUCT_TABLE;
        Log.d("getProduct", "::" + querystr);
        Cursor c = mdb.rawQuery(querystr, null);

		/*
		 * Cursor c = mdb.query(PRODUCT_TABLE, new String[] { _ID, PRODUCT_ID,
		 * PRODUCT_NAME, PRODUCT_NUM, PRODUCT_GROSS_PRICE, PRODUCT_VAT,
		 * PRODUCT_NET_PRICE, PRODUCT_CATEGORY_ID }, null, null, null, null,
		 * _ID);
		 */
        Log.i("query", c.getCount() + " - count");
        item.addAll(getproductfromdb(c, context));

    }

    public static void getProductAddons(Context context, SQLiteDatabase mdb,
                                        List<Item> item) {
        System.out.println("TakeOrderDB.getProductAddons()");
        String querystr = "Select * from " + PRODUCT_ADD_ON_TABLE;
        Log.d("getProductAddons", "::" + querystr);
        Cursor c = mdb.rawQuery(querystr, null);

		/*
		 * Cursor c = mdb.query(PRODUCT_ADD_ON_TABLE, new String[] { _ID,
		 * ADD_ON_ID, ADD_ON_NAME, ADD_ON_GROSS_PRICE, ADD_ON_PRODUCT_ID,
		 * ADD_ON_NET_PRICE }, null, null, null, null, _ID);
		 */// List<Item> list = new ArrayList<Item>();
        Log.d("getProductAddons", ":count:" + c.getCount());
        try {
            if (c != null && c.moveToFirst()) {
                Log.d(":c:", ":count:" + c.getCount());

                while (c != null && c.moveToNext()) {
                    ProductAddOn p = new ProductAddOn();
                    Product product = new Product();

                    product._id = c.getInt(c.getColumnIndex(_ID));
                    product.id = c.getInt(c.getColumnIndex(ADD_ON_ID));
                    product.name = c.getString(c.getColumnIndex(ADD_ON_NAME));
                    product.grossamount = c.getFloat(c
                            .getColumnIndex(ADD_ON_GROSS_PRICE));
                    product.netamount = c.getFloat(c
                            .getColumnIndex(ADD_ON_NET_PRICE));
                    product.categoryId = c.getInt(c
                            .getColumnIndex(ADD_ON_PRODUCT_ID));
                    Log.d(":productaddons:", ":nameee:" + p.name);
                    p.product = product;
                    item.add(p);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }

    }

    public static boolean saveDBVersion(Context context, String DBVersion) {

        boolean returnCheck = false;
        try {
            SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                    .getWritableDatabase();
            ContentValues cv = new ContentValues();

            if (DBVersion != null) {
                cv.put(TB_VERSION_MENUPRODUCT, DBVersion);
                cv.put(TB_VERSION_DB_VER, DBVersion);
            } else {
                Date date = new Date();
                cv.put(TB_VERSION_MENUPRODUCT, date.getTime());
                cv.put(TB_VERSION_DB_VER, date.getTime());
            }

            mdb.beginTransaction();

            if (mdb.insert(TB_VERSION_TABLE, null, cv) > 0)
                returnCheck = true;
            else
                returnCheck = false;

            mdb.setTransactionSuccessful();
            mdb.endTransaction();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            returnCheck = false;
        }
        return returnCheck;
    }

    public static String checkDBVersion(Context context) {

        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();
        Cursor c = mdb.query(TB_VERSION_TABLE, new String[]{
                        TB_VERSION_DB_VER, TB_VERSION_MENUPRODUCT}, null, null, null,
                null, null);

        if (c != null && c.moveToFirst())
            return c.getString(c.getColumnIndex(TB_VERSION_DB_VER));
        else
            return "";
    }
}

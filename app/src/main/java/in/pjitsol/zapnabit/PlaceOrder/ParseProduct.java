package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Db.DBConstants;
import in.pjitsol.zapnabit.Db.POSDatabase;
import in.pjitsol.zapnabit.Db.TakeOrderDB;
import in.pjitsol.zapnabit.Entity.Category;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.Exception.SearchException;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Util.Logger;
import in.pjitsol.zapnabit.Util.Util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class ParseProduct extends StaticConstants {

    public Object ClientProductResponse(Context context, String response,
                                        ResultMessage resultMessage2) throws JSONException {

        List<Item> itemlist = new ArrayList<Item>();

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has(JSON_TAG_AUTHENTICATION)) {

            if (jsonObject.has(JSON_TAG_MESSAGE))
                resultMessage2.RESPONSE = jsonObject
                        .getString(JSON_TAG_MESSAGE);

            if (jsonObject.getString(JSON_TAG_AUTHENTICATION).equalsIgnoreCase(
                    StaticConstants.JSON_TAG_AUTH_TRUE)) {

                if (jsonObject.has(JSON_TAG_PR_ITEMS)) {

                    // Logger.i(JSON_TAG_PR_ITEMS,":"+jsonObject.toString()+":");

                    JSONObject jsonObjectItem = new JSONObject(
                            jsonObject.getString(JSON_TAG_PR_ITEMS));

                    JSONArray jsonCatArray = null, jsonProArray = null, jsonProAddon = null;

                    if (jsonObjectItem.has(JSON_TAG_PR_CAT_ARRAY))
                        jsonCatArray = new JSONArray(
                                jsonObjectItem.getString(JSON_TAG_PR_CAT_ARRAY));

                    int totalcat = 0;
                    if (jsonCatArray != null)
                        totalcat = jsonCatArray.length();

                    if (jsonObjectItem.has(JSON_TAG_PR_PRO_ARRAY))
                        jsonProArray = new JSONArray(
                                jsonObjectItem.getString(JSON_TAG_PR_PRO_ARRAY));

                    int totalpro = 0;
                    if (jsonProArray != null)
                        totalpro = jsonProArray.length();

                    if (jsonObjectItem.has(JSON_TAG_PR_PRO_AD_ARRAY))
                        jsonProAddon = new JSONArray(
                                jsonObjectItem
                                        .getString(JSON_TAG_PR_PRO_AD_ARRAY));

                    int totalproAddon = 0;
                    if (jsonProAddon != null)
                        totalproAddon = jsonProAddon.length();

                    Log.i("jsonpaddon", JSON_TAG_PR_PRO_AD_ARRAY
                            + totalproAddon);

                    for (int i = 0; i < totalcat; i++) {

                        JSONObject json = jsonCatArray.getJSONObject(i);
                        Category c = new Category();

                        fillCategory(c, json, i, totalcat);

                        itemlist.add(c);
                        Logger.i("jsonCatArray", json.toString());
                    }

                    for (int i = 0; i < totalpro; i++) {

                        JSONObject jsonpro = jsonProArray.getJSONObject(i);
                        Product pr = new Product();

                        fillProduct(pr, jsonpro, totalproAddon);

                        itemlist.add(pr);
                        Logger.i("jsonProArray", jsonpro.toString());
                    }

                    for (int j = 0; j < totalproAddon; j++) {

                        ProductAddOn praddon = new ProductAddOn();
                        JSONObject jsonproaddon = jsonProAddon.getJSONObject(j);

                        Logger.i("jsonProAddon", jsonProAddon.toString());
                        praddon.product = new Product();
                        fillProduct(praddon.product, jsonproaddon, -1);

                        itemlist.add(praddon);

                    }
                    Logger.i("db", ":dd:");
                    TakeOrderDB.saveData(context, itemlist, false);

                }

            }

        }

        return resultMessage2;

    }

    private static void fillCategory(Category c, JSONObject json, int i,
                                     int totalcat) throws JSONException {

        c.id = json.getInt(JSON_TAG_PR_CAT_ID);
        c.name = json.getString(JSON_TAG_PR_CAT_NAME)
                .toString();
        c.parentId = Integer.parseInt(json.getString(JSON_TAG_PR_CAT_PARENT));

        Log.i(":total:", ":total:" + totalcat + ", i:" + i);

        if ((totalcat - 1) == i)
            c.hasSubCategory = false;
        else
            c.hasSubCategory = true;

        if ((totalcat - 1) == i)
            c.hasProducts = true;
        else
            c.hasProducts = false;

        c.productImage = json.has(JSON_TAG_PR_PRO_PRODUCT_IMG) ? json
                .getString(JSON_TAG_PR_PRO_PRODUCT_IMG) : "";
        c.status = json.has(JSON_TAG_PR_CAT_STATUS) ? json
                .getString(JSON_TAG_PR_CAT_STATUS) : "0";


    }

    private static void fillProduct(Product pr, JSONObject jsonpro,
                                    int addonCount) throws NumberFormatException, JSONException {

        if (pr != null) {

            Log.i(":pr:", ":pr:" + pr);

            if (jsonpro.has(JSON_TAG_PR_PRO_ID))
                pr.id = Integer.parseInt(jsonpro.getString(JSON_TAG_PR_PRO_ID));

            if (jsonpro.has(JSON_TAG_PR_PRO_NAME))
                pr.name =
                        jsonpro.getString(JSON_TAG_PR_PRO_NAME).toString();
            ;

            if (jsonpro.has(JSON_TAG_PR_PRO_NET_PRICE))
                pr.netamount = Float.parseFloat(jsonpro
                        .getString(JSON_TAG_PR_PRO_NET_PRICE));


            if (jsonpro.has(JSON_TAG_PR_PRO_PARENT))
                pr.categoryId = Integer.parseInt(jsonpro
                        .getString(JSON_TAG_PR_PRO_PARENT));


            if (addonCount != -1 || addonCount > 0)
                pr.addOns = new ArrayList<ProductAddOn>();
            else
                pr.addOns = null;


            pr.status = jsonpro.has(JSON_TAG_PR_CAT_STATUS) ? jsonpro
                    .getString(JSON_TAG_PR_CAT_STATUS) : "0";

            pr.productImage = jsonpro.has(JSON_TAG_PR_PRO_PRODUCT_IMG) ? jsonpro
                    .getString(JSON_TAG_PR_PRO_PRODUCT_IMG) : "";
        } else {

            Log.i(":null:", "pr is null");
        }

    }

    public String parseAddProductResponse(Context context, String usr_userId,
                                          String rest, String items) {

        Logger.i(":Json >item:", items);
        JSONObject jsonreturn = null;
        try {

            jsonreturn = new JSONObject();

            List<Item> itemlist = new ArrayList<>();

            JSONObject jsonitem = new JSONObject(items);

            if (jsonitem.has(JSON_TAG_PR_ITEMS)) {

                Logger.i(JSON_TAG_PR_ITEMS, ":" + jsonitem.toString() + ":");
                JSONObject jsonObjectItem = new JSONObject(
                        jsonitem.getString(JSON_TAG_PR_ITEMS));

                // if (jsonObjectItem.has(JSON_TAG_PR_CAT_ARRAY))
                JSONArray jsonCatArray = new JSONArray(
                        jsonObjectItem.getString(JSON_TAG_PR_CAT_ARRAY));

                int totalcat = jsonCatArray.length();

                JSONArray jsonProArray = new JSONArray(
                        jsonObjectItem.getString(JSON_TAG_PR_PRO_ARRAY));

                int totalpro = jsonProArray.length();

                JSONArray jsonProAddon = new JSONArray(
                        jsonObjectItem.getString(JSON_TAG_PR_PRO_AD_ARRAY));

                int totalproAddon = jsonProAddon.length();

                Log.i("jsonpaddon", JSON_TAG_PR_PRO_AD_ARRAY + totalproAddon);

                int catId = 0, prId = 0;

                for (int i = 0; i < totalcat; i++) {

                    JSONObject json = jsonCatArray.getJSONObject(i);
                    Category c = new Category();

                    catId = Create_n_fillCategory(context, c, json, i, totalcat);

                    itemlist.add(c);
                    Logger.i("jsonCatArray", json.toString());
                }

                for (int i = 0; i < totalpro; i++) {

                    JSONObject jsonpro = jsonProArray.getJSONObject(i);
                    Product pr = new Product();

                    prId = Create_n_fillProduct(context, pr, jsonpro,
                            totalproAddon, catId);

                    itemlist.add(pr);
                    Logger.i("jsonProArray", jsonpro.toString());
                }

                for (int j = 0; j < totalproAddon; j++) {

                    ProductAddOn praddon = new ProductAddOn();
                    JSONObject jsonproaddon = jsonProAddon.getJSONObject(j);

                    Logger.i("jsonProAddon", jsonProAddon.toString());
                    praddon.product = new Product();
                    Create_n_fillProduct(context, praddon.product,
                            jsonproaddon, -1, prId);

                    itemlist.add(praddon);

                }
                TakeOrderDB.saveData(context, itemlist, false);

                jsonreturn.put(StaticConstants.JSON_TAG_PR_ITEMS,
                        CreateJsonFromItemList(itemlist, true));
                jsonreturn.put(StaticConstants.JSON_TAG_MESSAGE,
                        StaticConstants.JSON_TAG_MESSAGE);
                jsonreturn.put(StaticConstants.JSON_TAG_AUTHENTICATION,
                        StaticConstants.JSON_TAG_AUTH_TRUE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return jsonreturn.toString();
    }

    private static int Create_n_fillCategory(Context context, Category c,
                                             JSONObject json, int i, int totalcat) throws JSONException {

        int inCrement = TakeOrderDB.getlastAutoIncrement(context,
                DBConstants.CATEGORY_TABLE);

        Log.i("increment:", ":" + inCrement);
        if (inCrement == 0)
            inCrement = 1;

        c.id = inCrement + json.getInt(JSON_TAG_PR_CAT_ID);
        c.name = json.getString(JSON_TAG_PR_CAT_NAME);

        if (i == 0)
            c.parentId = Integer.parseInt(json
                    .getString(JSON_TAG_PR_CAT_PARENT));
        else
            c.parentId = inCrement
                    + Integer.parseInt(json.getString(JSON_TAG_PR_CAT_PARENT));

        Log.i(":total:", ":total:" + totalcat + ", i:" + i);

        if ((totalcat - 1) == i)
            c.hasSubCategory = false;
        else {
            c.hasSubCategory = true;
        }

        if ((totalcat - 1) == i)
            c.hasProducts = true;
        else
            c.hasProducts = false;

        return c.id;
    }

    private int Create_n_fillProduct(Context context, Product pr,
                                     JSONObject jsonpro, int addonCount, int catid) throws JSONException {

        if (pr != null) {

            Log.i(":pr:", ":pr:" + pr);

            int inCrement = TakeOrderDB.getlastAutoIncrement(context,
                    DBConstants.PRODUCT_TABLE);

            Log.i("increment:", ":" + inCrement);
            if (inCrement == 0)
                inCrement = 1;

            if (jsonpro.has(JSON_TAG_PR_PRO_ID))
                pr.id = inCrement
                        + Integer.parseInt(jsonpro
                        .getString(JSON_TAG_PR_PRO_ID));

            if (jsonpro.has(JSON_TAG_PR_PRO_NAME))
                pr.name = jsonpro.getString(JSON_TAG_PR_PRO_NAME);

            if (jsonpro.has(JSON_TAG_PR_PRO_NET_PRICE))
                pr.netamount = Float.parseFloat(jsonpro
                        .getString(JSON_TAG_PR_PRO_NET_PRICE));

            if (jsonpro.has(JSON_TAG_PR_PRO_VAT))
                pr.vat = jsonpro.getString(JSON_TAG_PR_PRO_VAT);

            if (jsonpro.has(JSON_TAG_PR_PRO_GROSS_PRICE))
                pr.grossamount = Float.parseFloat(jsonpro
                        .getString(JSON_TAG_PR_PRO_GROSS_PRICE));

            if (jsonpro.has(JSON_TAG_PR_PRO_PRODUCT_CODE))
                pr.productNum = jsonpro.getString(JSON_TAG_PR_PRO_PRODUCT_CODE);

            if (jsonpro.has(JSON_TAG_PR_PRO_PARENT))
                pr.categoryId = catid;

            if (jsonpro.has(JSON_TAG_PR_PRO_QUANTITY))
                pr.quantity = Integer.parseInt(jsonpro
                        .getString(JSON_TAG_PR_PRO_QUANTITY));

            if (addonCount != -1 || addonCount > 0)
                pr.addOns = new ArrayList<ProductAddOn>();
            else
                pr.addOns = null;

        } else {

            Log.i(":null:", "pr is null");
        }

        return pr.id;
    }

    public JSONObject CreateJsonFromItemList(List<Item> arrayListitem,
                                             boolean checkName) throws JSONException {

        JSONObject jsonItem = new JSONObject();
        JSONArray jsonCat = new JSONArray();
        JSONArray jsonProArray = new JSONArray();
        JSONArray jsonAddonArray = new JSONArray();

        for (int i = 0; i < arrayListitem.size(); i++) {

            Item item = arrayListitem.get(i);

            if (item instanceof Category) {

                Category ct = (Category) item;
                JSONObject jsonCatObject = new JSONObject();
                jsonCatObject.put(JSON_TAG_PR_CAT_ID, ct.id);

                if (checkName) {
                    jsonCatObject.put(JSON_TAG_PR_CAT_NAME, ct.name);
                } else {
                    jsonCatObject.put(JSON_TAG_PR_CAT_NAME,
                            Util.nameCreate(ct.name));
                }

                jsonCatObject.put(JSON_TAG_PR_CAT_PARENT, ct.parentId);

                jsonCat.put(jsonCatObject);
            } else if (item instanceof Product) {

                Product pr = (Product) item;
                JSONObject jsonObject2 = new JSONObject();

                jsonObject2.put(JSON_TAG_PR_PRO_ID, pr.id);

                if (checkName) {
                    jsonObject2.put(JSON_TAG_PR_PRO_NAME, pr.name);
                } else {
                    jsonObject2.put(JSON_TAG_PR_PRO_NAME,
                            Util.nameCreate(pr.name));
                }
                jsonObject2.put(JSON_TAG_PR_PRO_NET_PRICE, pr.netamount);
                jsonObject2.put(JSON_TAG_PR_PRO_VAT, pr.vat);
                jsonObject2.put(JSON_TAG_PR_PRO_GROSS_PRICE, pr.grossamount);

                jsonObject2.put(JSON_TAG_PR_PRO_PRODUCT_CODE, pr.productNum);
                jsonObject2.put(JSON_TAG_PR_PRO_PRODUCT_IMG, "img/" + pr.name
                        + ".png");
                jsonObject2.put(JSON_TAG_PR_PRO_PARENT, pr.categoryId);
                jsonObject2.put(JSON_TAG_PR_PRO_QUANTITY, "" + 1);

                jsonProArray.put(jsonObject2);

            } else if (item instanceof ProductAddOn) {

                ProductAddOn pAd = (ProductAddOn) item;

                Product pr = (Product) pAd.product;

                if (pr != null) {

                    JSONObject jsonObject2 = new JSONObject();

                    jsonObject2.put(JSON_TAG_PR_PRO_ID, pr.id);

                    jsonObject2.put(JSON_TAG_PR_PRO_NAME, pr.name);
                    jsonObject2.put(JSON_TAG_PR_PRO_NET_PRICE, pr.netamount);
                    jsonObject2.put(JSON_TAG_PR_PRO_VAT, pr.vat);
                    jsonObject2
                            .put(JSON_TAG_PR_PRO_GROSS_PRICE, pr.grossamount);

                    jsonObject2
                            .put(JSON_TAG_PR_PRO_PRODUCT_CODE, pr.productNum);
                    jsonObject2.put(JSON_TAG_PR_PRO_PRODUCT_IMG, "img/"
                            + pr.name + ".png");
                    jsonObject2.put(JSON_TAG_PR_PRO_PARENT, pr.categoryId);

                    jsonAddonArray.put(jsonObject2);
                }
            }

            jsonItem.put("proArray", jsonProArray);
            jsonItem.put("catArray", jsonCat);
            jsonItem.put("addonsArray", jsonAddonArray);

        }
        Logger.i("jsonItem create Import", jsonCat.toString());

        return jsonItem;

    }

    public String deleteProductParse(Context context, String response) {

        ResultMessage resultMessage = new ResultMessage();
        String[] split = response.split(",");

        if (TakeOrderDB.deleteProduct(context, split)) {

            resultMessage.STATUS = (int) StaticConstants.ASYN_RESULT_OK;
        } else {
            resultMessage.STATUS = SearchException.SERVER_DELAY;
            try {
                JSONObject object = new JSONObject();
                object.put(StaticConstants.JSON_TAG_MESSAGE, "No record found");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return resultMessage.toString();
        }

        return resultMessage.toString();
    }

    public String CreateParseMenuItem(Context context) {
        System.out.println("ParseProduct.CreateParseMenuItem()");
        SQLiteDatabase mdb = POSDatabase.getInstanceMenu(context)
                .getWritableDatabase();

        JSONObject objectItem = new JSONObject();
        List<Item> item = new ArrayList<Item>();
        mdb.beginTransaction();
        TakeOrderDB.getCategory(context, mdb, item);
        TakeOrderDB.getProduct(context, mdb, item);
        TakeOrderDB.getProductAddons(context, mdb, item);

        try {
            objectItem.put(JSON_TAG_PR_ITEMS, CreateJson(item));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            mdb.setTransactionSuccessful();
            mdb.endTransaction();
        }

        return objectItem.toString();
    }

    public static JSONObject CreateJson(List<Item> arrayListitem)
            throws JSONException {

        JSONObject jsonItem = new JSONObject();
        JSONArray jsonCat = new JSONArray();
        JSONArray jsonProArray = new JSONArray();
        JSONArray jsonAddonArray = new JSONArray();

        for (int i = 0; i < arrayListitem.size(); i++) {

            Item item = arrayListitem.get(i);

            if (item instanceof Category) {

                Category ct = (Category) item;
                JSONObject jsonCatObject = new JSONObject();
                jsonCatObject.put(JSON_TAG_PR_CAT_ID, ct.id);
                jsonCatObject.put(JSON_TAG_PR_CAT_NAME, ct.name);
                jsonCatObject.put(JSON_TAG_PR_CAT_PARENT, ct.parentId);
                jsonCatObject.put(JSON_TAG_PR_CAT_HAS_SUB_CAT,
                        ct.hasSubCategory ? 1 : 0);
                jsonCatObject.put(JSON_TAG_PR_CAT_HAS_PRD, ct.hasProducts ? 1
                        : 0);
                jsonCatObject.put(JSON_TAG_PR_CAT_VAT, ct.vat);

                jsonCat.put(jsonCatObject);

            } else if (item instanceof Product) {

                Product pr = (Product) item;
                JSONObject jsonObject2 = new JSONObject();

                jsonObject2.put(JSON_TAG_PR_PRO_ID, pr.id);
                jsonObject2.put(JSON_TAG_PR_PRO_NAME, pr.name);
                jsonObject2.put(JSON_TAG_PR_PRO_NET_PRICE, pr.netamount);
                jsonObject2.put(JSON_TAG_PR_PRO_GROSS_PRICE, pr.grossamount);
                jsonObject2.put(JSON_TAG_PR_PRO_PRODUCT_CODE, pr.productNum);
                jsonObject2.put(JSON_TAG_PR_PRO_VAT, pr.vat);
                jsonObject2.put(JSON_TAG_PR_PRO_PARENT, pr.categoryId);
                jsonObject2.put(JSON_TAG_PR_PRO_QUANTITY, pr.quantity);

                jsonProArray.put(jsonObject2);

            } else if (item instanceof ProductAddOn) {

                ProductAddOn pAd = (ProductAddOn) item;

                Product pr = (Product) pAd.product;

                if (pr != null) {

                    JSONObject jsonObject2 = new JSONObject();

                    jsonObject2.put(JSON_TAG_PR_PRO_ID, pr.id);
                    jsonObject2.put(JSON_TAG_PR_PRO_NAME, pr.name);
                    jsonObject2.put(JSON_TAG_PR_PRO_NET_PRICE, pr.netamount);
                    jsonObject2.put(JSON_TAG_PR_PRO_VAT, pr.vat);
                    jsonObject2
                            .put(JSON_TAG_PR_PRO_GROSS_PRICE, pr.grossamount);
                    jsonObject2.put(JSON_TAG_PR_PRO_PARENT, pr.categoryId);

                    jsonAddonArray.put(jsonObject2);
                }
            }

        }

        jsonItem.put("proArray", jsonProArray);
        jsonItem.put("catArray", jsonCat);
        jsonItem.put("addonsArray", jsonAddonArray);

        return jsonItem;

    }

    public Object parseMenu_item(Context context, String response,
                                 ResultMessage resultMessage) throws JSONException {

        List<Item> itemlist = new ArrayList<Item>();

        JSONObject jsonObject = new JSONObject(response);

        JSONObject jsonObjectItem = new JSONObject(
                jsonObject.getString(JSON_TAG_PR_ITEMS));

        JSONArray jsonCatArray = null, jsonProArray = null, jsonProAddon = null,
                jsonProAttribute=null,jsonProAttributeOption=null;

        if (jsonObjectItem.has(JSON_TAG_PR_CAT_ARRAY))
            jsonCatArray = new JSONArray(
                    jsonObjectItem.getString(JSON_TAG_PR_CAT_ARRAY));

        int totalcat = 0;
        if (jsonCatArray != null)
            totalcat = jsonCatArray.length();

        if (jsonObjectItem.has(JSON_TAG_PR_PRO_ARRAY))
            jsonProArray = new JSONArray(
                    jsonObjectItem.getString(JSON_TAG_PR_PRO_ARRAY));

        int totalpro = 0;
        if (jsonProArray != null)
            totalpro = jsonProArray.length();

        if (jsonObjectItem.has(JSON_TAG_PR_PRO_AD_ARRAY)) {

            if (!TextUtils.isEmpty(jsonObjectItem.getString(JSON_TAG_PR_PRO_AD_ARRAY))){
                Object object = new JSONTokener(jsonObjectItem.getString(JSON_TAG_PR_PRO_AD_ARRAY)).nextValue();
                if (object instanceof JSONArray)
                    jsonProAddon = new JSONArray(
                            jsonObjectItem.getString(JSON_TAG_PR_PRO_AD_ARRAY));
            }

        }

        if (jsonObjectItem.has(JSON_TAG_PR_PRO_ATTRI_ARRAY)) {


            if (!TextUtils.isEmpty(jsonObjectItem.getString(JSON_TAG_PR_PRO_ATTRI_ARRAY))){
                Object object = new JSONTokener(jsonObjectItem.getString(JSON_TAG_PR_PRO_ATTRI_ARRAY)).nextValue();
                if (object instanceof JSONArray)
                    jsonProAttribute= new JSONArray(
                            jsonObjectItem.getString(JSON_TAG_PR_PRO_ATTRI_ARRAY));
            }

        }

        if (jsonObjectItem.has(JSON_TAG_PR_PRO_ATTRIOPTION_ARRAY)) {
            if (!TextUtils.isEmpty(jsonObjectItem.getString(JSON_TAG_PR_PRO_ATTRIOPTION_ARRAY))){
                Object object = new JSONTokener(jsonObjectItem.getString(JSON_TAG_PR_PRO_ATTRIOPTION_ARRAY)).nextValue();
                if (object instanceof JSONArray)
                    jsonProAttributeOption= new JSONArray(
                            jsonObjectItem.getString(JSON_TAG_PR_PRO_ATTRIOPTION_ARRAY));
            }

        }

        int totalproAddon = 0;
        if (jsonProAddon != null)
            totalproAddon = jsonProAddon.length();

        int totalproAttri = 0;
        if (jsonProAttribute != null)
            totalproAttri = jsonProAttribute.length();


        int totalproAttriOption = 0;
        if (jsonProAttributeOption != null)
            totalproAttriOption = jsonProAttributeOption.length();


        for (int i = 0; i < totalcat; i++) {
            JSONObject json = jsonCatArray.getJSONObject(i);
            Category c = new Category();
            fillCategoryNew(c, json);
            itemlist.add(c);
        }

        for (int i = 0; i < totalpro; i++) {

            JSONObject jsonpro = jsonProArray.getJSONObject(i);
            Product pr = new Product();

            fillProductNew(pr, jsonpro);

            itemlist.add(pr);
        }

        for (int j = 0; j < totalproAddon; j++) {

            ProductAddOn praddon = new ProductAddOn();
            JSONObject jsonproaddon = jsonProAddon.getJSONObject(j);

            praddon.product = new Product();
            fillProductNew(praddon.product, jsonproaddon);

            itemlist.add(praddon);

        }

        for (int j = 0; j < totalproAttri; j++) {

            ProductAtrribute praddon = new ProductAtrribute();
            JSONObject jsonproaddon = jsonProAttribute.getJSONObject(j);
            fillProductAttri(praddon, jsonproaddon);
            itemlist.add(praddon);

        }

        for (int j = 0; j < totalproAttriOption; j++) {

            ProductAtrributeOption praddon = new ProductAtrributeOption();
            JSONObject jsonproaddon = jsonProAttributeOption.getJSONObject(j);
            fillProductAttriOption(praddon, jsonproaddon);
            itemlist.add(praddon);

        }



        TakeOrderDB.saveData(context, itemlist, false);
		/*ParseMealItems parseMeals = new ParseMealItems();
		parseMeals.parseMeal_item(context, response, resultMessage);*/
        resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
        resultMessage.TYPE = StaticConstants.FETCH_MENU;
        return resultMessage;

    }

    private void fillProductNew(Product pr, JSONObject jsonpro)
            throws JSONException {
        // TODO Auto-generated method stub

        if (jsonpro.has(JSON_TAG_PR_PRO_ID))
            pr.id = jsonpro.getInt(JSON_TAG_PR_PRO_ID);

        pr.name = jsonpro.has(JSON_TAG_PR_PRO_NAME) ?
                jsonpro.getString(DBConstants.PRODUCT_NAME).toString() : "";
        pr.productType = jsonpro.has(JSON_TAG_PR_PRO_TYPE) ?
                jsonpro.getString(DBConstants.PRODUCT_TYPE).toString() : "";
        pr.productDescription = jsonpro.has(JSON_TAG_PR_PRO_DES) ?
                jsonpro.getString(DBConstants.JSON_TAG_PR_PRO_DES).toString() : "";

        pr.netamount = jsonpro.has(JSON_TAG_PR_PRO_NET_PRICE) ? Float
                .parseFloat(jsonpro.getString(JSON_TAG_PR_PRO_NET_PRICE)) : 0;


        pr.categoryId = jsonpro.has(JSON_TAG_PR_PRO_PARENT) ? Integer
                .parseInt(jsonpro.getString(JSON_TAG_PR_PRO_PARENT)) : 0;

        pr.productImage = jsonpro.has(JSON_TAG_PR_PRO_PRODUCT_IMGNEW) ? jsonpro
                .getString(JSON_TAG_PR_PRO_PRODUCT_IMGNEW) : "";


    }

    private void fillProductAttri(ProductAtrribute pr, JSONObject jsonpro)
            throws JSONException {
        // TODO Auto-generated method stub

        if (jsonpro.has(JSON_TAG_PR_PRO_ID))
            pr.id = jsonpro.getInt(JSON_TAG_PR_PRO_ID);

        pr.name = jsonpro.has(JSON_TAG_PR_PRO_NAME) ?
                jsonpro.getString(DBConstants.PRODUCT_NAME).toString() : "";

        pr.attr_condition = jsonpro.has(JSON_TAG_PR_PRO_ATTR_CONDITION) ?
                jsonpro.getString(DBConstants.JSON_TAG_PR_PRO_ATTR_CONDITION).toString() : "";

        pr.display_order = jsonpro.has(JSON_TAG_PR_PRO_ATTR_DISPLAYORDER) ?
                jsonpro.getString(DBConstants.JSON_TAG_PR_PRO_ATTR_DISPLAYORDER).toString() : "";

        pr.categoryId = jsonpro.has(JSON_TAG_PR_PRO_PARENT) ? Integer
                .parseInt(jsonpro.getString(JSON_TAG_PR_PRO_PARENT)) : 0;




    }

    private void fillProductAttriOption(ProductAtrributeOption pr, JSONObject jsonpro)
            throws JSONException {
        // TODO Auto-generated method stub

        if (jsonpro.has(JSON_TAG_PR_PRO_ID))
            pr.id = jsonpro.getInt(JSON_TAG_PR_PRO_ID);

        pr.name = jsonpro.has(JSON_TAG_PR_PRO_NAME) ?
                jsonpro.getString(DBConstants.PRODUCT_NAME).toString() : "";
        pr.categoryId = jsonpro.has(JSON_TAG_PR_PRO_PARENT) ? Integer
                .parseInt(jsonpro.getString(JSON_TAG_PR_PRO_PARENT)) : 0;

        pr.netamount = jsonpro.has(JSON_TAG_PR_PRO_NET_PRICE) ? Float
                .parseFloat(jsonpro.getString(JSON_TAG_PR_PRO_NET_PRICE)) : 0;


    }

    private void fillCategoryNew(Category c, JSONObject json)
            throws JSONException {

        if (json.has(JSON_TAG_PR_CAT_ID))
            c.id = json.getInt(JSON_TAG_PR_CAT_ID);

        c.name = json.has(JSON_TAG_PR_CAT_NAME) ?
                json.getString(JSON_TAG_PR_CAT_NAME).toString() : "";
        ;

        c.parentId = json.has(JSON_TAG_PR_CAT_PARENT) ? Integer.parseInt(json
                .getString(JSON_TAG_PR_CAT_PARENT)) : -1;

        c.status = json.has(JSON_TAG_PR_CAT_STATUS) ? json
                .getString(JSON_TAG_PR_CAT_STATUS) : "0";

        c.hasSubCategory = json.has(JSON_TAG_PR_CAT_HAS_SUB_CAT) ? json
                .getString(JSON_TAG_PR_CAT_HAS_SUB_CAT).equalsIgnoreCase("1") ? true
                : false
                : false;
        c.hasProducts = json.has(JSON_TAG_PR_CAT_HAS_PRD) ? json.getString(
                JSON_TAG_PR_CAT_HAS_PRD).equalsIgnoreCase("1") ? true : false
                : false;
        c.productImage = json.has(JSON_TAG_PR_PRO_PRODUCT_IMG) ? json
                .getString(JSON_TAG_PR_PRO_PRODUCT_IMG) : "";
    }

    public boolean saveMenuItemLocally(Context context, List<Item> itemList) {
        if (TakeOrderDB.saveData(context, itemList))
            return true;
        else
            return false;
    }

	/*public String outOfStockProductResponse(Context context,
			HashMap<String, String> hashmap) {

		AuthStockNotificationTask stockNotificationTask = new AuthStockNotificationTask(
				context, hashmap);
		AsyncTaskTools.execute(stockNotificationTask);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(StaticConstants.JSON_TAG_AUTH,
					StaticConstants.JSON_TAG_AUTH_T);
			jsonObject.put(StaticConstants.JSON_TAG_MESSAGE, "Notify to All");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}*/
}

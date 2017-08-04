package in.pjitsol.zapnabit.BarCode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Category;
import in.pjitsol.zapnabit.Entity.CategoryProductinfo;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.CheckPriceCategoryDialog;
import in.pjitsol.zapnabit.Ui.CustomAutoCompleteTextView;
import in.pjitsol.zapnabit.Ui.IcallBackBarcode;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.AutoCompleteAdapter;

/**
 * Created by websnoox android on 4/11/2017.
 */

public class ProductInfo extends Fragment implements
        OnBackPressListener, View.OnClickListener, IAsyncTaskRunner,
        DialogInterface.OnCancelListener,AdapterView.OnItemSelectedListener,IcallBackBarcode {
    private LayoutInflater inflater;
    private Activity context;
    private TextView text_scan;
    private TextView text_scanpublish;
  //  private CustomAutoCompleteTextView btn_categories;
    private ImageView img_productimg;
    private TextView text_itemname;
    private TextView text_itemprice;
    private TextView text_itemDesc;
    private ImageLoader imageLoader;
    private TextView text_PublishExit;
    private TextView text_publishscannew;
    private ProgressHUD progressDialog;
    private String categoryId;
    private int type;
    private Spinner btn_categories;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.scanned_productinfo,
                container, false);
        this.inflater = inflater;
        imageLoader = new ImageLoader(context);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn_categories = (Spinner)view. findViewById(R.id.spinner);
        img_productimg = (ImageView) view.findViewById(R.id.img_productimg);
        text_itemname = (TextView) view.findViewById(R.id.text_itemname);
        text_itemprice = (TextView) view.findViewById(R.id.text_itemprice);
        text_itemDesc = (TextView) view.findViewById(R.id.text_itemDesc);
        text_PublishExit = (TextView) view.findViewById(R.id.text_PublishExit);
        text_publishscannew = (TextView) view.findViewById(R.id.text_publishscannew);
        imageLoader.DisplayImage(StaticConstants.SCANNEDPRODUCTINFO.images, img_productimg);

        text_itemname.setText(StaticConstants.SCANNEDPRODUCTINFO.title);
        text_itemDesc.setText(StaticConstants.SCANNEDPRODUCTINFO.description);
        text_itemprice.setText(StaticConstants.SCANNEDPRODUCTINFO.lowest_recorded_price);
        ArrayAdapter<CategoryProductinfo> dataAdapter = new
                ArrayAdapter<CategoryProductinfo>(context, android.R.layout.simple_spinner_item,
                StaticConstants.SCANNEDPRODUCTINFO.categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btn_categories.setAdapter(dataAdapter);
        btn_categories.setOnItemSelectedListener(this);

        text_PublishExit.setOnClickListener(this);
        text_publishscannew.setOnClickListener(this);
    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnScanBArcodeFragment();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_publishscannew:
                if (TextUtils.isEmpty(categoryId)) {
                    Toast.makeText(context, "Please select category", Toast.LENGTH_LONG).show();
                } else {
                    type = 0;
                    new CheckPriceCategoryDialog(context,this).show();

                }

                break;
            case R.id.text_PublishExit:
                if (TextUtils.isEmpty(categoryId)) {
                    Toast.makeText(context, "Please select category", Toast.LENGTH_LONG).show();
                } else {
                    type = 1;
                    new CheckPriceCategoryDialog(context,this).show();
                }
                break;
        }
    }

    protected void save_qrcode_info_in_menu() {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_PRODUCTNAME, text_itemname.getText().toString()
        );
        mHashMap.put(StaticConstants.JSON_PRODUCTPRICE, text_itemprice.getText().toString()
        );
        mHashMap.put(StaticConstants.JSON_PRODUCTDESCRIPTION, text_itemDesc.getText().toString()
        );
        mHashMap.put(StaticConstants.JSON_PRODUCTCATEGORY, "" + categoryId
        );
      /*  Bitmap bitmap = imageLoader.memoryCache.get(StaticConstants.SCANNEDPRODUCTINFO.images);
        String encodedImageData="";
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            encodedImageData = Base64.encodeToString(byteArray, Base64.DEFAULT);

        }*/
        mHashMap.put(StaticConstants.JSON_PRODUCTIMAGE, StaticConstants.SCANNEDPRODUCTINFO.images
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_SAVE_QRCODE_INFO_IN_MENU, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void taskStarting() {

    }

    @Override
    public void taskCompleted(Object o) {
        Toast.makeText(context,"Product published successfully",Toast.LENGTH_LONG).show();
        if (type == 0) {
            new IntentIntegrator(context).initiateScan();
        } else {
            ((BaseFragmentActivity) context).OnScanBArcodeFragment();
        }

    }

    @Override
    public void taskProgress(String urlString, Object o) {

    }

    @Override
    public void taskErrorMessage(Object o) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryId=((CategoryProductinfo)parent.getItemAtPosition(position)).id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void callbackBarcode() {
        if (type == 0) {
            ((BaseFragmentActivity) context).OnScanBArcodeFragment();
            save_qrcode_info_in_menu();
        } else {
            save_qrcode_info_in_menu();
        }
    }
}

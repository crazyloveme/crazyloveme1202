package in.pjitsol.zapnabit.BarCode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.TakePictureDialog;

/**
 * Created by websnoox android on 4/11/2017.
 */

public class TakePictureInfo extends Fragment implements
        OnBackPressListener, View.OnClickListener, IAsyncTaskRunner, DialogInterface.OnCancelListener {
    private LayoutInflater inflater;
    private Activity context;
    private TextView text_scan;
    private TextView text_scanpublish;
    private ProgressHUD progressDialog;
    private int value;
    private TextView text_takepicture;
    private ImageView img_clickedphoto;
    private EditText text_itemname;
    private TextView text_saveExit;
    private TextView text_savescannew;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.takepictureinfo,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        img_clickedphoto = (ImageView) view.findViewById(R.id.img_clickedphoto);
        img_clickedphoto.setImageBitmap(StaticConstants.BITMAP);
        text_itemname = (EditText) view.findViewById(R.id.text_itemname);
        text_saveExit = (TextView) view.findViewById(R.id.text_saveExit);
        text_savescannew = (TextView) view.findViewById(R.id.text_savescannew);
        text_saveExit.setOnClickListener(this);
        text_savescannew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_saveExit:
                if (TextUtils.isEmpty(text_itemname.getText().toString())) {
                    new TakePictureDialog(context).show();

                } else {
                    value = 1;
                    save_photo_info_in_temp();
                }
                break;
            case R.id.text_savescannew:
                if (TextUtils.isEmpty(text_itemname.getText().toString())) {
                    new TakePictureDialog(context).show();

                } else {
                    value = 0;
                    save_photo_info_in_temp();
                }
                break;
        }

    }

    protected void save_photo_info_in_temp() {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_PRODUCTNAME, text_itemname.getText().toString()
        );


        Bitmap bitmap = StaticConstants.BITMAP;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImageData = Base64.encodeToString(byteArray, Base64.DEFAULT);
        mHashMap.put(StaticConstants.JSON_PRODUCTIMAGE, encodedImageData
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_SAVE_PHOTO_INFO_IN_TEMP, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnTakePictureFragment();
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void taskStarting() {

    }

    private static final int CAMERA_REQUEST = 1888;

    @Override
    public void taskCompleted(Object o) {
        if (value == 0) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            ((BaseFragmentActivity) context).OnTakePictureFragment();
        }
    }

    @Override
    public void taskProgress(String urlString, Object o) {

    }

    @Override
    public void taskErrorMessage(Object o) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }
}

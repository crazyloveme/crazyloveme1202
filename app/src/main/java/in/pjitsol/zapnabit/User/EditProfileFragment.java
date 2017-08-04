package in.pjitsol.zapnabit.User;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Login.AutoCompleteAdapter;
import in.pjitsol.zapnabit.Login.IcityInfo;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.ChangePassword;
import in.pjitsol.zapnabit.Ui.CustomAutoCompleteTextView;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.RoundedImageView;
import in.pjitsol.zapnabit.Util.Util;

/**
 * Created by websnoox android on 2/21/2017.
 */

public class EditProfileFragment extends Fragment implements View.OnClickListener,
        OnBackPressListener, DialogInterface.OnCancelListener, IAsyncTaskRunner, IcityInfo {
    private LayoutInflater inflater;
    private Activity context;
    private TextView et_username;
    private TextView et_userPhoneno;
    private EditText et_city;
    private TextView txt_update;
    private ProgressHUD progressDialog;
    AuthCommanTask<HashMap<String, String>, ResultMessage> task;
    private TextView text_changepaswrd;
    private RoundedImageView img_usericon;
    private int RESULT_OK = -1;
    private String encodedImageData;
    private ImageLoader imageloader;
    private TextView txt_uploadImage;
    private TextView text_notice;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.userprofile,
                container, false);
        this.inflater = inflater;
        imageloader = new ImageLoader(context);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = activity;
    }

    private void initView(View view) {
        img_usericon = (RoundedImageView) view.findViewById(R.id.img_usericon);
        txt_uploadImage = (TextView) view.findViewById(R.id.txt_uploadImage);
        et_username = (TextView) view.findViewById(R.id.et_username);
        et_userPhoneno = (TextView) view.findViewById(R.id.et_userPhoneno);
        et_city = (EditText) view.findViewById(R.id.et_city);
        txt_update = (TextView) view.findViewById(R.id.txt_update);
        text_notice = (TextView) view.findViewById(R.id.text_notice);
        text_changepaswrd = (TextView) view.findViewById(R.id.text_changepaswrd);
        et_username.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_NAME));
        et_userPhoneno.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_PHONE));
        et_city.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_CITY));
        txt_update.setOnClickListener(this);
        imageloader.DisplayImage(PrefHelper.getStoredString
                (context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE), img_usericon);
        if (PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY).equalsIgnoreCase(StaticConstants.ZAPNABIT)) {
            img_usericon.setOnClickListener(this);
            txt_uploadImage.setOnClickListener(this);
            text_changepaswrd.setVisibility(View.VISIBLE);
            txt_uploadImage.setText("Upload Image");
            text_notice.setVisibility(View.GONE);
        } else if (PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY).equalsIgnoreCase(StaticConstants.FB)) {
            String fbid = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FB_ID);
            String fburl = "https://graph.facebook.com/" + fbid + "/picture?type=large";
            imageloader.DisplayImage(fburl, img_usericon);
            text_changepaswrd.setVisibility(View.GONE);
            text_notice.setVisibility(View.GONE);
            txt_uploadImage.setText("Image automatically re-loads when you login with Facebook.");
        } else {
            img_usericon.setOnClickListener(this);
            txt_uploadImage.setOnClickListener(this);
            text_changepaswrd.setVisibility(View.GONE);
            txt_uploadImage.setText("Upload Image");
            text_notice.setVisibility(View.VISIBLE);
        }
        text_changepaswrd.setOnClickListener(this);


    }

    protected void GEtAddress(String string) {
        //  new GetPlacesAsyncTask(context, string,EditProfile.this).execute();

    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnNearBySearchFragment();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_update:
                if (Util.isNetworkAvailable(context)) {
                    progressDialog = ProgressHUD.show(context,
                            getResources().getString(R.string.label_loading_refresh), true, true, this);
                    HashMap<String, String> mHashMap = new HashMap<String, String>();
                    mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_ID));
                    mHashMap.put(StaticConstants.JSON_PHONE, et_userPhoneno.getText().toString().trim());
                    mHashMap.put(StaticConstants.RES_JSON_USER_CITY, et_city.getText().toString().trim());
                    mHashMap.put(StaticConstants.RES_JSON_USER_NAME, et_username.getText().toString().trim());

                    mHashMap.put(StaticConstants.JSON_USER_IMAGE, encodedImageData);
                    @SuppressWarnings("unchecked")
                    AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                            context, EditProfileFragment.this,
                            BaseNetwork.obj().KEY_EDIT_PROFILE, progressDialog, StaticConstants.POST_METHOD);
                    task.execute(mHashMap);
                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_changepaswrd:
                new ChangePassword(context).show();

                break;
            case R.id.img_usericon:
                openGallery(SELECT_FILE1);
                break;
            case R.id.txt_uploadImage:
                openGallery(SELECT_FILE1);
                break;

            default:
                break;
        }

    }

    public void openGallery(int req_code) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        context.startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);
    }

    @Override
    public void taskStarting() {
        // TODO Auto-generated method stub

    }

    private static final int SELECT_FILE1 = 1;
    String selectedPath1 = "NONE";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void taskCompleted(Object result) {

        if (PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY)
                .equalsIgnoreCase(StaticConstants.ZAPNABIT)
                || PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_REGISTERBY)
                .equalsIgnoreCase(StaticConstants.GOGGLE)) {

            ((BaseFragmentActivity) context).UpdateUserInfo();
            imageloader.DisplayImage(PrefHelper.getStoredString
                    (context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE), img_usericon);
        }
        else
            ((BaseFragmentActivity) context).UpdateUserInfo();


    }

    @Override
    public void taskProgress(String urlString, Object progress) {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskErrorMessage(Object result) {
        // TODO Auto-generated method stub

    }

    @Override
    public Context getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCancel(DialogInterface arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void gotCityinfo() {
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1, StaticConstants.PlacesList);

        //et_city.setAdapter(adapter);

    }

    public void UpdateImage(Intent data) {
        Uri selectedImageUri = data.getData();
        selectedPath1 = getPath(selectedImageUri);
        File f = new File("" + selectedPath1);

        Bitmap bitmap = decodeFile(f);

        try {
            img_usericon.setImageBitmap(Util.modifyOrientation(bitmap, selectedPath1));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            encodedImageData = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,StaticConstants.LAST_IMAGE_DATA,encodedImageData);
            // encodedImageData = Util.encodeFileToBase64Binary(f.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
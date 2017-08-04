package in.pjitsol.zapnabit.BarCode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.ProgressHUD;

/**
 * Created by websnoox android on 4/11/2017.
 */

public class TakePicture extends Fragment implements
        OnBackPressListener, View.OnClickListener {
    private LayoutInflater inflater;
    private Activity context;
    private TextView text_scan;
    private TextView text_scanpublish;
    private ProgressHUD progressDialog;
    private int value;
    private TextView text_takepicture;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.takepicture,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        text_takepicture=(TextView)view.findViewById(R.id.text_takepicture);
        text_takepicture.setOnClickListener(this);
    }
    private static final int CAMERA_REQUEST = 1888;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_takepicture:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                context.startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            StaticConstants.BITMAP=photo;
            //OnDisplayPhotoAndNameFragment();

        }
    }*/

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnMerchantMyOrderFragment();
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= (Activity) context;
    }
}

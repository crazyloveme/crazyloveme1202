package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.R;

/**
 * Created by websnoox android on 4/3/2017.
 */

public class ProductImageDialog extends Dialog {

    private final ImageLoader imageLoader;
    Context context;
    String imageUrl;
    private ImageView productimage;
    private Bitmap bitmap;
    private ProgressBar progress_bar;

    public ProductImageDialog(Context context,String imageUrl)

    {
        super(context);
        this.context=getContext();
        this.imageUrl=imageUrl;
        imageLoader = new ImageLoader(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_dialog);
        productimage=(ImageView)findViewById(R.id.productimage);
        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        new parseImage().execute();
       // imageLoader.DisplayImage(imageUrl, productimage);

    }
    public class parseImage extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {

            try {
                String result = URLDecoder.decode(imageUrl, "UTF-8");
                URL url=new URL(result);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                // QrcodeImage.setImageBitmap(bitmap);
                // btarray.add(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return "hello";
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress_bar.setVisibility(View.GONE);
            productimage.setImageBitmap(bitmap);
        }

    }
}

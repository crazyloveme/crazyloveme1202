package in.pjitsol.zapnabit;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;


/**
 * Created by websnoox android on 1/17/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    String refreshedToken;
    @Override
    public void onTokenRefresh() {

        //Getting registration token
         refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        if(TextUtils.isEmpty(refreshedToken))
             refreshedToken = FirebaseInstanceId.getInstance().getToken();

        PrefHelper.storeString(getApplicationContext(), PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN,refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
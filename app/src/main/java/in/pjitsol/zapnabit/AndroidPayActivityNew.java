package in.pjitsol.zapnabit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Util.WalletUtil;

/**
 * Created by Bhawna on 7/28/2017.
 */

public class AndroidPayActivityNew extends FragmentActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SupportWalletFragment mWalletFragment;
    Order order;
    private static final int REQUEST_CODE_MASKED_WALLET = 1001;
    private MaskedWallet maskedWallet;
    private Button confirm_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        order = StaticConstants.CURRENT_ORDER;
        setContentView(R.layout.android_pay_base);
        InitializeWalletRequest();
    }
    public void InitializeWalletRequest(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .build())
                .enableAutoManage(this, this)
                .build();
        confirm_button=(Button)findViewById(R.id.confirm_button);
        showProgressDialog();
        Wallet.Payments.isReadyToPay(mGoogleApiClient).setResultCallback(
                new ResultCallback<BooleanResult>() {
                    @Override
                    public void onResult(@NonNull BooleanResult booleanResult) {
                        hideProgressDialog();

                        if (booleanResult.getStatus().isSuccess()) {
                            if (booleanResult.getValue()) {
                                Log.d("Ready to Pay-----", "isReadyToPay:true");
                                createAndAddWalletFragment();
                            }
                        }
                    }
                });
        confirm_button.setOnClickListener(this);
    }

    private void createAndAddWalletFragment() {
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_LIGHT)
                .setMode(WalletFragmentMode.BUY_BUTTON)
                .build();
        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
        MaskedWalletRequest maskedWalletRequest = WalletUtil.createMaskedWalletRequest(
                order,
                getString(R.string.public_key));
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(REQUEST_CODE_MASKED_WALLET)
                .setAccountName("Zapnabit Merchant");
        mWalletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
                .commit();
    }



    public static final int REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET = 1004;
    public void requestFullWallet() {
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
        showProgressDialog();

        FullWalletRequest fullWalletRequest = WalletUtil.createFullWalletRequest(order,
                maskedWallet.getGoogleTransactionId());
        Wallet.Payments.loadFullWallet(mGoogleApiClient, fullWalletRequest,
                REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        }
        switch (requestCode) {
            case REQUEST_CODE_MASKED_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            maskedWallet =
                                    data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                            Toast.makeText(this,"inside masked token",Toast.LENGTH_LONG).show();
                            //requestFullWallet();
                        }
                        break;
                    case WalletConstants.RESULT_ERROR:
                        handleError(errorCode);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        handleError(errorCode);
                        break;
                }
                break;

            case REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null && data.hasExtra(WalletConstants.EXTRA_FULL_WALLET)) {
                            FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                            // the full wallet can now be used to process the customer's payment
                            // send the wallet info up to server to process, and to get the result
                            // for sending a transaction status
                            fetchTransactionStatus(fullWallet);
                        } else if (data != null && data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {

                            Toast.makeText(this,"inside final masked token",Toast.LENGTH_LONG).show();
                            // re-launch the activity with new masked wallet information
                           /* mMaskedWallet =  data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                            mActivityLaunchIntent.putExtra(Constants.EXTRA_MASKED_WALLET,
                                    mMaskedWallet);
                            startActivity(mActivityLaunchIntent);*/
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // nothing to do here
                        break;
                    default:
                        handleError(errorCode);
                        break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void fetchTransactionStatus(FullWallet fullWallet) {
        hideProgressDialog();
        Toast.makeText(this,"inside final full token",Toast.LENGTH_LONG).show();
        //OrderPlacedASync(order,"AndroidPay");
        /*PaymentMethodToken token = fullWallet.getPaymentMethodToken();

        if (token != null) {

           // OrderPlacedASync(order,"AndroidPay");

        }*/
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    protected void handleError(int errorCode) {
        switch (errorCode) {
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                Toast.makeText(this, "Limit Excedded",
                        Toast.LENGTH_LONG).show();
                break;
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                // unrecoverable error
                String errorMessage = "Google wallet unavailable" + "\n" +
                        "Error Code"+errorCode;
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.confirm_button:
                requestFullWallet();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

package in.pjitsol.zapnabit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import java.util.HashMap;

/**
 * Created by Bhawna on 6/9/2017.
 */

public class CardActivity extends FragmentActivity {

    private Token recvToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.card_layout);

        Stripe stripe = new Stripe(this,"pk_test_FIoANU1P8fX3Mes0LxQqTCOS");
        Card card = new Card("4242424242424242", 5, 19, "123");

        if(card.validateCard()){
            Toast.makeText(this,"Card is Valid",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Card is InValid",Toast.LENGTH_SHORT).show();
        }
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        recvToken=token;
                        Toast.makeText(CardActivity.this,"Token Created",Toast.LENGTH_SHORT).show();
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(CardActivity.this,"Invalied error",Toast.LENGTH_SHORT).show();
                    }
                }
        );

       /* final HashMap<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", 400);
        chargeParams.put("currency", "usd");
        chargeParams.put("card", recvToken.getId());


        new AsyncTask<Void, Void, Void>() {
            Charge charge;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    com.stripe.Stripe.apiKey = "YOUR_TEST_STRIPE_SECRET_KEY";
                    charge = Charge.create(chargeParams);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                   *//* showAlert("Exception while charging the card!",
                            e.getLocalizedMessage());*//*
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                Toast.makeText(OnlinePaymentActivity.this,
                        "Card Charged : " + charge.getCreated() + "\nPaid : " +charge.getPaid(),
                        Toast.LENGTH_LONG
                ).show();
            };

        }.execute();*/

      /*  CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            mErrorDialogHandler.showError("Invalid Card Data");
        }*/
    }
}

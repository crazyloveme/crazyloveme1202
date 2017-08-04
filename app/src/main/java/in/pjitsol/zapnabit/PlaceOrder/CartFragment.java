package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Order;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.Product.ExtraNote;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Entity.SaleAbleItem;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.DeleteCartItemConfirmationDialog;
import in.pjitsol.zapnabit.Ui.IDeleteConfirmation;
import in.pjitsol.zapnabit.Ui.IDeleteVaribaleProduct;
import in.pjitsol.zapnabit.Ui.IPaymentType;
import in.pjitsol.zapnabit.Ui.MinOrderInfoDialog;
import in.pjitsol.zapnabit.Ui.OrderPlacedDialog;
import in.pjitsol.zapnabit.Ui.PaymentOptionDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.VariableProductDialog;
import in.pjitsol.zapnabit.Util.Logger;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Util.WalletUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

public class CartFragment extends Fragment implements OnBackPressListener,
        OnClickListener, IAsyncTaskRunner, OnCancelListener, IDeleteConfirmation,
        IDeleteVaribaleProduct, IQuantityCallback,IPaymentType, GoogleApiClient.OnConnectionFailedListener {


    private OrderAdapter adapter;
    private ListView list_View;
    private TextView text_subtotal;
    private TextView text_total;
    private TextView text_paynow;
    private LinearLayout linear_addon;
    private TextView text_productname;
    private TextView text_productprice;
    private ImageView img_plus;
    private ImageView img_minus;
    private TextView text_productqty;
    private LinearLayout linear_upperaddon;
    private LinearLayout linear_addonslayout;
    private EditText edt_spclinstruction;
    private TextView text_comission;
    private TextView text_taxes;
    private TextView text_addextra;
    private TextView text_fee;
    private TextView text_staticfee;
    private TextView text_payarrival;
    private boolean showDialog = false;
    private boolean BACK_PRESSED;
    private LinearLayout linear_attributeslayout;
    private LinearLayout linear_attrbuteon;
    private TextView text_productnameAttribute;
    private TextView text_productqtyAttribute;
    private TextView text_productDescAttribute;
    private ImageView img_plusattribute;
    private ImageView img_minusAttribute;
    private TextView text_addextraAttribute;
    private LinearLayout linear_topAttribute;
    private SaleAbleItem CurrentProduct;
    private ImageLoader imageLoader;
    private ImageView img_productAttribute;
    private LinearLayout linear_addadress;
    private RelativeLayout rel_updateadress;
    private TextView text_pickupOrderType;
    private TextView text_deliveryOrderType;
    private TextView text_name;
    private TextView text_editaddress;
    private TextView text_address;
    private TextView text_city;
    private EditText edit_name;
    private EditText edit_address;
    private EditText edit_city;
    private EditText edit_state;
    private Button cancelButton;
    private Button saveButton;
    private boolean UPDATE_ADDRESS;
    private TextView text_state;
    //private Button btn_okotp;
  //  private RadioGroup RadioGroup_logintype;
    private String paymentType;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cart_fragment, container, false);
        BACK_PRESSED = getArguments().getBoolean(StaticConstants.BACKPRESSED);
        initView(rootView);
        imageLoader = new ImageLoader(context);
        return rootView;
    }

    private void initView(View rootView) {



        list_View = (ListView) rootView.findViewById(R.id.listCheckOutItems);
        View footerview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.cart_bottomlayout, null, false);
        list_View.addFooterView(footerview);
        text_subtotal = (TextView) rootView.findViewById(R.id.text_subtotal);
        text_total = (TextView) rootView.findViewById(R.id.text_total);
        text_paynow = (TextView) rootView.findViewById(R.id.text_paynow);
        text_payarrival = (TextView) rootView.findViewById(R.id.text_payarrival);

        linear_addon = (LinearLayout) rootView.findViewById(R.id.linear_addon);
        linear_attributeslayout = (LinearLayout) rootView.findViewById(R.id.linear_attributeslayout);
        linear_attrbuteon = (LinearLayout) rootView.findViewById(R.id.linear_attrbuteon);

        text_productname = (TextView) rootView.findViewById(R.id.text_productname);
        text_productprice = (TextView) rootView.findViewById(R.id.text_productprice);
        text_productnameAttribute = (TextView) rootView.findViewById(R.id.text_productnameAttribute);
        text_productDescAttribute = (TextView) rootView.findViewById(R.id.text_productDescAttribute);
        text_productqtyAttribute = (TextView) rootView.findViewById(R.id.text_productqtyAttribute);
        img_productAttribute = (ImageView) rootView.findViewById(R.id.img_productAttribute);
        img_plus = (ImageView) rootView.findViewById(R.id.img_plus);
        img_minus = (ImageView) rootView.findViewById(R.id.img_minus);
        img_plusattribute = (ImageView) rootView.findViewById(R.id.img_plusattribute);
        img_minusAttribute = (ImageView) rootView.findViewById(R.id.img_minusAttribute);
        linear_topAttribute = (LinearLayout) rootView.findViewById(R.id.linear_topAttribute);
        text_addextraAttribute = (TextView) rootView.findViewById(R.id.text_addextraAttribute);


        text_productqty = (TextView) rootView.findViewById(R.id.text_productqty);
        linear_upperaddon = (LinearLayout) rootView.findViewById(R.id.linear_upperaddon);
        linear_addonslayout = (LinearLayout) rootView.findViewById(R.id.linear_addonslayout);
        edt_spclinstruction = (EditText) rootView.findViewById(R.id.edt_spclinstruction);
        text_comission = (TextView) rootView.findViewById(R.id.text_comission);
        text_fee = (TextView) rootView.findViewById(R.id.text_fee);
        text_staticfee = (TextView) rootView.findViewById(R.id.text_staticfee);
        text_taxes = (TextView) rootView.findViewById(R.id.text_taxes);
        text_addextra = (TextView) rootView.findViewById(R.id.text_addextra);

        linear_addadress = (LinearLayout) rootView.findViewById(R.id.linear_addadress);
        rel_updateadress = (RelativeLayout) rootView.findViewById(R.id.rel_updateadress);
        text_pickupOrderType = (TextView) rootView.findViewById(R.id.text_pickupOrderType);
        text_deliveryOrderType = (TextView) rootView.findViewById(R.id.text_deliveryOrderType);
        text_name = (TextView) rootView.findViewById(R.id.text_name);
        text_editaddress = (TextView) rootView.findViewById(R.id.text_editaddress);
        text_address = (TextView) rootView.findViewById(R.id.text_address);
        text_city = (TextView) rootView.findViewById(R.id.text_city);
        text_state = (TextView) rootView.findViewById(R.id.text_state);
        edit_name = (EditText) rootView.findViewById(R.id.edit_name);
        edit_address = (EditText) rootView.findViewById(R.id.edit_address);
        edit_city = (EditText) rootView.findViewById(R.id.edit_city);
        edit_state = (EditText) rootView.findViewById(R.id.edit_state);
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        saveButton = (Button) rootView.findViewById(R.id.saveButton);
       // btn_okotp = (Button)rootView. findViewById(R.id.btn_okotp);
        /*RadioGroup_logintype = (RadioGroup)rootView.findViewById(R.id.RadioGroup_logintype);
        RadioGroup_logintype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.paypal) {
                    paymentType = StaticConstants.PAYPAL;

                } else if (checkedId == R.id.card) {
                    paymentType = StaticConstants.CARD;
                }
                else{
                    paymentType = StaticConstants.ANDROIDPAY;
                }
            }

        });*/


       /* btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(paymentType)){
                    Toast.makeText(context,"Please select Payment Option.",Toast.LENGTH_SHORT).show();
                }
                else{
                   PaymentType(paymentType);
                }



            }
        });
*/
        linear_upperaddon.setOnClickListener(this);
        text_paynow.setOnClickListener(this);
        text_payarrival.setOnClickListener(this);
        text_addextra.setOnClickListener(this);
        img_plus.setOnClickListener(this);
        img_minus.setOnClickListener(this);
        img_plusattribute.setOnClickListener(this);
        img_minusAttribute.setOnClickListener(this);
        linear_topAttribute.setOnClickListener(this);
        text_addextraAttribute.setOnClickListener(this);
        text_pickupOrderType.setOnClickListener(this);
        text_deliveryOrderType.setOnClickListener(this);
        text_editaddress.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        if (PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_SDK_NAME).equalsIgnoreCase(StaticConstants.GOOGLE)) {
            text_paynow.setVisibility(View.GONE);
            text_deliveryOrderType.setBackgroundResource(R.drawable.disabled_rounded_grey);
        } else {
            text_deliveryOrderType.setBackgroundResource(R.drawable.rounded_delivery);
            text_paynow.setVisibility(View.VISIBLE);
        }


        adapter = new OrderAdapter();
        list_View.setAdapter(adapter);
        adapter.order = StaticConstants.CURRENT_ORDER;
        adapter.order.order_type = "";
        adapter.notifyDataSetChanged();
        updateUI(adapter.order);

        edt_spclinstruction.setText(PrefHelper.getStoredString
                (context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_CUD_SPECIAL));
    }

    private void updateUI(Order order) {
        adapter.order = order;
        adapter.notifyDataSetChanged();
        SetFinalItemPrice();

    }

    private void SetFinalItemPrice() {
        text_subtotal.setText(StaticConstants.DOLLAR + Util.numberfornmat(adapter.order.netprice));
        adapter.order.comission = Util.calculateComission(adapter.order.netprice, context);
        adapter.order.taxes = Util.calculateTaxes(adapter.order.netprice, context);


        // adapter.order.fee = Util.calculateFee(adapter.order.netprice, context);

        if (adapter.order.order_type.equalsIgnoreCase("delivery")) {
            text_fee.setVisibility(View.VISIBLE);
            text_staticfee.setVisibility(View.VISIBLE);
            String deliveryType = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.MERCHANT_DELIVERY_TYPE);
            if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_FREE)) {
                adapter.order.fee = Util.numberfornmat(0);
                text_fee.setText("Free");
            } else if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_FREEMIN)) {
                adapter.order.fee = Util.numberfornmat(0);
                text_fee.setText("Free");
            } else {

                String deliveryValue = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_DELIVERY_VALUE);
                adapter.order.fee = Util.numberfornmat(adapter.order.netprice * (Float.valueOf(deliveryValue) / 100));
                text_fee.setText(StaticConstants.DOLLAR
                        + adapter.order.fee);
            }



        } else {
            text_fee.setVisibility(View.GONE);
            text_staticfee.setVisibility(View.GONE);
        }


        text_comission.setText(StaticConstants.DOLLAR
                + adapter.order.comission);
        text_taxes.setText(StaticConstants.DOLLAR
                + adapter.order.taxes);

        text_total.setText(StaticConstants.DOLLAR +
                Util.CalculateTotalAmount(adapter.order));
    }


    private static final int PRODUCT_TAG = -1;
    static final String ARROW = "\u2192";
    int lastPositionSelected = -1;

    @Override
    public void ideleteConfirmation() {
        StaticConstants.CURRENT_ORDER = new Order();
        adapter.order = StaticConstants.CURRENT_ORDER;
        adapter.notifyDataSetChanged();
      //  updateUI(adapter.order);
        ((BaseFragmentActivity) context).HideCArtItem();
        ((BaseFragmentActivity) context).OnNearBySearchFragment();
    }

    @Override
    public void IdeleteVariableProduct() {
        linear_attrbuteon.startAnimation(Util.BottomToTopAnimation());
        linear_attrbuteon.setVisibility(View.GONE);
        StaticConstants.CURRENT_ORDER.item.remove(CurrentProduct);
        ((BaseFragmentActivity) context).ShowCArtItem();
    }

    @Override
    public void QuantityCallback() {

    }

    @Override
    public void PaymentType(String type) {
        if(type.equalsIgnoreCase(StaticConstants.PAYPAL)){
            ((BaseFragmentActivity)context).paypalPayment( Util.CalculateTotalAmount(adapter.order));
        }
        else if(type.equalsIgnoreCase(StaticConstants.CARD)){
            ((BaseFragmentActivity)context).OnCardFragment();
        }
        else
        {
           // wallet_button_holder.setVisibility(View.VISIBLE);
        }

    }

    class OrderAdapter extends BaseAdapter implements OnClickListener {
        private Order order;
        List<SaleAbleItem> items = new ArrayList<SaleAbleItem>();
        private int count;

        @Override
        public boolean isEnabled(int position) {
            // TODO Auto-generated method stub
            return position >= order.itemsEnabledFrom;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public SaleAbleItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Logger.i("ordermanager", "get view " + position);
            if (convertView == null) {
                ViewHolder holder = new ViewHolder();

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.order_product_row_cashier, null);
                holder.quantity = (TextView) convertView
                        .findViewById(R.id.quantitiy_cashier);
                holder.img_edititem = (ImageView) convertView.findViewById(R.id.img_edititem);
                holder.img_deleteitem = (ImageView) convertView.findViewById(R.id.img_deleteitem);
                holder.name = (TextView) convertView.findViewById(R.id.name_cashier);
                holder.price = (TextView) convertView.findViewById(R.id.price_cashier);

                convertView.setTag(holder);
            }
            ViewHolder hold = (ViewHolder) convertView.getTag();
            final SaleAbleItem item = items.get(position);

            // either product or addon
            Product pr = null;
            if (item instanceof Product) {

                hold.name.setText(item.name);
                pr = (Product) item;
                hold.quantity.setText("x" + String.valueOf(pr.quantity));
                double x = item.netamount * item.quantity;
                hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(x));
                hold.img_edititem.setTag(PRODUCT_TAG, pr);
                hold.img_deleteitem.setTag(PRODUCT_TAG, pr);
                convertView.setTag(PRODUCT_TAG, pr);


            } else if (item instanceof ProductAddOn) {
                ProductAddOn pAd = (ProductAddOn) item;
                hold.quantity.setText("x" + String.valueOf(pAd.quantity));
                hold.name.setText(ARROW + " " + item.name);
                double x = item.netamount * item.quantity;
                hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(x));
                hold.img_edititem.setTag(PRODUCT_TAG, pAd);
                hold.img_deleteitem.setTag(PRODUCT_TAG, pAd);
                convertView.setTag(PRODUCT_TAG, pAd);
            } else if (item instanceof ExtraNote) {
                ExtraNote note = (ExtraNote) item;
                if (((ExtraNote) item).product == null)
                    hold.name.setText(item.name);
                else

                    hold.name.setText(ARROW + " "
                            + item.name);
                hold.quantity.setText("x" + String.valueOf(note.quantity));
                hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(item.netamount));
                hold.img_edititem.setTag(PRODUCT_TAG, note);
                hold.img_deleteitem.setTag(PRODUCT_TAG, note);
                convertView.setTag(PRODUCT_TAG, note);
            } else if (item instanceof ProductAtrributeOption) {
                ProductAtrributeOption pAd = (ProductAtrributeOption) item;
                hold.quantity.setText("x" + String.valueOf(pAd.product.quantity));
                hold.name.setText(ARROW + pAd.name + "(" + pAd.productAttribute.name + ")");
                double x = pAd.netamount * pAd.product.quantity;
                hold.price.setText(StaticConstants.DOLLAR + Util.numberfornmat(x));
                hold.img_edititem.setTag(PRODUCT_TAG, pAd);
                hold.img_deleteitem.setTag(PRODUCT_TAG, pAd);
                hold.img_deleteitem.setVisibility(View.INVISIBLE);
                hold.img_edititem.setVisibility(View.INVISIBLE);
                convertView.setTag(PRODUCT_TAG, pAd);
            }

            hold.img_edititem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Object tag = v.getTag(PRODUCT_TAG);
                    if (item instanceof Product) {
                        if (((Product) item).productType.equalsIgnoreCase("product"))
                            UpdateItemACtion(tag);
                        else
                            UpdateItemACtionAttribute(tag);
                    } else if (item instanceof ProductAtrributeOption)
                        UpdateItemACtionAttribute(((ProductAtrributeOption) tag).product);
                    else
                        UpdateItemACtion(tag);

                }
            });
            hold.img_deleteitem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Object tag = v.getTag(PRODUCT_TAG);


                    if (tag instanceof Product) {
                        order.item.remove(tag);
                    } else if (tag instanceof ProductAtrributeOption) {
                        ProductAtrributeOption item = (ProductAtrributeOption) tag;
                        ProductAtrribute product = item.productAttribute;
                        for (int i = 0; i < product.productAttributesOptions.size(); i++) {
                            if (product.productAttributesOptions.get(i).id == item.id) {
                                product.productAttributesOptions.remove(tag);
                                item.product.selectedAttributeOptions.remove(tag);
                            }


                        }

                        ((BaseFragmentActivity) context).ShowCArtItem();
                        notifyDataSetChanged();
                        SetFinalItemPrice();
                    } else {
                        ProductAddOn item = (ProductAddOn) tag;
                        Product product = item.product;

                        Iterator<ProductAddOn> itr = product.selectedAddon.iterator();
                        while (itr.hasNext()) {
                            ProductAddOn currentaddon = (ProductAddOn) itr.next();
                            if (currentaddon.id == item.id) {
                                product.selectedAddon.remove(tag);
                            }

                        }
                        for (int i = 0; i < product.productAddOns.size(); i++) {
                            if (product.productAddOns.get(i).id == item.id)
                                product.productAddOns.remove(tag);

                        }
                    }
                    ((BaseFragmentActivity) context).ShowCArtItem();
                    notifyDataSetChanged();
                    SetFinalItemPrice();
                }
            });

            return convertView;
        }


        @Override
        public void notifyDataSetChanged() {
            float totalPric = 0.00f;
            float totalgross = 0.00f;
            int totalitem = 00;
            count = 0;
            items.clear();
            if (order != null) {
                for (int i = 0; i < order.item.size(); i++) {
                    if (order.item.get(i) instanceof Product) {
                        Product p = (Product) order.item.get(i);

                        if (p.productType.equalsIgnoreCase("product")) {
                            totalPric = totalPric + (p.netamount * p.quantity);
                            totalgross = totalgross + (p.grossamount * p.quantity);
                            items.add(p);
                            totalitem += p.quantity;
                            count++;
                            if (p.selectedAddon != null && p.selectedAddon.size() > 0) {
                                for (ProductAddOn ad : p.selectedAddon) {
                                    totalPric = totalPric
                                            + (ad.quantity * ad.netamount);
                                    totalgross = totalgross
                                            + (ad.grossamount * ad.quantity);
                                    items.add(ad);
                                    count++;
                                }
                            } else {
                                if (p.productAddOns != null && p.productAddOns.size() > 0) {
                                    for (ProductAddOn ad : p.productAddOns) {
                                        if (ad.quantity > 0) {
                                            totalPric = totalPric
                                                    + (ad.quantity * ad.netamount);
                                            totalgross = totalgross
                                                    + (ad.grossamount * ad.quantity);
                                            items.add(ad);
                                            count++;
                                        }

                                    }
                                }
                            }

                            if (p.extraNote != null) {
                                for (ExtraNote note : p.extraNote) {
                                    totalPric = totalPric + (note.netamount);
                                    totalgross = totalgross + (note.netamount);
                                    count++;
                                    items.add(note);
                                }
                            }

                        } else {
                            boolean variableProductAdded = false;
                            if (p.productAttributes != null && p.productAttributes.size() > 0) {
                                for (int j = 0; j < p.productAttributes.size(); j++) {
                                    ProductAtrribute attri = p.productAttributes.get(j);
                                    if (attri.optionSelected) {
                                        if (!variableProductAdded) {
                                            variableProductAdded = true;
                                            items.add(p);
                                            count++;
                                        }
                                        if (attri.productAttributesOptions != null &&
                                                attri.productAttributesOptions.size() > 0) {
                                            for (int k = 0; k < attri.productAttributesOptions.size(); k++) {
                                                ProductAtrributeOption option = attri.productAttributesOptions.get(k);
                                                if (option.optionSelected) {
                                                    totalPric = totalPric
                                                            + (p.quantity * option.netamount);
                                                    totalgross = totalgross
                                                            + (p.grossamount * option.quantity);
                                                    option.productAttribute = attri;
                                                    option.product = p;
                                                    items.add(option);
                                                    count++;
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else if (order.item.get(i) instanceof ExtraNote) {
                        ExtraNote note = (ExtraNote) order.item.get(i);
                        totalPric = totalPric + (note.netamount);
                        totalgross = totalgross + (note.netamount);
                        count++;
                        items.add(note);
                    }

                }
            }

            super.notifyDataSetChanged();
            if (order != null) {
                order.netprice = totalPric;
                order.grossprice = totalgross;
            }
            /*if (!TextUtils.isEmpty(editFee.getText().toString())) {
                totalPric += Float.valueOf(editFee.getText().toString());
				totalgross += Float.valueOf(editFee.getText().toString());
			}

			totalPrice.setText("�" + Util.numberfornmat(totalPric));
			totalItem.setText(""
					+ (totalitem < 10 ? "0" + totalitem : totalitem));*/
            list_View.setSelection(count);

            super.notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
        }
    }

    protected void UpdateItemACtion(Object tag) {
        final SaleAbleItem item = (SaleAbleItem) tag;
        Animation bottomUp = AnimationUtils.loadAnimation(context,
                R.animator.bottom_up);
        linear_addon.startAnimation(bottomUp);
        linear_addon.setVisibility(View.VISIBLE);
        text_productname.setText(item.name);
        text_productprice.setText(Util.numberfornmat(item.quantity * item.netamount));
        text_productqty.setText("" + item.quantity);


        if (item.productAddOns != null) {
            if (item.productAddOns.size() > 0) {
                for (int i = 0; i < item.productAddOns.size(); i++) {
                    final View viewaddonItems = LayoutInflater.from(context).inflate(
                            R.layout.addon_item, null);
                    final TextView text_productprice = (TextView) viewaddonItems.findViewById(R.id.text_productprice);
                    TextView text_productname = (TextView) viewaddonItems.findViewById(R.id.text_productname);
                    final TextView text_addonqty = (TextView) viewaddonItems.findViewById(R.id.text_addonqty);
                    ImageView img_plus = (ImageView) viewaddonItems.findViewById(R.id.img_plus);
                    ImageView img_minus = (ImageView) viewaddonItems.findViewById(R.id.img_minus);
                    text_productname.setText(item.productAddOns.get(i).name);
                    text_productprice.setText(StaticConstants.DOLLAR + String.valueOf(Util.numberfornmat(item.productAddOns.get(i).netamount)));
                    text_addonqty.setText(String.valueOf(item.productAddOns.get(i).quantity));


                    viewaddonItems.setTag(item.productAddOns.get(i));
                    linear_addonslayout.addView(viewaddonItems);


                    img_plus.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (((Product) item).quantity > 0) {
                                ProductAddOn Addon = (ProductAddOn) viewaddonItems.getTag();
                                Addon.quantity++;
                                if (Addon.quantity == 1) {
                                    ((Product) item).selectedAddon.add(Addon);
                                } else {
                                    for (ProductAddOn ad : ((Product) item).selectedAddon) {
                                        if (ad.id == Addon.id) {
                                            ad.quantity = Addon.quantity;
                                        }
                                    }
                                }
                                text_addonqty.setText(String.valueOf(Addon.quantity));
                                text_productprice.setText(Util.numberfornmat(Util.calculateItemTotalPrice(Addon.quantity,
                                        Addon.netamount)));
                                ((BaseFragmentActivity) context).ShowCArtItem();
                                adapter.notifyDataSetChanged();
                                SetFinalItemPrice();

                            }


                        }
                    });


                    img_minus.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            ProductAddOn Addon = (ProductAddOn) viewaddonItems.getTag();
                            if (Addon.quantity > 1) {
                                Addon.quantity--;
                                text_addonqty.setText(String.valueOf(Addon.quantity));
                                text_productprice.setText(Util.numberfornmat(Util.calculateItemTotalPrice(Addon.quantity,
                                        Addon.netamount)));
                                ((BaseFragmentActivity) context).ShowCArtItem();
                            } else if (Addon.quantity == 1) {
                                ProductAddOn removewAddon = null;
                                Addon.quantity--;
                                for (ProductAddOn ad : ((Product) item).selectedAddon) {
                                    if (ad.id == Addon.id) {
                                        removewAddon = ad;
                                    }
                                }
                                ((Product) item).selectedAddon.remove(removewAddon);
                                text_addonqty.setText(String.valueOf(Addon.quantity));
                                text_productprice.setText(Util.numberfornmat(
                                        Addon.netamount));
                                ((BaseFragmentActivity) context).ShowCArtItem();
                                adapter.notifyDataSetChanged();

                            }
                            SetFinalItemPrice();
                        }
                    });


                }
            }
        }

        if (tag instanceof ProductAddOn) {
            img_plus.setTag(tag);
            img_minus.setTag(tag);
        } else if (tag instanceof Product) {
            img_plus.setTag(tag);
            img_minus.setTag(tag);
        } else if (tag instanceof ExtraNote) {
            img_plus.setTag(tag);
            img_minus.setTag(tag);
        }


    }


    protected void UpdateItemACtionAttribute(Object tag) {
        CurrentProduct = (SaleAbleItem) tag;
        final SaleAbleItem item = (SaleAbleItem) tag;
        Animation bottomUp = AnimationUtils.loadAnimation(context,
                R.animator.bottom_up);
        linear_attrbuteon.startAnimation(bottomUp);
        linear_attrbuteon.setVisibility(View.VISIBLE);
        linear_attributeslayout.removeAllViews();


        text_productnameAttribute.setText(item.name);
        text_productDescAttribute.setText(item.productDescription);
        text_productqtyAttribute.setText("" +
                ((Product) item).quantity);

        imageLoader.DisplayImage(CurrentProduct.productImage, img_productAttribute);

        for (int i = 0; i < item.productAttributes.size(); i++) {
            final ProductAtrribute currentAttribute = item.productAttributes.get(i);
            final View viewaddonItems = LayoutInflater.from(context).inflate(
                    R.layout.option_spinner_item, null);
            TextView text_attributename = (TextView) viewaddonItems.findViewById(R.id.text_attributename);
            Spinner spinnerOption = (Spinner) viewaddonItems.findViewById(R.id.spinnerOption);
            text_attributename.setText(currentAttribute.name);

            final CustomSpinnerAdapter optionAdapter = new
                    CustomSpinnerAdapter(context, currentAttribute.productAttributesOptions, currentAttribute, item, this);
            spinnerOption.setAdapter(optionAdapter);

            linear_attributeslayout.addView(viewaddonItems);
            img_plusattribute.setTag(tag);
            img_minusAttribute.setTag(tag);


        }
    }

    private FragmentActivity context;
    private ProgressHUD progressDialog;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = (FragmentActivity) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                UPDATE_ADDRESS = false;
                rel_updateadress.setVisibility(View.GONE);
                linear_addadress.setVisibility(View.GONE);
                break;
           /* case R.id.btn_androidpay:
               startActivity(new Intent(context, AndroidPayActivityNew.class));
                break;*/
            case R.id.saveButton:
                HashMap<String, String> par = new HashMap<String, String>();
                par.put("user_id", PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.USER_ID));
                par.put(StaticConstants.JSON_TAG_DELIVERY_NAME,
                        edit_name.getText().toString());
                par.put(StaticConstants.JSON_TAG_DELIVERY_ADDRESS,
                        edit_address.getText().toString());
                par.put(StaticConstants.JSON_TAG_DELIVERY_CITY,
                        edit_city.getText().toString());
                par.put(StaticConstants.JSON_TAG_DELIVERY_STATE,
                        edit_state.getText().toString());
                if (UPDATE_ADDRESS)
                    par.put(StaticConstants.RES_JSON_USER_DELIVERY_ID,
                            PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                    StaticConstants.RES_JSON_USER_DELIVERY_ID));
                progressDialog = ProgressHUD.show(context,
                        getResources().getString(R.string.label_loading_refresh), true, true, this);
                AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                        context, CartFragment.this,
                        BaseNetwork.obj().KEY_ADD_ADDRESS, progressDialog, StaticConstants.POST_METHOD);
                task.execute(par);
                break;

            case R.id.text_pickupOrderType:
                Toast.makeText(context, "Please re-check your Special Instructions.", Toast.LENGTH_SHORT).show();
                adapter.order.order_type = "pickup";
                rel_updateadress.setVisibility(View.GONE);
                linear_addadress.setVisibility(View.GONE);
                text_pickupOrderType.setBackgroundResource(R.drawable.rounded_deliveryselected);
                text_pickupOrderType.setTextColor(ContextCompat.getColor(context, R.color.white));
                if (!PrefHelper.getStoredString(context,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_SDK_NAME).equalsIgnoreCase(StaticConstants.GOOGLE)) {
                    text_deliveryOrderType.setBackgroundResource(R.drawable.rounded_delivery);
                    text_deliveryOrderType.setTextColor(ContextCompat.getColor(context, R.color.red));
                }

                SetFinalItemPrice();

                break;
            case R.id.text_editaddress:
                UPDATE_ADDRESS = true;
                linear_addadress.setVisibility(View.VISIBLE);
                rel_updateadress.setVisibility(View.GONE);
                edit_name.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.RES_JSON_USER_DELIVERY_NAME));
                edit_address.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS));
                edit_city.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.RES_JSON_USER_DELIVERY_CITY));
                edit_state.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.RES_JSON_USER_DELIVERY_STATE));
                break;
            case R.id.text_deliveryOrderType:
                Toast.makeText(context, "Please re-check your Special Instructions.", Toast.LENGTH_SHORT).show();
                adapter.order.order_type = "";
                text_pickupOrderType.setBackgroundResource(R.drawable.rounded_delivery);
                text_pickupOrderType.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (PrefHelper.getStoredString(context,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_SDK_NAME).equalsIgnoreCase(StaticConstants.GOOGLE)) {

                    Toast.makeText(context, getString(R.string.nodelivery), Toast.LENGTH_SHORT).show();
                } else {
                    String deliveryType = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.MERCHANT_DELIVERY_TYPE);
                    if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_NOTSET)) {
                        Toast.makeText(context, "Sorry, deliveries are not available at this time. Check again later.", Toast.LENGTH_SHORT).show();
                    } else if (deliveryType.equalsIgnoreCase(StaticConstants.JSON_MERCHANT_DELIVERY_TYPE_FREEMIN)) {
                        String minOrderValue = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.JSON_MERCHANT_DELIVERY_MIN_FEE);
                        if (adapter.order.netprice >= Float.valueOf(minOrderValue)) {

                            SetdeliveryOrder();
                        } else {
                            new MinOrderInfoDialog(context, minOrderValue).show();
                        }

                    } else {
                        SetdeliveryOrder();
                    }
                }
                break;
            case R.id.text_paynow:
               // InitializeWalletRequest(finalView);
                showDialog = false;
                adapter.order.specialInstru = edt_spclinstruction.getText().toString();
                if (adapter.order.netprice > 0)
                    if (!TextUtils.isEmpty(adapter.order.order_type)){
                        StaticConstants.CURRENT_ORDER=adapter.order;

                        ((BaseFragmentActivity)context).performPaymentFunction();

                       //paymentOptionLayout.setVisibility(View.VISIBLE);
                       // new PaymentOptionDialog(context,this).show();
                       // ((BaseFragmentActivity)context).paypalPayment(adapter.order.netprice+"");
                       // ((BaseFragmentActivity)context).OnCardFragment();
                    }
                    else
                        Toast.makeText(context, "Please select Pickup or Delivery first.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Please add an item in the cart.", Toast.LENGTH_SHORT).show();

                break;
            case R.id.text_payarrival:

                showDialog = true;
                adapter.order.specialInstru = edt_spclinstruction.getText().toString();
                if (adapter.order.netprice > 0)
                    if (!TextUtils.isEmpty(adapter.order.order_type)){
                        OrderPlacedASync(adapter.order,"payonarrival");
                    }
                    else
                        Toast.makeText(context, "Please select Pickup or Delivery first.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Please add an item in the cart.", Toast.LENGTH_SHORT).show();


                break;
            case R.id.text_addextra:
                linear_addon.startAnimation(Util.TopToBottomAnimation());
                linear_addon.setVisibility(View.GONE);
                linear_addonslayout.removeAllViews();
                break;
            case R.id.img_plusattribute:
                IncreaseQuantityAttribute((SaleAbleItem) v.getTag());
                break;
            case R.id.img_minusAttribute:
                DecreseQuantityAttribute((SaleAbleItem) v.getTag());
                break;
            case R.id.img_plus:
                SaleAbleItem currentItem = (SaleAbleItem) v.getTag();

                if (currentItem instanceof Product) {
                    text_productprice.setText(Util.numberfornmat(currentItem.quantity * currentItem.netamount));
                    text_productqty.setText("" + currentItem.quantity);
                    for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                        if (((Product) currentItem).id == StaticConstants.CURRENT_ORDER.item.get(k).id) {
                            ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity++;
                        }
                    }
                } else {

                    ProductAddOn currentAddon = (ProductAddOn) currentItem;
                    Product currentProduct = currentAddon.product;
                    for (ProductAddOn ad : currentProduct.selectedAddon) {
                        if (ad.id == currentAddon.id) {
                            ad.quantity++;
                        }
                    }

                }

                ((BaseFragmentActivity) context).ShowCArtItem();
                adapter.notifyDataSetChanged();
                SetFinalItemPrice();
                break;
            case R.id.img_minus:
                SaleAbleItem currentItem1 = (SaleAbleItem) v.getTag();
                if (currentItem1 instanceof Product) {
                    if (((Product) currentItem1).quantity > 1) {
                        currentItem1.quantity--;
                        text_productprice.setText(Util.numberfornmat(currentItem1.quantity * currentItem1.netamount));
                        text_productqty.setText("" + currentItem1.quantity);
                    } else if (((Product) currentItem1).quantity == 1) {
                        currentItem1.quantity--;
                        text_productqty.setText(String.valueOf(((Product) currentItem1).quantity));
                        text_productprice.setText(Util.numberfornmat(
                                ((Product) currentItem1).netamount));
                    }
                } else {
                    ProductAddOn currentAddon = (ProductAddOn) currentItem1;
                    Product currentProduct = currentAddon.product;
                    for (ProductAddOn ad : currentProduct.selectedAddon) {
                        if (ad.id == currentAddon.id) {
                            ad.quantity--;
                        }
                    }

                }
                ((BaseFragmentActivity) context).ShowCArtItem();
                adapter.notifyDataSetChanged();
                SetFinalItemPrice();
                break;
            case R.id.linear_upperaddon:
                linear_addon.startAnimation(Util.TopToBottomAnimation());
                linear_addon.setVisibility(View.GONE);
                linear_addonslayout.removeAllViews();
                break;
            case R.id.linear_topAttribute:

                boolean closeTheDialog = false;
                for (int i = 0; i < CurrentProduct.productAttributes.size(); i++) {
                    if (Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition) == 1
                            ) {
                        if (Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition)
                                == CurrentProduct.productAttributes.get(i).optionCount
                                ||
                                Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition)
                                        < CurrentProduct.productAttributes.get(i).optionCount
                                ) {
                            closeTheDialog = true;

                        } else {
                            closeTheDialog = false;
                            break;
                        }
                    } else if (Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition) == 2) {
                        if (CurrentProduct.productAttributes.get(i).optionCount >= 1
                                ) {
                            closeTheDialog = true;

                        } else {
                            closeTheDialog = false;
                            break;

                        }
                    } else
                        closeTheDialog = true;

                }

                if (closeTheDialog) {
                    linear_attrbuteon.startAnimation(Util.BottomToTopAnimation());
                    linear_attrbuteon.setVisibility(View.GONE);
                    ((BaseFragmentActivity) context).OnCartFragment(new Bundle());
                } else {
                    VariableProductDialog dialog = new VariableProductDialog(context, this);
                    dialog.show();
                }


                break;
            case R.id.text_addextraAttribute:
                boolean closeTheDialog1 = false;
                for (int i = 0; i < CurrentProduct.productAttributes.size(); i++) {
                    if (Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition) == 1
                            ) {
                        if (Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition)
                                == CurrentProduct.productAttributes.get(i).optionCount
                                ||
                                Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition)
                                        < CurrentProduct.productAttributes.get(i).optionCount
                                ) {
                            closeTheDialog1 = true;

                        } else {
                            closeTheDialog1 = false;
                            break;
                        }
                    } else if (Integer.valueOf(CurrentProduct.productAttributes.get(i).attr_condition) == 2) {
                        if (CurrentProduct.productAttributes.get(i).optionCount >= 1
                                ) {
                            closeTheDialog1 = true;

                        } else {
                            closeTheDialog1 = false;
                            break;

                        }
                    } else
                        closeTheDialog1 = true;

                }

                if (closeTheDialog1) {
                    linear_attrbuteon.startAnimation(Util.BottomToTopAnimation());
                    linear_attrbuteon.setVisibility(View.GONE);
                    ((BaseFragmentActivity) context).OnCartFragment(new Bundle());

                } else {
                    VariableProductDialog dialog = new VariableProductDialog(context, this);
                    dialog.show();
                }
              /*  linear_attrbuteon.startAnimation(Util.TopToBottomAnimation());
                linear_attrbuteon.setVisibility(View.GONE);*/
                break;
            default:
                break;
        }

    }

    public void SetdeliveryOrder() {
        adapter.order.order_type = "delivery";
        text_deliveryOrderType.setBackgroundResource(R.drawable.rounded_deliveryselected);
        text_deliveryOrderType.setTextColor(ContextCompat.getColor(context, R.color.white));

        if (PrefHelper.getStoredBoolean(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.ADDRESS_PRESENT)) {
            rel_updateadress.setVisibility(View.VISIBLE);
            linear_addadress.setVisibility(View.GONE);
            text_name.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_NAME));
            text_address.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS));
            text_city.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_CITY));
            text_state.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_STATE));

        } else {
            UPDATE_ADDRESS = false;
            rel_updateadress.setVisibility(View.GONE);
            linear_addadress.setVisibility(View.VISIBLE);
        }
        SetFinalItemPrice();
    }

    private void IncreaseQuantityAttribute(Item product) {

        text_productqtyAttribute.setText(String.valueOf(((Product) product).quantity));

        boolean productExistInOrder = false;
        if (StaticConstants.CURRENT_ORDER.item.size() > 0) {
            for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                if (((Product) product).id == StaticConstants.CURRENT_ORDER.item.get(k).id) {
                    productExistInOrder = true;
                    ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity++;
                }
            }
        } else {
            productExistInOrder = true;
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);
        }


        if (!productExistInOrder) {

            StaticConstants.CURRENT_ORDER.item.add(product);

        }

        text_productqtyAttribute.setText(String.valueOf(((Product) product).quantity));

        ((BaseFragmentActivity) context).ShowCArtItem();
    }

    private void DecreseQuantityAttribute(Item product) {

        if (((Product) product).quantity > 1) {
            ((Product) product).quantity--;
            text_productqtyAttribute.setText(String.valueOf(((Product) product).quantity));
        } else if (((Product) product).quantity == 1) {
            ((Product) product).quantity--;
            text_productqtyAttribute.setText(String.valueOf(((Product) product).quantity));
        }
        ((BaseFragmentActivity) context).ShowCArtItem();
    }

    private void OrderPlacedASync(Order order,String type) {


        if (order.fee.equalsIgnoreCase("-1") || order.fee.equalsIgnoreCase("-2"))
            order.fee = "0";

        HashMap<String, String> par = new HashMap<String, String>();
        par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.CUSTOMER_MERCHANT_ID));
        par.put(StaticConstants.JSON_USER_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.USER_ID));
        par.put(StaticConstants.JSON_TAG_CUD_NETPRICE,
                String.valueOf(order.netprice));
        if (order.order_type.equalsIgnoreCase("delivery"))
            par.put(StaticConstants.JSON_TAG_CUD_DELIVERYFEES,
                    String.valueOf(order.fee));
        else
            par.put(StaticConstants.JSON_TAG_CUD_DELIVERYFEES,
                    "0");
        par.put(StaticConstants.JSON_TAG_CUD_TOTALAMOUNT, Util.CalculateTotalAmount(order)
        );
        par.put(StaticConstants.JSON_TAG_CUD_SPECIAL,
               Util.encodeString( order.specialInstru));

        PrefHelper.storeString(context,
                PrefHelper.PREF_FILE_NAME,
                StaticConstants.JSON_TAG_CUD_SPECIAL, order.specialInstru);

        par.put(StaticConstants.JSON_TAG_CUD_REQUESTTYPE,
                "");
        par.put(StaticConstants.JSON_TAG_CUD_DEVICE_TYPE,
                StaticConstants.ANDROID);

        par.put(StaticConstants.JSON_TAG_CUD_DEVICEID,
                PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
        par.put(StaticConstants.JSON_TAG_CUD_PAYMENTSTATUS,
                StaticConstants.PAYMENT_STATUS_10);
        par.put(StaticConstants.JSON_TAG_CUD_ORDER_TYPE,
                order.order_type);
        par.put(StaticConstants.JSON_TAG_CUD_PAYMENT_TYPE,
                type);
        par.put(StaticConstants.JSON_TAG_CUD_PAID_STATUS,
                "2");
        if (order.order_type.equalsIgnoreCase("delivery")) {
            par.put(StaticConstants.RES_JSON_USER_DELIVERY_ID,
                    PrefHelper.
                            getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.RES_JSON_USER_DELIVERY_ID));
        }
        try {
            JSONArray products = (JSONArray) Util.getorderproduct(order);
            par.put(StaticConstants.JSON_TAG_CUD_CUSTOMERORDERHISTORY,
                    products.toString());
            order.orderJson = products.toString();

        } catch (JSONException je) {
            je.fillInStackTrace();
        }

        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, CartFragment.this,
                BaseNetwork.obj().KEY_PLACE_ORDER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(par);

    }

    static class ViewHolder {
        TextView name, price, quantity;
        ImageView img_edititem, img_deleteitem;

    }

    @Override
    public boolean onBackPressed() {

        if(((BaseFragmentActivity)context).paymentshown()){

        }
        else{
            if (BACK_PRESSED) {
                ((BaseFragmentActivity) context).OnUserMyOrderFragment();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(StaticConstants.MERCHANT_ID, PrefHelper.
                        getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CUSTOMER_MERCHANT_ID));
                ((BaseFragmentActivity) context).OnMenuFragment(bundle);

            }
        }
        return true;
    }

    @Override
    public void taskStarting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskCompleted(Object result) {
        ResultMessage message = (ResultMessage) result;
        if (message.TYPE.equalsIgnoreCase(StaticConstants.ADD_ADDRESS)) {
            linear_addadress.setVisibility(View.GONE);
            rel_updateadress.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Address added successfully", Toast.LENGTH_SHORT).show();
            text_name.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_NAME));
            text_address.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_ADDRESS));
            text_city.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_CITY));
            text_state.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.RES_JSON_USER_DELIVERY_STATE));
        } else {
            StaticConstants.CURRENT_ORDER = new Order();
            ((BaseFragmentActivity) context).HideCArtItem();

            if (showDialog) {
                if (PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_SDK_NAME).equalsIgnoreCase(StaticConstants.GOOGLE))
                    new OrderPlacedDialog(context).show();
                else
                    ((BaseFragmentActivity) context).OnUserMyOrderFragment();
            } else
                ((BaseFragmentActivity) context).OnUserMyOrderFragment();
            //new OrderPlacedDialog(context).show();
        }


    }

    @Override
    public void taskProgress(String urlString, Object progress) {

    }

    @Override
    public void taskErrorMessage(Object result) {

    }

    @Override
    public Context getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }

    public void DeleteCartItems() {

        new DeleteCartItemConfirmationDialog(context, this).show();

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


/*
 private MaskedWalletRequest generateMaskedWalletRequest() {

        String publicKey = "BO39Rh43UGXMQy5PAWWe7UGWd2a9YRjNLPEEVe+zWIbdIgALcDcnYCuHbmrrzl7h8FZjl6RCzoi5/cDrqXNRVSo=";
        PaymentMethodTokenizationParameters parameters =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(
                                PaymentMethodTokenizationType.NETWORK_TOKEN)
                        .addParameter("publicKey", publicKey)
                        .build();

        MaskedWalletRequest maskedWalletRequest =
                MaskedWalletRequest.newBuilder()
                        .setMerchantName("Zapnabit Merchant")
                        .setPhoneNumberRequired(true)
                        .setShippingAddressRequired(true)
                        .setCurrencyCode("USD")
                        .setCart(Cart.newBuilder()
                                .setCurrencyCode("USD")
                                .setTotalPrice("10.00")
                                .addLineItem(LineItem.newBuilder()
                                        .setCurrencyCode("USD")
                                        .setDescription("Zapnabit Merchant Items Test")
                                        .setQuantity("1")
                                        .setUnitPrice("5.00")
                                        .setTotalPrice("5.00")
                                        .build())
                                .build())
                        .setEstimatedTotalPrice("15.00")
                        .setPaymentMethodTokenizationParameters(parameters)
                        .build();
        return maskedWalletRequest;

    }
    private FullWalletRequest generateFullWalletRequest(String googleTransactionId) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.10")
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Zapnabit Merchant Items Test")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice(".10")
                                .build())
                        .build())
                .build();
        return fullWalletRequest;
    }








     /* mWalletFragment = (SupportWalletFragment) getChildFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);
        wallet_button_holder=(FrameLayout)view.findViewById(R.id.wallet_button_holder);
        if (mWalletFragment == null) {
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                    .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);


            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();

            WalletFragmentInitParams.Builder startParamsBuilder =
                    WalletFragmentInitParams.newBuilder()
                            .setMaskedWalletRequest(generateMaskedWalletRequest())
                            .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE)
                            .setAccountName("Google I/O Codelab");
            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
            mWalletFragment.initialize(startParamsBuilder.build());

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                    .commit();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(this)
                .enableAutoManage((FragmentActivity) context, 0, this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();*/





package in.pjitsol.zapnabit.PlaceOrder;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Entity.CategoryProductinfo;
import in.pjitsol.zapnabit.Entity.ProductAtrribute;
import in.pjitsol.zapnabit.Entity.ProductAtrributeOption;
import in.pjitsol.zapnabit.ImageLoader.ImageLoader;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Entity.Category;
import in.pjitsol.zapnabit.Entity.Item;
import in.pjitsol.zapnabit.Entity.Product;
import in.pjitsol.zapnabit.Entity.ProductAddOn;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.IDeleteConfirmation;
import in.pjitsol.zapnabit.Ui.IDeleteVaribaleProduct;
import in.pjitsol.zapnabit.Ui.ProductImageDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.VariableProductDialog;
import in.pjitsol.zapnabit.Util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.places.model.CurrentPlaceRequestParams;

public class MenuFragment extends Fragment implements
        OnBackPressListener,
        OnCancelListener, OnItemClickListener, OnClickListener,
        IAsyncTaskRunner, IMenuFetch, IcallBackImage, IDeleteVaribaleProduct, IQuantityCallback {


    private LayoutInflater inflater;
    private ListView list_menuitems;
    private Activity context;
    private MenuAdapter adapter;
    private String MERCHANT_ID;
    List<Item> receivedItems = new ArrayList<>();
    private int parentId;
    public Stack<List<Item>> catStack = new Stack<List<Item>>();
    private ProgressHUD progressDialog;
    private LinearLayout linear_addon;
    private LinearLayout linear_addonslayout;
    private TextView text_productname;
    private TextView text_productprice;
    private TextView text_productqty;
    private ImageView img_plus;
    private ImageView img_minus;
    private Item CurrentProduct;
    private TextView text_addextra;
    private TextView text_choose;
    private TextView text_productDesc;
    private ImageView img_product;
    private ImageLoader imageLoader;
    private LinearLayout linear_top;
    private LinearLayout linear_attrbuteon;
    private LinearLayout linear_attributeslayout;
    private TextView text_productnameAttribute;
    private TextView text_productDescAttribute;
    private ImageView img_plusattribute;
    private ImageView img_minusAttribute;
    private TextView text_productqtyAttribute;
    private ImageView img_productAttribute;
    private LinearLayout linear_topAttribute;
    private TextView text_addextraAttribute;
    private TextView text_chooseattri;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.menu_screen,
                container, false);
        MERCHANT_ID = getArguments().getString(StaticConstants.MERCHANT_ID);
        //StaticConstants.CURRENT_ORDER=new Order();
        this.inflater = inflater;
        imageLoader = new ImageLoader(context);
        initView(view);
        return view;
    }


    private void initView(View view) {
        list_menuitems = (ListView) view.findViewById(R.id.list_menuitems);
        linear_addon = (LinearLayout) view.findViewById(R.id.linear_addon);
        linear_addonslayout = (LinearLayout) view.findViewById(R.id.linear_addonslayout);
        linear_attributeslayout = (LinearLayout) view.findViewById(R.id.linear_attributeslayout);
        linear_attrbuteon = (LinearLayout) view.findViewById(R.id.linear_attrbuteon);
        linear_top = (LinearLayout) view.findViewById(R.id.linear_top);
        img_product = (ImageView) view.findViewById(R.id.img_product);
        text_productname = (TextView) view.findViewById(R.id.text_productname);
        text_productDesc = (TextView) view.findViewById(R.id.text_productDesc);
        text_productprice = (TextView) view.findViewById(R.id.text_productprice);
        text_productqty = (TextView) view.findViewById(R.id.text_productqty);
        img_plus = (ImageView) view.findViewById(R.id.img_plus);
        img_minus = (ImageView) view.findViewById(R.id.img_minus);
        text_productnameAttribute = (TextView) view.findViewById(R.id.text_productnameAttribute);
        text_chooseattri = (TextView) view.findViewById(R.id.text_chooseattri);
        text_productDescAttribute = (TextView) view.findViewById(R.id.text_productDescAttribute);
        text_productqtyAttribute = (TextView) view.findViewById(R.id.text_productqtyAttribute);
        img_plusattribute = (ImageView) view.findViewById(R.id.img_plusattribute);
        img_minusAttribute = (ImageView) view.findViewById(R.id.img_minusAttribute);
        img_productAttribute = (ImageView) view.findViewById(R.id.img_productAttribute);
        linear_topAttribute = (LinearLayout) view.findViewById(R.id.linear_topAttribute);
        text_addextraAttribute = (TextView) view.findViewById(R.id.text_addextraAttribute);


        text_addextra = (TextView) view.findViewById(R.id.text_addextra);
        text_choose = (TextView) view.findViewById(R.id.text_choose);
        adapter = new MenuAdapter(context, receivedItems, this);
        list_menuitems.setAdapter(adapter);
        list_menuitems.setOnItemClickListener(this);

        linear_addon.setOnClickListener(this);
        linear_addonslayout.setOnClickListener(this);
        img_plus.setOnClickListener(this);
        img_minus.setOnClickListener(this);
        text_addextra.setOnClickListener(this);
        linear_top.setOnClickListener(this);
        img_plusattribute.setOnClickListener(this);
        img_minusAttribute.setOnClickListener(this);
        linear_topAttribute.setOnClickListener(this);
        text_addextraAttribute.setOnClickListener(this);

        new GetMenuFROMDbTask(Category.TOP_LEVEL_CAT_PARENT_ID, true,
                MenuFragment.this
        )
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);

    }


    @Override
    public void taskStarting() {

    }

    @Override
    public void taskCompleted(Object result) {
        new GetMenuFROMDbTask(Category.TOP_LEVEL_CAT_PARENT_ID, true,
                MenuFragment.this
        )
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);

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
    public void onClick(View v) {
        switch (v.getId()) {
        /*case R.id.linear_upperaddon:
            linear_addon.startAnimation(Util.TopToBottomAnimation());
			linear_addon.setVisibility(View.GONE);
			break;*/
            case R.id.linear_top:
                linear_addon.startAnimation(Util.TopToBottomAnimation());
                linear_addon.setVisibility(View.GONE);

               /* if (((Product) CurrentProduct).productType.equalsIgnoreCase("product")) {
                    linear_addon.startAnimation(Util.TopToBottomAnimation());
                    linear_addon.setVisibility(View.GONE);
                } else {

                }*/


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

                } else {
                    VariableProductDialog dialog = new VariableProductDialog(context, this);
                    dialog.show();
                }


                break;

            case R.id.img_minus:

                DecreseQuantity(CurrentProduct);


                break;
            case R.id.img_plus:

                IncreaseQuantity(CurrentProduct);

                break;

            case R.id.img_minusAttribute:

                DecreseQuantityAttribute(CurrentProduct);


                break;
            case R.id.img_plusattribute:

                IncreaseQuantityAttribute(CurrentProduct);

                break;

            case R.id.text_addextra:
                linear_addon.startAnimation(Util.TopToBottomAnimation());
                linear_addon.setVisibility(View.GONE);
                if (((Product) CurrentProduct).quantity == 1) {
                    StaticConstants.CURRENT_ORDER.item.add(CurrentProduct);
                    ((BaseFragmentActivity) context).ShowCArtItem();
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
                        closeTheDialog = true;

                }

                if (closeTheDialog1) {
                    linear_attrbuteon.startAnimation(Util.BottomToTopAnimation());
                    linear_attrbuteon.setVisibility(View.GONE);

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

    private void IncreaseQuantity(Item product) {

        text_productqty.setText(String.valueOf(((Product) product).quantity));
        if (((Product) product).quantity == 0)
            text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(
                    ((Product) product).netamount));
        else
            text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(Util.calculateItemTotalPrice(((Product) product).quantity,
                    ((Product) product).netamount)));
        /*boolean productExistInOrder = false;

        if (StaticConstants.CURRENT_ORDER.item.size() > 0) {
            for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                if (((Product) product).id == StaticConstants.CURRENT_ORDER.item.get(k).id) {
                    productExistInOrder = true;
                    ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity++;
                    // ((Product) CurrentProduct).quantity=((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity;
                }
            }
        } else {
            productExistInOrder = true;
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);
        }


        if (!productExistInOrder) {
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);

        }*/

        boolean productExistInOrder = false;
        if (StaticConstants.CURRENT_ORDER.item.size() > 0) {
            for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                if (product.equals(StaticConstants.CURRENT_ORDER.item.get(k))) {
                    productExistInOrder = true;
                    ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity++;
                    ;
                    ((Product) CurrentProduct).quantity = ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity;
                }
            }
        } else {
            productExistInOrder = true;
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);
        }
        if (!productExistInOrder) {
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);

        }


        text_productqty.setText(String.valueOf(((Product) product).quantity));
        text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(Util.calculateItemTotalPrice(((Product) product).quantity,
                ((Product) product).netamount)));
        ((BaseFragmentActivity) context).ShowCArtItem();
    }


    private void DecreseQuantity(Item product) {
        if (((Product) product).quantity > 1) {
            ((Product) product).quantity--;
            text_productqty.setText(String.valueOf(((Product) product).quantity));
            text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(Util.calculateItemTotalPrice(((Product) product).quantity,
                    ((Product) product).netamount)));

        } else if (((Product) product).quantity == 1) {
            ((Product) product).quantity--;
            text_productqty.setText(String.valueOf(((Product) product).quantity));
            text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(
                    ((Product) product).netamount));
        }
        ((BaseFragmentActivity) context).ShowCArtItem();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        CurrentProduct = receivedItems.get(position);


        if (CurrentProduct instanceof Product) {
            /*if (StaticConstants.CURRENT_ORDER.item.size() > 0) {
                for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                    if (((Product) CurrentProduct).id == StaticConstants.CURRENT_ORDER.item.get(k).id) {
                        CurrentProduct = ((Product) (StaticConstants.CURRENT_ORDER.item.get(k)));
                    }
                }
            }*/
            if (((Product) CurrentProduct).productType.equalsIgnoreCase("product")) {
                ((Product) CurrentProduct).quantity = 1;
                text_productname.setText(CurrentProduct.name);
                text_productDesc.setText(CurrentProduct.productDescription);
                text_productqty.setText("" +
                        ((Product) CurrentProduct).quantity);
                if (((Product) CurrentProduct).quantity == 0)
                    text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(
                            ((Product) CurrentProduct).netamount));
                else
                    text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(Util.calculateItemTotalPrice(((Product) CurrentProduct).quantity,
                            ((Product) CurrentProduct).netamount)));

                imageLoader.DisplayImage(CurrentProduct.productImage, img_product);


                if (CurrentProduct.productAddOns != null) {
                    if (CurrentProduct.productAddOns.size() > 0) {
                        text_choose.setVisibility(View.VISIBLE);
                        text_addextra.setText("Add To Cart");
                    } else {
                        text_addextra.setText("Add To Cart");

                        text_choose.setVisibility(View.GONE);
                    }

                }
                CreateAddOnUi(CurrentProduct);
            } else {

                //  IncreaseQuantityAttribute(CurrentProduct);

                text_productnameAttribute.setText(CurrentProduct.name);
                text_productDescAttribute.setText(CurrentProduct.productDescription);
                text_productqtyAttribute.setText("" +
                        ((Product) CurrentProduct).quantity);

                text_addextraAttribute.setText("Add To Cart");
                text_chooseattri.setText("BUILD YOUR ITEM");
                imageLoader.DisplayImage(CurrentProduct.productImage, img_productAttribute);


                CreateAttributeUi(CurrentProduct);
            }


        } else {

            int parentID = CurrentProduct.id;
            new GetMenuFROMDbTask(parentID, true,
                    MenuFragment.this
            ).execute();
        }
    }

    private void CreateAddOnUi(final Item item) {
        Animation bottomUp = AnimationUtils.loadAnimation(context,
                R.animator.bottom_up);
        linear_addon.startAnimation(bottomUp);
        linear_addon.setVisibility(View.VISIBLE);
        linear_addonslayout.removeAllViews();

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
                        text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(Util.calculateItemTotalPrice(Addon.quantity,
                                Addon.netamount)));
                        ((BaseFragmentActivity) context).ShowCArtItem();
                    } else
                        Toast.makeText(context, "Please select an item", Toast.LENGTH_SHORT).show();

                }
            });


            img_minus.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    ProductAddOn Addon = (ProductAddOn) viewaddonItems.getTag();
                    if (Addon.quantity > 1) {
                        Addon.quantity--;
                        text_addonqty.setText(String.valueOf(Addon.quantity));
                        text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(Util.calculateItemTotalPrice(Addon.quantity,
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
                        text_productprice.setText(StaticConstants.DOLLAR + Util.numberfornmat(
                                Addon.netamount));
                        ((BaseFragmentActivity) context).ShowCArtItem();
                    }
                }
            });

        }
    }


    private void CreateAttributeUi(final Item item) {
        Animation bottomUp = AnimationUtils.loadAnimation(context,
                R.animator.bottom_up);
        linear_attrbuteon.startAnimation(bottomUp);
        linear_attrbuteon.setVisibility(View.VISIBLE);
        linear_attributeslayout.removeAllViews();

        for (int i = 0; i < item.productAttributes.size(); i++) {
            final ProductAtrribute currentAttribute = item.productAttributes.get(i);
            final View viewaddonItems = LayoutInflater.from(context).inflate(
                    R.layout.option_spinner_item, null);
            TextView text_attributename = (TextView) viewaddonItems.findViewById(R.id.text_attributename);
            Spinner spinnerOption = (Spinner) viewaddonItems.findViewById(R.id.spinnerOption);
            if (Integer.valueOf(currentAttribute.attr_condition) == 1 ||
                    Integer.valueOf(currentAttribute.attr_condition) == 2)
                text_attributename.setText(currentAttribute.name + " ( Min. " + currentAttribute.attr_condition + " )");
            else
                text_attributename.setText(currentAttribute.name);


            if (!currentAttribute.hintAdded) {
                ProductAtrributeOption hint = new ProductAtrributeOption();
                hint.name = "Select " + currentAttribute.name;
                currentAttribute.productAttributesOptions.add(0, hint);
                currentAttribute.hintAdded = true;
            }


            final CustomSpinnerAdapter optionAdapter = new CustomSpinnerAdapter
                    (context, currentAttribute.productAttributesOptions,
                            currentAttribute, item, this);
            spinnerOption.setAdapter(optionAdapter);
            spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (((Product) item).quantity == 0)
                        IncreaseQuantityAttribute(item);


                    ProductAtrributeOption option = (ProductAtrributeOption) parent.getItemAtPosition(position);
                    if (!option.name.contains("Select")) {
                        if (currentAttribute.attr_condition.equalsIgnoreCase("1")) {

                            if (currentAttribute.optionSelected) {
                                for (int i = 0; i < currentAttribute.productAttributesOptions.size(); i++) {
                                    currentAttribute.productAttributesOptions.get(i).optionSelected = false;
                                }
                                option.optionSelected = true;
                                currentAttribute.optionCount = 1;
                                option.productAttribute = currentAttribute;
                                item.selectedAttributeOptions.add(option);
                                optionAdapter.notifyDataSetChanged();
                                ((BaseFragmentActivity) context).ShowCArtItem();
                            } else {
                                currentAttribute.optionSelected = true;
                                option.optionSelected = true;
                                currentAttribute.optionCount = 1;
                                item.selectedAttributeOptions.add(option);
                                optionAdapter.notifyDataSetChanged();
                                ((BaseFragmentActivity) context).ShowCArtItem();
                            }
                        } else if (currentAttribute.attr_condition.equalsIgnoreCase("2")) {
                            currentAttribute.optionSelected = true;
                            if (option.optionSelected) {
                                if (currentAttribute.optionCount > 1) {
                                    option.optionSelected = false;
                                    item.selectedAttributeOptions.remove(option);
                                    currentAttribute.optionCount--;
                                } else
                                    Toast.makeText(context,
                                            "Choose atleast one add ons", Toast.LENGTH_SHORT).show();
                            } else {
                                option.optionSelected = true;
                                item.selectedAttributeOptions.add(option);
                                currentAttribute.optionCount++;
                            }
                            option.productAttribute = currentAttribute;
                            optionAdapter.notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();

                        } else {
                            currentAttribute.optionSelected = true;
                            if (option.optionSelected) {
                                option.optionSelected = false;
                                item.selectedAttributeOptions.remove(option);
                            } else {
                                option.optionSelected = true;
                                item.selectedAttributeOptions.add(option);
                            }

                            option.productAttribute = currentAttribute;
                            optionAdapter.notifyDataSetChanged();
                            ((BaseFragmentActivity) context).ShowCArtItem();
                        }

                    }
                   /*else
                        Toast.makeText(context, "Please select the quantity of an item first", Toast.LENGTH_SHORT).show();*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            linear_attributeslayout.addView(viewaddonItems);


        }
    }

    private void IncreaseQuantityAttribute(Item product) {

        text_productqtyAttribute.setText(String.valueOf(((Product) product).quantity));

       /* boolean productExistInOrder = false;
        if (StaticConstants.CURRENT_ORDER.item.size() > 0) {
            for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                if (((Product) product).id == StaticConstants.CURRENT_ORDER.item.get(k).id) {
                    productExistInOrder = true;
                    ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity++;
                    ((Product) CurrentProduct).quantity=((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity;
                }
            }
        } else {
            productExistInOrder = true;
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);
        }


        if (!productExistInOrder) {

            StaticConstants.CURRENT_ORDER.item.add(product);

        }*/

        boolean productExistInOrder = false;
        if (StaticConstants.CURRENT_ORDER.item.size() > 0) {
            for (int k = 0; k < StaticConstants.CURRENT_ORDER.item.size(); k++) {
                if (product.equals(StaticConstants.CURRENT_ORDER.item.get(k))) {
                    productExistInOrder = true;
                    ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity++;
                    ;
                    ((Product) CurrentProduct).quantity = ((Product) (StaticConstants.CURRENT_ORDER.item.get(k))).quantity;
                }
            }
        } else {
            productExistInOrder = true;
            ((Product) product).quantity++;
            StaticConstants.CURRENT_ORDER.item.add(product);
        }
        if (!productExistInOrder) {
            ((Product) product).quantity++;
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


    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onBackPressed() {
        if (linear_addon.getVisibility() == View.VISIBLE) {
            linear_addon.startAnimation(Util.BottomToTopAnimation());
            linear_addon.setVisibility(View.GONE);
            return true;
        } else if (linear_attrbuteon.getVisibility() == View.VISIBLE) {
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
                return true;

            } else {

                VariableProductDialog dialog = new VariableProductDialog(context, this);
                dialog.show();


                return true;
            }

        } else {
            if (adapter != null && adapter.onBackPressed()) {

                if (!catStack.isEmpty()) {

                    List<Item> lastCat = null;
                    String lastNAme = null;
                    for (int i = catStack.size() - 1; i > 0; i--) {
                        catStack.remove(i);
                        if (catStack.size() > 0) {
                            lastCat = catStack.get(i - 1);
                            break;
                        }

                    }
                    if (lastCat != null) {
                        receivedItems = lastCat;
                        adapter = new MenuAdapter(context, lastCat, this);
                        list_menuitems.setAdapter(adapter);
                        return true;
                    } else {

                        return false;
                    }

                } else {
                    return false;
                }

            }
        }


        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = activity;
    }


    @Override
    public void addedMenuItem(List<Item> result, int parentId, boolean trytomenu) {
        if (result != null && result.size() > 0) {
            this.parentId = parentId;
            receivedItems = result;
            catStack.push(result);
            adapter = new MenuAdapter(context, (ArrayList<Item>) result, this);
            list_menuitems.setAdapter(adapter);

        } else {
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> map = new HashMap<>();
            map.put(StaticConstants.JSON_USER_ID, PrefHelper.
                    getStoredString(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.USER_ID));
            map.put(StaticConstants.JSON_MERCHANT_ID,
                    MERCHANT_ID);


			/*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
			 */

            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                    context, MenuFragment.this,
                    BaseNetwork.obj().KEY_FETCH_MENU, progressDialog, StaticConstants.POST_METHOD);
            task.execute(map);


        }
    }

    public String getMerchantID() {
        return MERCHANT_ID;
    }

    @Override
    public void ShowImage(String productImage) {
        new ProductImageDialog(getActivity(), productImage).show();
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
        IncreaseQuantityAttribute(CurrentProduct);
    }




	/*@Override
    public void increaseQuantity(Item product) {

		IncreaseQuantity(product);

	}


	@Override
	public void decreaseQuantity(Item product) {
		DecreseQuantity(product);
	}*/

}

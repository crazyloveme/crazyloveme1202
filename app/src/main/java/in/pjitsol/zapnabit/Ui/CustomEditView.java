package in.pjitsol.zapnabit.Ui;

import in.pjitsol.zapnabit.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditView extends EditText {
	private static final String GothamBold = "GothamBold";
	private static final String GothamLight = "GothamLight";
	private static final String GothamMedium= "GothamMedium";
	private static final String UbuntuBold = "UbuntuBold";
	private static final String UbuntuLight = "UbuntuLight";
	private static final String UbuntuMedium= "UbuntuMedium";
	

	public CustomEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (!isInEditMode())
			init(attrs);

	}

	public CustomEditView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (!isInEditMode())
			init(attrs);

	}

	public CustomEditView(Context context) {
		super(context);

		if (!isInEditMode())
			init(null);
	}

	private void init(AttributeSet attrs) {
		// TODO Auto-generated method stub
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.CustomEditText);
			String fontName = a
					.getString(R.styleable.CustomEditText_customfontName);
			if (GothamBold.equals(fontName)) {
				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), "fonts/" + "GothamBold.ttf");
				setTypeface(myTypeface);
			}
			if (GothamLight.equals(fontName)) {
				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), "fonts/" + "GothamLight.ttf");
				setTypeface(myTypeface);
			}
			if (GothamMedium.equals(fontName)) {
				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), "fonts/" + "GothamMedium.ttf");
				setTypeface(myTypeface);
			}
			if (UbuntuBold.equals(fontName)) {
				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), "fonts/" + "UbuntuBold.ttf");
				setTypeface(myTypeface);
			}
			if (UbuntuLight.equals(fontName)) {
				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), "fonts/" + "UbuntuLight.ttf");
				setTypeface(myTypeface);
			}
			if (UbuntuMedium.equals(fontName)) {
				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), "fonts/" + "UbuntuMedium.ttf");
				setTypeface(myTypeface);
			}

			a.recycle();
		}

	}
}
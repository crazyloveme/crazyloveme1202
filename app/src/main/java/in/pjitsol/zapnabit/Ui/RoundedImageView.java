package in.pjitsol.zapnabit.Ui;


import in.pjitsol.zapnabit.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
public class RoundedImageView extends ImageView {
	private int borderWidth = 4;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	private Context context;

	public RoundedImageView(Context context)
	{
		super(context);
		this.context=context;
		setup();
	}

	public RoundedImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context=context;
		setup();
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context=context;
		setup();
	}

	@SuppressLint("NewApi")
	private void setup()
	{
		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);

		paintBorder = new Paint();
		setBorderColor(context.getResources().getColor(R.color.redsepratorline));
		setBorderWidth(1);
		paintBorder.setAntiAlias(true);
		/*setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		 * paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
		paintBorder.setShadowLayer(3.0f, 5.0f, 5.0f, Color.parseColor("#999999"));*/
	}

	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		this.invalidate();
	}

	public void setBorderColor(int borderColor)
	{
		if (paintBorder != null)
			paintBorder.setColor(borderColor);

		this.invalidate();
	}

	private void loadBitmap()
	{
		BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

		if (bitmapDrawable != null)
			image = bitmapDrawable.getBitmap();
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas)
	{
		// load the bitmap
		loadBitmap();

		// init shader
		if (image != null)
		{
			shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			paint.setShader(shader);
			int circleCenter = viewWidth / 2;

			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth - 4.0f, paintBorder);
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter - 4.0f, paint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

		viewWidth = width - (borderWidth * 2);
		viewHeight = height - (borderWidth * 2);

		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY)
		{
			// We were told how big to be
			result = specSize;
		}
		else
		{
			// Measure the text
			result = viewWidth;
		}

		return result;
	}

	private int measureHeight(int measureSpecHeight, int measureSpecWidth)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY)
		{
			// We were told how big to be
			result = specSize;
		}
		else
		{
			// Measure the text (beware: ascent is a negative number)
			result = viewHeight;
		}

		return (result + 2);
	}
	/*public RoundedImageView(Context context) {
		super(context);
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();

		Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
		canvas.drawBitmap(roundBitmap, 0, 0, null);

	}

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;

		if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
			float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
			float factor = smallest / radius;
			sbmp = Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth() / factor), (int)(bmp.getHeight() / factor), false);
		} else {
			sbmp = bmp;
		}

		Bitmap output = Bitmap.createBitmap(radius, radius,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, radius, radius);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#333333"));
		paint.setShadowLayer(10f, 0.5f, 0.5f,Color.parseColor("#999999") );

		setBorderColor(Color.parseColor("#999999"));
		setLayerType(LAYER_TYPE_SOFTWARE, paint);
		paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);

		canvas.drawCircle(radius / 2 + 0.7f,
				radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		this.invalidate();
	}

	public static void setBorderColor(int borderColor)
	{
		if (paintBorder != null)
			paintBorder.setColor(borderColor);

		this.invalidate();
	}*/

}

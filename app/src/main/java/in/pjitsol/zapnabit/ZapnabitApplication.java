package in.pjitsol.zapnabit;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", mailTo = "bhawna.websnoox@gmail.com", customReportContent = {
		ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
		ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
		ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
		forceCloseDialogAfterToast = false, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class ZapnabitApplication extends Application {
	private static ZapnabitApplication singleton;
	public static ZapnabitApplication getApp() {
		return singleton;
	}

	public ZapnabitApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		singleton = this;

		/*POSDatabase.getInstanceLogin(getApplicationContext());
		new Thread(new LoadDataRunnable()).start();*/
		

	}
	



}

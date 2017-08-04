package in.pjitsol.zapnabit.AsyncTask;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Exception.SearchException;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.LoginFailureDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.Util;

import java.lang.ref.WeakReference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
/**Task for handling the result of every end point and displaying the message accordingly.
 * @author Bhawna Verma
 *
 */
public abstract class AsyncTaskRunner<Parems, Progress, Result> extends
AsyncTask<Parems, Progress, Result> {
	protected String urlString;
	protected ProgressHUD pDialog;
	protected ProgressBar pBar;
	protected Context context;
	protected String jsonString = "";
	private WeakReference<Context> contextWeakRef;
	private IAsyncTaskRunner<Progress, Result> _asyncTaskRunner;

	public AsyncTaskRunner(Context context,
			IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
		_asyncTaskRunner = asyncTaskRunner;
		contextWeakRef = new WeakReference<Context>(
				asyncTaskRunner.getContext());
		this.context = context;
	}

	public AsyncTaskRunner(Context context, String urlString,
			IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
		_asyncTaskRunner = asyncTaskRunner;
		this.urlString = urlString;
		this.context = context;
		contextWeakRef = new WeakReference<Context>(
				asyncTaskRunner.getContext());
	}

	public AsyncTaskRunner(Context context, String urlString, ProgressBar pBar,
			IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
		_asyncTaskRunner = asyncTaskRunner;
		this.context = context;
		this.urlString = urlString;
		this.pBar = pBar;
		contextWeakRef = new WeakReference<Context>(context);

	}

	public AsyncTaskRunner(Context context, String urlString,
			ProgressHUD pDialog,
			IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
		_asyncTaskRunner = asyncTaskRunner;
		this.context = context;
		this.urlString = urlString;
		this.pDialog = pDialog;
		contextWeakRef = new WeakReference<Context>(context);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Result doInBackground(Parems... params) {
		return (Result) new ResultMessage();
	}

	@Override
	protected void onPreExecute() {
		_asyncTaskRunner.taskStarting();
	}

	@Override
	protected void onPostExecute(Result result) {
		if (contextWeakRef.get() != null) {
			ResultMessage resultMessage = (ResultMessage) result;
			if (resultMessage.STATUS == (int) StaticConstants.ASYN_NETWORK_FAIL
					) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);
				if(Util.isNetworkAvailable(context))
					try {
						BaseNetwork.obj().showErrorNetworkDialog(
								context,
								context.getResources().getString(
										R.string.error_dialog_error));
					} catch (Exception e) {
						System.out.println("BasefragmentActivity finished");
					}
					
				return;
			} 
			else if (resultMessage.STATUS==(int)StaticConstants.ASYN_RESULT_PARSE_FAILED) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);
				if(Util.isNetworkAvailable(context))
					try {
						BaseNetwork.obj().showErrorNetworkDialog(
								context,
								context.getResources().getString(
										R.string.error_dialog_search_no_results));
					} catch (Exception e) {
						System.out.println("BasefragmentActivity finished");
					}
					
				return;
			}

			else if (resultMessage.STATUS == SearchException.NO_RESULTS) {
				if(Util.isNetworkAvailable(context))
					try {
						BaseNetwork.obj().showErrorNetworkDialog(
								context,
								context.getResources().getString(
										R.string.error_dialog_error));
					} catch (Exception e) {
						System.out.println("BasefragmentActivity finished");
					}
					
				return;

			} else if (resultMessage.STATUS == SearchException.SERVER_DELAY) {

				if (pDialog != null && pDialog.isShowing())
					pDialog.dismiss();
				if (pBar != null && pBar.isShown())
					pBar.setVisibility(View.GONE);
				if (context != null)
					if(Util.isNetworkAvailable(context))
						try {
							BaseNetwork.obj().showErrorNetworkDialog(
									context,
									context.getResources().getString(
											R.string.error_dialog_server_error));
						} catch (Exception e) {
							System.out.println("BasefragmentActivity finished");
						}
						
				return;
			} else if (resultMessage.STATUS == SearchException.ERROR) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);
				if(Util.isNetworkAvailable(context))
					try {
						BaseNetwork.obj().showErrorNetworkDialog(
								context,
								context.getResources().getString(
										R.string.error_dialog_no_results));
					} catch (Exception e) {
						System.out.println("BasefragmentActivity finished");
					}
					

				return;
			} else if (resultMessage.STATUS == (int) StaticConstants.ASYN_RESULT_OK) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);
				_asyncTaskRunner.taskCompleted(result);
				return;
			} else if (resultMessage.STATUS == (int) StaticConstants.ASYN_RESULT_CANCEL) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);
				if(Util.isNetworkAvailable(context))
					try {
						showErrorNetworkDialog(context, result);
					} catch (Exception e) {
						System.out.println("BasefragmentActivity finished");
					}
					

				return;
			}
			else if (resultMessage.STATUS == (int) StaticConstants.ASYN_RESULT_LOGINFAILURE) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);
				if(Util.isNetworkAvailable(context))
					try {
						new LoginFailureDialog(context).show();
					} catch (Exception e) {
						System.out.println("BasefragmentActivity finished");
					}


				return;
			}
			else if (resultMessage.STATUS == (int) StaticConstants.ASYN_RESULT_DRAFTCANCEL) {
				if (pDialog != null)
					if (pDialog.isShowing())
						pDialog.dismiss();
				if (pBar != null)
					pBar.setVisibility(View.GONE);

				return;
			}

		}

	}

	@Override
	protected void onProgressUpdate(
			@SuppressWarnings("unchecked") Progress... progress) {

		_asyncTaskRunner.taskProgress(urlString,progress[0]);
		if (pDialog != null)
			if (!pDialog.isShowing()) {
				pDialog.show();

			}
		if (pBar != null) {
			pBar.setVisibility(View.VISIBLE);
			Log.i("TCV", "Show progres bar");
		}

		/*if (progress[0].equals(StaticConstants.ASYNTASK_STARTING)) {
	if (pDialog != null)
		if (!pDialog.isShowing()) {
			pDialog.show();

		}
	if (pBar != null) {
		pBar.setVisibility(View.VISIBLE);
		Log.i("TCV", "Show progres bar");
	}
}*/
	}

	public void showErrorNetworkDialog(final Context c, final Result result) {
		ResultMessage messageresult = (ResultMessage) result;
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(messageresult.ERRORMESSAGE).setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				_asyncTaskRunner.taskErrorMessage(result);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}

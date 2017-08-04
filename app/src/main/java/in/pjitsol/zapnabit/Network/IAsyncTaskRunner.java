package in.pjitsol.zapnabit.Network;

import android.content.Context;

public interface IAsyncTaskRunner<Progress, Result> {

	void taskStarting();

	void taskCompleted(Result result);

	void taskProgress(String urlString, Progress progress);

	void taskErrorMessage(Result result);
	
	Context getContext();


}

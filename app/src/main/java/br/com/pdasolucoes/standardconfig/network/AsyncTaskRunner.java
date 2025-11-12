package br.com.pdasolucoes.standardconfig.network;

import android.os.AsyncTask;

import br.com.pdasolucoes.standardconfig.network.interfaces.IAsyncTaskCallback;


public abstract class AsyncTaskRunner<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	private IAsyncTaskCallback<Progress, Result> callback;

	public AsyncTaskRunner(IAsyncTaskCallback<Progress, Result> callback) {
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.callback != null) {
			this.callback.taskStart();
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		if (this.callback != null) {
			this.callback.taskCompleted(result);
		}
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		if (this.callback != null) {
			this.callback.taskProgress(values);
		}
	}
}
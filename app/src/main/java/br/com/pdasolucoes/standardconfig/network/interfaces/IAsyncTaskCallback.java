package br.com.pdasolucoes.standardconfig.network.interfaces;

public interface IAsyncTaskCallback<Progress,Result> {

	 void taskStart();
	 void taskCompleted(Result result);
	 void taskProgress(Progress... progress);
	 
}

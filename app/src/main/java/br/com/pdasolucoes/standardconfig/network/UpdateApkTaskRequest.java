package br.com.pdasolucoes.standardconfig.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.pdasolucoes.standardconfig.R;
import br.com.pdasolucoes.standardconfig.network.enums.MessageConfiguration;
import br.com.pdasolucoes.standardconfig.network.enums.RequestInfo;
import br.com.pdasolucoes.standardconfig.network.enums.RequestType;
import br.com.pdasolucoes.standardconfig.utils.NavigationHelper;

public class UpdateApkTaskRequest extends JsonRequestBase {

    String mobileNameApk;
    String paste;

    public UpdateApkTaskRequest(String paste, String mobileNameApk) {
        this.mobileNameApk = mobileNameApk.replaceAll(".apk", "");
        this.paste = paste;
    }

    @Override
    public JSONObject getBody() {
        return null;
    }

    @Override
    protected RequestInfo getRequestInfo() {
        return new RequestInfo(
                paste,
                mobileNameApk.concat(".apk"),
                RequestType.OnLine,
                R.string.download_apk
        );
    }

    @Override
    public void processResult(Object data) {
        Context context = NavigationHelper.getCurrentAppCompat();
        if (context == null)
            return;

        JSONObject uriJson = (JSONObject) data;
        String fileUriString = uriJson.optString("uri"); // Ex: "file:///storage/emulated/0/Download/planograma.apk"

        if (fileUriString.isEmpty()) {
            Toast.makeText(context, "URI do arquivo local inválida.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Converter a string da URI em um objeto File
        Uri localFileUri = Uri.parse(fileUriString);
        File apkFile = new File(Objects.requireNonNull(localFileUri.getPath()));

        if (!apkFile.exists()) {
            Toast.makeText(context, "Erro: Arquivo de instalação não encontrado no caminho especificado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Obter a URI segura com FileProvider
        Uri contentUri;
        String authority = context.getPackageName() + ".provider";
        contentUri = FileProvider.getUriForFile(context, authority, apkFile);

        // 3. Iniciar a instalação com a URI segura
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // Adicione um Toast de erro aqui se desejar
        }
    }


    @Override
    public void processError(MessageConfiguration result) {

    }
}

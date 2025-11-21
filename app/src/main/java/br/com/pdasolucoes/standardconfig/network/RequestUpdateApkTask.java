package br.com.pdasolucoes.standardconfig.network;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.pdasolucoes.standardconfig.R;
import br.com.pdasolucoes.standardconfig.managers.NetworkManager;
import br.com.pdasolucoes.standardconfig.network.enums.MessageConfiguration;
import br.com.pdasolucoes.standardconfig.network.interfaces.IRequest;
import br.com.pdasolucoes.standardconfig.utils.ConfigurationHelper;
import br.com.pdasolucoes.standardconfig.utils.MyApplication;
import br.com.pdasolucoes.standardconfig.utils.NavigationHelper;
import br.com.pdasolucoes.standardconfig.utils.PermissionHelper;

public class RequestUpdateApkTask extends AsyncTaskRunner<Void, Void, Object> {

    private IRequest request;

    public RequestUpdateApkTask(IRequest request) {
        super(request);
        this.request = request;

    }

    protected Object doInBackground(Void... params) {

        // Verificações iniciais (permanecem as mesmas)
        if (!NetworkManager.isNetworkOnline())
            return MessageConfiguration.NetworkError;

        Context context = NavigationHelper.getCurrentAppCompat();
        if (context == null)
            return MessageConfiguration.ContextViewError;

        try {
            // Construção da URL (permanece a mesma)
            String service = this.request.getService();
            String action = this.request.getAction(); // Ex: "planograma.apk"
            String baseUrl =
                    ConfigurationHelper
                            .loadPreference(ConfigurationHelper.ConfigurationEntry.ServerAddress, "")
                            .concat("/")
                            .concat(ConfigurationHelper.loadPreference(ConfigurationHelper.ConfigurationEntry.Directory, "")
                                    .concat("/"));
            URL url = new URL(baseUrl + service + "/" + action);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            // Você pode adicionar c.connect() aqui se necessário
            InputStream is = c.getInputStream();

            // ========================================================================
            // === INÍCIO DO BLOCO DE CÓDIGO SUBSTITUÍDO (LÓGICA MODERNA) ===
            // ========================================================================

            Uri finalUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // LÓGICA PARA ANDROID 10 (API 29) E SUPERIOR - USA MEDIADTORE

                ContentResolver resolver = context.getContentResolver();
                ContentValues values = new ContentValues();

                // 1. Descreve o arquivo que queremos criar
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, action); // "planograma.apk"
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.android.package-archive");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                // 2. Pede ao sistema para criar o arquivo na pasta Downloads e nos dar a URI
                Uri collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                Uri itemUri = resolver.insert(collection, values);

                if (itemUri == null) {
                    throw new IOException("Não foi possível criar o arquivo na pasta de Downloads.");
                }

                // 3. Usa a URI para obter um OutputStream seguro e escrever o arquivo
                try (OutputStream fos = resolver.openOutputStream(itemUri)) {
                    if (fos == null) {
                        throw new IOException("Não foi possível abrir o OutputStream para o arquivo.");
                    }
                    byte[] buffer = new byte[1024];
                    int len1;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                } // O try-with-resources fecha o 'fos' e o 'is' (se declarado nele)

                finalUri = itemUri; // A URI final é a content:// URI segura

            } else {
                // LÓGICA ANTIGA PARA ANDROID 9 (API 28) E INFERIOR - SEU CÓDIGO ORIGINAL
                // Esta parte ainda precisa da permissão WRITE_EXTERNAL_STORAGE em tempo de execução.

                File PATH = Environment.getExternalStorageDirectory();
                File file = new File(PATH, Environment.DIRECTORY_DOWNLOADS);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, action);
                if (outputFile.exists()) {
                    outputFile.delete();
                }

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int len1;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                }

                // Para versões antigas, precisamos usar o FileProvider
                finalUri = FileProvider.getUriForFile(context, MyApplication.getInstance().getPackageName() + ".provider", outputFile);
            }

            is.close(); // Fecha o InputStream da conexão

            // Retorna a URI correta (seja content:// ou file:// via FileProvider)
            return new JSONObject().put("uri", finalUri.toString());

        } catch (Exception e) {
            MessageConfiguration.ExceptionError.setExceptionErrorMessage(e.getMessage());
            return MessageConfiguration.ExceptionError;
        }
    }
}

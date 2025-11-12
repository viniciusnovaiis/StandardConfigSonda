package br.com.pdasolucoes.standardconfig.network;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.standardconfig.R;
import br.com.pdasolucoes.standardconfig.managers.NetworkManager;
import br.com.pdasolucoes.standardconfig.network.enums.MessageConfiguration;
import br.com.pdasolucoes.standardconfig.network.enums.RequestInfo;
import br.com.pdasolucoes.standardconfig.network.enums.RequestType;
import br.com.pdasolucoes.standardconfig.utils.MyApplication;
import br.com.pdasolucoes.standardconfig.utils.NavigationHelper;
import br.com.pdasolucoes.standardconfig.utils.PermissionHelper;

public class GetMobileVersionRequest extends SoapRequestBase {

    @Override
    public SoapObject getBodySoap() {
        return null;
    }

    @Override
    protected RequestInfo getRequestInfo() {
        return new RequestInfo("wsautenticacao.asmx",
                "MobileConfiguration",
                RequestType.Backgroud,
                R.string.verify_update_version,
                "http://tempuri.org/");
    }

    @Override
    public void processResult(Object data) {

        Activity context = NavigationHelper.getCurrentAppCompat();

        if (context == null || data == null)
            return;

        if (!PermissionHelper.checkWriteExternalStoragePermission(context))
            return;

        String mobileVersion;
        String mobileUpdateDescription;
        String mobileNamePaste;
        String mobileNameApk;

        try {
            mobileVersion = ((SoapObject) data).getPropertyAsString("MobileVersion");
            //usar posteriormente, caso necessário
            mobileUpdateDescription = ((SoapObject) data).getPropertyAsString("MobileUpdateDescription");
            mobileNamePaste = ((SoapObject) data).getPropertyAsString("MobileNamePaste");
            mobileNameApk = ((SoapObject) data).getPropertyAsString("MobileNameApk");
        } catch (Exception e) {
            return;
        }

        //usar posteriormente, caso necessário
        //int versionCode = BuildConfig.VERSION_CODE;
        String versionName;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }
        String[] mVersions = mobileVersion.split("[;]");
        List<Boolean> isUpdates = new ArrayList<>();

        for (String v : mVersions) {
            isUpdates.add(!versionName.equals(v.trim()));
        }

        if (!isUpdates.contains(false)) {
            NetworkManager.sendRequestApk(new UpdateApkTaskRequest(mobileNamePaste, mobileNameApk));
            return;
        }

        MyApplication.setCorrectVersion(true);

    }

    @Override
    public void processError(MessageConfiguration result) {

    }


}

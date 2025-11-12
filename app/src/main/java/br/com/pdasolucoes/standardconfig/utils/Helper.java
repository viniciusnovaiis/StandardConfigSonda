package br.com.pdasolucoes.standardconfig.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import br.com.pdasolucoes.standardconfig.R;

public class Helper {

    private static MediaPlayer mediaPlayer;

    public static String formatDateToCsharp(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    public static String formatDateTimeToCsharp(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date).replaceAll(" ", "T");
    }

    public static void actionError(boolean vibrate, boolean song) {

        Context context = NavigationHelper.getCurrentAppCompat();

        if (context == null)
            return;

        if (vibrate) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (v != null)
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

            } else if (v != null)
                v.vibrate(500);
        }

        if (song) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(context, R.raw.error);
            mediaPlayer.start();
        }

    }

    private static void closeKeyBoard(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        View focusedView = ((Activity) context).getCurrentFocus();
        if (focusedView != null) {
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    0);
        }
    }

    public static void openKeyBoard(Context context, EditText editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.requestFocus();
    }

    public static void focusEditText(Context context, final EditText editText) {
        editText.requestFocus();
        editText.setText("");
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocusFromTouch();
            }
        }, 200);
        closeKeyBoard(context);

    }

    public static void focusEditTextWithKeyBoard(Context context, final EditText editText) {
        editText.requestFocus();
        editText.setText("");
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocusFromTouch();
            }
        }, 200);
        openKeyBoard(context, editText);

    }
}

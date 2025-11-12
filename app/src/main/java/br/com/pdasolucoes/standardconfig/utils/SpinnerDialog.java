package br.com.pdasolucoes.standardconfig.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.standardconfig.R;
import br.com.pdasolucoes.standardconfig.utils.interfaces.OnSpinerItemClick;


public class SpinnerDialog {
    ArrayList<?> items;
    Activity context;
    String dTitle;
    OnSpinerItemClick onSpinerItemClick;
    AlertDialog alertDialog;
    int pos;
    int style;
    View.OnClickListener closeClickListener;
    private ArrayAdapter<Object> adapter;
    private ListView listView;

    public SpinnerDialog(Activity activity, ArrayList<?> items, String dialogTitle) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
    }

    public SpinnerDialog(Activity activity, ArrayList<?> items, String dialogTitle, View.OnClickListener closeClickListener) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.closeClickListener = closeClickListener;
    }

    public SpinnerDialog(Activity activity, ArrayList<?> items, String dialogTitle, int style) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.style = style;
    }

    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }

    public void updateListView(List<?> list){
        if (adapter!= null){

            adapter = new ArrayAdapter(this.context,
                    R.layout.adapter_item_spinner_dialog, list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(in.galaxyofandroid.spinerdialog.R.layout.dialog_layout, null);
        final TextView rippleViewClose = v.findViewById(in.galaxyofandroid.spinerdialog.R.id.close);
        rippleViewClose.setText(context.getString(R.string.fechar));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleViewClose.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
        } else {
            rippleViewClose.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);
        }
        TextView title = v.findViewById(in.galaxyofandroid.spinerdialog.R.id.spinerTitle);
        title.setText(this.dTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            title.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
        } else {
            title.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);
        }

        listView = v.findViewById(in.galaxyofandroid.spinerdialog.R.id.list);
        final EditText searchBox = v.findViewById(in.galaxyofandroid.spinerdialog.R.id.searchBox);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            searchBox.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
        } else {
            searchBox.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);
        }


        adapter = new ArrayAdapter(this.context,
                R.layout.adapter_item_spinner_dialog, this.items);
        listView.setAdapter(adapter);
        adb.setView(v);
        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = view.findViewById(R.id.text1);
                Object object = adapterView.getItemAtPosition(i);

                for (int j = 0; j < SpinnerDialog.this.items.size(); ++j) {
                    if (t.getText().toString().equalsIgnoreCase((items.get(j).toString()))) {
                        SpinnerDialog.this.pos = j;
                    }
                }

                onSpinerItemClick.onClick(object);
                SpinnerDialog.this.alertDialog.dismiss();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = view.findViewById(R.id.text1);
                Object object = parent.getItemAtPosition(position);

                for (int j = 0; j < SpinnerDialog.this.items.size(); ++j) {
                    if (t.getText().toString().equalsIgnoreCase((items.get(j).toString()))) {
                        SpinnerDialog.this.pos = j;
                    }
                }

                onSpinerItemClick.onLongClick(object);
                return true;
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(searchBox.getText().toString());
            }
        });
        if (closeClickListener == null)
            rippleViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        else
            rippleViewClose.setOnClickListener(closeClickListener);

        this.alertDialog.setOnShowListener((DialogInterface.OnShowListener) context.getApplication());
        this.alertDialog.show();
    }

}

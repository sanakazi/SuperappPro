package com.superapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.superapp.R;

import java.lang.reflect.Field;

public class PickerDialog extends Dialog implements OnClickListener {
    private String[] values;
    private TextView txtSDDone;
    private TextView txtSDTitle;
    private NumberPicker sdNPicker;
    private IDoneClickListener listener;

    public PickerDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.picker_dialog);
        Window window = this.getWindow();
        window.getAttributes().windowAnimations = R.style.AppDialogPullAnimation;
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setCancelable(true);
        initialize();
    }

    public static PickerDialog show(Context context, String[] values, IDoneClickListener listener) {
        PickerDialog pDialog = new PickerDialog(context);
        pDialog.listener = listener;
        pDialog.values = values;
        pDialog.show();
        return pDialog;
    }

    private void initialize() {
        txtSDDone = (TextView) findViewById(R.id.txtSDDone);
        txtSDTitle = (TextView) findViewById(R.id.txtSDTitle);
        sdNPicker = (NumberPicker) findViewById(R.id.sdNPicker);
        txtSDDone.setOnClickListener(this);
        setSdListView();

    }

    private void setSdListView() {
        if (values != null) {
            sdNPicker.setMinValue(0); //from array first value
            sdNPicker.setMaxValue(values.length - 1); //to array last value
            sdNPicker.setDisplayedValues(values);
            sdNPicker.setWrapSelectorWheel(true);
            setNumberPickerTextColor(sdNPicker, R.color.colorPrimary);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtSDDone) {
            getSelectedValue();
            dismiss();
        }
    }

    public void setTitle(String title){
        if(txtSDTitle!= null)
            txtSDTitle.setText(title);
    }

    public void setSelectedIndex(int index){
        if(sdNPicker !=null)
            sdNPicker.setValue(index);
    }

    public int getSelectedIndex(){
        if(sdNPicker !=null)
            return sdNPicker.getValue();
        return -1;
    }

    private void getSelectedValue() {
        if (listener != null) {
            if (values != null)
                listener.onDoneClick(values[sdNPicker.getValue()]);
        }
    }

    private boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (Exception e) {

                }
            }
        }
        return false;
    }

    public interface IDoneClickListener {
        void onDoneClick(Object object);
    }
}

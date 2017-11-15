package com.superapp.fragment.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.ViewMain;
import com.superapp.activity.ActivityMain;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;


public class BaseFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, ViewMain, OnResponseListener {

    public int fragmentType;
    public BaseAppCompatActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseAppCompatActivity) {
            activity = (BaseAppCompatActivity) context;
        }
    }


    public void onFloatingButtonClick() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        ((ActivityMain) activity).lockDrawer();
//        ((ActivityMain) activity).showHideFloatingButton(false);
        ((ActivityMain) activity).setLiveFragment(this);
        ((ActivityMain) activity).changeHeaderButton(false);
        ((ActivityMain) activity).showHideNotificationButton(true);
        ((ActivityMain) activity).showHIdeSliderButton(true);
        ((ActivityMain) activity).showHIdeSearchButton(true);
    }

//    public void showToast(String message) {
//        if (message == null)
//            return;
//        if (activity != null && activity instanceof ActivityMain) {
//            activity.makeToast(message);
//        } else {
//            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//        }
//    }

    public View setTouchNClick(int id, View view) {

        View v = view.findViewById(id);
        v.setOnClickListener(this);
        v.setOnTouchListener(BaseAppCompatActivity.TOUCH);
        return v;
    }

    public View setTouchNClickSrc(int id, View view) {
        View v = view.findViewById(id);
        v.setOnClickListener(this);
        v.setOnTouchListener(BaseAppCompatActivity.imageTouch);
        return v;
    }

    /**
     * Method use to set Click listener on View.
     *
     * @param id   Resource id of View
     * @param view your layout view.
     * @return
     */
    public View setClick(int id, View view) {

        View v = view.findViewById(id);
        v.setOnClickListener(this);
        return v;
    }


    @Override
    public void showProgressing() {
        activity.showProgressingView();
    }

    @Override
    public void hideProgressing() {
        activity.hideProgressingView();
    }

    @Override
    public void initView() {

    }

    public void onSliderButtonClick(View view) {
    }

    public void onBackButtonClick(View view) {
        activity.onBackPressed();
    }

    @Override
    public void updateView() {

    }

    /**
     * Method use to enable/disable view.
     *
     * @param id   resource id.
     * @param view
     * @param flag flag true if you want to make view enable else false
     */
    public void setViewEnable(int id, View view, boolean flag) {
        View v = view.findViewById(id);
        v.setEnabled(flag);
    }

    /**
     * Method use to set ViewVisiblity
     *
     * @param id   id resource id of View.
     * @param view
     * @param flag flag value can be VISIBLE, GONE, INVISIBLE.
     */
    public void setViewVisibility(int id, View view, int flag) {
        View v = view.findViewById(id);
        v.setVisibility(flag);
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
//		Intent i  = null;
        switch (arg0.getId()) {

        }
    }

    /**
     * Method use to set text on TextView, EditText, Button.
     *
     * @param id   resource of TextView, EditText, Button.
     * @param text string you want to set on TextView, EditText, Button
     * @param view
     */
    public void setViewText(int id, String text, View view) {
        View v = ((View) view.findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            tv.setText(text);
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setText(text);
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            btn.setText(text);
        }

    }

    /**
     * Method use to get Text from TextView, EditText, Button.
     *
     * @param id   resource id TextView, EditText, Button
     * @param view
     * @return string text from view
     */
    public String getViewText(int id, View view) {
        String text = "";
        View v = ((View) view.findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            text = tv.getText().toString().trim();
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            text = et.getText().toString().trim();
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            text = btn.getText().toString().trim();
        }
        return text;
    }


    /**
     * Method use to put focus on EditText.
     *
     * @param id   resourceid of EditText.
     * @param view
     */
    public void setEditTextFocus(int id, View view) {
        EditText et = (EditText) view.findViewById(id);
        et.requestFocus();
    }

    /**
     * To check whether ToggleButton is checked or not
     *
     * @param id   resource id ToggleButton
     * @param view
     * @return true if ToggleButton is checked else false
     */
    public boolean isToggleButtonChecked(int id, View view) {
        ToggleButton cb = (ToggleButton) view.findViewById(id);
        return cb.isChecked();
    }

    /**
     * Method use to add Checkbox listener on CheckBox
     *
     * @param id   resource id of checkbox
     * @param view
     */
    public void setCheckBoxCheckListener(int id, View view) {

        CheckBox cb = (CheckBox) view.findViewById(id);
        cb.setOnCheckedChangeListener(this);
    }

    /**
     * Method use to check checkbox
     *
     * @param id      resourceid of CheckBox
     * @param checked true for checked and else for unchecked.
     * @param view
     */
    public void setCheckBoxCheck(int id, boolean checked, View view) {
        CheckBox cb = (CheckBox) view.findViewById(id);
        cb.setChecked(checked);
    }

    /**
     * Method use to add ToggleButtonListner
     *
     * @param id   resource id of Togglebutton
     * @param view
     */
    public void setToggleButtonListner(int id, View view) {
        ToggleButton cb = (ToggleButton) view.findViewById(id);
        cb.setOnCheckedChangeListener(this);
    }


    /**
     * To check whether checkbox is checked or not
     *
     * @param id   resouce id of checkbox
     * @param view
     * @return true if checkbox is checked else false
     */
    public boolean isCheckBoxChecked(int id, View view) {
        CheckBox cb = (CheckBox) view.findViewById(id);
        return cb.isChecked();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub

    }

    /**
     * Method use to select/unselect view.
     *
     * @param id   resource id.
     * @param view
     * @param flag flag true if you want to make view selected else false
     */
    public void setViewSelected(int id, View view, boolean flag) {
        View v = view.findViewById(id);
        v.setSelected(flag);
    }

//	public void customizedActionBar(View view, String title){
//		TextView barTitle = (TextView) view.findViewById(R.id.drawer_title);
//		if(title != null && !title.equals(""))
//			barTitle.setText(title);
//		else
//			barTitle.setText("WhoAround ?");
////		ImageView drawerIcon = (ImageView) mCustomView.findViewById(R.id.drawer_icon);
//		ImageView drawerIcon = (ImageView) view.findViewById(R.id.drawer_icon);
//		drawerIcon.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				((WhoAroundHome)getActivity()).toggelDrawer();
//			}
//		});
//	}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void doAfterSaveUser() {

    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        if (responsePacket.getErrorCode() == 410) {
            ((ActivityMain) activity).logoutUser();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {

        try {
            if (ErrorType.ERROR500 == errorType) {
                activity.makeToast(getString(R.string.error500));
            } else if (ErrorType.ERROR == errorType) {
                activity.makeToast(getString(R.string.error));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

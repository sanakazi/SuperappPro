package com.superapp.activity.supersearch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.activity.base.ViewMain;
import com.superapp.beans.Region;
import com.superapp.beans.SuperSearchBean;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.RegionService;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySuperSearch extends BaseAppCompatActivity implements ViewMain {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_search);
        setStatusBarColor();
        initView();
    }

    RecyclerView rv_superSearchList;
    SSearchListAdapter adapterSuperSearch;


    @Override
    public void showProgressing() {
        showProgressingView();
    }

    @Override
    public void hideProgressing() {
        hideProgressingView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    ImageView fab;

    @Override
    public void initView() {
        setTouchNClickSrc(R.id.iv_back);
        fab = (ImageView) findViewById(R.id.faButton);
//        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.floatingButtonColor)));
        fab.setOnClickListener(v -> showSuperSearchFilterPopUp());

        rv_superSearchList = (RecyclerView) findViewById(R.id.rv_superSearchList);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_superSearchList.setHasFixedSize(true);
        rv_superSearchList.setLayoutManager(manager);
        rv_superSearchList.setItemAnimator(new DefaultItemAnimator());
        superSearchList(new JSONObject());
//        showSuperSearchFilterPopUp();
    }

    @Override
    public void updateView() {

    }

    private void setSuperSearch(ArrayList<SuperSearchBean> superSearchBean) {
        adapterSuperSearch = new SSearchListAdapter(ActivitySuperSearch.this, superSearchBean, new OnItemClickListener() {
            @Override
            public void onItemClick(Object object, int position) {
                SuperSearchBean superSearchBean = (SuperSearchBean) object;
                Intent intent = new Intent(ActivitySuperSearch.this, ActivitySearchItemDetail.class);
                intent.putExtra("superSearchBean", superSearchBean);
                startActivity(intent);
            }
        });
        rv_superSearchList.setAdapter(adapterSuperSearch);
    }

    public void superSearchList(JSONObject jsonObject) {
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivitySuperSearch.this, this, Interactor.RequestCode_GetSuperSearchDetailList, Interactor.Tag_GetSuperSearchDetailList)
                .makeJsonPostRequest(Interactor.Method_GetSuperSearchDetailList, jsonObject, true);
    }

    Spinner spinner_countryList, spinner_stateList, spinner_cityList, spinner_localityList;
    Region beanCountry, beanState, beanCity, beanLocality;


    private void showSuperSearchFilterPopUp() {
        beanCountry = null;
        beanState = null;
        beanCity = null;
        beanLocality = null;
        final Dialog superSearchFilterDialog = new Dialog(ActivitySuperSearch.this);
        superSearchFilterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) ActivitySuperSearch.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_supersearchfilter, null, false);
        superSearchFilterDialog.setContentView(view);
        final Window window = superSearchFilterDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        final TextInputLayout textInputLayoutSuperSearchName = (TextInputLayout) view.findViewById(R.id.textInputLayoutSuperSearchName);

        final EditText et_superSearchName = (EditText) view.findViewById(R.id.et_superSearchName);
        final LinearLayout ll_country = (LinearLayout) view.findViewById(R.id.ll_country);
        final LinearLayout ll_state = (LinearLayout) view.findViewById(R.id.ll_state);
        final LinearLayout ll_city = (LinearLayout) view.findViewById(R.id.ll_city);
        final LinearLayout ll_locality = (LinearLayout) view.findViewById(R.id.ll_locality);
        superSearchFilterDialog.show();

        ImageView iv_superSearchClose = (ImageView) view.findViewById(R.id.iv_superSearchClose);
        iv_superSearchClose.setOnClickListener(v -> superSearchFilterDialog.dismiss());

        TextView tv_superSearchClear = (TextView) view.findViewById(R.id.tv_superSearchClear);
        tv_superSearchClear.setOnClickListener(v -> getCountryList());

        spinner_countryList = (Spinner) view.findViewById(R.id.spinner_countryList);
        spinner_stateList = (Spinner) view.findViewById(R.id.spinner_stateList);
        spinner_cityList = (Spinner) view.findViewById(R.id.spinner_cityList);
        EditText spinner_localityList = (EditText) view.findViewById(R.id.spinner_localityList);
        spinner_countryList.setEnabled(false);
        spinner_stateList.setEnabled(false);
        spinner_cityList.setEnabled(false);
//        spinner_localityList.setEnabled(false);
        ll_state.setVisibility(View.GONE);
        ll_city.setVisibility(View.GONE);
        ll_locality.setVisibility(View.GONE);

        spinner_countryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ll_state.setVisibility(View.GONE);
                ll_city.setVisibility(View.GONE);
                ll_locality.setVisibility(View.GONE);

                if (position > 0) {
                    beanCountry = ((Region) adapterView.getItemAtPosition(position));
                    getStateList(beanCountry.getId());
                    spinner_stateList.setEnabled(false);
                    spinner_cityList.setEnabled(false);
//                    spinner_localityList.setEnabled(false);
                    ll_state.setVisibility(View.VISIBLE);
                    ll_city.setVisibility(View.GONE);
                    ll_locality.setVisibility(View.GONE);

                    beanState = null;
                    beanCity = null;
                    beanLocality = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_stateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    ll_city.setVisibility(View.GONE);
                    ll_locality.setVisibility(View.GONE);
                    return;
                }

                beanState = (Region) adapterView.getAdapter().getItem(position);
                spinner_cityList.setEnabled(false);
//                spinner_localityList.setEnabled(false);
                ll_city.setVisibility(View.VISIBLE);
                ll_locality.setVisibility(View.GONE);

                beanCity = null;
                beanLocality = null;
                getCityList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_cityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    ll_locality.setVisibility(View.GONE);
                    return;
                }

                beanCity = (Region) adapterView.getAdapter().getItem(position);
//                spinner_localityList.setEnabled(false);
                ll_locality.setVisibility(View.VISIBLE);

                beanLocality = null;
//                getLocalityList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* spinner_localityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                beanLocality = (Region) adapterView.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        view.findViewById(R.id.btn_superSearchFilter).setOnClickListener(v -> {
            JSONObject object = new JSONObject();
            try {
                if (!TextUtils.isEmpty(et_superSearchName.getText().toString().trim())) {
                    object.put("searchByName", et_superSearchName.getText().toString().trim());
                }
                if (beanCountry != null) {
                    object.put("countryid", beanCountry.getId());
                    if (beanState != null) {
                        object.put("stateid", beanState.getId());
                        if (beanCity != null) {
                            object.put("cityid", beanCity.getId());
                            /*if (beanLocality != null) {
                                object.put("localityid", beanLocality.getId());
                            }*/
                            object.put("localityid", spinner_localityList.getText().toString().trim());
                        }
                    }
                }
                superSearchFilterDialog.dismiss();
                superSearchList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getCountryList();
    }

    private void getCountryList() {
        RegionService.getRegion(this, 0, RegionService.REGION_COUNTRY, list -> {
                    SpinnerListAdapter adapter = new SpinnerListAdapter(ActivitySuperSearch.this, list);
                    spinner_countryList.setAdapter(adapter);
                    spinner_countryList.setEnabled(true);
                    spinner_stateList.setEnabled(false);
                    spinner_cityList.setEnabled(false);
                    spinner_localityList.setEnabled(false);
                }
        );
    }

    private void getStateList(long countryId) {
        RegionService.getRegion(this, countryId, RegionService.REGION_STATE, list -> {
                    SpinnerListAdapter adapter = new SpinnerListAdapter(ActivitySuperSearch.this, list);
                    spinner_stateList.setAdapter(adapter);
                    spinner_stateList.setEnabled(true);
                    spinner_cityList.setEnabled(false);
                    spinner_localityList.setEnabled(false);
                }
        );
    }

    private void getCityList() {
        RegionService.getRegion(this, beanState.getId(), RegionService.REGION_CITY, list -> {
                    SpinnerListAdapter adapter = new SpinnerListAdapter(ActivitySuperSearch.this, list);
                    spinner_cityList.setAdapter(adapter);
                    spinner_cityList.setEnabled(true);
//                    spinner_localityList.setEnabled(false);
                }
        );
    }

    public void getLocalityList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cityId", beanCity.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivitySuperSearch.this, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    ArrayList<Region> list = new ArrayList<>();
                    list.add(new Region(-1, "Select locality"));
                    list.addAll(responsePacket.getLocalityList());
                    SpinnerListAdapter adapter = new SpinnerListAdapter(ActivitySuperSearch.this, list);
                    spinner_localityList.setAdapter(adapter);
                    spinner_localityList.setEnabled(true);
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetLocality, Interactor.Tag_GetLocality)
                .makeJsonPostRequest(Interactor.Method_GetLocality, jsonObject, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (Interactor.RequestCode_GetSuperSearchDetailList == requestCode) {
            setSuperSearch(responsePacket.getSuperSearchDetailList());
        } else {
            activity.makeToast(responsePacket.getMessage());
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
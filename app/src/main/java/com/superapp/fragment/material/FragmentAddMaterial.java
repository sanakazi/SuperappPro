package com.superapp.fragment.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.beans.BeanAddMaterial;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.MaterialService;

import org.json.JSONObject;

public class FragmentAddMaterial extends BaseFragment {

    View fragmentView;

    EditText et_selectMaterial, et_selectSubMaterial, et_brand, et_color, et_size, et_quantity, et_note, et_measurements;
    TextInputLayout textInputLayoutSelectMaterial, textInputLayoutSelectSubMaterial, textInputLayoutBrand, textInputLayoutColor,
            textInputLayoutSize, textInputLayoutQuantity, textInputLayoutNote, textInputLayoutUnit;
    long materialCategoryId = 0;
    Button btn_popUpAddMaterialSubmit;
    LinearLayout ll_unit, ll_color, ll_size;
    boolean callService = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.frag_add_material, container, false);
        if (getArguments() != null) {
            materialCategoryId = getArguments().getLong("materialCategoryId");

        }
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Add Material");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
        ((ActivityMain) activity).showAd(false);
    }

    @Override
    public void initView() {
        textInputLayoutSelectMaterial = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutSelectMaterial);
        textInputLayoutSelectSubMaterial = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutSelectSubMaterial);
        textInputLayoutBrand = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutBrand);
        textInputLayoutColor = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutColor);
        textInputLayoutUnit = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutUnit);
        textInputLayoutSize = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutSize);
        textInputLayoutQuantity = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutQuantity);
        textInputLayoutNote = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutNote);
        btn_popUpAddMaterialSubmit = (Button) setTouchNClick(R.id.btn_popUpAddMaterialSubmit, fragmentView);

        ll_unit = (LinearLayout) fragmentView.findViewById(R.id.ll_unit);
        ll_color = (LinearLayout) fragmentView.findViewById(R.id.ll_color);
        ll_size = (LinearLayout) fragmentView.findViewById(R.id.ll_size);
        et_selectMaterial = (EditText) setTouchNClick(R.id.et_selectMaterial, fragmentView);
        et_selectSubMaterial = (EditText) setTouchNClick(R.id.et_selectSubMaterial, fragmentView);
        et_brand = (EditText) setTouchNClick(R.id.et_brand, fragmentView);
        et_color = (EditText) setTouchNClick(R.id.et_color, fragmentView);
        et_measurements = (EditText) setTouchNClick(R.id.et_measurements, fragmentView);
        et_size = (EditText) setTouchNClick(R.id.et_size, fragmentView);
        et_quantity = (EditText) setTouchNClick(R.id.et_quantity, fragmentView);
        et_note = (EditText) setTouchNClick(R.id.et_note, fragmentView);
        setMaterialLayout();
//        senderInterface = (DataSenderInterface) getActivity();
        ll_color.setVisibility(View.VISIBLE);
        ll_size.setVisibility(View.VISIBLE);
    }

    private boolean validateFields() {
        if (et_selectMaterial.getText().toString().equals("")) {
            textInputLayoutSelectMaterial.setErrorEnabled(true);
            textInputLayoutSelectMaterial.setError(activity.getString(R.string.thisFieldIsRequired));
            et_selectMaterial.requestFocus();
            return false;
        } else {
            textInputLayoutSelectMaterial.setErrorEnabled(false);
            textInputLayoutSelectMaterial.setError(null);
        }

        if (et_selectSubMaterial.getText().toString().equals("")) {
            textInputLayoutSelectSubMaterial.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutSelectSubMaterial.setErrorEnabled(true);
            et_selectSubMaterial.requestFocus();
            return false;
        } else {
            textInputLayoutSelectSubMaterial.setErrorEnabled(false);
            textInputLayoutSelectSubMaterial.setError(null);
        }

        if (et_brand.getText().toString().equals("")) {
            textInputLayoutBrand.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutBrand.setErrorEnabled(true);
            et_brand.requestFocus();
            return false;
        } else {
            textInputLayoutBrand.setErrorEnabled(false);
            textInputLayoutBrand.setError(null);
        }

        if (et_color.getText().toString().equals("")) {
            textInputLayoutColor.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutColor.setErrorEnabled(true);
            et_color.requestFocus();
            return false;
        } else {
            textInputLayoutColor.setErrorEnabled(false);
            textInputLayoutColor.setError(null);
        }

        if (et_measurements.getText().toString().equals("")) {
            textInputLayoutUnit.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutUnit.setErrorEnabled(true);
            et_measurements.requestFocus();
            return false;
        } else {
            textInputLayoutUnit.setErrorEnabled(false);
            textInputLayoutUnit.setError(null);
        }

        if (et_size.getText().toString().equals("")) {
            textInputLayoutSize.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutSize.setErrorEnabled(true);
            et_size.requestFocus();
            return false;
        } else {
            textInputLayoutSize.setErrorEnabled(false);
            textInputLayoutSize.setError(null);
        }

        if (et_quantity.getText().toString().equals("")) {
            textInputLayoutQuantity.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutQuantity.setErrorEnabled(true);
            et_quantity.requestFocus();
            return false;
        } else {
            textInputLayoutQuantity.setErrorEnabled(false);
            textInputLayoutQuantity.setError(null);
        }

//        if (et_note.getText().toString().equals("")) {
//            textInputLayoutNote.setError(activity.getString(R.string.thisFieldIsRequired));
//            textInputLayoutNote.setErrorEnabled(true);
//            et_note.requestFocus();
//            return false;
//        } else {
//            textInputLayoutNote.setErrorEnabled(false);
//            textInputLayoutNote.setError(null);
//        }

        return true;
    }

    public void setMaterialLayout() {
        if (materialCategoryId == 0) {
            et_selectMaterial.setFocusableInTouchMode(true);
            et_selectMaterial.setInputType(InputType.TYPE_CLASS_TEXT);
            et_selectMaterial.setFocusable(true);
            et_selectMaterial.setClickable(true);
            et_selectMaterial.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            setSubMaterial();
        }
    }

    public void setSubMaterial() {
        et_selectSubMaterial.setFocusableInTouchMode(true);
        et_selectSubMaterial.setInputType(InputType.TYPE_CLASS_TEXT);
        et_selectSubMaterial.setFocusable(true);
        et_selectSubMaterial.setClickable(true);
        et_selectSubMaterial.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ll_color.setVisibility(View.VISIBLE);
        ll_size.setVisibility(View.VISIBLE);
        et_selectSubMaterial.setText("");
        material.setTitle(et_selectSubMaterial.getText().toString());
        setBrand();
    }

    public void setBrand() {
        et_brand.setFocusableInTouchMode(true);
        et_brand.setInputType(InputType.TYPE_CLASS_TEXT);
        et_brand.setFocusable(true);
        et_brand.setClickable(true);
        et_brand.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_brand.setText("");
        material.setTitle(et_brand.getText().toString());
        setColor();
    }

    public void setColor() {
        et_color.setFocusableInTouchMode(true);
        et_color.setInputType(InputType.TYPE_CLASS_TEXT);
        et_color.setFocusable(true);
        et_color.setClickable(true);
        et_color.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_color.setText("");
        material.setTitle(et_color.getText().toString());
        setMeasurement();
    }

    public void setMeasurement() {
        et_measurements.setFocusableInTouchMode(true);
        et_measurements.setInputType(InputType.TYPE_CLASS_TEXT);
        et_measurements.setFocusable(true);
        et_measurements.setClickable(true);
        et_measurements.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_measurements.setText("");
        material.setTitle(et_measurements.getText().toString());
        setSize();
    }

    public void setSize() {
        et_size.setFocusableInTouchMode(true);
        et_size.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_size.setFocusable(true);
        et_size.setClickable(true);
        et_size.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_size.setText("");
        material.setTitle(et_size.getText().toString());
//        ll_unit.setVisibility(View.VISIBLE);
    }

    public void reSetSubMaterial() {
        et_selectSubMaterial.setFocusableInTouchMode(false);
        et_selectSubMaterial.setInputType(InputType.TYPE_NULL);
        et_selectSubMaterial.setFocusable(false);
        et_selectSubMaterial.setClickable(false);
        et_selectSubMaterial.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.category, 0);
        et_selectSubMaterial.setText("");
        reSetBrand();
    }

    public void reSetBrand() {
        et_brand.setFocusableInTouchMode(false);
        et_brand.setInputType(InputType.TYPE_NULL);
        et_brand.setFocusable(false);
        et_brand.setClickable(false);
        et_brand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.category, 0);
        et_brand.setText("");
        reSetColor();
    }

    public void reSetColor() {
        et_color.setFocusableInTouchMode(false);
        et_color.setInputType(InputType.TYPE_NULL);
        et_color.setFocusable(false);
        et_color.setClickable(false);
        et_color.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.category, 0);
        et_color.setText("");
        reSetMeasurement();
    }

    public void reSetMeasurement() {
        et_measurements.setFocusableInTouchMode(false);
        et_measurements.setInputType(InputType.TYPE_NULL);
        et_measurements.setFocusable(false);
        et_measurements.setClickable(false);
        et_measurements.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.category, 0);
        et_measurements.setText("");
        reSetSize();
    }

    public void reSetSize() {
        et_size.setFocusableInTouchMode(false);
        et_size.setInputType(InputType.TYPE_NULL);
        et_size.setFocusable(false);
        et_size.setClickable(false);
        et_size.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.category, 0);
        et_size.setText("");
//        ll_unit.setVisibility(View.GONE);
//        et_measurements.setText("");
    }

    public void sendData() {
        Intent intent = new Intent();
        intent.putExtra("materials", getMaterialList());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getFragmentManager().popBackStack();
    }

    private String getMaterialList() {
        JSONObject materials = new JSONObject();
        JSONObject material = new JSONObject();
        String strUnit = "";
        try {
            BeanSelectCategory obj = (BeanSelectCategory) et_selectMaterial.getTag();

            material.put("id", 0);
            material.put("title", et_selectMaterial.getText().toString());
            if (obj != null) {
                material.put("id", obj.getId());
                material.put("title", obj.getTitle());
            }
            materials.put("material", material);
            //-------------------------
            material = new JSONObject();
            obj = (BeanSelectCategory) et_selectSubMaterial.getTag();
            material.put("id", 0);
            material.put("title", et_selectSubMaterial.getText().toString());
            if (obj != null) {
                material.put("id", obj.getId());
                material.put("title", obj.getTitle());
            }
            materials.put("subMaterial", material);
            //----------------
            material = new JSONObject();
            obj = (BeanSelectCategory) et_brand.getTag();
            material.put("id", 0);
            material.put("title", et_brand.getText().toString());
            if (obj != null) {
                material.put("id", obj.getId());
                material.put("title", obj.getTitle());
            }
            materials.put("brand", material);
            //------------------------
            material = new JSONObject();
            obj = (BeanSelectCategory) et_color.getTag();
            material.put("id", 0);
            material.put("title", et_color.getText().toString());
            if (obj != null) {
                material.put("id", obj.getId());
                material.put("title", obj.getTitle());
            }
            materials.put("color", material);
            //--------------------
            material = new JSONObject();
            obj = (BeanSelectCategory) et_measurements.getTag();
            material.put("id", 0);
            material.put("title", et_measurements.getText().toString());
            if (obj != null) {
                material.put("id", obj.getId());
                material.put("title", obj.getTitle());
//                strUnit = obj.getTitle();
            }
            materials.put("measurement", material);
            //-------------------
            material = new JSONObject();
            obj = (BeanSelectCategory) et_size.getTag();
            material.put("id", 0);
            material.put("title", et_size.getText().toString() /*+ " " + strUnit*/);
            if (obj != null) {
                material.put("id", obj.getId());
                material.put("title", obj.getTitle());
            }
            materials.put("size", material);

            //-------------------------
            materials.put("quantity", et_quantity.getText().toString());
            if (!TextUtils.isEmpty(et_note.getText()))
                materials.put("note", et_note.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return materials.toString();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.et_selectMaterial:
                if (!view.isFocusable())
                    GetAllMaterial();
                break;
            case R.id.et_selectSubMaterial:
                if (!view.isFocusable())
                    GetAllSubMaterial();
                break;
            case R.id.et_brand:
                if (!view.isFocusable())
                    GetAllBrand();
                break;
            case R.id.et_color:
                if (!view.isFocusable())
                    GetAllColors();
                break;
            case R.id.et_measurements:
                if (!view.isFocusable())
                    GetAllMeasurements();
//                    setMeasurements();
                break;
            case R.id.et_size:
                if (!view.isFocusable())
                    GetAllSize();
                break;
            case R.id.btn_popUpAddMaterialSubmit:
                if (validateFields())
                    sendData();
                break;

        }
    }

//    private void setMeasurements() {
//        getGetAllMeasurements(activity, list -> {
//            String[] values = new String[list.size()];
//            for (int i = 0; i < list.size(); i++) {
//                values[i] = list.get(i).toString();
//            }
//            PickerDialog.show(activity, values, object -> {
//                for (BeanSelectCategory unit : list) {
//                    if (unit.getTitle().equals(object.toString())) {
//                        et_unit.setTag(unit);
//                        et_unit.setText(unit.getTitle());
//                        break;
//                    }
//                }
//            });
//        });
//    }

    public void setLayout() {
        if (id == 0) {
            setSubMaterial();
        }
    }

    long id = 0;
    BeanSelectCategory beanMaterial, beanSubMaterial, beanAllBrand, beanSize, beanColors, beanMeasurements;
    BeanAddMaterial addMaterial = new BeanAddMaterial();
    BeanMaterial material = new BeanMaterial();

    public void GetAllMaterial() {
        MaterialService.getGetAllMaterial(activity, materialCategoryId, list ->
                PopupUtils.getInstance().showMaterialListPopup(activity, "Select Material", "Search Material", object -> {
                    beanMaterial = (BeanSelectCategory) object;
                    et_selectMaterial.setText(beanMaterial.getTitle());
                    et_selectMaterial.setTag(beanMaterial);
                    id = beanMaterial.getId();
                    addMaterial.setBeanMaterial(beanMaterial);
                    material.setId(beanMaterial.getId());
                    material.setTitle(beanMaterial.getTitle());
                    if (beanMaterial.getId() == 0) {
                        setSubMaterial();
                    } else {
                        reSetSubMaterial();
                    }

                }, list, false));
    }

    public void GetAllSubMaterial() {
        MaterialService.getGetAllSubMaterials(activity, beanMaterial != null ? beanMaterial.getId() : 0, list ->
                PopupUtils.getInstance().showMaterialListPopup(activity, "Select Sub Material", "Search Sub Material", new OnViewClickListener() {
                    @Override
                    public void onViewItemClick(Object object) {
                        beanSubMaterial = (BeanSelectCategory) object;
                        et_selectSubMaterial.setTag(beanSubMaterial);
                        et_selectSubMaterial.setText(beanSubMaterial.getTitle());
                        addMaterial.setBeanSubMaterial(beanSubMaterial);
                        material.setId(beanSubMaterial.getId());
                        material.setTitle(beanSubMaterial.getTitle());
                        if (beanSubMaterial.getId() == 0) {
                            setBrand();
                        } else {
                            reSetBrand();
                        }
                    }
                }, list, false));
    }

    public void GetAllBrand() {
        MaterialService.getGetAllBrand(activity, beanSubMaterial != null ? beanSubMaterial.getId() : 0, list -> {
            PopupUtils.getInstance().showMaterialListPopup(activity, "Select Brand", "Search Brand", new OnViewClickListener() {
                @Override
                public void onViewItemClick(Object object) {
                    beanAllBrand = (BeanSelectCategory) object;
                    et_brand.setTag(beanAllBrand);
                    et_brand.setText(beanAllBrand.getTitle());
                    addMaterial.setBeanAllBrand(beanAllBrand);
                    material.setId(beanAllBrand.getId());
                    material.setTitle(beanAllBrand.getTitle());
                    if (beanAllBrand.getId() == 0) {
                        setColor();
                    } else {
                        reSetColor();
                        if (beanAllBrand != null) {
                            if (beanAllBrand.getIsColor().equalsIgnoreCase("t")) {
                                ll_color.setVisibility(View.VISIBLE);
                            } else {
                                ll_color.setVisibility(View.GONE);
                            }

//                            if (beanAllBrand != null) {
//                                if (beanAllBrand.getIsMeasurement().equalsIgnoreCase("t")) {
//                                    ll_unit.setVisibility(View.VISIBLE);
//                                } else {
//                                    ll_unit.setVisibility(View.GONE);
//                                }
//
//                                if (beanAllBrand.getIsSize().equalsIgnoreCase("t")) {
//                                    ll_size.setVisibility(View.VISIBLE);
//                                } else {
//                                    ll_size.setVisibility(View.GONE);
//                                }
//                            }
                        }
                    }
                }
            }, list, false);
        });
    }

    public void GetAllColors() {
        MaterialService.getGetAllColors(activity, beanAllBrand != null ? beanAllBrand.getId() : 0, list -> {
            PopupUtils.getInstance().showMaterialListPopup(activity, "Select Color", "Search Color", new OnViewClickListener() {
                @Override
                public void onViewItemClick(Object object) {
                    beanColors = (BeanSelectCategory) object;
                    et_color.setTag(beanColors);
                    et_color.setText(beanColors.getTitle());
                    addMaterial.setBeanColors(beanColors);
                    material.setId(beanColors.getId());
                    material.setTitle(beanColors.getTitle());
                    if (beanColors.getId() == 0) {
                        setSize();
                    } else {
                        reSetSize();
                    }
                }
            }, list, false);
        });
    }

    public void GetAllMeasurements() {
        MaterialService.getGetAllMeasurements(activity, list -> {
            PopupUtils.getInstance().showMaterialListPopup(activity, "Select Measurement", "Search Measurement", new OnViewClickListener() {
                @Override
                public void onViewItemClick(Object object) {
                    beanMeasurements = (BeanSelectCategory) object;
                    et_measurements.setTag(beanMeasurements);
                    et_measurements.setText(beanMeasurements.getTitle());
                    addMaterial.setBeanMeasurements(beanMeasurements);
                    material.setId(beanMeasurements.getId());
                    material.setTitle(beanMeasurements.getTitle());
                    if (beanMeasurements.getId() == 0) {
                        setSize();
                    } else {
                        reSetSize();
                    }
                }
            }, list, false);
        });
    }

//    public static void getGetAllMeasurements(Context context, MaterialService.OnSuccessListener listener) {
//        try {
//            JSONObject postData = new JSONObject();
//            postData.put("userId", PrefSetup.getInstance().getUserId());
//            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
//            ArrayList<BeanSelectCategory> list = new ArrayList<>();
//
//            new InteractorImpl(context, new OnResponseListener() {
//                @Override
//                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
//                        ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.addAll(responsePacket.getMeasurements());
//                        listener.OnSuccess(list);
//                    } else {
//                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onError(int requestCode, ErrorType errorType) {
//                    listener.OnSuccess(list);
//                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
//                }
//            }, Interactor.RequestCode_GetAllMeasurements, Interactor.Tag_GetAllMeasurements).makeJsonPostRequest(Interactor.Method_GetAllMeasurements, postData, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void GetAllSize() {
        MaterialService.getGetAllSize(activity, beanMeasurements != null ? beanMeasurements.getId() : 0, list -> {
            PopupUtils.getInstance().showMaterialListPopup(activity, "Select Size", "Search Size", new OnViewClickListener() {
                @Override
                public void onViewItemClick(Object object) {
                    beanSize = (BeanSelectCategory) object;
                    et_size.setTag(beanSize);
                    et_size.setText(beanSize.getTitle());
                    addMaterial.setBeanSize(beanSize);
                    material.setId(beanSize.getId());
                    material.setTitle(beanSize.getTitle());
                }
            }, list, false);
        });
    }
}
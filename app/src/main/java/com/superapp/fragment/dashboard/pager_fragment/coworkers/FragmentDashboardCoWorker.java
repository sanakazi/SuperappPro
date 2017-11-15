package com.superapp.fragment.dashboard.pager_fragment.coworkers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.CoworkerBean;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.dashboard.add_forms.FragmentAddCoWorker;
import com.superapp.swipe.Attributes;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentDashboardCoWorker extends Fragment implements OnResponseListener {

    private View fragmentView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dashboard_coworker, container, false);

        context = getActivity();
        initView();
        return fragmentView;
    }

    ProgressBar progressBarCoworker;
    RecyclerView rv_ccoWorkerList;
    TextView tv_noDataFound;
    AdapterCoWorker adapterCoWorker;
    AdapterCoWorkerNew adapterCoWorkerNew;

    public void initView() {
        progressBarCoworker = (ProgressBar) fragmentView.findViewById(R.id.progressBarCoworker);
        rv_ccoWorkerList = (RecyclerView) fragmentView.findViewById(R.id.rv_coWorkerList);
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        rv_ccoWorkerList.setHasFixedSize(true);
        rv_ccoWorkerList.setLayoutManager(manager);
        rv_ccoWorkerList.setItemAnimator(new DefaultItemAnimator());

        getAllCoWorker();
    }

    private void getAllCoWorker() {
        progressBarCoworker.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            if (subCategory != null)
                jsonObject.put("subCategoryId", subCategory.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentDashboardCoWorker.this, Interactor.RequestCode_GetCoWorkers, Interactor.Tag_GetCoWorkers)
                .makeJsonPostRequest(Interactor.Method_GetCoWorkers, jsonObject, false);
    }

    CoworkerBean coworkerBean;
    FragmentAddCoWorker fragmentAddCoworker;

    private void setAllCoWorkers(ArrayList<CoworkerBean> coWorker) {
        if (coWorker.size() > 0) {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.GONE);
            }
            rv_ccoWorkerList.setVisibility(View.VISIBLE);
            CoworkerListAdapter adapterCoWorker = new CoworkerListAdapter(ApplicationContext.getInstance(),
                    coWorker, !addingCoWorkerWithResponse, hideArrow, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(object, 1);
                    } else if (position == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("coworkerId", ((CoworkerBean) object).getId());
                        ((ActivityMain) context).replaceFragment(FragmentNames.FragEditProfileCoworker, bundle, false, false);
                    } else if (position == 2) {
                        PopupUtils.getInstance().showYesNoDialog(context, "Delete Co-worker", "Are you sure you want to delete ?", object1 ->
                                deleteCoWorker((CoworkerBean) object));
                    } else {
                        FragmentDashboardCoWorker.this.onItemClick((CoworkerBean) object);
                    }
                }
            });
            ((CoworkerListAdapter) adapterCoWorker).setMode(Attributes.Mode.Single);
            rv_ccoWorkerList.setAdapter(adapterCoWorker);
        } else {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.VISIBLE);
            }
            rv_ccoWorkerList.setVisibility(View.GONE);
        }
    }

    private void setAllCoWorkersNew(ArrayList<CoworkerBean> coWorker) {
        if (coWorker.size() > 0) {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.GONE);
            }
            rv_ccoWorkerList.setVisibility(View.VISIBLE);
            adapterCoWorkerNew = new AdapterCoWorkerNew(ApplicationContext.getInstance(), coWorker, !addingCoWorkerWithResponse, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(object, 1);
                    } /*else if (position == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("coworkerId", ((CoworkerBean) object).getId());
                        ((ActivityMain) context).replaceFragment(FragmentNames.FragEditProfileCoworker, bundle, false, false);
                    } else if (position == 2) {
                        PopupUtils.getInstance().showYesNoDialog(context, "Delete Co-worker", "Are you sure you want to delete ?", object1 -> deleteCoWorker((CoworkerBean) object));
                    }*/ else {
                        FragmentDashboardCoWorker.this.onItemClick((CoworkerBean) object);
                    }
                }
            });
            rv_ccoWorkerList.setAdapter(adapterCoWorkerNew);
        } else {
            if (tv_noDataFound != null)
                tv_noDataFound.setVisibility(View.VISIBLE);
            rv_ccoWorkerList.setVisibility(View.GONE);
        }
    }


    private void onItemClick(CoworkerBean coworker) {
        Bundle bundle = new Bundle();
        bundle.putString("CoworkerId", coworker.getId() + "");
        if (getActivity() instanceof ActivityMain) {
            ((ActivityMain) getActivity()).replaceFragment(FragmentNames.FragmentCoworkerDetail, bundle, false, false);
        }
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        if (getActivity() instanceof ActivityMain) {
            if (responsePacket.getErrorCode() == 410) {
                ((ActivityMain) getActivity()).logoutUser();
                return;
            }
        }

        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetCoWorkers == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    progressBarCoworker.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(showSwipe)) {
                        setAllCoWorkersNew(responsePacket.getCoWorkerList());
                    } else
                        setAllCoWorkers(responsePacket.getCoWorkerList());
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            } else if (Interactor.RequestCode_DeleteCoworker == requestCode) {
                getAllCoWorker();
            }
        } else {
            Toast.makeText(getActivity(), responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        if (Interactor.RequestCode_GetCoWorkers == requestCode) {
            progressBarCoworker.setVisibility(View.GONE);
        }
    }

    public void deleteCoWorker(CoworkerBean coworker) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("coworkerId", coworker.getId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());


        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentDashboardCoWorker.this, Interactor.RequestCode_DeleteCoworker, Interactor.Tag_DeleteCoworker)
                .makeJsonPostRequest(Interactor.Method_DeleteCoworker, jsonObject, false);
        getAllCoWorker();
    }


    boolean addingCoWorkerWithResponse = false;
    boolean hideArrow = false;
    OnItemClickListener onItemClick;
    BeanSelectCategory subCategory = null;
    String showSwipe;

    public void showCoWorkerPopUp(final BaseAppCompatActivity activity, final OnItemClickListener onItemClick, BeanSelectCategory subCategory, String showSwipe) {
        context = activity;
        addingCoWorkerWithResponse = true;
        this.subCategory = subCategory;
        this.showSwipe = showSwipe;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fragmentView = inflater.inflate(R.layout.popupwindow_coworkerlist, null, false);
        dialog.setContentView(fragmentView);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        this.onItemClick = (Object object, int position) -> {
            if (onItemClick != null)
                onItemClick.onItemClick(object, position);
            dialog.dismiss();
        };

        final ImageView iv_close = (ImageView) fragmentView.findViewById(R.id.iv_close);

        iv_close.setOnClickListener((View v) -> dialog.dismiss());
        initView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentAddCoworker != null) {
            fragmentAddCoworker.onActivityResult(requestCode, resultCode, data);
        }
    }
}
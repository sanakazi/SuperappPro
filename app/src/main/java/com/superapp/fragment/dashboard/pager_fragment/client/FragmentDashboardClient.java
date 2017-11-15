package com.superapp.fragment.dashboard.pager_fragment.client;

import android.app.Dialog;
import android.content.Context;
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
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.ClientBean;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.dashboard.add_forms.FragmentAddClient;
import com.superapp.swipe.Attributes;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rakesh on 03-Aug-16.
 */
public class FragmentDashboardClient extends Fragment implements OnResponseListener {

    private View fragmentView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dashboard_client, container, false);
        context = getActivity();
        initView();
        return fragmentView;
    }

    TextView tv_noDataFound;
    ProgressBar progressBarClient;
    RecyclerView rv_clientList;
    AdapterClient adapterClient;
    AdapterClientNew adapterClientNew;

    public void initView() {
        progressBarClient = (ProgressBar) fragmentView.findViewById(R.id.progressBarClient);
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_clientList = (RecyclerView) fragmentView.findViewById(R.id.rv_clientList);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        rv_clientList.setHasFixedSize(true);
        rv_clientList.setLayoutManager(manager);
        rv_clientList.setItemAnimator(new DefaultItemAnimator());
        getAllClients();
    }

    private void getAllClients() {
        progressBarClient.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentDashboardClient.this, Interactor.RequestCode_GetClients, Interactor.Tag_GetClients)
                .makeJsonPostRequest(Interactor.Method_GetClients, jsonObject, false);
    }

    ClientBean clientBean;
    FragmentAddClient fragmentAddClient;

    private void onItemClick(ClientBean client) {
        Bundle bundle = new Bundle();
        bundle.putString("ClientId", client.getId() + "");

        if (getActivity() instanceof ActivityMain) {
            ((ActivityMain) getActivity()).replaceFragment(FragmentNames.FragmentClientDetail, bundle, false, false);
        }
    }

    private void setAllClients(ArrayList<ClientBean> clients) {
        if (clients.size() > 0) {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.GONE);
            }
            rv_clientList.setVisibility(View.VISIBLE);
            ClientListAdapter adapter = new ClientListAdapter(ApplicationContext.getInstance(), clients, !addingClientWithResponse, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(object, 1);
                    } else if (position == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("clientId", ((ClientBean) object).getId());
                        ((ActivityMain) context).replaceFragment(FragmentNames.FragEditProfileClient, bundle, false, false);
                    } else if (position == 2) {
                        PopupUtils.getInstance().showYesNoDialog(context, "Delete Client", "Are you sure you want to delete ?", object1 ->
                                deleteClient((ClientBean) object));
                    } else {
                        FragmentDashboardClient.this.onItemClick((ClientBean) object);
                    }
                }
            });
            ((ClientListAdapter) adapter).setMode(Attributes.Mode.Single);
            rv_clientList.setAdapter(adapter);
        } else {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.VISIBLE);
            }
            rv_clientList.setVisibility(View.GONE);
        }
    }

    private void setAllClientsNew(ArrayList<ClientBean> clients) {
        if (clients.size() > 0) {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.GONE);
            }
            rv_clientList.setVisibility(View.VISIBLE);
            adapterClientNew = new AdapterClientNew(ApplicationContext.getInstance(), clients, !addingClientWithResponse, (object, position) -> {
                if (onItemClick != null) {
                    onItemClick.onItemClick(object, 1);
                } /*else if (position == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("clientId", ((ClientBean) object).getId());
                    ((ActivityMain) context).replaceFragment(FragmentNames.FragEditProfileClient, bundle, false, false);
                } else if (position == 2) {
                    PopupUtils.getInstance().showYesNoDialog(context, "Delete Client", "Are you sure you want to delete ?", object1 ->
                            deleteClient((ClientBean) object));
                }*/ else {
                    FragmentDashboardClient.this.onItemClick((ClientBean) object);
                }
            });
            rv_clientList.setAdapter(adapterClientNew);
        } else {
            if (tv_noDataFound != null) {
                tv_noDataFound.setVisibility(View.VISIBLE);
            }
            rv_clientList.setVisibility(View.GONE);
        }
    }


    public void deleteClient(ClientBean clients) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("clientId", clients.getId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentDashboardClient.this, Interactor.RequestCode_DeleteClient, Interactor.Tag_DeleteClient)
                .makeJsonPostRequest(Interactor.Method_DeleteClient, jsonObject, false);
        getAllClients();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            if (Interactor.RequestCode_GetClients == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    progressBarClient.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(showSwipe)) {
                        setAllClientsNew(responsePacket.getClientList());
                    } else
                        setAllClients(responsePacket.getClientList());
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            }
        } else {
            Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        if (Interactor.RequestCode_GetClients == requestCode) {
            progressBarClient.setVisibility(View.GONE);
        }
    }

    boolean addingClientWithResponse = false;
    OnItemClickListener onItemClick;
    String showSwipe;

    public void showClientPopUp(final Context activity, final OnItemClickListener onItemClick, String showSwipe) {
        context = activity;
        addingClientWithResponse = true;
        this.showSwipe = showSwipe;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fragmentView = inflater.inflate(R.layout.popupwindow_clientlist, null, false);
        dialog.setContentView(fragmentView);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        final ImageView iv_close = (ImageView) fragmentView.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> dialog.dismiss());
        this.onItemClick = (object, position) -> {
            if (onItemClick != null)
                onItemClick.onItemClick(object, position);
            dialog.dismiss();
        };

        TextView tv_title = (TextView) fragmentView.findViewById(R.id.tv_title);
        tv_title.setText(R.string.selectClient);

        initView();
    }
}
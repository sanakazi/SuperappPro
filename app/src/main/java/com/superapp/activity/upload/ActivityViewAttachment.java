package com.superapp.activity.upload;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ViewMain;
import com.superapp.beans.CommunicationBean;
import com.superapp.beans.FileBean;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityViewAttachment extends BaseAppCompatActivity implements ViewMain {

    LayoutInflater inflater;
    CommunicationBean communicationBean = null;

    TextView tv_title;
    private LinearLayout ll_approvalPhotos, ll_approvalDocuments, ll_approvalComment, ll_photos, ll_document;
    private TextView tv_approvalDescription;
    EditText et_comment;
    ImageView iv_dotLine;
    LinearLayout ll_buttons;

    long selectedPhotoId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_acceptance);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            Bundle bundle = getIntent().getExtras();
            communicationBean = bundle.getParcelable("CommunicationBean");
            communicationType = bundle.getInt("communicationType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public enum COMMUNICATION_TYPE {

        APPROVAL(1),
        RECOMMENDATION(2),
        CLARIFICATION(3);

        private final int value;

        COMMUNICATION_TYPE(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    int communicationType;

    boolean enableImageSelection = true;

    @Override
    public void initView() {
        setTouchNClickSrc(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_dotLine = (ImageView) findViewById(R.id.iv_dotLine);
        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
            tv_title.setText(getString(R.string.approval));
        } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {
            tv_title.setText("Client Suggestions");
        } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
            tv_title.setText(getString(R.string.clarification));
        }

        ll_buttons = (LinearLayout) findViewById(R.id.ll_buttons);
        et_comment = (EditText) findViewById(R.id.et_comment);
        ll_approvalPhotos = (LinearLayout) findViewById(R.id.ll_approvalPhotos);
        ll_approvalDocuments = (LinearLayout) findViewById(R.id.ll_approvalDocuments);
        ll_approvalComment = (LinearLayout) findViewById(R.id.ll_approvalComment);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
        ll_document = (LinearLayout) findViewById(R.id.ll_document);

        ll_buttons.setVisibility(View.GONE);
        ll_approvalComment.setVisibility(View.VISIBLE);
        et_comment.setText(communicationBean.getClientReply());
        et_comment.setEnabled(false);
        iv_dotLine.setVisibility(View.GONE);

        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType && PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            if (TextUtils.isEmpty(communicationBean.getClientReply())) {
                ll_buttons.setVisibility(View.VISIBLE);
                ll_approvalComment.setVisibility(View.VISIBLE);
                et_comment.setEnabled(true);
                iv_dotLine.setVisibility(View.VISIBLE);
                enableImageSelection = true;
            } else {
                enableImageSelection = false;
            }

            if (communicationBean.getImages().size() > 0) {
                ll_photos.setVisibility(View.VISIBLE);
            } else {
                ll_photos.setVisibility(View.GONE);
            }


        } else if (COMMUNICATION_TYPE.APPROVAL.getValue() != communicationType && PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (TextUtils.isEmpty(communicationBean.getClientReply())) {
                ll_buttons.setVisibility(View.VISIBLE);
                et_comment.setEnabled(true);
                iv_dotLine.setVisibility(View.VISIBLE);
                enableImageSelection = true;
            } else {
                enableImageSelection = false;
            }
        } else {
            ll_buttons.setVisibility(View.GONE);
            et_comment.setEnabled(false);
            iv_dotLine.setVisibility(View.GONE);
            enableImageSelection = false;

            if (communicationBean.getImages().size() > 0) {
                ll_photos.setVisibility(View.VISIBLE);
            } else {
                ll_photos.setVisibility(View.GONE);
            }
        }

        tv_approvalDescription = (TextView) findViewById(R.id.tv_approvalDescription);
        tv_approvalDescription.setText(communicationBean.getDescription());

        setTouchNClick(R.id.btn_accept);
        setTouchNClick(R.id.btn_reject);
        setTouchNClick(R.id.btn_notConvinced);

        addApprovedImages();
        addDocuments();
        showNewView();
    }

    private void showNewView() {
        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
            ll_approvalPhotos.setVisibility(View.VISIBLE);
            ll_approvalDocuments.setVisibility(View.VISIBLE);
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                if (TextUtils.isEmpty(communicationBean.getClientReply())) {
                    ll_approvalComment.setVisibility(View.GONE);
                } else {
                    ll_approvalComment.setVisibility(View.VISIBLE);
                }
            } else {
                ll_approvalComment.setVisibility(View.VISIBLE);
            }
        } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {

            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
                if (communicationBean.getImages().size() > 0) {
                    ll_photos.setVisibility(View.VISIBLE);
                } else {
                    ll_photos.setVisibility(View.GONE);
                }
//            ll_approvalPhotos.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(communicationBean.getClientReply())) {
                    ll_approvalComment.setVisibility(View.GONE);
                } else {
                    ll_approvalComment.setVisibility(View.VISIBLE);
                }
                ll_document.setVisibility(View.GONE);
            }

            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                if (communicationBean.getImages().size() > 0) {
                    ll_photos.setVisibility(View.VISIBLE);
                } else {
                    ll_photos.setVisibility(View.GONE);
                }
            }
        } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
                if (communicationBean.getImages().size() > 0) {
                    ll_photos.setVisibility(View.VISIBLE);
                } else {
                    ll_photos.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(communicationBean.getClientReply())) {
                    ll_approvalComment.setVisibility(View.GONE);
                } else {
                    ll_approvalComment.setVisibility(View.VISIBLE);
                }
            }

            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                if (communicationBean.getImages().size() > 0) {
                    ll_photos.setVisibility(View.VISIBLE);
                } else {
                    ll_photos.setVisibility(View.GONE);
                }
            }
            ll_document.setVisibility(View.GONE);

//            ll_approvalPhotos.setVisibility(View.VISIBLE);
//            ll_approvalComment.setVisibility(View.VISIBLE);
        }
//        ll_approvalComment.setVisibility(View.VISIBLE);
    }

    private View previousView;

    private void addApprovedImages() {
        ll_approvalPhotos.removeAllViews();
        for (final FileBean image : communicationBean.getImages()) {
            final View viewItem = inflater.inflate(R.layout.activity_upload_image_item, null);
            final ImageView iv_image = (ImageView) viewItem.findViewById(R.id.iv_image);

            iv_image.setOnClickListener(v -> {
                PopupUtils.getInstance().showImageDialog(ActivityViewAttachment.this, image.getFileUrl());
            });

            TextView tv_fileName = (TextView) viewItem.findViewById(R.id.tv_fileName);
            tv_fileName.setText(image.getFileName());

            ProgressBar progressBar = (ProgressBar) viewItem.findViewById(R.id.progressBar);
//            ApplicationContext.getInstance().loadImage("http://192.168.1.2/super-app/images/coworker/" + image.getFileName(), iv_image, progressBar, R.drawable.ic_launcher);
            ApplicationContext.getInstance().loadImage(image.getFileUrl(), iv_image, progressBar, R.drawable.no_image);

            final ImageView iv_checkImage = (ImageView) viewItem.findViewById(R.id.iv_deleteImage);
            iv_checkImage.setImageResource(R.drawable.chek);

            iv_image.setOnClickListener(v -> {
                PopupUtils.getInstance().showImageDialog(ActivityViewAttachment.this, image.getFileUrl());
            });

            if ((COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType && PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c"))
                    /*|| (COMMUNICATION_TYPE.APPROVAL.getValue() != communicationType && PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d"))*/) {

                if (enableImageSelection)
                    tv_fileName.setOnClickListener(v -> {
                        if (previousView != null) {
                            previousView.findViewById(R.id.iv_deleteImage).setVisibility(View.GONE);
                        }
                        previousView = viewItem;
                        previousView.setTag(image);
                        previousView.findViewById(R.id.iv_deleteImage).setVisibility(View.VISIBLE);
                    });
            }

            if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
                if (image.getId() == communicationBean.getApprovedDocumentid()) {
                    previousView = viewItem;
                    previousView.setTag(image);
                    previousView.findViewById(R.id.iv_deleteImage).setVisibility(View.VISIBLE);
                }
            }
            ll_approvalPhotos.addView(viewItem);
        }
    }

    private void addDocuments() {
        ll_approvalDocuments.removeAllViews();
        if (communicationBean.getDocuments().size() > 0) {
            ll_document.setVisibility(View.VISIBLE);
        } else {
            ll_document.setVisibility(View.GONE);
        }
        for (final FileBean doc : communicationBean.getDocuments()) {
            final View viewItem = inflater.inflate(R.layout.activity_upload_image_item, null);
            final ImageView docImage = (ImageView) viewItem.findViewById(R.id.iv_image);

            TextView tv_fileName = (TextView) viewItem.findViewById(R.id.tv_fileName);
            tv_fileName.setText(doc.getFileName());

            if (doc.getFileType().equalsIgnoreCase("doc") || doc.getFileType().equalsIgnoreCase("docx")) {
                docImage.setImageResource(R.drawable.word);
            } else if (doc.getFileType().equalsIgnoreCase("pdf")) {
                docImage.setImageResource(R.drawable.pdf);
            } else if (doc.getFileType().equalsIgnoreCase("xls") || doc.getFileType().equalsIgnoreCase("xlsx")) {
                docImage.setImageResource(R.drawable.xls);
            }

            docImage.setOnClickListener(v -> {
                try {
                    Utilities.getInstance().openWebUrl(doc.getFileUrl(), ActivityViewAttachment.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            ll_approvalDocuments.addView(viewItem);
        }
    }

    @Override
    public void updateView() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.btn_accept:
                if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
                    if (communicationBean.getImages().size() > 0 && previousView == null) {
                        makeToast(getString(R.string.pleaseSelectAtLeastOnePhoto));
                        return;
                    }
                }

                if (previousView != null)
                    selectedPhotoId = ((FileBean) previousView.getTag()).getId();

                if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
                    et_comment.setError(getString(R.string.thisFieldIsRequired));
                    et_comment.requestFocus();
                } else {
                    et_comment.setError(null);
                }

                submitFeedBack("Accept");
                break;

            case R.id.btn_reject:
                selectedPhotoId = 0;
                if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
                    et_comment.setError(getString(R.string.thisFieldIsRequired));
                    et_comment.requestFocus();
                } else {
                    et_comment.setError(null);
                }
                submitFeedBack("Decline");
                break;

            case R.id.btn_notConvinced:
                selectedPhotoId = 0;
                if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
                    et_comment.setError(getString(R.string.thisFieldIsRequired));
                    et_comment.requestFocus();
                } else {
                    et_comment.setError(null);
                }
                submitFeedBack("Not Convinced");
                break;
        }
    }

    @Override
    public void showProgressing() {

    }

    @Override
    public void hideProgressing() {

    }

    private void submitFeedBack(String actionType) {

        Map<String, String> params = new HashMap<>();
        ArrayList<File> files = new ArrayList<>();
        try {
            params.put("userId", PrefSetup.getInstance().getUserId() + "");
            params.put("loginType", PrefSetup.getInstance().getUserLoginType() + "");
            params.put("projectId", ApplicationContext.getInstance().project.getProjectId() + "");
            params.put("communicationId", communicationBean.getId() + "");
            params.put("userFeedBack", et_comment.getText().toString());
            params.put("selectedPhotoId", selectedPhotoId + "");
            if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
                params.put("type", "Approval");
            } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {
                params.put("type", "Recommendations");
            } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
                params.put("type", "Clarification");
            }
            params.put("status", actionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(this, this, Interactor.RequestCode_AddEditCommunication, Interactor.Tag_AddEditCommunication)
                .makeFileUploadingRequest(Interactor.Method_AddEditCommunication, null, files, params, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
//        makeToast(responsePacket.getMessage());
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_AddEditCommunication == requestCode) {
                onBackPressed();
            }
        }
    }
}

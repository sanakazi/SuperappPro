package com.superapp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.beans.SQuestion;
import com.superapp.beans.Survey;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.ResponsePacket;
import com.superapp.webservice.SurveyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySurvey extends BaseAppCompatActivity {
    private ImageView iv_close;
    private TextView tvQuestion;
    private Button btn_next;
    private EditText etAnswer;
    private RadioGroup rgAnsOptions;
    private RadioGroup rgIndicator;
    private LinearLayout llTextAnswer;
    private ArrayList<SQuestion> questions = new ArrayList<>();
    private JSONObject surveyData;
    private JSONArray answerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSurvey();
    }

    public void initializeUI() {
        setContentView(R.layout.activity_survey);
        iv_close = (ImageView) setClick(R.id.iv_close);
        if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2") ||
                PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
            iv_close.setVisibility(View.VISIBLE);
        } else {
            iv_close.setVisibility(View.GONE);
        }

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);

        llTextAnswer = (LinearLayout) findViewById(R.id.llTextAnswer);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        rgAnsOptions = (RadioGroup) findViewById(R.id.rgAnsOptions);
        rgIndicator = (RadioGroup) findViewById(R.id.rgIndicator);

        btn_next = (Button) setClick(R.id.btn_next);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Utilities.getInstance().hideKeyboard(ActivitySurvey.this);
        switch (v.getId()) {
            case R.id.iv_close:
                onBackPressed();
                break;

            case R.id.btn_next:
                btnNextClickHandler();
                break;
        }
    }

    private void getSurvey() {
        Survey survey = (Survey) getIntent().getSerializableExtra("survey");
        if (survey != null) {
            initializeUI();
            setSurveyData(survey);
        } else {
            SurveyService.getSurvey(this, obj -> {
                if (obj instanceof Survey) {
                    initializeUI();
                    setSurveyData(((Survey) obj));
                } else {
                    ActivitySurvey.this.finish();
//                makeToast(((ResponsePacket) obj).getMessage());
                }
            });
        }
    }

    private void setSurveyData(Survey survey) {
        try {
            surveyData = new JSONObject();
            answerData = new JSONArray();
            initializeUI();
            if (survey.getType().equals("s")) {
                getSurveyQuestions(survey);
            } else {
                btn_next.setTag(survey);
                survey.getQuestion().setType("m");
                setQuestionView(survey.getQuestion());
            }

            surveyData.put("surveyId", survey.getId());
            surveyData.put("surveyType", survey.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getSurveyQuestions(Survey survey) {
        try {
            if (isValidAnswers() || survey.getType().equals("s")) {
                surveyData.put("dynamicAns", getSelectedAns());
                answerData = new JSONArray();
                SurveyService.getSurveyQuestions(this, survey.getId(), getSelectedAns(), obj -> {
                    ResponsePacket response = (ResponsePacket) obj;
                    if (response.getSurveyQuestions() != null) {
                        questions.clear();
                        btn_next.setTag(Integer.valueOf(0));
                        questions.addAll(response.getSurveyQuestions());
                        rgIndicator.setVisibility(View.VISIBLE);

                        setQuestionView(questions.get(0));
                        setPageIndicator(questions.size());
                        if (questions.size() == 1) {
                            btn_next.setTag("Submit");
                            btn_next.setText(getResources().getString(R.string.submit));
                        }
                    } else {
                        ActivitySurvey.this.finish();
                        makeToast(response.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnNextClickHandler() {
        if (btn_next.getTag() instanceof Survey) {
            getSurveyQuestions(((Survey) btn_next.getTag()));

        } else if (btn_next.getTag() instanceof Integer) {
            if (isValidAnswers()) {
                int count = (int) btn_next.getTag();
                addAnswer(questions.get(count));
                if (count < questions.size()) {
                    count++;
                    btn_next.setTag(count);
                    setIndicatorChecked(count);
                    setQuestionView(questions.get(count));

                    if (count == questions.size() - 1) {
                        btn_next.setTag("Submit");
                        btn_next.setText(getResources().getString(R.string.submit));
                    }
                }
            }

        } else if (btn_next.getTag() instanceof String) {
            if (isValidAnswers()) {
                if (questions.size() == 1) {
                    addAnswer(questions.get(0));
                }
                surveySubmission();
//                makeToast("Survey Submitted");
            }
        }
    }

    private void addAnswer(SQuestion sQuestion) {
        try {
            JSONObject ans = new JSONObject();
            ans.put("answer", getSelectedAns());
            ans.put("questionType", sQuestion.getType());
            ans.put("questionId", sQuestion.getId());
            answerData.put(ans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void surveySubmission() {
        try {
            surveyData.put("answers", answerData);
            SurveyService.surveySubmission(this, surveyData, obj -> {
                if (obj instanceof Survey) {
                    setSurveyData(((Survey) obj));
                } else {
                    setResult(1050);
                    ActivitySurvey.this.finish();
                    makeToast(((ResponsePacket) obj).getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidAnswers() {
        if (getSelectedAns().isEmpty() && rgAnsOptions.getVisibility() == View.VISIBLE) {
            makeToast("Please select one option");
            return false;
        }
        if (getSelectedAns().isEmpty() && llTextAnswer.getVisibility() == View.VISIBLE) {
            makeToast("Please fill your answer");
            return false;
        }
        return true;
    }

    private void setQuestionView(SQuestion question) {
        tvQuestion.setText(question.getQuestion());
        rgAnsOptions.setVisibility(View.GONE);
        llTextAnswer.setVisibility(View.GONE);
        rgAnsOptions.removeAllViews();
        etAnswer.setText("");
        if (question.getType().equals("m")) {
            rgAnsOptions.setVisibility(View.VISIBLE);
            if (question.getAns1() != null) {
                setAnsOption(question.getAns1(), "1");
            }
            if (question.getAns2() != null) {
                setAnsOption(question.getAns2(), "2");
            }
            if (question.getAns3() != null) {
                setAnsOption(question.getAns3(), "3");
            }
            if (question.getAns4() != null) {
                setAnsOption(question.getAns4(), "4");
            }
        } else {
            llTextAnswer.setVisibility(View.VISIBLE);
        }
    }

    private void setAnsOption(String ans, String tag) {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 20;
        RadioButton rBtn = new RadioButton(this);
        rBtn.setTag(tag);
        rBtn.setText(" " + ans);
        rBtn.setTextSize(16.0f);
        rBtn.setPadding(10, 0, 0, 0);
        rBtn.setLayoutParams(params);
        rBtn.setTypeface(null, Typeface.BOLD);
        rBtn.setButtonDrawable(R.drawable.checkbox_background_green);
        rBtn.setTextColor(getResources().getColor(R.color.black));
        rgAnsOptions.addView(rBtn);
    }

    private String getSelectedAns() {
        String tag = "";
        if (rgAnsOptions.getChildCount() > 0) {
            for (int i = 0; i < rgAnsOptions.getChildCount(); i++) {
                View view = rgAnsOptions.getChildAt(i);
                if (view instanceof RadioButton) {
                    if (((RadioButton) view).isChecked()) {
                        tag = view.getTag().toString();
                        break;
                    }
                }
            }
        } else {
            tag = etAnswer.getText().toString().trim();
        }
        return tag;
    }

    private void setPageIndicator(int count) {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, 10);
        params.weight = (100 / count);
        for (int i = 0; i < count; i++) {
            RadioButton rBtn = new RadioButton(this);
            rBtn.setTag("rBtn" + i);
            rBtn.setLayoutParams(params);
            rBtn.setButtonDrawable(R.drawable.survey_indicator);
            rBtn.setBackgroundResource(R.drawable.survey_indicator);
            rBtn.setClickable(false);
            rgIndicator.addView(rBtn);
            View view = new View(this);
            if (i < count - 1) {
                view.setLayoutParams(new RadioGroup.LayoutParams(10, 10));
                rgIndicator.addView(view);
            }
        }
        setIndicatorChecked(0);
    }

    private void setIndicatorChecked(int index) {
        for (int i = 0; i < rgIndicator.getChildCount(); i++) {
            View view = rgIndicator.getChildAt(i);
            if (view instanceof RadioButton) {
                String tag = view.getTag().toString();
                if (tag.equals("rBtn" + index)) {
                    ((RadioButton) view).setChecked(true);
                    break;
                }
            }
        }
    }
}

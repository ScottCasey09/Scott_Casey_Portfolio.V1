package com.fullsail.caseyscott.scottcasey_ce03;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private EditText et;
    private RadioButton viewColor_RadioButton1;
    private RadioButton viewColor_RadioButton2;
    private RadioButton viewColor_RadioButton3;
    private RadioButton textColor_RadioButton1;
    private RadioButton textColor_RadioButton2;
    private RadioButton textColor_RadioButton3;
    private FrameLayout frameLayout;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.frame_TextView);
        et = (EditText) findViewById(R.id.editText);
        et.addTextChangedListener(mTextWatcher);
        viewColor_RadioButton1 = (RadioButton) findViewById(R.id.radioButton1_backgroundColor);
        viewColor_RadioButton2 = (RadioButton) findViewById(R.id.radioButton2_backgroundColor);
        viewColor_RadioButton3 = (RadioButton) findViewById(R.id.radioButton3_backgroundColor);
        viewColor_RadioButton1.setOnClickListener(mListener);
        viewColor_RadioButton2.setOnClickListener(mListener);
        viewColor_RadioButton3.setOnClickListener(mListener);
        textColor_RadioButton1 = (RadioButton) findViewById(R.id.radioButton1_textColor);
        textColor_RadioButton2 = (RadioButton) findViewById(R.id.radioButton2_textColor);
        textColor_RadioButton3 = (RadioButton) findViewById(R.id.radioButton3_textColor);
        textColor_RadioButton1.setOnClickListener(mListener);
        textColor_RadioButton2.setOnClickListener(mListener);
        textColor_RadioButton3.setOnClickListener(mListener);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnClickListener(mListener);


    }

    private final View.OnClickListener mListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.radioButton1_backgroundColor:
                    viewColor_RadioButton2.setChecked(false);
                    viewColor_RadioButton3.setChecked(false);
                    frameLayout.setBackgroundColor(getResources().getColor(R.color.colorMinty, getTheme()));
                    break;
                case R.id.radioButton2_backgroundColor:
                    viewColor_RadioButton1.setChecked(false);
                    viewColor_RadioButton3.setChecked(false);
                    frameLayout.setBackgroundColor(getResources().getColor(R.color.colorDarkGreen, getTheme()));
                    break;
                case R.id.radioButton3_backgroundColor:
                    viewColor_RadioButton1.setChecked(false);
                    viewColor_RadioButton2.setChecked(false);
                    frameLayout.setBackgroundColor(getResources().getColor(R.color.colorBlue, getTheme()));
                    break;
                case R.id.radioButton1_textColor:
                    textColor_RadioButton2.setChecked(false);
                    textColor_RadioButton3.setChecked(false);
                    tv.setTextAppearance(R.style.textColorGreen);
                    break;
                case R.id.radioButton2_textColor:
                    textColor_RadioButton1.setChecked(false);
                    textColor_RadioButton3.setChecked(false);
                    tv.setTextAppearance(R.style.textColorOrange);
                    break;
                case R.id.radioButton3_textColor:
                    textColor_RadioButton2.setChecked(false);
                    textColor_RadioButton1.setChecked(false);
                    tv.setTextAppearance(R.style.textColorTeal);
                    break;
                case R.id.checkbox:
                    if(checkBox.isChecked()){
                        tv.setTextAppearance(R.style.textStyle_bold);
                        selectColor();
                    }else {
                        tv.setTextAppearance(R.style.textStyle_normal);
                        selectColor();
                        setFontStyle();
                    }
                    break;
            }
            if (checkBox.isChecked()){
                tv.setTextAppearance(R.style.textStyle_bold);
                selectColor();
            }

        }
    };

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv.setText(et.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectColor(){

        if (textColor_RadioButton1.isChecked()){
            tv.setTextColor(getColor(R.color.colorTextGreen));
        }else if (textColor_RadioButton2.isChecked()){
            tv.setTextColor(getColor(R.color.colorTextOrange));
        }else if(textColor_RadioButton3.isChecked()){
            tv.setTextColor(getColor(R.color.colorTextTeal));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFontStyle(){

        if (textColor_RadioButton1.isChecked()){
            tv.setTextAppearance(R.style.textColorGreen);
        }else if (textColor_RadioButton2.isChecked()){
            tv.setTextAppearance(R.style.textColorOrange);
        }else if(textColor_RadioButton3.isChecked()){
            tv.setTextAppearance(R.style.textColorTeal);
        }

    }
}

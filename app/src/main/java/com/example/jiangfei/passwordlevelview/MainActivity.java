package com.example.jiangfei.passwordlevelview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.jiangfei.passwordlevelview.widget.PasswordLevelView;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtText;
    private PasswordLevelView mPasswordLevelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPasswordLevelView = (PasswordLevelView) findViewById(R.id.pswd_level_view);
        mEdtText = (EditText) findViewById(R.id.edt_password);
        mEdtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 这里只是简单做个实例，实际项目中可以通过正则表达式来判断密码强度
                PasswordLevelView.Level level = null;
                if (TextUtils.equals(s.toString(), "1")) {
                    level = PasswordLevelView.Level.DANGER;
                } else if (TextUtils.equals(s.toString(), "12")) {
                    level = PasswordLevelView.Level.LOW;
                } else if (TextUtils.equals(s.toString(), "123")) {
                    level = PasswordLevelView.Level.MID;
                } else if (TextUtils.equals(s.toString(), "1234")) {
                    level = PasswordLevelView.Level.STRONG;
                }
                mPasswordLevelView.showLevel(level);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
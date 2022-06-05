package com.example.app_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    TextInputEditText inputUser, inputPassword, inputRegisterEmail,
            inputRegisterNickName, inputRegisterPassword,
            inputRegisterEnsure;
    Button buttonLogin, buttonRegister, buttonRegisterSubmit;
    String id, psd;
    String response;
    RequestBody body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);

        inputUser = (TextInputEditText) findViewById(R.id.inputUser);
        inputPassword = (TextInputEditText) findViewById(R.id.inputPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        clickListen(buttonLogin);
        clickListen(buttonRegister);
    }

    private void clickListen(Button view){
        view.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.buttonLogin:
                        if(!Common.isEmpty(inputUser) && !Common.isEmpty(inputPassword)) {
                            body = new FormBody.Builder()
                                    .add("id", inputUser.getText().toString())
                                    .add("psd", inputPassword.getText().toString())
                                    .build();
                            try {
//                                response = Common.doPost(Common.requestLogin, body);
                                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "do send" + response, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.buttonRegister:
                        setContentView(R.layout.page_register);
                        buttonRegisterSubmit = (Button) findViewById(R.id.buttonRegisterSubmit);
                        inputRegisterEmail = (TextInputEditText) findViewById(R.id.inputRegisterEmail);
                        inputRegisterNickName = (TextInputEditText) findViewById(R.id.inputRegisterNickName);
                        inputRegisterPassword = (TextInputEditText) findViewById(R.id.inputRegisterPassword);
                        inputRegisterEnsure = (TextInputEditText) findViewById(R.id.inputRegisterEnsure);
                        clickListen(buttonRegisterSubmit);
                        break;
                    case R.id.buttonRegisterSubmit:
                        if(Common.isEmpty(inputRegisterEmail) && Common.isEmpty(inputRegisterNickName)
                                && Common.isEmpty(inputRegisterPassword) && Common.isEmpty(inputRegisterEnsure)) {
                            if (inputRegisterPassword.getText().toString().equals(inputRegisterEnsure.getText().toString())) {
                                body = new FormBody.Builder()
                                        .add("email", inputRegisterEmail.getText().toString())
                                        .add("nickName", inputRegisterNickName.getText().toString())
                                        .add("psd", inputRegisterPassword.getText().toString())
                                        .build();
                                try {
                                    response = Common.doPost(Common.requestLogin, body);
                                    Toast.makeText(MainActivity.this, "do send" + response, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else
                                Toast.makeText(MainActivity.this, "2次密码不符，请重新输入", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
    }




}


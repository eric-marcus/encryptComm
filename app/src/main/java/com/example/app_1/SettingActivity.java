package com.example.app_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private OptionsPickerView pvNoLinkOptions;
    private ArrayList<String> sEncrypt = new ArrayList<>();
    private ArrayList<String> asEncrypt = new ArrayList<>();
    private ArrayList<String> encode = new ArrayList<>();
    private ArrayList<String> nullList = new ArrayList<>();
    Switch doubEncrySwitch;
    Switch encodeSwitch;
    SharedPreferences setting;
    SharedPreferences.Editor settingEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_setting);
        getNoLinkData();
        initNoLinkOptionsPicker();
        setting = getSharedPreferences("config",0);
        settingEditor = setting.edit();

        doubEncrySwitch = (Switch) findViewById(R.id.doubleEncrypt);
        encodeSwitch = (Switch) findViewById(R.id.encode);
        Button showPickerButton = (Button) findViewById(R.id.showPicker);
        Button settingEnsure = (Button) findViewById(R.id.settingEnsure);
        onChecked(doubEncrySwitch);
        onChecked(encodeSwitch);
        onClick(showPickerButton);
        onClick(settingEnsure);
    }

    private void onClick(Button view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.showPicker)
                    pvNoLinkOptions.show();
                if(view.getId() == R.id.settingEnsure){
                    Intent intent = new Intent(SettingActivity.this, ChatActivity.class);
                    intent.putExtra("sEncrypt",setting.getString("sEncrypt",""));
                    intent.putExtra("asEncrypt",setting.getString("asEncrypt",""));
                    intent.putExtra("encode",setting.getString("encode",""));
                    startActivity(intent);
                }

            }
        });
    }

    private void onChecked(Switch view){
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //replace 4 case with ( ? : )
                pvNoLinkOptions.setNPicker(sEncrypt, doubEncrySwitch.isChecked()?asEncrypt:nullList, encodeSwitch.isChecked()?encode:nullList);
            }
        });
    }



    private void initNoLinkOptionsPicker() {
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = "sEncrypt:" + sEncrypt.get(options1)
                        + "\nasEncrypt:" + (doubEncrySwitch.isChecked()?asEncrypt.get(options2):null)
                        + "\nencode:" + (encodeSwitch.isChecked()?encode.get(options3):null);
                settingEditor.putString("sEncrypt",sEncrypt.get(options1))
                        .putString("asEncrypt",doubEncrySwitch.isChecked()?asEncrypt.get(options2):null)
                        .putString("encode",encodeSwitch.isChecked()?encode.get(options3):null);
                settingEditor.commit();
                Toast.makeText(SettingActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        })
                .setItemVisibleCount(3)
                .build();
        pvNoLinkOptions.setNPicker(sEncrypt, nullList, nullList);
        pvNoLinkOptions.show();
    }

    private void getNoLinkData() {
        sEncrypt.add("DES");
        sEncrypt.add("AES");
        sEncrypt.add("3DES");
        asEncrypt.add("ECC");
        asEncrypt.add("RSA");
        encode.add("hex");
        encode.add("base64");
    }

}
package com.example.app_1;
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
    SharedPreferences.Editor setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_setting);
        getNoLinkData();
        initNoLinkOptionsPicker();
        setting = getSharedPreferences("config",0).edit();

        doubEncrySwitch = (Switch) findViewById(R.id.doubleEncrypt);
        encodeSwitch = (Switch) findViewById(R.id.encode);
        Button showPickerButton = (Button) findViewById(R.id.showPicker);
        onChecked(doubEncrySwitch);
        onChecked(encodeSwitch);
        onClick(showPickerButton);
    }

    private void onClick(Button view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvNoLinkOptions.show();
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
                setting.putString("sEncrypt",sEncrypt.get(options1))
                        .putString("asEncrypt",doubEncrySwitch.isChecked()?asEncrypt.get(options2):null)
                        .putString("encode",encodeSwitch.isChecked()?encode.get(options3):null);
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
//    private String getTime(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return format.format(date);
//    }
//    private void getOptionData() {
//        /*
//         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
//         * PickerView会通过getPickerViewText方法获取字符串显示出来。
//         */
//        getCardData();
//        getNoLinkData();
////选项1
//        options1Items.add(new PickerBean(0, "广东", "描述部分", "其他数据"));
//        options1Items.add(new PickerBean(1, "湖南", "描述部分", "其他数据"));
//        options1Items.add(new PickerBean(2, "广西", "描述部分", "其他数据"));
////选项2
//        ArrayList<String> options2Items_01 = new ArrayList<>();
//        options2Items_01.add("广州");
//        options2Items_01.add("佛山");
//        options2Items_01.add("东莞");
//        options2Items_01.add("珠海");
//        ArrayList<String> options2Items_02 = new ArrayList<>();
//        options2Items_02.add("长沙");
//        options2Items_02.add("岳阳");
//        options2Items_02.add("株洲");
//        options2Items_02.add("衡阳");
//        ArrayList<String> options2Items_03 = new ArrayList<>();
//        options2Items_03.add("桂林");
//        options2Items_03.add("玉林");
//        options2Items.add(options2Items_01);
//        options2Items.add(options2Items_02);
//        options2Items.add(options2Items_03);
//        /*--------数据源添加完毕---------*/
//    }
//    private void getCardData() {
//        for (int i = 0; i < 5; i++) {
//            cardItem.add(new CardBean(i, "No.ABC12345 " + i));
//        }
//        for (int i = 0; i < cardItem.size(); i++) {
//            if (cardItem.get(i).getCardNo().length() > 6) {
//                String str_item = cardItem.get(i).getCardNo().substring(0, 6) + "...";
//                cardItem.get(i).setCardNo(str_item);
//            }
//        }
//    }

}
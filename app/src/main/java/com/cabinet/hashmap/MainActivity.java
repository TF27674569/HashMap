package com.cabinet.hashmap;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cabinet.hashmap.hash.HashMap;
import com.cabinet.hashmap.hash.Map;


public class MainActivity extends AppCompatActivity {

    HashMap<String,String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 100; i++) {
            if (i == 0){
                map.put(0+"",i+"");
            }else {
                map.put(i+"",i+"");
            }
        }
        map.put("99","131131315335153135315353153315153315351351531315351");



        Map.Iterator<Map.Entry<String, String>> iterator = map.iterator();

        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            Log.e("TAG", "key:"+next.getKey()+", value:"+next.getValue() );
        }
    }
}

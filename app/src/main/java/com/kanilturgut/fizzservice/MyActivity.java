package com.kanilturgut.fizzservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        startService(new Intent(this, MyService.class));

        finish();
    }
}

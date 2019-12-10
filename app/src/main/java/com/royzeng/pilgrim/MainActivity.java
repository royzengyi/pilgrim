package com.royzeng.pilgrim;

import android.app.Activity;
import android.os.Bundle;

import com.royzeng.pilgrim.view.MainRootView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainRootView(this));
    }
}

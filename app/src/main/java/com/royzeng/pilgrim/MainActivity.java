package com.royzeng.pilgrim;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.royzeng.pilgrim.view.MainRootView;
import com.royzeng.timecostlog.ITimeCostLogger;
import com.royzeng.timecostlog.TimeCostManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainRootView(this));
        TimeCostManager.setLogger(new ITimeCostLogger() {
            @Override
            public void logger(String method, long cost) {
                if (cost > 16){
                    Log.e("DropFrame", "method:" + method + " cost:" + cost);
                }
            }
        });
    }
}

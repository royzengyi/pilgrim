package com.royzeng.pilgrim.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainRootView extends FrameLayout {

    private TextView mTitleView;
    private Button mPluginEntry;

    public MainRootView(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        mTitleView = new TextView(getContext());
        mTitleView.setText("我是主程序");
        mTitleView.setTextColor(0xff000000);
        mTitleView.setBackgroundColor(0xffeeeeee);
        mTitleView.setTextSize(16);
        mTitleView.setGravity(Gravity.CENTER);
        mTitleView.setPadding(0, 50, 0, 50);
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.TOP;
        addView(mTitleView, titleParams);

        mPluginEntry = new Button(getContext());
        mPluginEntry.setText("戳我！俺是插件");
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        buttonParams.topMargin = 500;
        addView(mPluginEntry, buttonParams);
    }
}

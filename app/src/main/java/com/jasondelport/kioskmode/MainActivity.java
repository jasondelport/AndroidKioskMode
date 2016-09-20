package com.jasondelport.kioskmode;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CustomViewGroup topView;
    private CustomViewGroup bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showNavigation = (Button) findViewById(R.id.button_show_navigation);
        showNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeBlockingViews();

                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        });

        Button hideNavigation = (Button) findViewById(R.id.button_hide_navigation);
        hideNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBlockingViews();

                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
        });

        Button setHome = (Button) findViewById(R.id.button_set_home);
        setHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });

        Button setPermission = (Button) findViewById(R.id.button_set_permission);
        setPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            }
        });

    }

    private void addBlockingViews() {

        try {
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
            WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
            localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            localLayoutParams.format = PixelFormat.RGBX_8888;
            localLayoutParams.gravity = Gravity.BOTTOM;
            localLayoutParams.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
            bottomView = new CustomViewGroup(this);
            ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    50);
            //bottomView.setBackgroundColor(getColor(android.R.color.holo_blue_light));
            bottomView.setLayoutParams(layoutParams1);
            manager.addView(bottomView, localLayoutParams);
            localLayoutParams.height = (int) (25 * getResources().getDisplayMetrics().scaledDensity);
            localLayoutParams.gravity = Gravity.TOP;
            topView = new CustomViewGroup(this);
            ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    25);
            //topView.setBackgroundColor(getColor(android.R.color.holo_blue_light));
            topView.setLayoutParams(layoutParams2);
            manager.addView(topView, localLayoutParams);
        } catch (Exception e) {
            // handle error
        }

    }

    @Override
    protected void onPause() {
        removeBlockingViews();
        super.onPause();
    }


    private void removeBlockingViews() {
        try {
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
            if (topView != null) {
                manager.removeView(topView);
            }
            if (bottomView != null) {
                manager.removeView(bottomView);
            }
        } catch (Exception e) {
            // handle error
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        addBlockingViews();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onBackPressed() {
        // do nothing
        //super.onBackPressed();
    }
}

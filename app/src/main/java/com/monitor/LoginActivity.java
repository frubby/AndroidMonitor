package com.monitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.frw.monitor.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BootstrapButton btn = (BootstrapButton) this.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.i("test", "time "+System.currentTimeMillis()/1000);
//                if (System.currentTimeMillis() / 1000 > 1496246400) {
//                    Toast toast = Toast.makeText(LoginActivity.this, "已过期.", Toast.LENGTH_LONG);
//                    toast.show();
//                    return;
//                }

                Intent it = new Intent();
                it.setClass(LoginActivity.this, ActivityNav.class);
                LoginActivity.this.startActivity(it);
                LoginActivity.this.finish();
            }
        });
    }
}

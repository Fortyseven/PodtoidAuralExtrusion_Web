package com.hacsoft.thepodtoidauralextrusion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends Activity
{

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_about);
        }


        public void btnClose(View v)
        {
                finish();
        }

        public void btnAboutClick(View v)
        {
                Button btn = (Button) v;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse((String) btn.getTag()));
                startActivity(i);
        }
}

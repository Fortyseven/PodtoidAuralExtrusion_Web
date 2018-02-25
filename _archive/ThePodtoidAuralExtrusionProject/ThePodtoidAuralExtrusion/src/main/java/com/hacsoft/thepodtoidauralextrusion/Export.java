package com.hacsoft.thepodtoidauralextrusion;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Fortyseven on 8/19/13.
 */
public class Export extends Activity
{
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.export);
        }

        public void btnExport(View v)
        {
                //Toast.makeText(this, v.toString(), Toast.LENGTH_SHORT).show();
                finish();
        }
}

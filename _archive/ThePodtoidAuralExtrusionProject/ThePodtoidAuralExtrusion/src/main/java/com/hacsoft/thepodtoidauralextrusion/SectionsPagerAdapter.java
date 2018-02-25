package com.hacsoft.thepodtoidauralextrusion;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_HOLMES;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_OTHER;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_STERLING;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_ZIMMERMAN;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{
        Context mContext;

        public SectionsPagerAdapter(Context ctx, FragmentManager fm)
        {
                super(fm);
                mContext = ctx;
        }

        @Override
        public Fragment getItem(int position)
        {
                Fragment fragment = new AudioBankFragment(mContext, position);
                return fragment;
        }

        @Override
        public int getCount()
        {
                // Show 3 total pages.
                return MainActivity.banks.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
                Locale l = Locale.getDefault();
                switch (position) {
                        case BANK_STERLING:
                                return "Sterling";
                        case BANK_HOLMES:
                                return "Holmes";
                        case BANK_ZIMMERMAN:
                                return "Zimmerman";
                        case BANK_OTHER:
                                return "Other";
                }
                return null;
        }
}

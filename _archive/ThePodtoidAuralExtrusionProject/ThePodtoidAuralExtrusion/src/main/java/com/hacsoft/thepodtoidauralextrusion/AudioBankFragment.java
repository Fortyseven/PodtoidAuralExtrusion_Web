package com.hacsoft.thepodtoidauralextrusion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_HOLMES;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_OTHER;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_STERLING;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.BANK_ZIMMERMAN;
import static com.hacsoft.thepodtoidauralextrusion.MainActivity.banks;

public class AudioBankFragment extends Fragment
{
        private static final int MAX_BUTTONS = 2;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private              int section     = BANK_STERLING;
        private LinearLayout buttonlayout;
        private LinearLayout cur_row;
        private int      col_count  = 0;
        private Typeface ttfButtons = null;
        private Typeface ttfDivider = null;


        public AudioBankFragment()
        {

        }

        public AudioBankFragment(Context ctx, int section)
        {
                this.section = section;
                cur_row = null;
                buttonlayout = null;
                col_count = 0;

                ttfButtons = Typeface.createFromAsset(ctx.getAssets(), "roboto-condensed.ttf");
                ttfDivider = Typeface.createFromAsset(ctx.getAssets(), "roboto-light.ttf");

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
                int header_id = 0;

                ArrayList<SoundBankEntry> bank;

                switch (section) {
                        case BANK_STERLING:
                                header_id = R.drawable.header_sterling;
                                bank = banks[BANK_STERLING];
                                break;
                        case BANK_HOLMES:
                                header_id = R.drawable.header_holmes;
                                bank = banks[BANK_HOLMES];
                                break;
                        case BANK_ZIMMERMAN:
                                header_id = R.drawable.header_zimmerman;
                                bank = banks[BANK_ZIMMERMAN];
                                break;
                        default:
                                header_id = R.drawable.header_other;
                                bank = banks[BANK_OTHER];
                                break;
                }

                View rootView = inflater.inflate(R.layout.fragment_main_dummy, null);

                if (rootView == null) {
                        throw new RuntimeException("Couldn't find rootView");
                }

                buttonlayout = (LinearLayout) rootView.findViewById(R.id.buttonContainer);

                ImageView ivHeader = (ImageView) rootView.findViewById(R.id.ivHeader);
                ivHeader.setImageResource(header_id);

                for (SoundBankEntry entry : bank) {
                        switch (entry.Type) {
                                case ENTRY_TYPE_LONG:
//                                                btn = createCustomButton(entry, inflater);
//                                                if (buttonlayout != null) {
//                                                        buttonlayout.addView(btn);
//                                                }
//                                                break;
                                case ENTRY_TYPE_SHORT:
                                        if (col_count % MAX_BUTTONS == 0) {
                                                // if cur_row already exists
                                                if (cur_row != null) {
                                                        // And only if there's children in the row
                                                        if (cur_row.getChildCount() > 0) {
                                                                buttonlayout.addView(cur_row);
                                                                col_count = 0;
                                                        }
                                                }
                                                cur_row = createTableRow(buttonlayout.getContext());
                                        }

                                        createCustomButton(entry, inflater);

                                        col_count++;
                                        break;

                                case ENTRY_TYPE_DIVIDER:
                                        if (cur_row != null) {
                                                buttonlayout.addView(cur_row);
                                                cur_row = null;
                                                col_count = 0;
                                        }

                                        LinearLayout vg = (LinearLayout) inflater.inflate(R.layout.divider_block, buttonlayout, false);

                                        TextView tv = (TextView) vg.getChildAt(0);

                                        tv.setTypeface(ttfDivider);
                                        tv.setText(entry.Name);

                                        buttonlayout.addView(vg);

                                        cur_row = createTableRow(buttonlayout.getContext());
                                        break;
                        }
                }

                // Attach any unattached rows
                if (cur_row != null) {
                        if (cur_row.getChildCount() > 0) {
                                buttonlayout.addView(cur_row);
                        }
                }

                cur_row = null;
                col_count = 0;

                return rootView;
        }

        /**
         * A dummy fragment representing a section of the app, but that simply
         * displays dummy text.
         */

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
                inflater.inflate(R.menu.main, menu);
        }

        private LinearLayout createTableRow(Context ctx)
        {
                LinearLayout ll = new LinearLayout(ctx);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.setLayoutParams(lp);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                return ll;
        }

        private void createCustomButton(final SoundBankEntry entry, final LayoutInflater inflater)
        {
                LinearLayout llBtn = (LinearLayout) inflater.inflate(R.layout.button_foo, cur_row, false);

                assert llBtn != null;
                Button btn = (Button) llBtn.getChildAt(0);

                if (btn != null) {
                        btn.setSoundEffectsEnabled(false);
                        btn.setTag(entry);

                        btn.setTypeface(ttfButtons);
                        btn.setOnClickListener(new View.OnClickListener()
                        {
                                @Override
                                public void onClick(View v)
                                {
                                        play(v.getContext(), ((SoundBankEntry) v.getTag()).Resource_ID);
                                }
                        });
                        btn.setText(entry.Name);
                        btn.setHorizontallyScrolling(false);
                        btn.setOnLongClickListener(new View.OnLongClickListener()
                        {
                                @Override
                                public boolean onLongClick(View v)
                                {
                                        Intent i = new Intent(inflater.getContext(), Export.class);
                                        i.putExtra("clipid", entry.Resource_ID);
                                        startActivity(i);
                                        return false;
                                }
                        });

                        if (entry.IsNew) {
                                btn.setTextColor(Color.rgb(0x99, 0xCC, 0x00));
                        }
                }
                cur_row.addView(llBtn);
        }

        void play(Context ctx, int resource_id)
        {
                if (MainActivity.last_mp != null) {
                        if (MainActivity.last_mp.isPlaying()) {
                                MainActivity.last_mp.stop();
                                MainActivity.last_mp.release();
                        }
                }
                MediaPlayer mp = MediaPlayer.create(ctx, resource_id);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                        @Override
                        public void onCompletion(MediaPlayer mp)
                        {
                                mp.release();
                                MainActivity.last_mp = null;
                        }
                });
                MainActivity.last_mp = mp;
        }


}

package com.hacsoft.thepodtoidauralextrusion;


import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.hacsoft.thepodtoidauralextrusion.SoundBankEntry.SoundBankEntryType.ENTRY_TYPE_SHORT;

public class MainActivity extends FragmentActivity
{

        public static final int         BANK_STERLING  = 0;
        public static final int         BANK_HOLMES    = 1;
        public static final int         BANK_ZIMMERMAN = 2;
        public static final int         BANK_OTHER     = 3;
        public static final int         MAX_BANKS      = 4;
        public static final Boolean     NEW_SOUND      = true;
        public static       MediaPlayer last_mp        = null;

        //public static ArrayList<SoundBankEntry>[] banks;
        static HashMap<String, ArrayList> banks = null;

        private SectionsPagerAdapter mSectionsPagerAdapter;
        private ViewPager            mViewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                try {
                        setupBanks();
                }
                catch (Exception e) {
                        Log.e("BTPLR", "Error parsing XML: " + e.getMessage());
                        finish();
                        //e.printStackTrace();
                }

                // Create the adapter that will return a fragment for each of the three
                // primary sections of the app.
                mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.pager);
                mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        @Override
        protected void onPause()
        {
                super.onPause();
//                stopSound();
                if (last_mp != null) {
                        if (last_mp.isPlaying()) {
                                last_mp.pause();
                        }
                }
        }

        @Override
        protected void onResume()
        {
                super.onResume();
                if (last_mp != null) {
                        last_mp.start();
                }
        }

        private void setupBanks() throws XmlPullParserException, IOException
        {
                banks = new HashMap<String, ArrayList>(0);

                XmlResourceParser parser = getResources().getXml(R.xml.sounds);
                int event = parser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                        ArrayList<SoundBankEntry> cur_bank = null;
                        event = parser.getEventType();

                        if (event == XmlPullParser.START_TAG && parser.getName().contentEquals("page")) {
                                Log.i("BTPLR", "creating new page = " + parser.getAttributeValue(null, "name"));
                                cur_bank = new ArrayList<SoundBankEntry>(0);
                                banks.put(parser.getAttributeValue(null, "name"), cur_bank);
                        }
                        else if (event == XmlPullParser.START_TAG && parser.getName().contentEquals("clip")) {
                                if (cur_bank != null) {
                                        cur_bank.add(new SoundBankEntry(R.raw.sterling_dafoe_yeehaw, parser.getAttributeValue(null, "name"), ENTRY_TYPE_SHORT));
                                }
                                else {
                                        throw new RuntimeException("Adding clip outside page container");
                                }
                        }
                        parser.next();
                }

                parser.close();

//                parser = getResources().getXml(R.xml.sounds);
//                int event = parser.getEventType();
//                while (event != XmlPullParser.END_DOCUMENT) {
//                        event = parser.getEventType();
//                        if (event == XmlPullParser.START_TAG && parser.getName().contentEquals("page")) {
//                                banks.add( new ArrayList<SoundBankEntry>() );
//                                Log.e("BTPLR", "sound = " + parser.getAttributeValue(null, "name"));
//                        }
//                        parser.next();
//                }

//                banks = new ArrayList[MAX_BANKS];
//                for (int i = 0; i < MAX_BANKS; i++) {
//                        banks[i] = new ArrayList<SoundBankEntry>();
//                }

//                ////JIM//////////////////////////
//
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_right, "Right?", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_exactly, "Exactly", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_laugh1, "Chuckle", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_yeeaaah, "Yeeaaaah!", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_fuckssake, "Fucks sake", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_lazyfatcunt, "Lazy fat cunt", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_havago, "Have a go", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_hormones, "Hormones", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_letdown, "That's a letdown", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_podtoidlisteners, "Podtoid listeners", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_podtoiddrinkinggame, "Podtoid drinking game", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_giveashit, "Don't give a shit", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_halflistening, "Half listening ", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_inthemood, "I'm in the mood ", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_slapahorse, "Slap a horse", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_sheepheadlock, "Sheep in a headlock", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_animalabuse, "Laugh at animal abuse", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_burningcat, "Burning cat", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_suckvideogames, "Suck a video game character...", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_celebicon, "Celebrity icon...", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_suckadickatxmas, "Dick at XMas", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_painus, "Pain in your anus", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_ducktales_erection, "McDuck's erection", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_lifeleavemybody, "See the life leave my body", ENTRY_TYPE_LONG));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dogpiss, "God's power", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_philcollins, "Phil Collins", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_philcollins2, "Phil Collins 2", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_generalvicinity, "Gonna cum on ya!", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_drinktheshit, "So happy I'm pregnant", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_massiveblackdildo, "Massive black dildo", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_profuseapologies, "Profuse apologies", ENTRY_TYPE_SHORT));
//
//                banks[BANK_STERLING].add(new SoundBankEntry("Dafoe"));
//
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_farmeranimals, "I'M FARMER ANIMALS", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_heykids, "HEY KIDS, WANNA DIE?", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_heykids_short, "HEY KIDS - Short", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_heykids_alternate, "HEY KIDS - Alt", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_horsenamedkeanu, "HORSE NAMED KEANU", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_whitegold, "WHITE GOLD", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_justtobesure, "JUST TO BE SURE...", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_yeehaw, "YEE HAW!", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_democrat, "DON'T FORGET TO VOTE", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_hellosun, "SAY HELLO TO THE SUN", ENTRY_TYPE_SHORT, true));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_dafoe_skullfuck, "SKULLFUCK YOUR MOTHER", ENTRY_TYPE_SHORT, true));
//
//                banks[BANK_STERLING].add(new SoundBankEntry("Songs"));
//
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_song1, "Streets of Rage", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_song2_killtheworld, "Kill the World", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_fight, "Fuh-fuh-fuh-fight!", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_suckaduckboner, "Suck A Duck Boner", ENTRY_TYPE_SHORT));
//
//                banks[BANK_STERLING].add(new SoundBankEntry("Trent Reznor"));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.other_trent_skyrim, "Arrow to the knee", ENTRY_TYPE_LONG));
//
//                banks[BANK_STERLING].add(new SoundBankEntry("The Lust Gurney"));
//
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_preposterous_videogame, "Preposterous name", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_fabrige_eggs, "Faberge eggs", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_burymeinthesand, "Bury me in the sand", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_disneyshead, "Get me Walt Disney's head!", ENTRY_TYPE_SHORT));
//
//                banks[BANK_STERLING].add(new SoundBankEntry("The Spidered Man"));
//
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_whyholmesspiderman, "Why isn't Jonathan Holmes Spiderman?", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_law_of_averages, "Law of Averages", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_mindmeld, "We've got a mind meld...", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_haventbeenbitten, "You haven't been bitten by the right spider.", ENTRY_TYPE_LONG));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_nogoodpieceofshit, "You no good piece of shit!", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_goddamnitparker, "Goddamn it, Peter Parker!", ENTRY_TYPE_SHORT));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_jameson, "Get me the Spidered Man!", ENTRY_TYPE_LONG));
//                banks[BANK_STERLING].add(new SoundBankEntry(R.raw.sterling_jameson_ex, "Get me the Spidered Man! - Full", ENTRY_TYPE_LONG));
//
//                ////HOLMES//////////////////////////
//
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_what, "What?", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_nudeman, "When a nude man...", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_notthatspecial, "Not that special", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_owlsarehot, "Owls are  hot", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_wii, "Everytime I say Wii", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_buttdie, "Bicycle butt", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_assisnotok, "My ass is not okay", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_safespace, "Safe space", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_welikeyou, "We like you", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_rememberskeletondick, "Remember skeleton dick", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_excitedyeah, "Excited yeah", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_sofunnysofunny, "So funny", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_somucholder, "So much older", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_prettybigforabutthole, "Pretty big butthole", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_heyguyschips, "Got any chips?", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_sexynoises, "Sexy noises", ENTRY_TYPE_SHORT));
//                banks[BANK_HOLMES].add(new SoundBankEntry(R.raw.holmes_allshitandaids, "It's all shit and AIDS", ENTRY_TYPE_SHORT, NEW_SOUND));
//
//                /////CONRAD/////////////////////////
//
//                banks[BANK_ZIMMERMAN].add(new SoundBankEntry(R.raw.zim_areyousuggestingbitten, "...bitten by every spider we can find?", ENTRY_TYPE_LONG));
//                banks[BANK_ZIMMERMAN].add(new SoundBankEntry(R.raw.zim_rebelteen, "First rebel teen", ENTRY_TYPE_SHORT));
//                banks[BANK_ZIMMERMAN].add(new SoundBankEntry(R.raw.zim_russiansub, "Like a Russian Sub", ENTRY_TYPE_SHORT));
//                banks[BANK_ZIMMERMAN].add(new SoundBankEntry(R.raw.zim_allfours, "All fours", ENTRY_TYPE_SHORT, NEW_SOUND));
//
//                ////OTHER//////////////////////////
//
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_agreement, "Group agreement", ENTRY_TYPE_SHORT));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_owlsarefuckers, "Owls are fuckers", ENTRY_TYPE_SHORT));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_littlewhitetail, "Little white tail", ENTRY_TYPE_SHORT));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_womenwithwheels, "Women with wheels", ENTRY_TYPE_SHORT));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_lustgurney, "Fetch me my lust gurney!", ENTRY_TYPE_SHORT));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_hedgehog, "Hedgehog Croquet", ENTRY_TYPE_SHORT));
//
//                banks[BANK_OTHER].add(new SoundBankEntry("Themes"));
//
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_theme_1, "1.Reptile (Skrillex)", ENTRY_TYPE_LONG));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_theme_2, "2.Main Theme (Metal Arms)", ENTRY_TYPE_LONG));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_theme_3, "3.Zed Boss Battle (Lollipop Chainsaw)", ENTRY_TYPE_LONG));
//                banks[BANK_OTHER].add(new SoundBankEntry(R.raw.other_theme_4, "4.Rave On (Killer7)", ENTRY_TYPE_LONG));

                //getApplicationContext().getResources().getIdentifier("other_theme_2", "raw", getPackageName());
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        public void btnAbout(View v)
        {
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
        }

        public void btnStopSounds(View v)
        {
                stopSound();
        }

        private void stopSound()
        {
                if (last_mp != null) {
                        if (last_mp.isPlaying()) {
                                last_mp.stop();
                                last_mp.release();
                                last_mp = null;
                        }
                }

        }
}

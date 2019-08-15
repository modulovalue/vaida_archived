package com.modestasvalauskas.vaida.Tabs.ChordProgressionTransposer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.modestasvalauskas.vaida.R;
import com.nks.dropdownwarninglibrary.DropDownWarning;

import java.util.ArrayList;
import java.util.List;

public class CPTMain extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    DropDownWarning dropDownWarning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
    }

    public void setupLayout() {
        setContentView(R.layout.activity_main);
        setupUI(findViewById(R.id.mainactivity));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CPTMainTabFragment(), "View");
        adapter.addFragment(new ChordProgressionChooserTab(), "Load");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        dropDownWarning = new DropDownWarning.Builder(this, (ViewGroup) findViewById(R.id.mainactivity))
                .interpolatorIn(new BounceInterpolator()) //Intepolator used for the "show" animation
                .interpolatorOut(new BounceInterpolator()) //Interpolator used for the "hide" animation
                .animationLength(1000) // in ms
                .textHeight(100) //Height of the text view
                .message("Super") //Message to display
                .foregroundColor(R.color.colorPrimary) //Color of the text in argb
                .backgroundColor(R.color.white) //Color of the background in argb
                .build();

        tabLayout.setupWithViewPager(viewPager);
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        hideSoftKeyboard(CPTMain.this);
                    } catch (Exception e) {
                    }
                    ;
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.test) {
            dropDownWarning.show();

            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    System.err.println(("Seconds remaining: " + millisUntilFinished / 1000));
                }

                public void onFinish() {
                    dropDownWarning.hide();
                }

            }.start();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

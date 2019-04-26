package administrator.example.com.energy;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;

import administrator.example.com.energy.fragment.AnalysisFragment;
import administrator.example.com.energy.fragment.HomeFragment;
import administrator.example.com.energy.fragment.WarningFragment;

public class MainActivity extends AppCompatActivity implements
            AnalysisFragment.OnFragmentInteractionListener,
                HomeFragment.OnFragmentInteractionListener,
                WarningFragment.OnFragmentInteractionListener
{


    private ViewPager mViewPager;
    private BottomNavigationView navigation;

    private Fragment home,analysis,warning;

    private DrawerLayout drawerlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mViewPager=(ViewPager)findViewById(R.id.mViewPager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
                //写滑动页面后做的事，使每一个fragmen与一个page相对应

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        final ArrayList<Fragment>listfragment=new ArrayList<Fragment>(3);
        home=new HomeFragment();
        analysis=new AnalysisFragment();
        warning=new WarningFragment();
        listfragment.add(home);
        listfragment.add(analysis);
        listfragment.add(warning);



        FragmentPagerAdapter mPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return listfragment.get(position);
            }

            @Override
            public int getCount() {
                return listfragment.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

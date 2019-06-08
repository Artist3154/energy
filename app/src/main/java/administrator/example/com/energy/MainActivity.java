package administrator.example.com.energy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import administrator.example.com.energy.fragment.AnalysisFragment;
import administrator.example.com.energy.fragment.HomeFragment;
import administrator.example.com.energy.fragment.WarningFragment;
import administrator.example.com.energy.service.AutoUpdateService;
import administrator.example.com.energy.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements
            AnalysisFragment.OnFragmentInteractionListener,
                HomeFragment.OnFragmentInteractionListener,
                WarningFragment.OnFragmentInteractionListener
{


    private ViewPager mViewPager;
    private BottomNavigationView navigation;

    private Fragment home,analysis,warning;
    private NavigationView naview;//侧滑栏
    private DrawerLayout drawerlayout;


    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mViewPager=(ViewPager)findViewById(R.id.mViewPager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        naview =(NavigationView)findViewById(R.id.nav_view) ;

        //必应图逻辑
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
            Intent intent=new Intent(this,AutoUpdateService.class);
            startService(intent);
        } else {
            loadBingPic();
        }

        //编写侧滑栏逻辑
        naview.setCheckedItem(R.id.nav_call);
        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_call:
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + "15957171385");
                        intent.setData(data);
                        startActivity(intent);
                        return true;
                    case R.id.nav_friends:
                        Toast.makeText(getApplicationContext(),"friends",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_location:
                        Toast.makeText(getApplicationContext(),"杭州市西湖区留和路288号",Toast.LENGTH_SHORT).show();

                        return true;
                    case R.id.nav_mail:
                        Toast.makeText(getApplicationContext(),"905985442@qq.com",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_task:
                        Toast.makeText(getApplicationContext(),"浙江工业大学",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });


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
    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this).load(bingPic).into(bingPicImg);

                        }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


}

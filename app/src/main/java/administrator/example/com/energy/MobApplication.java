package administrator.example.com.energy;

import android.app.Application;

import cn.smssdk.SMSSDK;

public class MobApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            SMSSDK.initSDK(this, "2b00ea084cec5", "8fe0b6b9ba4dacfebeb60bddb614a8e1");
        }
    }


package administrator.example.com.energy;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent1=new Intent(WelcomeActivity.this,Login2Activity.class);
                startActivity(intent1);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(timerTask,1000*2);
    }
}

package administrator.example.com.energy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends AppCompatActivity {
    private EditText nameedit;
    private EditText passwordedit;
    private Button login;
    private CheckBox checkauto;
    private CheckBox checkpswd;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button join;
    private Toolbar toolbar;

    private boolean isHideFirst = true;// 输入框密码是否是隐藏的，默认为true 小眼睛
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //sendCode(this);
        nameedit = (EditText) findViewById(R.id.username);
        passwordedit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        checkpswd = (CheckBox) findViewById(R.id.checkpswd);
        checkauto=(CheckBox)findViewById(R.id.checkauto);
        imageview = (ImageView) findViewById(R.id.eye1);
        join=(Button) findViewById(R.id.join);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //checkname=(CheckBox) findViewById(R.id.checkname);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isRemember = pref.getBoolean("remember_password", false);//初始为false
        boolean AutoLogin=pref.getBoolean("autologin",false);

        if (isRemember)//将账号和密码都设置到文本框中
        {
            String name = pref.getString("name", "");
            String password = pref.getString("password", "");
            nameedit.setText(name);
            passwordedit.setText(password);
            checkpswd.setChecked(true);
        }

        if(AutoLogin)
        {
            checkauto.setChecked(true);
        }


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameedit.getText().toString();
                String pswd = passwordedit.getText().toString();
                if (name.equals("admin") && pswd.equals("123456")) {
                    editor = pref.edit();
                    if (checkpswd.isChecked())//若复选框记住密码被选中，则将信息保存到sharedpreference，并将key设置成true
                    {
                        editor.putBoolean("remember_password", true);
                        editor.putString("name", name);
                        editor.putString("password", pswd);
                    }

                    if(checkauto.isChecked())
                    {
                        editor.putBoolean("autologin", true);
                    }

                    else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isHideFirst == true) {
                    //密文
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    passwordedit.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    //密文
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    passwordedit.setTransformationMethod(method);
                    isHideFirst = true;

                }

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //选择定制样式的toolbar

 /*   public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.user:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                //    设置Title的图标
                builder.setIcon(R.drawable.user);
                //    设置Title的内容
                builder.setTitle("警告");
                //    设置Content来显示一个信息
                builder.setMessage("确定下线吗");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(LoginActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(LoginActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(LoginActivity.this, "neutral: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
                break;

            default:
        }
        return true;
    }


    //使用官方mob图形化界面
    /*public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }*/

}

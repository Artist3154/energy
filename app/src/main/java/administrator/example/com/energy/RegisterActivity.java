package administrator.example.com.energy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Button back;
    private EditText nameedit;
    private EditText pswd1edit;
    private EditText pswd2edit;
    private Button join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back=(Button) findViewById(R.id.back);
        nameedit=(EditText)findViewById(R.id.username);
        pswd1edit=(EditText)findViewById(R.id.password1);
        pswd2edit=(EditText)findViewById(R.id.password2);
        join=(Button)findViewById(R.id.join);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameedit.getText().toString();
                String pswd1 = pswd1edit.getText().toString();
                String pswd2 = pswd2edit.getText().toString();
                if(name.equals("")||pswd1.equals("")||pswd2.equals(""))
                {
                    Toast.makeText(v.getContext(), "请完善用户信息", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(pswd1.equals(pswd2))
                    {
                        Toast.makeText(v.getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(v.getContext(), "两次密码输入不同，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}


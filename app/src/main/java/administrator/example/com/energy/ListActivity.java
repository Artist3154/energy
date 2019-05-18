package administrator.example.com.energy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import administrator.example.com.energy.Adapter.equipmentAdapter;
import administrator.example.com.energy.gson.equipment;
import administrator.example.com.energy.util.GsonUtil;
import administrator.example.com.energy.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {
    private Button back;
    private RecyclerView recyclerView;
    private List<equipment> equipmentList = new ArrayList<equipment>();
    private equipmentAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent=new Intent(ListActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //initequipment();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //编写okhhtp和gson核心代码
        Log.d("MainActivity", "1 ");
        sendRequestWithOKHttp();
        Log.d("MainActivity", "2 ");

        /*adapter = new equipmentAdapter(equipmentList);//将初始化好的equipmentList传入适配器
        Log.d("MainActivity", "5 ");
        recyclerView.setAdapter(adapter);*/

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshequ();
            }
        });

    }

    private void sendRequestWithOKHttp() {
        HttpUtil.sendOkHttpRequest("http://192.168.155.3:9099/rua", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 在这里对异常情况进行处理
                Log.d("onFailure","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 得到服务器返回的具体内容
                Log.d("MainActivity", "3 ");
                String responseData = response.body().string();//得到response中的json数据
                //equipmentList= (List<equipment>) GsonUtil.handleequ(responseData);

                //showResponse();
                parseJSONWithGSON(responseData);
            }
        });
    }

      private void parseJSONWithGSON(String jsonData) {
            Gson gson = new Gson();
          Log.d("MainActivity", "4 ");
            equipmentList = gson.fromJson(jsonData, new TypeToken<List<equipment>>() {}.getType());//将Json数据解析成equipment类的数组
            for (equipment equ : equipmentList) {
                Log.d("MainActivity", "no is " + equ.getid());
                Log.d("MainActivity", "data is " + equ.getname());
                Log.d("MainActivity", "value is " + equ.getpower());
                Log.d("MainActivity", "state is " + equ.getenergy());
            }
          showResponse();
        }

            private void showResponse()
            {
                //开启子线程显示ui
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new equipmentAdapter(equipmentList);//将初始化好的equipmentList传入适配器
                        Log.d("MainActivity", "5 ");
                        recyclerView.setAdapter(adapter);
                    }
                });
        }
    /*private void initequipment() {
        for(int i=0;i<4;i++) {
            equipment e1 = new equipment("001", 10, 1, "正常");
            equipmentList.add(e1);
            equipment e2 = new equipment("002", 5, -1, "正常");
            equipmentList.add(e2);
            equipment e3 = new equipment("003", 3, -3, "异常");
            equipmentList.add(e3);
            equipment e4 = new equipment("004", 9, 2, "正常");
            equipmentList.add(e4);
            equipment e5 = new equipment("005", 8, -2, "正常");
            equipmentList.add(e5);
        }自定义数据测试

    }*/

    private void refreshequ() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequestWithOKHttp();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}


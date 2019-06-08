package administrator.example.com.energy.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import administrator.example.com.energy.Adapter.alarmlogAdapter;
import administrator.example.com.energy.Adapter.equipmentAdapter;
import administrator.example.com.energy.ListActivity;
import administrator.example.com.energy.Login2Activity;
import administrator.example.com.energy.LoginActivity;
import administrator.example.com.energy.MainActivity;
import administrator.example.com.energy.R;
import administrator.example.com.energy.gson.equipment;
import administrator.example.com.energy.gson.history;
import administrator.example.com.energy.util.AliyunIoTSignUtil;
import administrator.example.com.energy.util.HttpUtil;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    public static String current;
    public static String energy;
    public static int[] score;//将该int类型的数组设置成静态变量，这样在全局都指向同一个空间。
    static{
        score=new int[24];
        for(int i=0;i<24;i++)
        {
            score[i]=20;
        }
    }
    public static int[] value;
    static{
        value=new int[7];
        for(int i=0;i<7;i++)
        {
            value[i]=20;
        }
    }

    //植入Android接入云平台的代码
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView msgTextView;
    private TextView currentView;
    private TextView energyView;

    private String productKey = "a1zyHPR4ive";// 高级版产品key
    private String deviceName = "Device1";//已经注册的设备id
    private String deviceSecret = "yEXO7o57Zm7AE0GtiyaF6sbYn1BSRirw";//设备秘钥

    //property post topic
    private final String payloadJson = "{\"id\":%s,\"params\":{\"current\": %s,\"energy\": %s},\"method\":\"thing.event.property.post\"}";
    private MqttClient mqttClient = null;

    final int POST_DEVICE_PROPERTIES_SUCCESS = 1002;
    final int POST_DEVICE_PROPERTIES_ERROR = 1003;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POST_DEVICE_PROPERTIES_SUCCESS:
                    showToast("发送数据成功");
                    break;
                case POST_DEVICE_PROPERTIES_ERROR:
                    showToast("post数据失败");
                    break;
            }
        }
    };

    private String responseBody = "";




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button click;
    //植入图表的代码
    private LineChartView lineChart;
    private LineChartView lineChart2;
    private List<history> historyList = new ArrayList<history>();

    //横坐标与数据点数组
    String[] date = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};//X轴的标注
    String[] day={"6-1","6-2","6-3","6-4","6-5","6-6","6-7"};
    //int[] score=new int [24];//图表的数据点
    //int [] score={12,23,32,32,22,24,12,23,32,32,22,24,12,23,32,32,22,24,12,23,32,32,22,25};
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    private List<PointValue> mPointValues2 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues2 = new ArrayList<AxisValue>();


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //toolbar.inflateMenu(R.menu.toolbar);
        setHasOptionsMenu(true);//定制标题栏时需要加上这一行
        /*View view=getActivity().getLayoutInflater().inflate(R.layout.home_fragment,null);
        toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //获取视图
        View view=inflater.inflate( R.layout.home_fragment,container,false);
        click=(Button)view.findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //标题栏
        toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu((R.menu.toolbar));
        ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("");

        //获取数据
        sendRequestWithOKHttp();

        //获取图表
        lineChart2 = view.findViewById(R.id.chart2);
        lineChart = view.findViewById(R.id.chart1);
        getAxisXLables();//获取x轴的标注
        //获取坐标点
        getAxisPoints();
        initLineChart();
        initLineChart2();//初始化



        //植入Android接入云物联网平台的代码
        msgTextView = view.findViewById(R.id.MessageView);
        currentView = view.findViewById(R.id.currentView);
        energyView=view.findViewById(R.id.energyView);

        view.findViewById(R.id.con).setOnClickListener((l) -> {
            new Thread(() -> initAliyunIoTClient()).start();
            Toast.makeText(getActivity(),"连接成功", Toast.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.reflash).setOnClickListener((l) -> {
            mHandler.postDelayed(() -> postDeviceProperties(), 1000);
        });
        view.findViewById(R.id.dis).setOnClickListener((l) -> {
            try {
                mqttClient.disconnect();
                Toast.makeText(getActivity(),"断开连接成功", Toast.LENGTH_SHORT).show();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });

        /*drawerLayout=view.findViewById(R.id.drawer_layout);
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.user);
        }*/


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d("MMM", "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*下线对话框*/
            case R.id.user:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                //    设置Title的图标
                builder.setIcon(R.drawable.quit);
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
                        Toast.makeText(getActivity(),"下线成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getActivity(),Login2Activity.class);
                        startActivity(intent);

                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(getActivity(), "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                       //Toast.makeText(getActivity(), "neutral: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
                break;
           /* case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;*/
            default:
        }
        return true;
    }


   /*  @Override
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //add toolbar
        View view=getActivity().getLayoutInflater().inflate(R.layout.home_fragment,null);
        toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("通讯录");
    }*/


    /*public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
           default:
        }
        return true;
    }*/

    /*
    设置x轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }

        for (int i = 0; i < day.length; i++) {
            mAxisXValues2.add(new AxisValue(i).setLabel(day[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {

            for (int i = 0; i < score.length; i++) {
                Log.d("aaa", "score is" + score[i]);
                mPointValues.add(new PointValue(i, score[i]));
            }//getAxisPoints();

        for (int i = 0; i < value.length; i++) {
            Log.d("aaa", "value is" + value[i]);
            mPointValues2.add(new PointValue(i, value[i]));
        }//getAxisPoints();
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        //line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setName("hour");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("电能消耗");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    private void initLineChart2() {
        Line line = new Line(mPointValues2).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        //line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setName("day");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues2);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("电能消耗");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart2.setInteractive(true);
        lineChart2.setZoomType(ZoomType.HORIZONTAL);
        lineChart2.setMaxZoom((float) 2);//最大方法比例
        lineChart2.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart2.setLineChartData(data);
        lineChart2.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart2.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart2.setCurrentViewport(v);
    }

    public void sendRequestWithOKHttp() {
        HttpUtil.sendOkHttpRequest("http://118.25.155.108:9099/qqq", new Callback() {
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

    public void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        Log.d("MainActivity", "4 ");
        historyList = gson.fromJson(jsonData, new TypeToken<List<history>>() {}.getType());//将Json数据解析成equipment类的数组
        for (history his : historyList) {
            Log.d("MainActivity", "no is " + his.getid());
            Log.d("MainActivity", "data is " + his.getname());
            Log.d("MainActivity", "value is " + his.gethour());
            Log.d("MainActivity", "state is " + his.getday());
            score=StringToIntArray(his.gethour());
            value=StringToIntArray2(his.getday());
            //getAxisPoints();
        }

        for(int i=0;i<score.length;i++) {
            Log.d("MainActivity", "score is" + score[i]);
            //mPointValues.add(new PointValue(i, score[i]));
        }
        for(int i=0;i<value.length;i++) {
            Log.d("value", "value is" + value[i]);
            //mPointValues.add(new PointValue(i, score[i]));
        }
        Log.d("MainActivity", "score length is" + score.length);
    }

    public int[] StringToIntArray(String str){
        StringTokenizer st=new StringTokenizer(str,",");
        int n=st.countTokens();
        if(n==0)return null;
        for(int i=0;i<n;i++)
        {
            score[i]=Integer.parseInt(st.nextToken());
        }
        //getAxisPoints();
        return score;
    }

    public int[] StringToIntArray2(String str){
        StringTokenizer st=new StringTokenizer(str,"，");
        int n=st.countTokens();
        if(n==0)return null;
        for(int i=0;i<n;i++)
        {
            value[i]=Integer.parseInt(st.nextToken());
        }
        //getAxisPoints();
        return value;
    }




    private void initAliyunIoTClient() {

        try {
            String clientId = "androidthings" + System.currentTimeMillis();

            Map<String, String> params = new HashMap<String, String>(16);
            params.put("productKey", productKey);
            params.put("deviceName", deviceName);
            params.put("clientId", clientId);
            String timestamp = String.valueOf(System.currentTimeMillis());
            params.put("timestamp", timestamp);

            // cn-shanghai
            String targetServer = "tcp://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

            String mqttclientId = clientId + "|securemode=3,signmethod=hmacsha1,timestamp=" + timestamp + "|";
            String mqttUsername = deviceName + "&" + productKey;
            String mqttPassword = AliyunIoTSignUtil.sign(params, deviceSecret, "hmacsha1");

            connectMqtt(targetServer, mqttclientId, mqttUsername, mqttPassword);


        } catch (Exception e) {
            e.printStackTrace();
            responseBody = e.getMessage();
            mHandler.sendEmptyMessage(POST_DEVICE_PROPERTIES_ERROR);
            Log.d(TAG, "fail " );
        }
    }

    public void connectMqtt(String url, String clientId, String mqttUsername, String mqttPassword) throws Exception {
        //设置ClienId保存形式，默认以内存报存
        MemoryPersistence persistence = new MemoryPersistence();
        mqttClient = new MqttClient(url, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // MQTT 3.1.1
        connOpts.setMqttVersion(4);
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(true);

        connOpts.setUserName(mqttUsername);
        connOpts.setPassword(mqttPassword.toCharArray());
        connOpts.setKeepAliveInterval(60);

        mqttClient.connect(connOpts);
        //Toast.makeText(getActivity(),"连接成功", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "connected " + url);
    }


    private void postDeviceProperties() {

        try {

            Random random = new Random();

            //上报数据
            int r_current=10 + random.nextInt(20);
            int r_energy=50 + random.nextInt(50);
            current=Integer.toString(r_current)+"A";
            energy=Integer.toString(r_energy)+"KJ";
            showResponse();
            String payload = String.format(payloadJson, String.valueOf(System.currentTimeMillis()), r_current,r_energy);
            responseBody = payload;
            MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
            message.setQos(1);



            String pubTopic = "/" + productKey + "/" + deviceName + "/user/data";
            mqttClient.publish(pubTopic, message);
            //mqttClient.subscribe(pubTopic);


            Log.d(TAG, "publish topic=" + pubTopic + ",payload=" + payload);
            mHandler.sendEmptyMessage(POST_DEVICE_PROPERTIES_SUCCESS);

            mHandler.postDelayed(() -> postDeviceProperties(), 5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = e.getMessage();
            mHandler.sendEmptyMessage(POST_DEVICE_PROPERTIES_ERROR);
            Log.e(TAG, "postDeviceProperties error " + e.getMessage(), e);
        }
    }

    private void showToast(String msg) {
        msgTextView.setText(msg + "\n" + responseBody);
    }


    private void showResponse()
    {
        //开启子线程显示ui
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               currentView.setText(current);
               energyView.setText(energy);
            }
        });
    }



}

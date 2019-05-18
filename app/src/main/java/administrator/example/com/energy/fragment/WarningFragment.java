package administrator.example.com.energy.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import administrator.example.com.energy.Adapter.alarmlogAdapter;
import administrator.example.com.energy.Adapter.equipmentAdapter;
import administrator.example.com.energy.LoginActivity;
import administrator.example.com.energy.R;
import administrator.example.com.energy.gson.alarmlog;
import administrator.example.com.energy.gson.equipment;
import administrator.example.com.energy.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WarningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WarningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WarningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private List<alarmlog> alarmlogList = new ArrayList<alarmlog>();
    private alarmlogAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public WarningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WarningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WarningFragment newInstance(String param1, String param2) {
        WarningFragment fragment = new WarningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate( R.layout.warning_fragment,container,false);
        toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
       // toolbar.inflateMenu((R.menu.toolbar));
        ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("");

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        sendRequestWithOKHttp();

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

    /*public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d("MMM", "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/



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
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
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


    private void sendRequestWithOKHttp() {
        HttpUtil.sendOkHttpRequest("http://192.168.155.3:9099/hello", new Callback() {
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
        alarmlogList = gson.fromJson(jsonData, new TypeToken<List<alarmlog>>() {}.getType());//将Json数据解析成equipment类的数组
        for (alarmlog ala : alarmlogList) {
            Log.d("MainActivity", "no is " + ala.getid());
            Log.d("MainActivity", "data is " + ala.getname());
            Log.d("MainActivity", "value is " + ala.getdate());
            Log.d("MainActivity", "state is " + ala.getReason());
        }
        showResponse();
    }

    private void showResponse()
    {
        //开启子线程显示ui
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new alarmlogAdapter(alarmlogList);//将初始化好的equipmentList传入适配器
                Log.d("MainActivity", "5 ");
                recyclerView.setAdapter(adapter);
            }
        });
    }
}

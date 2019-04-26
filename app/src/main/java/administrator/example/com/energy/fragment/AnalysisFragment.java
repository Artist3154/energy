package administrator.example.com.energy.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import administrator.example.com.energy.LoginActivity;
import administrator.example.com.energy.R;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalysisFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Toolbar toolbar;

    private PieChartView pieChart;     //饼状图View
    private PieChartData data;         //存放数据


    private boolean hasLabels = true;                   //是否有标语
    private boolean hasLabelsOutside = false;           //扇形外面是否有标语
    private boolean hasCenterCircle = false;            //是否有中心圆
    private boolean hasCenterText1 = false;             //是否有中心的文字
    private boolean hasCenterText2 = false;             //是否有中心的文字2
    private boolean isExploded = false;                  //是否是炸开的图像
    private boolean hasLabelForSelected = false;         //选中的扇形显示标语


    private OnFragmentInteractionListener mListener;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalysisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalysisFragment newInstance(String param1, String param2) {
        AnalysisFragment fragment = new AnalysisFragment();
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
        View view=inflater.inflate( R.layout.analysis_fragment,container,false);

        toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
       // toolbar.inflateMenu((R.menu.toolbar));
        ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("");

        pieChart=view.findViewById(R.id.pieChart);
        pieChart.setOnValueTouchListener(new ValueTouchListener());
        generateData();
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

    private void generateData() {
        int numValues = 6;   //扇形的数量
        String[] equ = {"冰箱","空调","电风扇","热水器","地暖","机器人"};
        //存放扇形数据的集合
        //设置弧度数和弧度显示文字的关键代码
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            sliceValue.setLabel(equ[i]); //设置扇形显示的文字
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");//设置中心文字

        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

        }
        pieChart.setPieChartData(data);//为饼图设置数据
    }


    /**
     * 点击监听
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }

    }

}

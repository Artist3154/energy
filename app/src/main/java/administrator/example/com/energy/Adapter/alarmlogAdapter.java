package administrator.example.com.energy.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import administrator.example.com.energy.MessageActivity;
import administrator.example.com.energy.R;
import administrator.example.com.energy.gson.alarmlog;
import administrator.example.com.energy.gson.equipment;

public class alarmlogAdapter extends RecyclerView.Adapter<alarmlogAdapter.ViewHolder> {
    private List<alarmlog> malarmlogList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View equView;
        TextView equno;
        TextView equdata;
        TextView equvalue;
        TextView equstate;

        public ViewHolder(View view) {
            super(view);
            equView = view;
            equno = (TextView) view.findViewById(R.id.no);
            equdata = (TextView) view.findViewById(R.id.data);
            equvalue = (TextView) view.findViewById(R.id.value);
            equstate = (TextView) view.findViewById(R.id.state);

        }
    }
    public alarmlogAdapter(List<alarmlog> alarmlogList)
    {
        malarmlogList=alarmlogList;
    }

    @Override
    public alarmlogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equ_item, parent, false);
        final alarmlogAdapter.ViewHolder holder=new alarmlogAdapter.ViewHolder(view);
        holder.equView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int position=holder.getAdapterPosition();
                alarmlog equ=malarmlogList.get(position);
                Toast.makeText(v.getContext(),"you clicked view"+equ.getid(),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(v.getContext(),MessageActivity.class);
                //v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(alarmlogAdapter.ViewHolder holder, int position) {
        alarmlog equ = malarmlogList.get(position);
        holder.equno.setText(equ.getid()+" ");
        holder.equdata.setText(equ.getname());
        holder.equvalue.setText(equ.getdate());
        holder.equstate.setText(equ.getReason());
    }

    @Override
    public int getItemCount() {
        return malarmlogList.size();
    }

}


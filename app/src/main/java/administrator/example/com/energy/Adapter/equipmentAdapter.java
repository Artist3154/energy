package administrator.example.com.energy.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import administrator.example.com.energy.LoginActivity;
import administrator.example.com.energy.MessageActivity;
import administrator.example.com.energy.R;
import administrator.example.com.energy.gson.equipment;

public class equipmentAdapter extends RecyclerView.Adapter<equipmentAdapter.ViewHolder>{
    private List<equipment>mequipmentList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View equView;
        TextView equno;
        TextView equdata;
        TextView equvalue;
        TextView equstate;

        public ViewHolder(View view)
        {
            super(view);
            equView=view;
            equno=(TextView)view.findViewById(R.id.no);
            equdata=(TextView)view.findViewById(R.id.data);
            equvalue=(TextView)view.findViewById(R.id.value);
            equstate=(TextView)view.findViewById(R.id.state);

        }
    }
    public equipmentAdapter(List<equipment> equipmentList)
    {
        mequipmentList=equipmentList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equ_item, parent, false);
        final ViewHolder holder=new ViewHolder(view);
        holder.equView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int position=holder.getAdapterPosition();
                equipment equ=mequipmentList.get(position);
                Toast.makeText(v.getContext(),"you clicked view"+equ.getno(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),MessageActivity.class);
                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        equipment equ = mequipmentList.get(position);
        holder.equno.setText(equ.getno());
        holder.equdata.setText(equ.getdata()+" ");
        holder.equvalue.setText(equ.getvalue()+" ");
        holder.equstate.setText(equ.getstate());
    }

    @Override
    public int getItemCount() {
        return mequipmentList.size();
    }

}


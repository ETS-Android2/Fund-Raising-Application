package com.abdulaleem.appcon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulaleem.appcon.databinding.RowProgressBinding;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProgressAdapter1 extends RecyclerView.Adapter<ProgressAdapter1.ProgressViewHolder> {
    private List<Progress> progressList;
    Context context;
    public ProgressAdapter1(Context context, List<Progress> progressList){this.progressList=progressList;
        this.context = context;};
    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress,parent,false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        Progress progress = progressList.get(position);



        //int size =progress.getImg().size();
       // System.out.println(progress.getImg());
        Glide.with(context).load(progress.getImg()).into(holder.binding.imageViewRv);





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        RowProgressBinding binding;
        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowProgressBinding.bind(itemView);
        }
    }
}

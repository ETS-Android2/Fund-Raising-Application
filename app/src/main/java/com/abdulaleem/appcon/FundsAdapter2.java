package com.abdulaleem.appcon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulaleem.appcon.databinding.RowHomeBinding;
import com.bumptech.glide.Glide;

import java.util.List;

public class FundsAdapter2 extends RecyclerView.Adapter<FundsAdapter2.FundsViewHolder>{
    private List<Funds> fundsList;
    Context context;
    public FundsAdapter2(Context context, List<Funds> fundsList){this.fundsList=fundsList;
        this.context = context;};

    @NonNull
    @Override
    public FundsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home,parent,false);
        return new FundsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundsViewHolder holder, int position) {
        Funds funds = fundsList.get(position);

        holder.binding.title.setText(funds.getTitle());

        System.out.println(funds.getPhoto());
        Glide.with(context).load(funds.getPhoto())
                .placeholder(R.drawable.ic_baseline_photo_camera_24)
                .into(holder.binding.titlePhoto);

        holder.binding.status.setText(funds.getStatus());
        holder.binding.amnt.setText(funds.getGoal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProgressActivity.class);
                intent.putExtra("title",funds.getTitle());
                intent.putExtra("category",funds.getCategory());
                intent.putExtra("status",funds.getStatus());
                intent.putExtra("goal",funds.getGoal());
                intent.putExtra("photo",funds.getPhoto());
                intent.putExtra("descr",funds.getDescription());
                intent.putExtra("key",funds.getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fundsList.size() ;
    }


    public class FundsViewHolder extends RecyclerView.ViewHolder {

        RowHomeBinding binding;
        public FundsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowHomeBinding.bind(itemView);
        }
    }
}

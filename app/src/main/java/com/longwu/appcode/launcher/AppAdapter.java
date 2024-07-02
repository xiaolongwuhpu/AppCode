package com.longwu.appcode.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longwu.appcode.R;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private Context context;
    private List<AppInfo> appList;

    public AppAdapter(Context context, List<AppInfo> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo appInfo = appList.get(position);
        holder.iconImageView.setImageDrawable(appInfo.getIcon());
        holder.nameTextView.setText(appInfo.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent launchIntent = new Intent();
            launchIntent.setComponent(new ComponentName(appInfo.getPackageName(), appInfo.getClassName()));
            context.startActivity(launchIntent);
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}

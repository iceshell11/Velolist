package com.dark1103.velolist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dark1103.velolist.R;
import org.json.JSONArray;
import org.json.JSONException;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    public Context mContext;
    public JSONArray mParks = new JSONArray();

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView parkAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parkAddress = (TextView) itemView.findViewById(R.id.address);
            this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
        }
    }

    public RecyclerViewAdapter(Context mContext2, JSONArray parks) {
        this.mParks = parks;
        this.mContext = mContext2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.id.list_item, parent, false));
        Log.d(TAG, "!!!!!!!!!!!!!:onViewHolderCreated");
        return holder;
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        try {
            Log.d(TAG, "!!!!!!!!!:onViewHolderCalled");
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.parkAddress.setText(this.mParks.getJSONObject(i).getString("Address"));
            holder.parentLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String str = "Address";
                    String str2 = RecyclerViewAdapter.TAG;
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onClick: ");
                        sb.append(RecyclerViewAdapter.this.mParks.getJSONObject(i).getString(str));
                        Log.d(str2, sb.toString());
                        Toast.makeText(RecyclerViewAdapter.this.mContext, RecyclerViewAdapter.this.mParks.getJSONObject(i).getString(str), 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    public int getItemCount() {
        StringBuilder sb = new StringBuilder();
        sb.append("!!!!!!:Parks count:");
        sb.append(this.mParks.length());
        Log.d(TAG, sb.toString());
        return this.mParks.length();
    }
}

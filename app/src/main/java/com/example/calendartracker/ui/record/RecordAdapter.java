package com.example.calendartracker.ui.record;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartracker.R;
import com.example.calendartracker.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<Record> recordList = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Record record);
    }

    public RecordAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordAdapter.RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(recordList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecordList(List<Record> records) {
        this.recordList = records;
        notifyDataSetChanged();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView dDayTextView;
        private TextView gregorianTextView;
        private TextView easternLunarTextView;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.record_name);
            dDayTextView = itemView.findViewById(R.id.record_d_day);
            gregorianTextView = itemView.findViewById(R.id.record_gr_date);
            easternLunarTextView = itemView.findViewById(R.id.record_el_date);
        }

        public void bind(final Record record, final OnItemClickListener listener) {
            nameTextView.setText(record.getName());
            dDayTextView.setText(record.getName());
            gregorianTextView.setText(record.getName());
            easternLunarTextView.setText(record.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(record);
                }
            });
        }
    }
}


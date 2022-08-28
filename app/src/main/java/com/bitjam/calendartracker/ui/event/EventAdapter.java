package com.bitjam.calendartracker.ui.event;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitjam.calendartracker.R;
import com.bitjam.calendartracker.model.Event;
import com.bitjam.calendartracker.model.Record;
import com.bitjam.calendartracker.utility.EventConverter;
import com.bitjam.calendartracker.utility.LunarSolarConverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Record> recordList = new ArrayList<>();

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Record record = recordList.get(position);

        Event upcomingEvent = EventConverter.upcomingEvent(record.getCalendarid());
        Event originalEvent = EventConverter.originalEvent(record.getCalendarid());

        LocalDate recordInstant = EventConverter.toInstant(originalEvent);
        LocalDate eventInstant = EventConverter.toInstant(upcomingEvent);
        LocalDate todayInstant = LocalDate.now(ZoneId.of(ZoneId.systemDefault().toString()));

        int recurrence = (int) ChronoUnit.YEARS.between(recordInstant, eventInstant);
        int dDay = (int) ChronoUnit.DAYS.between(eventInstant, todayInstant);

        holder.nameTextView.setText(record.getName());
        holder.eventNumberTextView.setText(String.valueOf(recurrence));
        holder.dDayTextView.setText(String.valueOf(dDay));
        holder.gregorianTextView.setText(upcomingEvent.toString().replace(',','/'));
        holder.easternLunarTextView.setText(LunarSolarConverter.lunarToString(
                EventConverter.toLunar(upcomingEvent)));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public Record getItem(int pos) {
        return recordList.get(pos);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecordList(List<Record> records) {
        this.recordList = records;
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView eventNumberTextView;
        private TextView dDayTextView;
        private TextView gregorianTextView;
        private TextView easternLunarTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name);
            eventNumberTextView = itemView.findViewById(R.id.event_number_of_reoccurrence);
            dDayTextView = itemView.findViewById(R.id.event_d_day);
            gregorianTextView = itemView.findViewById(R.id.event_gr_date);
            easternLunarTextView = itemView.findViewById(R.id.event_el_date);
        }
    }
}

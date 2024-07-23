package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.OnIntermittentFastingItemClick;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.IntermittentPlanResponse.Setting_data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class IntermittentFastingAdapter extends RecyclerView.Adapter<IntermittentFastingAdapter.VH> {

    private final DockActivity myDockActivity;
    private List<Setting_data.Plan_data> dayData;
    //    private List<Setting_data.Plan_data> setData;
    private final Context context;
    private static final RequestOptions requestOptions = new RequestOptions();
    private final OnIntermittentFastingItemClick mClickListener;
    private TimePicker timePicker;
//    private Boolean checkboxChecked = false;
    private int secsInADay = 86400;


    public IntermittentFastingAdapter(DockActivity myDockActivity, List<Setting_data.Plan_data> durationList, Context context, OnIntermittentFastingItemClick mClickListener) {
        this.myDockActivity = myDockActivity;
        this.dayData = durationList;
        this.context = context;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity).inflate(R.layout.item_intermittent_fasting, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
        Setting_data.Plan_data item = dayData.get(position);

        ArrayAdapter myAdap = (ArrayAdapter) holder.sp_hour.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(item.getIntermittent_hrs().toString());
        if (spinnerPosition == -1) spinnerPosition = 0;

        if (item.getActive()) {
            holder.checkBox.setChecked(true);
//            checkboxChecked = true;
        }
        holder.day.setText(item.getPlan_day());
        holder.sp_hour.setSelection(spinnerPosition);
        holder.startTime.setText(String.valueOf(UIHelper.convert24HourTo12Hour(item.getStart_time())));
        holder.endDate.setText(String.valueOf(UIHelper.convert24HourTo12Hour(item.getEnd_time())));

        updateCheckboxPermissions(holder, holder.checkBox.isChecked());
        holder.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.sp_hour.isEnabled()) {
                    onCreateDialog(holder, position);
                }
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

//                checkboxChecked = isChecked;
                mClickListener.onItemClick(position, holder.checkBox.isChecked(),
                        Integer.parseInt(holder.sp_hour.getSelectedItem().toString()), UIHelper.convert12HourTo24Hour(holder.startTime.getText().toString()),
                        UIHelper.convert12HourTo24Hour(holder.endDate.getText().toString()));
                updateCheckboxPermissions(holder, isChecked);
            }
        });

        holder.sp_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                String endtime = updateEndTime(holder.sp_hour.getSelectedItem().toString(), holder.startTime.getText().toString());
//                if (checkIfTimeWithinDay(holder.startDate.getText().toString(),endtime)) {
                    holder.endDate.setText(endtime);

                    mClickListener.onItemClick(position, holder.checkBox.isChecked(),
                            Integer.parseInt(holder.sp_hour.getSelectedItem().toString()), UIHelper.convert12HourTo24Hour(holder.startTime.getText().toString()),
                            UIHelper.convert12HourTo24Hour(holder.endDate.getText().toString()));
//                } else {
//                    Toast.makeText(context, "Please change the starting time or fasting hours! Current time not acceptable", Toast.LENGTH_SHORT).show();
//                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

//    private Boolean checkIfTimeWithinDay(String startTime, String endTime) {
//
//        int endTimeInSecs = convert12HourTimeToSeconds(endTime);
//        if (endTimeInSecs < 43200) { endTimeInSecs += secsInADay; }
//        int startTimeInSecs = convert12HourTimeToSeconds(startTime);
//        endTimeInSecs = Math.abs(endTimeInSecs - startTimeInSecs);
//        int compareTime = secsInADay;
//        compareTime = Math.abs(compareTime - startTimeInSecs);
//
//
//        if (endTimeInSecs <= compareTime) {
//            System.out.println("true");
//            return true;
//        } else {
//            System.out.println("false");
//            return false;
//        }
//    }

    private void updateCheckboxPermissions(VH holder, Boolean isChecked) {
        if (isChecked) {
            holder.sp_hour.setEnabled(true);
        } else {
            holder.sp_hour.setEnabled(false);
        }
    }

    private String updateEndTime(String hours, String time) {

        String updatedEndTime = null;
        int timeInSecs = convert12HourTimeToSeconds(time);
        int hoursInSecs = Integer.parseInt(hours) * 60 * 60;
        int newEndTimeInSecs = (timeInSecs + hoursInSecs) - secsInADay; //86400 are seconds in a day

        try {
            String _24HourTime = LocalTime.MIN.plusSeconds(newEndTimeInSecs).toString();
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            updatedEndTime = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedEndTime;
    }

    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<Setting_data.Plan_data> durationList) {
        this.dayData = durationList;
//        this.setData = durationList;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView day;
        Spinner sp_hour;
        TextView startTime;
        TextView endDate;
        CheckBox checkBox;

        public VH(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day_tv);
            sp_hour = itemView.findViewById(R.id.spinner_hour);
            startTime = itemView.findViewById(R.id.start_date_tv);
            endDate = itemView.findViewById(R.id.end_date_tv);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public Dialog onCreateDialog(VH holder, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_time, null);

        timePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date myDate = sdf.parse(holder.startTime.getText().toString());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            timePicker.setHour(hours);
            timePicker.setMinute(minutes);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new AlertDialog.Builder(context)
                .setView(v)
                .setTitle("Choose your start time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = 0;
                        int minute = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                        } else {
                            hour = timePicker.getCurrentHour();
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            minute = timePicker.getMinute();
                        } else {
                            minute = timePicker.getCurrentMinute();
                        }
                        String time12hours = convertTimeTo12(hour, minute);

                        //holder.endDate.setText(convertTimeTo24(time12hours));
                        String endtime = updateEndTime(holder.sp_hour.getSelectedItem().toString(), time12hours);
//                        if (checkIfTimeWithinDay(time12hours, endtime)) {
                            holder.startTime.setText(time12hours);
                            holder.endDate.setText(endtime);

                            mClickListener.onItemClick(position, holder.checkBox.isChecked(),
                                    Integer.parseInt(holder.sp_hour.getSelectedItem().toString()),  UIHelper.convert12HourTo24Hour(holder.startTime.getText().toString()),
                                    UIHelper.convert12HourTo24Hour(holder.endDate.getText().toString()));
//                        } else {
//                            Toast.makeText(context, "Please change the starting time or fasting hours! Current time not acceptable", Toast.LENGTH_SHORT).show();
//                        }
//                        holder.endDate.setText(updateEndTime(holder.sp_hour.getSelectedItem().toString(), time12hours));
//                        mClickListener.onItemClick(position, holder.checkBox.isChecked(),
//                                Integer.parseInt(holder.sp_hour.getSelectedItem().toString()), holder.startDate.getText().toString(),
//                                holder.endDate.getText().toString());
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private int convert12HourTimeToSeconds(String time) {
        int totalSeconds = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date myDate = sdf.parse(time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            int hourToSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60;
            int minutesToSeconds = calendar.get(Calendar.MINUTE) * 60;

            totalSeconds = hourToSeconds + minutesToSeconds; //+ seconds;

        } catch (Exception e) {
        }
        return totalSeconds;
    }

    private String convertTimeTo12(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String minutes = "";
        if (mins < 10) {
            minutes = "0" + mins;
        } else {
            minutes = String.valueOf(mins);
        }

        return new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
    }
}


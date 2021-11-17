package com.example.roomdatabase.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roomdatabase.MainActivity;
import com.example.roomdatabase.R;
import com.example.roomdatabase.Room.ITasksDao;
import com.example.roomdatabase.Room.Tasks;
import com.example.roomdatabase.Room.TasksDatabase;
import com.example.roomdatabase.databinding.FragmentInsertBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class InsertBottomSheetFragment extends BottomSheetDialogFragment {

    Long dateTime = System.currentTimeMillis();
    SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a");
    String timeString = sdfTime.format(dateTime);

    int hour, minute;

    DatePickerDialog  datePickerDialog;
    Button buttonDatePicker;
    Button buttonTime;
    ITasksDao tasksDao;

    FragmentInsertBottomSheetBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insert_bottom_sheet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasksDao = TasksDatabase.getInstance(requireContext()).tasksDao();

        binding.bsTvTime.setText(timeString);
        binding.bsTvDate.setText(getTodayDate());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Task Time")
                .build();

        binding.btnInsertDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        datePicker.getSelection();
                        binding.bsTvDate.setText(datePicker.getHeaderText());
                    }
                });
            }
        });

        binding.btnInsertTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(requireActivity().getSupportFragmentManager(), "timePicker");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getHour();
                        int minutes = timePicker.getMinute();
                        if(hour>12){
                            if (minutes < 10){
                                binding.bsTvTime.setText((hour-12)+ ":0" + timePicker.getMinute()+ " Pm");
                            }else {
                                binding.bsTvTime.setText((hour-12)+ ":" + timePicker.getMinute()+ " Pm");
                            }
                        }
                        if(hour==12){
                            if (minutes < 10){
                                binding.bsTvTime.setText((hour)+ ":0" + timePicker.getMinute()+ " Pm");
                            }else {
                                binding.bsTvTime.setText((hour)+ ":" + timePicker.getMinute()+ " Pm");
                            }
                        }
                        if((hour>0) && hour<12){
                            if (minutes < 10){
                                binding.bsTvTime.setText((hour)+ ":0" + timePicker.getMinute()+ " Am");
                            }else {
                                binding.bsTvTime.setText((hour)+ ":" + timePicker.getMinute()+ " Am");
                            }
                        }
                        if(hour==0){
                            if (minutes < 10){
                                binding.bsTvTime.setText((12)+ ":0" + timePicker.getMinute()+ " Am");
                            }else {
                                binding.bsTvTime.setText((12)+ ":" + timePicker.getMinute()+ " Am");
                            }
                        }
                    }
                });

            }
        });

        binding.btnInsertTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = binding.tieTaskTitle.getText().toString();
                String taskDate = binding.bsTvDate.getText().toString();
                String taskTime = binding.bsTvTime.getText().toString();
                if(taskTitle.isEmpty()){
                    binding.tilTitleLayout.setError("Task Title is required");
                    return;
                }
                Tasks task1 = new Tasks(taskTitle, taskDate, taskTime);
                tasksDao.insertTask(task1);

                Intent intent  = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                String date = makeDateString(day, month, year);
                buttonDatePicker.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month)+ " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }
}
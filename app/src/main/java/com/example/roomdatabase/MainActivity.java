package com.example.roomdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.roomdatabase.Fragments.ActiveFragment;
import com.example.roomdatabase.Fragments.ArchiveFragment;
import com.example.roomdatabase.Fragments.DoneFragment;
import com.example.roomdatabase.Fragments.InsertBottomSheetFragment;
import com.example.roomdatabase.Room.ITasksDao;
import com.example.roomdatabase.Room.Tasks;
import com.example.roomdatabase.Room.TasksDatabase;
import com.example.roomdatabase.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ITasksDao tasksDao;
//    BottomNavigationView bottomNavigationView;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        tasksDao = TasksDatabase.getInstance(this).tasksDao();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout, new ActiveFragment())
                .commit();

        binding.mainBn.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                binding.mainBn.getMenu().findItem(item.getItemId()).setChecked(true);
                Fragment fragment = null;
                if(item.getItemId() == R.id.item_active){
                    fragment = new ActiveFragment();
                }
                else if(item.getItemId() == R.id.item_done){
                    fragment = new DoneFragment();
                }
                else if(item.getItemId() == R.id.item_archive) {
                    fragment = new ArchiveFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .commit();
                return false;
            }
        });

    }//==============================================================================================

    public void addTasks(View view) {
        new InsertBottomSheetFragment().show(getSupportFragmentManager(), "insertBottomSheet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optional_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuItem_logOut){
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId() == R.id.menuItem_profile){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @BindingAdapter("glide")
    public static void setImageUrl(ImageView imageview, String imageUrl){
        Glide.with(imageview).load(imageUrl).into(imageview);
        Log.i(TAG, "setImageUrl: success");
    }
}
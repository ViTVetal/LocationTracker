package com.gocodes.locationtracker.ui.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.services.LocationService;
import com.gocodes.locationtracker.utils.GlobalVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etAssetId;
    private SeekBar seekBar;
    private TextView tvFrequency;
    private CheckBox cbUpdateOnMove, cbUpdateHistory, cbRealTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setText(GlobalVariables.getEmail(this));
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setText(GlobalVariables.getPassword(this));
        etAssetId = (EditText) findViewById(R.id.etAssetId);
        etAssetId.setText(GlobalVariables.getAssertId(this));

        cbUpdateOnMove = (CheckBox) findViewById(R.id.cbUpdateOnMove);
        cbUpdateOnMove.setChecked(GlobalVariables.isUpdateOnMove(this));
        cbUpdateHistory = (CheckBox) findViewById(R.id.cbUpdateHistory);
        cbUpdateHistory.setChecked(GlobalVariables.isUpdateHistory(this));
        cbRealTime = (CheckBox) findViewById(R.id.cbRealTime);
        cbRealTime.setChecked(GlobalVariables.isRealTimeUpdate(this));

        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvFrequency.setText(GlobalVariables.FREQUENCIES[GlobalVariables.getFrequencyIndex(this)]);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(GlobalVariables.getFrequencyIndex(this));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvFrequency.setText(GlobalVariables.FREQUENCIES[i]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onClickUpdate(View v) {
        boolean validationError = false;
        if(isValidEmail(etEmail.getText()))
            GlobalVariables.setEmail(etEmail.getText().toString(), this);
        else {
            etEmail.setError(getResources().getString(R.string.incorrect_email));
            Toast toast = Toast.makeText(this, getResources().getString(R.string.incorrect_email), Toast.LENGTH_SHORT);
            toast.show();
            validationError = true;
        }

        if(TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError(getResources().getString(R.string.empty_password));
            Toast toast = Toast.makeText(this, getResources().getString(R.string.empty_password), Toast.LENGTH_SHORT);
            toast.show();
            validationError = true;
        } else
            GlobalVariables.setPassword(etPassword.getText().toString(), this);

        if(TextUtils.isEmpty(etAssetId.getText())) {
            etAssetId.setError(getResources().getString(R.string.empty_assert_id));
            Toast toast = Toast.makeText(this, getResources().getString(R.string.empty_assert_id), Toast.LENGTH_SHORT);
            toast.show();
            validationError = true;
        } else
            GlobalVariables.setAssertID(etAssetId.getText().toString(), this);

        if(validationError)
            return;

        GlobalVariables.setFrequencyIndex(seekBar.getProgress(), this);

        GlobalVariables.setUpdateOnMove(cbUpdateOnMove.isChecked(), this);
        GlobalVariables.setUpdateHistory(cbUpdateHistory.isChecked(), this);
        GlobalVariables.setRealTimeUpdate(cbRealTime.isChecked(), this);

        Intent intent = new Intent(this, LocationService.class);

        if(isServiceRunning(LocationService.class)) {
            stopService(intent);
            startService(intent);
        }

        Toast toast = Toast.makeText(this, getResources().getString(R.string.updated_successfully), Toast.LENGTH_LONG);
        toast.show();
    }

    private final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
package com.example.postcutter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SUGGESTION_SWITCH = "suggestionSwitch";
    public static final Boolean SWITCH_DEFAULT = true;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Switch suggestionSwitch = findViewById(R.id.settings_switch);
        suggestionSwitch.setChecked(sharedPreferences.getBoolean(SUGGESTION_SWITCH, SWITCH_DEFAULT));
        suggestionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isChecked){
                    editor.putBoolean(SUGGESTION_SWITCH, true);
                }else{
                    editor.putBoolean(SUGGESTION_SWITCH, false);
                }
                editor.apply();
            }
        });
    }
}
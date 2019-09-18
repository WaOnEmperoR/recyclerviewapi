package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import id.govca.recyclerviewapi.alarm.AlarmReceiver;

public class SetAlarmActivity extends AppCompatActivity {

    private Switch switch_daily_reminder;
    private Switch switch_release_today;
    private final String TAG = this.getClass().getSimpleName();
    private AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        switch_daily_reminder = findViewById(R.id.switch_daily);
        switch_release_today = findViewById(R.id.switch_release_today);

        if (!alarmReceiver.isAlarmSet(this, 101))
        {
            switch_release_today.setChecked(false);
        }
        else
        {
            switch_release_today.setChecked(true);
        }

        if (!alarmReceiver.isAlarmSet(this, 100))
        {
            switch_daily_reminder.setChecked(false);
        }
        else
        {
            switch_daily_reminder.setChecked(true);
        }

        switch_daily_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    alarmReceiver.setRepeatingReminderAlarm(getApplicationContext(), AlarmReceiver.TYPE_REMINDER, "07:00", "Bukalah Aplikasi Movie Anda", 100);
                    DynamicToast.make(getApplicationContext(), "Daily Reminder set up").show();
                }
                else
                {
                    alarmReceiver.cancelAlarm(getApplicationContext(), 100);
                    DynamicToast.make(getApplicationContext(), "Daily Reminder cancelled").show();
                }
            }
        });

        switch_release_today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    alarmReceiver.setRepeatingReminderAlarm(getApplicationContext(), AlarmReceiver.TYPE_RELEASE_TODAY, "08:00", "B", 101);
                    DynamicToast.make(getApplicationContext(), "Today Release Reminder set up").show();
                }
                else
                {
                    alarmReceiver.cancelAlarm(getApplicationContext(), 101);
                    DynamicToast.make(getApplicationContext(), "Today Release Reminder cancelled").show();
                }
            }
        });
    }
}

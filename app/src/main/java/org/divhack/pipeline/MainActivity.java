package org.divhack.pipeline;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    private int totalDaysMissed;
    private EditText missed;
    private Button yesButton;
    private TextView resultText;
    private String reminder = "Daily reminder to check in! \n -Pipeline";

    public void buttonOnClick(View view){

        Button button = (Button) view;

        missed = (EditText)findViewById(R.id.days_missed);

        totalDaysMissed += Integer.parseInt(missed.toString());

        Toast.makeText(this, totalDaysMissed, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        missed = (EditText)findViewById(R.id.days_missed);
        yesButton = (Button)findViewById(R.id.yes_button);
        resultText = (TextView)findViewById(R.id.result_text);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                int currentDaysMissed = Integer.parseInt(missed.getText().toString());
//                if(currentDaysMissed < 0 || currentDaysMissed > 5) {
//                    int messageResId = R.string.notInRange;
//                    Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
//                } else {
//                    updateDays(currentDaysMissed);
//                }

                sendNotification();
                createScheduledNotification(1);
                updateDays(1);

            }
        });

    }

    private void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(reminder);
        builder.setSmallIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(8, notification);
    }

    private void updateDays(int currentDaysMissed) {
        totalDaysMissed += currentDaysMissed;
        resultText.setText("The total days missed is " + totalDaysMissed );
    }

    private void createScheduledNotification(int days)
    {
        // Get new calendar object and set the date to now
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Add defined amount of days to the date
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        // Retrieve alarm manager from the system
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);
        // Every scheduled intent needs a different ID, else it is just executed once
        int id = (int) System.currentTimeMillis();

        // Prepare the intent which should be launched at the date
        Intent intent = new Intent(this, TimeAlarm.class);

        // Prepare the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TimeAlarm extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent paramIntent) {
            // Request the notification manager
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Create a new intent which will be fired if you click on the notification
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("http://www.papers.ch"));
            // Attach the intent to a pending intent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Create the notification
            Notification notification = new Notification(R.drawable.abc_tab_indicator_material, "Visit our homepage", System.currentTimeMillis());
            notification.setLatestEventInfo(context, "Visit our homepage", "http://www.papers.ch",pendingIntent);
          // Fire the notification
            notificationManager.notify(1, notification);
        }
    }


}

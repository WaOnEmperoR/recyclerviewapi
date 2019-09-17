package id.govca.recyclerviewapi.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;

import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AlarmReceiver extends BroadcastReceiver {

    private final int ID_REMINDER = 100;
    private final int ID_RELEASE_TODAY = 101;

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public static final String TYPE_REMINDER = "ReminderAlarm";
    public static final String TYPE_RELEASE_TODAY = "ReleaseTodayAlarm";
    private CompositeDisposable disposable = new CompositeDisposable();

    private MovieList movieList = new MovieList();

    private final String TAG = this.getClass().getSimpleName();

    public AlarmReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        String title = type.equalsIgnoreCase(TYPE_REMINDER) ? TYPE_REMINDER : TYPE_RELEASE_TODAY;
        int notifId = type.equalsIgnoreCase(TYPE_REMINDER) ? ID_REMINDER : ID_RELEASE_TODAY;

        Log.d(TAG, "onReceive from type : " + title);

        if (notifId == ID_RELEASE_TODAY){
            ObserveTodayMovies(context, title, notifId);
        }
        else {
            showAlarmNotification(context, title, message, notifId);
        }

    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_clock)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setRepeatingReminderAlarm(Context context, String type, String time, String message) {
        Log.d(TAG, "Set Repeating : " + type);
        if (isDateInvalid(time, TIME_FORMAT)) {
            return;
        }
        else
        {
            Log.d(TAG, "Time Correct");
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d(TAG, "Not null");
        }
        Toast.makeText(context, "Repeating reminder alarm set up", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Repeating reminder alarm set up");
    }

    private String DATE_FORMAT = "yyyy-MM-dd";
    private String TIME_FORMAT = "HH:mm";

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            Log.e(TAG, "Haha " + e.getMessage());
            return true;
        }
    }

    private Observable<MovieList> getTodayMovieListObs(){

        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        Log.d(TAG, strDate);

        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxGetMoviesToday(Constants.API_KEY, strDate, strDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveTodayMovies(final Context context, final String title, final int notifId){
        Observable<MovieList> movieListObservable = getTodayMovieListObs();

        disposable.add(
          movieListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MovieList>() {
                    @Override
                    public void onNext(MovieList movieListObs) {
                        movieList.setMovieArrayList(movieListObs.getMovieArrayList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete from RXJava");

                        StringJoiner joiner = new StringJoiner(", ");
                        for (int i=0; i<movieList.getMovieArrayList().size(); i++)
                        {
                            joiner.add(movieList.getMovieArrayList().get(i).getTitle());
                        }
                        String fullJoin = joiner.toString();
                        Log.d(TAG, fullJoin);

                        showAlarmNotification(context, title, fullJoin, notifId);
                    }
                })
        );
    }


}

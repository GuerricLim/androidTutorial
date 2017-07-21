package com.tutorialapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;

import com.example.guerriclim.tutorialapp.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.content.ContentValues;
import android.content.CursorLoader;

import android.database.Cursor;

import android.view.Menu;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button decreaseFont, increaseFont, sendNotification, btnShowLocation;
    AutoCompleteTextView autoComplete;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    // GPSTracker class
    GPSTracker gps;

    //Date Picker Example
    Button btnDatePicker;
    private static TextView dateView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button increasing/decreasing font sizes
        decreaseFont = (Button)findViewById(R.id.buttonDecreaseFontSize);
        increaseFont = (Button)findViewById(R.id.buttonIncreaseFontSize);

        decreaseFont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView txtView = (TextView) findViewById(R.id.sampleTextFontSize);
                float sizeVar = txtView.getTextSize() - 10;
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX,sizeVar);
            }
        });
        decreaseFont.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "On Long Click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        increaseFont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView txtView = (TextView) findViewById(R.id.sampleTextFontSize);
                float sizeVar = txtView.getTextSize() + 10;
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeVar);
            }
        });

        //Spinner Example
        Spinner fontSizeSpinner = (Spinner) findViewById(R.id.spinnerFontSize);

        // Spinner click listener
        fontSizeSpinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Please select font size.");
        categories.add("Small");
        categories.add("Medium");
        categories.add("Large");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            public boolean areAllItemsEnabled() {
                return false;
            }

        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fontSizeSpinner.setAdapter(dataAdapter);

        //AutoComplete Example

        String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
                "Padua,Italy", "Pasadena,CA,United States"};

        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        autoComplete.setThreshold(2);
        autoComplete.setAdapter(adapter);

        //Date Picker Example
        dateView = (TextView) findViewById(R.id.textSelectedDate);
        btnDatePicker = (Button) findViewById(R.id.buttonSetDate);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        //Notification Example
        sendNotification = (Button)findViewById(R.id.buttonSendNotification);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification();
            }
        });

        PackageManager packageManager = getPackageManager();
        //Location
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != packageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = (Button) findViewById(R.id.buttonGetLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

        //Send Email

        Button btnSendEmail = (Button) findViewById(R.id.buttonSendEmail);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });

        //Send SMS

        Button btnSendSMS = (Button) findViewById(R.id.buttonSendSMS);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                sendSMS();
            }
        });

    }
    protected void sendSMS() {
        Log.i("Send SMS", "");
        Uri uri = Uri.parse("smsto:0800000123");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "The SMS text");



        try {
            startActivity(it);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.my_nine_patch)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(createID(), builder.build());
    }
    protected void displayNotification() {
        Log.i("Start", "notification");
        int numMessages = 0;
   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.mipmap.my_nine_patch);

   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(createID(), mBuilder.build());
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            /*dialog.getDatePicker().setMaxDate(c.getTimeInMillis());*/
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateView.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
    }


  /*  private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        TextView txtView = (TextView) findViewById(R.id.sampleTextFontSize);

        if (item == "Small")
        {
            txtView.setTextSize(15);
        }
        else if (item == "Medium")
        {
            txtView.setTextSize(30);
        }
        else if (item == "Large")
        {
            txtView.setTextSize(50);
        }
        // Showing selected spinner item
        /*Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void broadcastIntent(View view){
        Intent intent = new Intent();
        intent.setAction("com.tutorialapp.CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(StudentsProvider.NAME,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(StudentsProvider.GRADE,
                ((EditText)findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                StudentsProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickDeleteAll(View view) {
        String URL = "content://com.tutorialapp.StudentsProvider";

        Uri students = Uri.parse(URL);

        getContentResolver().delete(StudentsProvider.CONTENT_URI, null, null);

        Toast.makeText(this, "Deleted All Students", Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = "content://com.tutorialapp.StudentsProvider";

        Uri students = Uri.parse(URL);
        Cursor c = getContentResolver().query(students, null, null, null, "name");
        if (c.getCount() != 0)
        {
            if (c.moveToFirst()) {
                do{
                    Toast.makeText(this,
                            c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                    ", " +  c.getString(c.getColumnIndex( StudentsProvider.NAME)) +
                                    ", " + c.getString(c.getColumnIndex( StudentsProvider.GRADE)),
                            Toast.LENGTH_SHORT).show();
                } while (c.moveToNext());
            }
        }
        else
        {
            Toast.makeText(this, "No Content Yet", Toast.LENGTH_LONG).show();
        }

    }
}

package com.andreea.ewa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.os.Build;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.andreea.ewa.BradcastReceiver.DailyQuizAlarm;
import com.andreea.ewa.healthPage.Temperature;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * Created by andreeagb.
 */

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1234";
    //UI.
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView menu;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private Toolbar mToolbar;
    private Button edit;
    private ImageView profilePicture;

    // Firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int REQ_SIGNIN = 3;

    private static final List<Temperature> temperatures = new ArrayList<>();
    private static ConnectivityManager cm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Log.d("MAIN_ACTIVITY", "Created main activity");
        createNotificationChannel();
        registerAlarm();

        // setup authentication
        mAuth = FirebaseAuth.getInstance();

        /*
        * Listen to any changes in the state of the user.
        * The listener  will fire when it is attached, and then every time a user logs in, out or his token expires.
        * The triggered method returns the currently logged in user (or null).
        */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    AppState.get().setUserId(user.getUid());
                    attachDBListeners(user.getUid());
                } else {
                    // If user not signed in, start new activity.
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), REQ_SIGNIN);
                }
            }
        };

        menu = (NavigationView) findViewById(R.id.nMenu);
        edit = findViewById(R.id.editButton);
        drawerLayout = (DrawerLayout) findViewById(R.id.lDrawer);
        mToolbar = (Toolbar) findViewById(R.id.nav_bar);
        mToolbar.setTitleTextColor(getColor(R.color.primaryTextColor));
        setSupportActionBar(mToolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sign out action.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: reset main activity on sign out
                        // calling finish closes the app on a sign in after a sign out
                        // not calling finish() leaves the activity in the same state as when the sign out happened
                        //finish();
                        mAuth.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        edit.setVisibility(View.INVISIBLE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new HealthFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Health");

        // Menu switch.
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.logOut):
                        alert.show();
                        break;
                    case (R.id.account):
                        edit.setVisibility(View.VISIBLE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new AccountFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("My account");
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case (R.id.settings):
                        edit.setVisibility(View.INVISIBLE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new SettingsFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Settings");
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case (R.id.health):
                        edit.setVisibility(View.INVISIBLE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Fragment health = new HealthFragment();
                        fragmentTransaction.replace(R.id.main_container, health);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Health");
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case (R.id.appointments):
                        edit.setVisibility(View.INVISIBLE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new AppointmentsFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Appointments");
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case (R.id.graph):
                        edit.setVisibility(View.INVISIBLE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new GraphFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Charts");
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, 19);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.SECOND, 0);

        Intent newIntent = new Intent(MainActivity.this, DailyQuizAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void attachDBListeners(String uid) {
        // setup firebase database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);


        attachTemperatureListener(databaseReference, uid);
        userDataListener(databaseReference, uid);
    }

    public void userDataListener(DatabaseReference databaseReference, String uid) {

        databaseReference.child("Patients").child(uid).child("Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccount userData = dataSnapshot.getValue(UserAccount.class);
                Log.d("USER CHANGE", "onDataChange: User data changed");
                if (userData == null) {
                    // TODO(): maybe do something
                    return;
                }

                profilePicture = findViewById(R.id.profile_image);

                /*
                if( uri != null && !uri.isEmpty()) {
                    profilePicture.setImageURI(Uri.parse(uri));
                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void attachTemperatureListener(DatabaseReference databaseReference, String uid) {
        temperatures.clear();

        databaseReference.child("Patients").child(uid).child("Temperature").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Temperature update.
                try {
                    String date = dataSnapshot.getKey();
                    double value2;
                    if (dataSnapshot.getValue() instanceof Long)
                        value2 = ((Long) dataSnapshot.getValue()).doubleValue();
                    else
                        value2 = (double) dataSnapshot.getValue();
                    temperatures.add(new Temperature(date, value2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // Retreive result from Sigin activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_SIGNIN) {
            if (resultCode == RESULT_OK) {
                // get data from intent
                String user = data.getStringExtra("user");
            } else if (resultCode == RESULT_CANCELED) {
                // data was not retrieved
                finish();
            }
        }
    }

    public static List<Temperature> getTemperatures() {
        return temperatures;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Open menu item from toggle.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // Check for internet connectivity.
    public static boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        NetworkInfo[] netInfo;
        if (cm != null) {
            netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void mainClick(View view) {
        switch (view.getId()) {
            case R.id.editButton:
                Intent intent = new Intent(this, EditAccountActivity.class);
                this.startActivity(intent);
                break;
        }
    }

}
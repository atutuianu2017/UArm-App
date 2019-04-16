package com.example.uarmbt;

import android.app.Activity;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.lang.String;
import android.view.Gravity;
import android.widget.TextView;
import android.os.CountDownTimer;
import pl.droidsonroids.gif.GifImageView;




//library for bluetooth communication
import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;


public class step1Activity extends AppCompatActivity implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener {

    private TextView textViewTitle, textViewContent, textView;
    private BluetoothSerial bluetoothSerial;
    private Button button;
    private ImageView imageView;
    public Handler handler1;
    MyCountDownTimer myCountDownTimer;
    String data;


    private final static int REQUEST_ENABLE_BLUETOOTH = 1; // used to identify adding bluetooth names

    private MenuItem actionConnect, actionDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //animate transitions


        bluetoothSerial = new BluetoothSerial(this, this);
        imageView = findViewById(R.id.imageView_step1);
        textViewTitle = findViewById(R.id.textView_step1);
        textViewContent = findViewById(R.id.textView_relax);
        button = findViewById(R.id.button_step1);



    }


    @Override
    protected void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
        bluetoothSerial.setup();
        myCountDownTimer = new MyCountDownTimer(90000, 30000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        myCountDownTimer = new MyCountDownTimer(90000, 30000);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Connect your device with the UArm via Bluetooth before starting the calibration process!";
                int duration = Toast.LENGTH_LONG;

                if (bluetoothSerial.isConnected()){
                    //start calibration process on Arduino
                    data = "<1>";
                    bluetoothSerial.write(data);
                    myCountDownTimer.start();

                    //show progress bar
                    handler1 = new Handler(getMainLooper());
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        // Open a Bluetooth serial port and get ready to establish a connection
        if (bluetoothSerial.checkBluetooth() && bluetoothSerial.isBluetoothEnabled()) {
            if (!bluetoothSerial.isConnected()) {
                bluetoothSerial.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from the remote device and close the serial port
        bluetoothSerial.stop();
        myCountDownTimer.cancel();

    }

    @Override
    protected void onPause() {
        super.onPause();
        myCountDownTimer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        actionConnect = menu.findItem(R.id.action_connect);
        actionDisconnect = menu.findItem(R.id.action_disconnect);

        // return true so that the menu pop up is opened
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connect:
                // User chose the "Connect" item
                showDeviceListDialog();
                return true;

            case R.id.action_disconnect:
                bluetoothSerial.stop();
                return true;

            case R.id.action_overflow:
                //onBluetoothSerialWrite(String.valueOf(2));

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void invalidateOptionsMenu() {
        if (bluetoothSerial == null)
            return;

        // Show or hide the "Connect" and "Disconnect" buttons on the app bar
        if (bluetoothSerial.isConnected()) {
            if (actionConnect != null)
                actionConnect.setVisible(false);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(true);
        } else {
            if (actionConnect != null)
                actionConnect.setVisible(true);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                // Set up Bluetooth serial port when Bluetooth adapter is turned on
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothSerial.setup();
                }
                break;
        }
    }

    public class MyCountDownTimer extends CountDownTimer {


        //animated gifs
        GifImageView gesture1=(GifImageView) findViewById(R.id.lowerarm);
        GifImageView gesture2=(GifImageView) findViewById(R.id.upperarm);
        GifImageView gesture3=(GifImageView) findViewById(R.id.bicepscali);

        //progress bars
        ProgressBar progressBar11=(ProgressBar) findViewById(R.id.progressBar1);
        ProgressBar progressBar2=(ProgressBar) findViewById(R.id.progressBar2);
        ProgressBar progressBar3=(ProgressBar) findViewById(R.id.progressBar3);

        //checkbook buttons
        CheckBox checkBox1=(CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2=(CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3=(CheckBox) findViewById(R.id.checkBox3);





        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {



            int progress = (int) (millisUntilFinished/30000);
            if(progress == 2) {
                textViewTitle.setTextColor(Color.parseColor("#FFAF021C"));
                gesture1.setVisibility(View.VISIBLE);
                progressBar11.setVisibility(View.VISIBLE);
                textViewTitle.setText(R.string.title_step1);
                textViewContent.setText(R.string.content_step1);
                checkBox1.setVisibility(View.VISIBLE);


            }
            else if (progress == 1) {
                data = "<2>";
                bluetoothSerial.write(data);
                checkBox1.setChecked(true);
                gesture1.setVisibility(View.GONE);
                progressBar11.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                gesture2.setVisibility(View.VISIBLE);
                textViewTitle.setTextColor(Color.parseColor("#FF25A22B"));
                textViewTitle.setText(R.string.title_step2);
                textViewContent.setText(R.string.content_step2);
            }
            else if (progress == 0) //maybe check if zero
            {
                //display an image of finalization
                data = "<3>";
                bluetoothSerial.write(data);
                gesture2.setVisibility(View.GONE);
                checkBox2.setChecked(true);
                gesture3.setVisibility(View.VISIBLE);
                progressBar2.setVisibility(View.GONE);
                progressBar3.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                textViewTitle.setTextColor(Color.parseColor("#FFFFCC33"));
                textViewTitle.setText(R.string.title_step3);
                textViewContent.setText(R.string.content_step3);
            }

        }

        @Override
        public void onFinish() {
            //finish();
            gesture3.setVisibility(View.GONE);
            progressBar3.setVisibility(View.GONE);
            checkBox3.setChecked(true);
            imageView.setImageResource(R.drawable.robotblue);
            textViewTitle.setTextColor(Color.parseColor("#03b9ff"));
            textViewTitle.setText(R.string.title_finished);
            textViewContent.setText(R.string.content_finished);
        }
    }

    private void updateBluetoothState() {
        // Get the current Bluetooth state
        final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;

        // Display the current state on the app bar as the subtitle
        String subtitle;
        switch (state) {
            case BluetoothSerial.STATE_CONNECTING:
                subtitle = getString(R.string.status_connecting);
                break;
            case BluetoothSerial.STATE_CONNECTED:
                subtitle = getString(R.string.status_connected, bluetoothSerial.getConnectedDeviceName());
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle(R.string.paired_devices);
        dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.showAddress(true);
        dialog.show();
    }

    /* Implementation of BluetoothSerialListener */

    @Override
    public void onBluetoothNotSupported() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_bluetooth)
                .setPositiveButton(R.string.action_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBluetoothDisabled() {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
        textView.append(message);


        //svTerminal.post(scrollTerminalToBottom);
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen
        //bluetoothSerial.write(message);
        //textView.append(getString(R.string.terminal_message_template,
        //bluetoothSerial.getLocalAdapterName(),
        //message));
        //svTerminal.post(scrollTerminalToBottom);
    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
    }

    /* End of the implementation of listeners */

}

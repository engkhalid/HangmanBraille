package com.ece558winter.khalidalkhulayfi.hangmanbraille;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Khalid Alkhulayfi on 3/5/2015.
 */

public class MainActivity extends ActionBarActivity {

    private Button mGame_BTN, mPractice_BTN,mTest_BTN;

    protected boolean aboutDialogShownFlag;
    protected static final String KEY_ABOUT_DIALOG_SHOWN = "About dialog shown";

    /**
     * This method will create all the necessary items for the app to start.
     *
     * @param savedInstanceState variable that holds the saved values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame_BTN =(Button) findViewById(R.id.game_BTN);
        mPractice_BTN =(Button) findViewById(R.id.practice_BTN);
        mTest_BTN = (Button) findViewById(R.id.start_test_BTN);

        mTest_BTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting TestActivity.
                Intent intent1 = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent1);
            }
        });
        mGame_BTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting GameActivity.
                Intent intent1 = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent1);
            }
        });

        mPractice_BTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting PracticeActivity.
                Intent intent2 = new Intent(MainActivity.this, PracticeActivity.class);
                startActivity(intent2);
            }
        });

        //Restoring what was saved when the app was destroyed.
        if ( savedInstanceState != null){
            aboutDialogShownFlag = savedInstanceState.getBoolean(KEY_ABOUT_DIALOG_SHOWN);

            //Show the dialog that was display before the app was destroyed.
            if(aboutDialogShownFlag) showAboutDialog();
        }
    }


    /**
     * This method will save all the needed variables if the app was destroyed.
     * @param savedInstanceState Used to hold all the needed variables.
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ABOUT_DIALOG_SHOWN, aboutDialogShownFlag );
    }

    /**
     * This method will inflate all the game menu.
     *
     * @param menu Menu that will be shown
     * @return True if the method handles the request.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method will show a dialog that has some information about the app.
     */
    protected void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.about)
                .setCancelable(false) //To prevent the user from using the back button.
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Used to save the state of the dialog in order to show the dialog again
                        //if the device is rotated.
                        aboutDialogShownFlag = false;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        aboutDialogShownFlag = true;
    }

    /**
     * This method will take the menu item object and attach it to the app options.
     *
     * @param item Element in the app options.
     * @return True if the method handles the request.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

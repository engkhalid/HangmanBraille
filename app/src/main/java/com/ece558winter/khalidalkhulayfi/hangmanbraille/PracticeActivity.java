package com.ece558winter.khalidalkhulayfi.hangmanbraille;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Khalid Alkhulayfi on 3/5/2015.
 */
public class PracticeActivity extends BaseActivity {

    private static TextView mLetter_TextView;

    /**
     * This method will be used when this activity is created and called by the super class. It
     * has all the specific configurations for this activity.
     *
     * @param savedInstanceState variable that holds the saved values.
     */
    @Override
    protected void onCreateCompletion(Bundle savedInstanceState) {
        mLetter_TextView = (TextView) findViewById(R.id.letter_textView);

        brailleCode = new boolean[6];

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
        //When the orientation of the device is changed the fragments will be removed to prevent
        //overlapping.
        if(findViewById(R.id.right_fragment_container) != null){
            getSupportFragmentManager().beginTransaction()
                    .remove(rightFragment)
                    .commit();
        }
        if(findViewById(R.id.left_fragment_container) != null){
            getSupportFragmentManager().beginTransaction()
                    .remove(leftFragment)
                    .commit();
        }

        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_ABOUT_DIALOG_SHOWN, aboutDialogShownFlag );
    }

    /**
     * This method will return the view ID of the activity to used by the super class.
     *
     * @return view ID of the activity.
     */
    @Override
    protected int getViewID() {
        return R.layout.activity_practice;
    }

    /**
     * This method will be used by the super class to tell it's subclass that Braille code is
     * changed and saved, and you can use it. Then it will create only one waiting thread.
     */
    @Override
    protected void useBrailleCode() {
        mLetter_TextView.setText(brailleToLetter(brailleCode));
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
        getMenuInflater().inflate(R.menu.menu_practice, menu);
        return true;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about){
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

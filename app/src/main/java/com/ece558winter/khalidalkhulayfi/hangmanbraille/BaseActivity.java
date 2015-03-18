package com.ece558winter.khalidalkhulayfi.hangmanbraille;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;

/**
 * Created by Khalid Alkhulayfi on 3/10/2015.
 */
public abstract class BaseActivity extends ActionBarActivity implements ThreeButtonsListener {

    protected boolean[] brailleCode;
    protected ThreeButtonsFragment rightFragment, leftFragment;
    protected ImageView mDot1,mDot2,mDot3,mDot4,mDot5,mDot6;
    protected boolean aboutDialogShownFlag, accessibilityEnabled, paused;

    protected static final String KEY_ABOUT_DIALOG_SHOWN = "About dialog shown";

    /**
     * This method will create two fragments for to be used to enter the Braille code. In addition,
     * it will pass the  status of the accessibility option before creating those fragments.
     * Finally, it will call the onCreateCompletion method and pass the savedInstanceState bundle
     * to it, so the it can be used by the current activity.
     *
     * @param savedInstanceState variable that holds the saved values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewID());

        //Checking the status of the accessibility.
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        accessibilityEnabled = am.isEnabled();

        mDot1 = (ImageView) findViewById(R.id.dot1_imageView);
        mDot2 = (ImageView) findViewById(R.id.dot2_imageView);
        mDot3 = (ImageView) findViewById(R.id.dot3_imageView);
        mDot4 = (ImageView) findViewById(R.id.dot4_imageView);
        mDot5 = (ImageView) findViewById(R.id.dot5_imageView);
        mDot6 = (ImageView) findViewById(R.id.dot6_imageView);

        FragmentManager fm = getSupportFragmentManager();

        //Creating a Bundle that will have the status of the accessibility.
        Bundle information = new Bundle();
        information.putBoolean("Accessibility", accessibilityEnabled);

        //Creating the right and the left fragments and passing the information Bundle as an
        //argument.
        rightFragment = new ThreeButtonsFragment();
        rightFragment.setArguments(information);
        fm.beginTransaction()
                .add(R.id.right_fragment_container, rightFragment).commit();

        leftFragment = new ThreeButtonsFragment();
        leftFragment.setArguments(information);
        fm.beginTransaction()
                .add(R.id.left_fragment_container, leftFragment).commit();

        //Assigning an ID number for each fragment to know which one did the user touch or click
        //it's buttons.
        rightFragment.setFragmentID(0);
        leftFragment.setFragmentID(1);

        //Used to prevent the app from recreating the fragments in the resume state.
        paused = false;

        onCreateCompletion(savedInstanceState);
    }


    /**
     * This method will update the color of the image view that shows that the button is pressed.
     */
    protected void updateColor(){
        //The corresponding color of each code will change according to it's value.
        if(brailleCode[0]) {
            mDot1.setBackgroundColor(Color.BLUE);
        }else{
            mDot1.setBackgroundColor(Color.LTGRAY);
        }
        if(brailleCode[1]){
            mDot2.setBackgroundColor(Color.BLUE);
        }else{
            mDot2.setBackgroundColor(Color.LTGRAY);
        }
        if(brailleCode[2]){
            mDot3.setBackgroundColor(Color.BLUE);
        }else{
            mDot3.setBackgroundColor(Color.LTGRAY);
        }
        if(brailleCode[3]){
            mDot4.setBackgroundColor(Color.BLUE);
        }else{
            mDot4.setBackgroundColor(Color.LTGRAY);
        }
        if(brailleCode[4]){
            mDot5.setBackgroundColor(Color.BLUE);
        }else{
            mDot5.setBackgroundColor(Color.LTGRAY);
        }
        if(brailleCode[5]){
            mDot6.setBackgroundColor(Color.BLUE);
        }else{
            mDot6.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * This method will take a boolean array that has all the six braille code status, and return
     * the corresponding letter.
     *
     * @param brailleCode An array of boolean elements that has all the six braille code.
     * @return The letter that matches the status of the braille code.
     */
    protected String brailleToLetter(boolean[] brailleCode){
        String letter = "";

        //The String variable will be changed according to the state of each code element.
        if (brailleCode[0] & !brailleCode[1] & !brailleCode[2] &
                !brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "a";
        }else if (brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                !brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "b";
        }else if (brailleCode[0] & !brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "c";
        }else if (brailleCode[0] & !brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "d";
        }else if (brailleCode[0] & !brailleCode[1] & !brailleCode[2] &
                !brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "e";
        }else if (brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "f";
        }else if (brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "g";
        }else if (brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                !brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "h";
        }else if (!brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "i";
        }else if (!brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "j";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "k";
        }else if (brailleCode[0] & brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "l";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "m";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "n";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "o";
        }else if (brailleCode[0] & brailleCode[1] & brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "p";
        }else if (brailleCode[0] & brailleCode[1] & brailleCode[2] &
                brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "q";
        }else if (brailleCode[0] & brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "r";
        }else if (!brailleCode[0] & brailleCode[1] & brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & !brailleCode[5]){
            letter = "s";
        }else if (!brailleCode[0] & brailleCode[1] & brailleCode[2] &
                brailleCode[3] & brailleCode[4] & !brailleCode[5]){
            letter = "t";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & !brailleCode[4] & brailleCode[5]){
            letter = "u";
        }else if (brailleCode[0] & brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & !brailleCode[4] & brailleCode[5]){
            letter = "v";
        }else if (!brailleCode[0] & brailleCode[1] & !brailleCode[2] &
                brailleCode[3] & brailleCode[4] & brailleCode[5]){
            letter = "w";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                brailleCode[3] & !brailleCode[4] & brailleCode[5]){
            letter = "x";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                brailleCode[3] & brailleCode[4] & brailleCode[5]){
            letter = "y";
        }else if (brailleCode[0] & !brailleCode[1] & brailleCode[2] &
                !brailleCode[3] & brailleCode[4] & brailleCode[5]){
            letter = "z";
        }
        return letter;
    }

    /**
     * This method will be called by the fragments to update the braille code. It will take a
     * boolean array of a three elements that has the status of the three buttons. In addition,
     * it will take the fragment ID number that identify the left and the right fragments.
     *
     * @param changedButtonsState Boolean array of the three values that is changed in the fragment.
     * @param fragmentID The ID number of the fragment whose buttons was changed.
     */
    public void updateBrailleCode(boolean[] changedButtonsState, int fragmentID) {
        if ( fragmentID == 0){ //Change coming from the right fragment.
            brailleCode[3]= changedButtonsState[0];
            brailleCode[4]= changedButtonsState[1];
            brailleCode[5]= changedButtonsState[2];
        }else if ( fragmentID == 1 ){ //Change coming from the left fragment.
            brailleCode[0]= changedButtonsState[0];
            brailleCode[1]= changedButtonsState[1];
            brailleCode[2]= changedButtonsState[2];
        }
        updateColor();
        useBrailleCode();
    }

    /**
     * This method will show a dialog that has some information about the app.
     */
    protected void showAboutDialog(){
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
     * This method is raising a flag that shows that the app is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    /**
     * This method will reattach the right and the left fragments to their container after the app
     * is paused.
     */
    @Override
    protected void onResume() {
        super.onResume();

        //Reshowing the fragments if the app is paused.
        if ( rightFragment != null && paused){
            getSupportFragmentManager().beginTransaction().
                    add(R.id.right_fragment_container, rightFragment).commit();
        }
        if ( leftFragment != null && paused ){
            getSupportFragmentManager().beginTransaction().
                    add(R.id.left_fragment_container, leftFragment).commit();
        }
    }

    //These methods will be implemented differently in all the subclasses.
    /**
     * This method will be used when this class is done from creating all the fragments in the
     * onCreate method.
     *
     * @param savedInstanceState variable that holds the saved values.
     */
    protected abstract void onCreateCompletion(Bundle savedInstanceState);

    /**
     * This method will return the view ID of the activity to set the content view.
     *
     * @return view ID of the activity.
     */
    protected abstract int getViewID();

    /**
     * This method will be used by the super class to tell it's subclass that Braille code is
     * changed and saved, and you can use it.
     */
    protected abstract void useBrailleCode();
}

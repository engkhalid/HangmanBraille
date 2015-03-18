package com.ece558winter.khalidalkhulayfi.hangmanbraille;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Khalid Alkhulayfi on 3/5/2015.
 */
public class GameActivity extends BaseActivity {

    private static TextView mLetter_TextView, mWord_TextView, mUsedLetters_TextView;
    private ImageView mPictureIMV;
    private int[] picturesNums = { R.drawable.head, R.drawable.body, R.drawable.legs,
                                   R.drawable.hands, R.drawable.dead };
    private String[] pictureDescription = {"head is drown 4 wrong moves left",
                                           "body  is drown 3 wrong moves left",
                                           "legs are drown 2 wrong moves left",
                                           "hands are drown 1 wrong moves left",
                                           "the girl is dead"};
    private String[] mysteryWords = { "android", "java", "braille",
                                      "the five boxing wizards jump quickly"};

    private static final String KEY_CORRECT_LETTERS_ARRAY = "Current letters number";
    private static final String KEY_CURRENT_PICTURE_NUM = "Current picture number";
    private static final String KEY_MYSTERY_WORD_NUM = "Current mystery word number";
    private static final String KEY_NEXT_MYSTERY_WORD_Message = "next mystery word message";
    private static final String KEY_WAITING_FLAG = "Waiting flag";
    private static final String KEY_SLEEP_TIME = "Sleep time";
    private static final String KEY_CONFORMING_DIALOG_SHOWN = "Conforming dialog shown";
    private static final String KEY_NEXT_MWORD_DIALOG_SHOWN = "Next mystery word dialog shown";
    private static final String KEY_DELAY_DIALOG_SHOWN = "Delay dialog shown";
    private static final String KEY_LETTER_TEXT_VIEW = "Letter text view";
    private static final String KEY_USED_LETTER_TEXT_VIEW = "Used letters text view";
    private static final String KEY_MYSTERY_WORD_TEXT_VIEW = "Mystery word text view";

    private int currentPic;
    private boolean waitingFlag;
    private int currentMysteryWordNum;
    private String[] correctLetters;
    private int sleepTime;
    private String nextMysteryWordDialogMessage;
    private boolean conformingDialogShownFlag, delayDialogShownFlag, nextMysteryWordDialogFlag;

    /**
     * This method will be used when this activity is created and called by the super class. It
     * has all the specific configurations for this activity.
     *
     * @param savedInstanceState variable that holds the saved values.
     */
    @Override
    protected void onCreateCompletion(Bundle savedInstanceState) {
        mLetter_TextView = (TextView) findViewById(R.id.letter_textView);
        mWord_TextView = (TextView) findViewById(R.id.word_textView);
        mUsedLetters_TextView = (TextView) findViewById(R.id.UsedLetters_textView);
        mPictureIMV = (ImageView) findViewById(R.id.picture_imageView);


        brailleCode = new boolean[6];
        currentPic = 0;
        mPictureIMV.setContentDescription(pictureDescription[currentPic]);

        //Restoring what was saved when the app was destroyed.
        if ( savedInstanceState != null){
            correctLetters = savedInstanceState.getStringArray(KEY_CORRECT_LETTERS_ARRAY);

            currentPic = savedInstanceState.getInt(KEY_CURRENT_PICTURE_NUM);
            currentMysteryWordNum = savedInstanceState.getInt(KEY_MYSTERY_WORD_NUM);
            sleepTime = savedInstanceState.getInt(KEY_SLEEP_TIME);

            waitingFlag = savedInstanceState.getBoolean(KEY_WAITING_FLAG);
            conformingDialogShownFlag = savedInstanceState.getBoolean(KEY_CONFORMING_DIALOG_SHOWN);
            nextMysteryWordDialogFlag = savedInstanceState.getBoolean(KEY_NEXT_MWORD_DIALOG_SHOWN);
            delayDialogShownFlag = savedInstanceState.getBoolean(KEY_DELAY_DIALOG_SHOWN);
            aboutDialogShownFlag = savedInstanceState.getBoolean(KEY_ABOUT_DIALOG_SHOWN);


            mLetter_TextView.setText(savedInstanceState.getString(KEY_LETTER_TEXT_VIEW));
            mUsedLetters_TextView.setText(savedInstanceState.getString(KEY_USED_LETTER_TEXT_VIEW));
            mWord_TextView.setText(savedInstanceState.getString(KEY_MYSTERY_WORD_TEXT_VIEW));
            mPictureIMV.setImageResource(picturesNums[currentPic]);
            nextMysteryWordDialogMessage = savedInstanceState.getString(KEY_NEXT_MYSTERY_WORD_Message);

            //Show the dialog that was display before the app was destroyed.
            if(conformingDialogShownFlag) conformLetter();
            if(nextMysteryWordDialogFlag) nextMysteryWordDialog(nextMysteryWordDialogMessage);
            if(delayDialogShownFlag) changeDelayTime();
            if(aboutDialogShownFlag) showAboutDialog();

        }else {
            sleepTime = 500;
            currentMysteryWordNum = 0 ;
            startNewMysteryWord();
        }
    }

    /**
     * This method will return the view ID of the activity to used by the super class.
     *
     * @return view ID of the activity.
     */
    @Override
    protected int getViewID() {
        return R.layout.activity_game;
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
        savedInstanceState.putStringArray(KEY_CORRECT_LETTERS_ARRAY,correctLetters);

        savedInstanceState.putInt(KEY_CURRENT_PICTURE_NUM, currentPic);
        savedInstanceState.putInt(KEY_MYSTERY_WORD_NUM,currentMysteryWordNum );
        savedInstanceState.putInt(KEY_SLEEP_TIME, sleepTime);

        savedInstanceState.putBoolean(KEY_WAITING_FLAG, waitingFlag );
        savedInstanceState.putBoolean(KEY_CONFORMING_DIALOG_SHOWN, conformingDialogShownFlag );
        savedInstanceState.putBoolean(KEY_NEXT_MWORD_DIALOG_SHOWN, nextMysteryWordDialogFlag );
        savedInstanceState.putBoolean(KEY_DELAY_DIALOG_SHOWN, delayDialogShownFlag );
        savedInstanceState.putBoolean(KEY_ABOUT_DIALOG_SHOWN, aboutDialogShownFlag );

        savedInstanceState.putString(KEY_LETTER_TEXT_VIEW,
                mLetter_TextView.getText().toString());
        savedInstanceState.putString(KEY_USED_LETTER_TEXT_VIEW,
                mUsedLetters_TextView.getText().toString());
        savedInstanceState.putString(KEY_MYSTERY_WORD_TEXT_VIEW,
                mWord_TextView.getText().toString());
        savedInstanceState.putString(KEY_NEXT_MYSTERY_WORD_Message,
                nextMysteryWordDialogMessage);
    }

    /**
     * This method will show a dialog that has the passed message.
     * The dialog has only one OK button.
     *
     * @param message A message that will be displayed.
     */
    private void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * This method will take the method that should be shown to the user when they win or lose.
     * It provide the user with two option. They can continue to the next mystery word or quit the
     * app. On the other hand if the user chose to continue it will go to the next mystery word by
     * calling the prepareNextMysteryWord method.
     *
     * @param message The shown message when the user win or lose.
     */
    private void nextMysteryWordDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(message)
                .setCancelable(false) //To prevent the user from using the back button.
                .setPositiveButton("GO TO THE NEXT MYSTERY WORD?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (currentPic == (picturesNums.length - 1)) nextPicture();
                        //Used to save the state of the dialog in order to show the dialog again
                        //if the device is rotated.
                        nextMysteryWordDialogFlag = false ;
                        prepareNextMysteryWord();
                    }
                })
                .setNegativeButton("QUIT?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nextMysteryWordDialogFlag = false ;
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        //Saving the message in case the device rotated.
        nextMysteryWordDialogMessage = message;
        nextMysteryWordDialogFlag =true ;
    }

    /**
     * This method will increment the mystery word counter, clear the text view, and call the
     * startNewMysteryWord method.
     *
     */
    private void prepareNextMysteryWord(){
        currentMysteryWordNum = (currentMysteryWordNum + 1) % mysteryWords.length;
        mLetter_TextView.setText("");
        startNewMysteryWord();
    }

    /**
     * This method will create a new array that will hold all the correct entered letters, prepare
     * an empty mystery word and show it.
     */
    private void startNewMysteryWord() {
        correctLetters = new String[mysteryWords[currentMysteryWordNum].length()];
        mUsedLetters_TextView.setText("");
        addLetterToTheMysteryWord(" ");
        updateMysteryWord_TextView();
    }

    /**
     * This method will take the letter that the used entered and is correct and try to see where
     * it will fit tin the mystery word. Then it will save it in the array that holds all the
     * correct used letters.
     *
     * @param usedLetter The correct letter that the user entered.
     */
    private void addLetterToTheMysteryWord(String usedLetter){
        int usedLetterIndex = mysteryWords[currentMysteryWordNum].indexOf(usedLetter);
        //Check if there is more than one place where the sane letter was replicated.
        while (usedLetterIndex >= 0) {
            correctLetters[usedLetterIndex] = usedLetter;
            //Getting the index of the next place where the same letter is used.
            usedLetterIndex = mysteryWords[currentMysteryWordNum].indexOf(usedLetter, usedLetterIndex + 1);
        }
    }

    /**
     * This method will be used by the super class to tell it's subclass that Braille code is
     * changed and saved, and you can use it. Then it will create only one waiting thread.
     */
    @Override
    protected void useBrailleCode() {
        if ( !waitingFlag & !brailleToLetter(brailleCode).equals("")) {
            //To prevent other threads from being created.
            waitingFlag = true;
            new WaitingThread().execute();
        }
    }

    /**
     * This method will take whatever the user entered and show a message that asks the user if
     * he/she wants to use the entered letter. If the user chose to use the letter, it will
     * call the addNewLetter method and pass that letter to it. It will also check it the
     * user uses the accessibility option to clear all the entered code.
     */
    private void conformLetter(){
        if( !brailleToLetter(brailleCode).equals("") || conformingDialogShownFlag) {
            if(!conformingDialogShownFlag) mLetter_TextView.setText(brailleToLetter(brailleCode));
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setMessage("Are you sure you want to use the letter ( " +
                                mLetter_TextView.getText().toString() + " )?")
                    .setCancelable(false) //To prevent the user from using the back button.
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addNewLetter(mLetter_TextView.getText().toString());
                            //Clearing the braille code and if the user uses the accessibility.
                            if(accessibilityEnabled){
                                for (int i = 0 ; i< brailleCode.length; i++) brailleCode[i]=false;
                                rightFragment.clearButtonsState();
                                leftFragment.clearButtonsState();
                                updateColor();
                            }
                            //Allow the user to enter another letter.
                            waitingFlag = false;
                            //Used to save the state of the dialog in order to show the dialog again
                            //if the device is rotated.
                            conformingDialogShownFlag = false;
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            waitingFlag = false;
                            conformingDialogShownFlag = false;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            conformingDialogShownFlag = true;
        }else{
            waitingFlag = false;
        }
    }

    /**
     * This method will take the entered letter and check if it is one of the used letters.
     * If so, it will tell the user (Show Toast when the accessibility option is off and call
     * showDialog method when the accessibility is on) that the letter was used before. If not,
     * it will add it to the used letters and call checkLetter method.
     *
     * @param usedLetter The new entered letter.
     */
    private void addNewLetter(String usedLetter){
        if( !mUsedLetters_TextView.getText().toString().contains(usedLetter) ) {
            mUsedLetters_TextView.setText(mUsedLetters_TextView.getText().toString() + usedLetter + " ");
            checkLetter(usedLetter);
        }else {
            if (accessibilityEnabled){
                showDialog("You used this letter before.");
            }else {
                Toast.makeText(getApplicationContext(),
                        "You used this letter before.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method will check if the letter is one of the mystery word letters. If so, it will
     * call the addLetterToTheMysteryWord and pass that letter to it. Also it will show a message
     * to the user that the entered letter is correct (Show Toast when the accessibility option
     * is off and call showDialog method when the accessibility is on) , and will call.
     * updateMysteryWord_TextView method.
     *
     * @param usedLetter The new entered letter.
     */
    private void checkLetter(String usedLetter){
        if( mysteryWords[currentMysteryWordNum].contains(usedLetter) ){
            addLetterToTheMysteryWord(usedLetter);
            if (accessibilityEnabled){
                showDialog("Great, (" + mLetter_TextView.getText().toString() +
                        ") letter is added to the mystery word.");
            }else {
                Toast.makeText(GameActivity.this, "Great, (" +
                        mLetter_TextView.getText().toString() + ") letter is added to" +
                        " the mystery word.", Toast.LENGTH_SHORT).show();
            }
            updateMysteryWord_TextView();
        }else{
            //If the picture changed to the picture before the last picture then the user lost.
            if( currentPic == ( picturesNums.length - 2 ) ){
                nextPicture();
                nextMysteryWordDialog("Sorry, you failed to save the girls life.");
            }else {
                nextPicture();
                if (accessibilityEnabled){
                    showDialog("Wrong letter. The picture is changed.");
                }else {
                    Toast.makeText(GameActivity.this, "Wrong letter. The picture is changed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * This method will check the elements of the correct letters array to display the mystery
     * word. It also check it the user is done from figuring all the letters of the mystery word
     * to show that he/she won the game.
     */
    private void updateMysteryWord_TextView(){
        String shownMysteryWord = "";

        //Show all the correct used letters in the mystery word.
        for ( int i=0; i < correctLetters.length ; i++ ){
            if( correctLetters[i] != null){
                shownMysteryWord += correctLetters[i] + " ";
            }else{
                shownMysteryWord += "_ ";
            }
        }
        mWord_TextView.setText(shownMysteryWord);

        //If all the letters are correct then the user win.
        boolean done = true;
        for ( int i=0; i < correctLetters.length ; i++ ){
            if( correctLetters[i] == null){
                done = false;
                break;
            }
        }
        if(done){
            currentPic = (picturesNums.length - 1);
            nextMysteryWordDialog("Congratulation, you saved the girls life.");
        }
    }

    /**
     * This method will change the picture to the next one and change it's Description.
     */
    private void nextPicture(){
        currentPic = ( currentPic + 1 ) % picturesNums.length;
        mPictureIMV.setImageResource(picturesNums[currentPic]);
        mPictureIMV.setContentDescription(pictureDescription[currentPic]);
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
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    /**
     * This method will show input dialog that will take an number from the user and save it to the
     * sleepTime variable.
     */
    private void changeDelayTime() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Changing delay time");
        alert.setMessage("New delay time in seconds:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setCancelable(false); //To prevent the user from using the back button.

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    int newDelayTime = Integer.parseInt(input.getText().toString());
                    //Creating a new dialog that will allow the user to enter again without erasing
                    //the edit text view.
                    if ( newDelayTime < 1 || newDelayTime > 10){
                        Toast.makeText(GameActivity.this, "Please enter a number between 1 and 10",
                                       Toast.LENGTH_SHORT).show();
                        changeDelayTime();
                        return;
                    }
                    sleepTime = newDelayTime * 1000;
                }catch (NumberFormatException ex){
                    Toast.makeText(GameActivity.this, "Please enter a number between 1 and 10",
                            Toast.LENGTH_SHORT).show();
                    changeDelayTime();
                }
                //Used to save the state of the dialog in order to show the dialog again
                //if the device is rotated.
                delayDialogShownFlag = false;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                delayDialogShownFlag = false;
            }
        });

        alert.show();
        delayDialogShownFlag = true;
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

        if (id == R.id.action_delay_time) {
            changeDelayTime();
            return true;
        }else if (id == R.id.action_about){
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This class is created to handle the waiting thread that will sleep for some time before it
     * call the conformLetter method.
     */
    class WaitingThread extends AsyncTask<Void,Void,Void> {

        /**
         * This method will make the thread sleep in the background for the value in the sleepTime.
         * This method will be used by the waiting thread.
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Waiting for the user to enter the whole code.
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * This method will be used by the UI thread that will call the conformLetter method.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            conformLetter();
        }
    }

}

package com.ece558winter.khalidalkhulayfi.hangmanbraille;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Khalid Alkhulayfi on 3/9/2015.
 */
public class TestActivity extends BaseActivity {

    private static TextView mQuestion_TextView, mQuestionNumber_TextView;
    private Button mReport_BTN, mflush_BTN;

    private final String fileName = "UserReport";
    private String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p",
                                "q","r","s","t","u","v","w","x","y","z"};

    private static final String KEY_LETTERS_SCORE_ARRAY = "Letters score array";
    private static final String KEY_REPETITION = "Repetition";
    private static final String KEY_CURRENT_LETTER_NUM = "Current letters number";
    private static final String KEY_QUESTION_NUM = "Question number";
    private static final String KEY_WAITING_FLAG = "Waiting flag";
    private static final String KEY_SLEEP_TIME = "Sleep time";
    private static final String KEY_REPORT_SHOWN = "Report shown";
    private static final String KEY_REPETITION_DIALOG_SHOWN = "Repetition dialog shown";
    private static final String KEY_DELAY_DIALOG_SHOWN = "Delay dialog shown";


    private int[] lettersScore;
    private boolean waitingFlag, reportDialogShownFlag, repetitionDialogShownFlag, delayDialogShownFlag;
    private int repetition, currentLetterNum, questionCount;
    private int sleepTime;

    /**
     * This method will be used when this activity is created and called by the super class. It
     * has all the specific configurations for this activity.
     *
     * @param savedInstanceState variable that holds the saved values.
     */
    @Override
    protected void onCreateCompletion(Bundle savedInstanceState) {
        mQuestion_TextView = (TextView) findViewById(R.id.question_textView);
        mQuestionNumber_TextView = (TextView) findViewById(R.id.question_number_textView);
        mReport_BTN = (Button) findViewById(R.id.report_BTN);
        mflush_BTN = (Button) findViewById(R.id.flush_BTN);


        mReport_BTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport();
            }
        });


        mflush_BTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                builder.setMessage("Are you sure you want to clear all the data? \nAll the " +
                                   "letters score will be reset to zero!!!")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                for ( int i = 0 ; i < letters.length ; i++) {
                                    lettersScore[i]= 0;
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        brailleCode = new boolean[6];
        lettersScore = new int[letters.length];

        //Restoring what was saved when the app was destroyed.
        if ( savedInstanceState != null){
            lettersScore = savedInstanceState.getIntArray(KEY_LETTERS_SCORE_ARRAY);

            repetition = savedInstanceState.getInt(KEY_REPETITION);
            currentLetterNum = savedInstanceState.getInt(KEY_CURRENT_LETTER_NUM);
            questionCount =  savedInstanceState.getInt(KEY_QUESTION_NUM);
            sleepTime = savedInstanceState.getInt(KEY_SLEEP_TIME);

            waitingFlag = savedInstanceState.getBoolean(KEY_WAITING_FLAG);
            reportDialogShownFlag = savedInstanceState.getBoolean(KEY_REPORT_SHOWN);
            repetitionDialogShownFlag = savedInstanceState.getBoolean(KEY_REPETITION_DIALOG_SHOWN);
            delayDialogShownFlag = savedInstanceState.getBoolean(KEY_DELAY_DIALOG_SHOWN);
            aboutDialogShownFlag = savedInstanceState.getBoolean(KEY_ABOUT_DIALOG_SHOWN);

            //Show the dialog that was display before the app was destroyed.
            if(reportDialogShownFlag) showReport();
            if(repetitionDialogShownFlag) changeRepetition();
            if(delayDialogShownFlag) changeDelayTime();
            if(aboutDialogShownFlag) showAboutDialog();

            mQuestion_TextView.setText(letters[currentLetterNum]);
            mQuestionNumber_TextView.setText("" + questionCount);

        }else{
            sleepTime= 1000;
            repetition = 3;
            questionCount = 0;
            load();
            nextLetter();
        }
    }

    /**
     * This method will return the view ID of the activity to used by the super class.
     *
     * @return view ID of the activity.
     */
    @Override
    protected int getViewID() {
        return R.layout.activity_test;
    }

    /**
     * This method will show a dialog that has all the letters score.
     */
    private void showReport(){
        String report = "";
        for ( int i = 0 ; i < letters.length ; i++){
            report += letters[i] + " : " + lettersScore[i] + "\n";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        builder.setMessage(report)
                .setCancelable(false) //To prevent the user from using the back button.
                .setPositiveButton("Back to test?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Used to save the state of the dialog in order to show the dialog again
                        //if the device is rotated.
                        reportDialogShownFlag = false;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        reportDialogShownFlag = true;
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

        savedInstanceState.putIntArray(KEY_LETTERS_SCORE_ARRAY,lettersScore);

        savedInstanceState.putInt(KEY_REPETITION, repetition);
        savedInstanceState.putInt(KEY_CURRENT_LETTER_NUM,currentLetterNum );
        savedInstanceState.putInt(KEY_QUESTION_NUM, questionCount);
        savedInstanceState.putInt(KEY_SLEEP_TIME, sleepTime);

        savedInstanceState.putBoolean(KEY_WAITING_FLAG, waitingFlag );
        savedInstanceState.putBoolean(KEY_REPORT_SHOWN, reportDialogShownFlag );
        savedInstanceState.putBoolean(KEY_REPETITION_DIALOG_SHOWN, repetitionDialogShownFlag );
        savedInstanceState.putBoolean(KEY_DELAY_DIALOG_SHOWN, delayDialogShownFlag );
        savedInstanceState.putBoolean(KEY_ABOUT_DIALOG_SHOWN, aboutDialogShownFlag );
    }

    /**
     * This method will return the letter number that is chosen. It will check if the remainder
     * of dividing the question number by the the repetition number is zero, then it will return the
     * minimum letter score. Otherwise, it will return a random letter number.
     *
     * @return The number of the chosen letter.
     */
    private int getLetter(){
        Random random = new Random();
        //Getting a new letter according to the repetition number.
        if ( (questionCount % repetition) != 0 ){
            return random.nextInt(letters.length);
        }else{
            return getMinScore();
        }
    }

    /**
     * This method will return the number of the letter with the minimum score.
     *
     * @return The number of the letter with the minimum score.
     */
    private int getMinScore(){
        int minPosition = 0 ;
        for ( int i = 0 ; i < lettersScore.length ; i++){
            if ( lettersScore[i] < lettersScore[minPosition]) minPosition = i;
        }
        return minPosition;
    }

    /**
     * This method will prepare the activity with the next letter.
     */
    private void nextLetter() {
        questionCount++;
        currentLetterNum = getLetter();
        mQuestion_TextView.setText(letters[currentLetterNum]);
        mQuestionNumber_TextView.setText(""+questionCount);
    }

    /**
     * This method will check if the entered letter is right or wrong and will show a message
     * according to the answer. It will also check it the user uses the accessibility option to
     * clear all the entered code.
     */
    private void evaluateInput() {
        if( !brailleToLetter(brailleCode).equals("")) {
            if (mQuestion_TextView.getText().toString().equals(brailleToLetter(brailleCode))) {
                Toast.makeText(this, "Great! you got that letter right.", Toast.LENGTH_SHORT).show();
                lettersScore[currentLetterNum]++;
                //Clearing the braille code and if the user uses the accessibility.
                if(accessibilityEnabled){
                    for (int i = 0 ; i< brailleCode.length; i++) brailleCode[i]=false;
                    rightFragment.clearButtonsState();
                    leftFragment.clearButtonsState();
                    updateColor();
                }
            } else {
                Toast.makeText(this, "Your input was wrong.", Toast.LENGTH_SHORT).show();
                lettersScore[currentLetterNum]--;
                //Clearing the braille code and if the user uses the accessibility.
                if(accessibilityEnabled){
                    for (int i = 0 ; i< brailleCode.length; i++) brailleCode[i]=false;
                    rightFragment.clearButtonsState();
                    leftFragment.clearButtonsState();
                    updateColor();
                }
            }
            nextLetter();
        }
        waitingFlag = false;
    }

    /**
     * This method will save all the letters score to a file and will use letter as a tag
     * for it's score.
     */
    private void save(){
        SharedPreferences sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Saving the letter score by using the letter as a tag.
        for ( int i = 0 ; i < letters.length ; i++){
            editor.putInt(letters[i],lettersScore[i]);
        }
        editor.commit();
    }

    /**
     * This method will load the letters score from the file by using letter as tags for the scores.
     */
    private void load(){
        SharedPreferences sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        for ( int i = 0 ; i < letters.length ; i++) {
            lettersScore[i]= sharedPreferences.getInt(letters[i],0);
        }
    }

    /**
     * This method allow the app to save the changes in the scores when the app is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        //Saving the letters scores before the app get destroyed.
        save();
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
     * This method will inflate all the game menu.
     *
     * @param menu Menu that will be shown
     * @return True if the method handles the request.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    /**
     * This method allows the user to change the repetition of the letter with the minimum score.
     */
    private void changeRepetition() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TestActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("How often do you want to see the minimum score letter?");

        //Creating the an array adapter that will be shown in the dialog.
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                TestActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Every time.");
        arrayAdapter.add("After one random letter.");
        arrayAdapter.add("After two random letter.");
        arrayAdapter.add("After three random letter.");
        arrayAdapter.add("After four random letter.");

        builderSingle.setNegativeButton("cancel",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Used to save the state of the dialog in order to show the dialog again
                //if the device is rotated.
                repetitionDialogShownFlag = false;
            }
        });
        builderSingle.setCancelable(false); //To prevent the user from using the back button.
        builderSingle.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                switch (strName){
                    case "Every time.":
                        repetition = 1;
                        break;
                    case "After one random letter.":
                        repetition = 2;
                        break;
                    case "After two random letter.":
                        repetition = 3;
                        break;
                    case "After three random letter.":
                        repetition = 4;
                        break;
                    case "After four random letter.":
                        repetition = 5;
                        break;
                }
                repetitionDialogShownFlag = false;
            }
        });
        builderSingle.show();
        repetitionDialogShownFlag = true;
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
                        Toast.makeText(TestActivity.this, "Please enter a number between 1 and 10",
                                Toast.LENGTH_SHORT).show();
                        changeDelayTime();
                        return;
                    }
                    sleepTime = newDelayTime * 1000;
                }catch (NumberFormatException ex){
                    Toast.makeText(TestActivity.this, "Please enter a number between 1 and 10",
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

        if (id == R.id.action_repetition){
            changeRepetition();
            return true;
        }else if (id == R.id.action_delay_time) {
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
     * call the evaluateInput method.
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
         * This method will be used by the UI thread that will call the evaluateInput method.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            evaluateInput();
        }
    }



}

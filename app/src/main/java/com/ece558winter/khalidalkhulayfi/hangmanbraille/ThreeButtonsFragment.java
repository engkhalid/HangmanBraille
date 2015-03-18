package com.ece558winter.khalidalkhulayfi.hangmanbraille;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Khalid Alkhulayfi on 3/5/2015.
 */
public class ThreeButtonsFragment extends Fragment {

    private static Button mButton1,mButton2,mButton3;
    private boolean[] buttonsState;
    private int fragmentID;
    ThreeButtonsListener activityCommander;

    /**
     * This method will create an instance of the hosting activity to use its implemented methods .
     *
     * @param activity The hosting activity.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //Creating an instance of the hosting activity for communication.
            activityCommander = (ThreeButtonsListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    /**
     * This method will create take the arguments that are passed by the creation of the hosting
     * activity to create the right type of buttons. It will create a touch buttons when the
     * accessibility option is off and click button if the accessibility option is on.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_buttons_fragment, container, false);
        //Getting the accessibility status.
        Boolean accessibilityEnabled = getArguments().getBoolean("Accessibility");

        mButton1 = (Button) view.findViewById(R.id.dot1_BTN);
        mButton2 = (Button) view.findViewById(R.id.dot2_BTN);
        mButton3 = (Button) view.findViewById(R.id.dot3_BTN);

        buttonsState = new boolean[3];

        //Using the type of the buttons according to the accessibility status.
        if(!accessibilityEnabled) {
            mButton1.setText("Touch");
            mButton2.setText("Touch");
            mButton3.setText("Touch");

            mButton1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonsState[0] = true;
                        buttonChanged();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        buttonsState[0] = false;
                        buttonChanged();
                        return false;
                    } else
                        return false;
                }
            });
            mButton2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonsState[1] = true;
                        buttonChanged();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        buttonsState[1] = false;
                        buttonChanged();
                        return false;
                    } else
                        return false;
                }
            });
            mButton3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonsState[2] = true;
                        buttonChanged();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        buttonsState[2] = false;
                        buttonChanged();
                        return false;
                    } else
                        return false;
                }
            });
        }else{
            mButton1.setText("Click");
            mButton2.setText("Click");
            mButton3.setText("Click");

            mButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsState[0] = !buttonsState[0];
                    buttonChanged();
                }
            });
            mButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsState[1] = !buttonsState[1];
                    buttonChanged();
                }
            });
            mButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsState[2] = !buttonsState[2];
                    buttonChanged();
                }
            });
        }

        return view;
    }

    /**
     * This method will change the fragment ID number.
     * @param fragmentID The new fragment ID number.
     */
    public void setFragmentID(int fragmentID){
        this.fragmentID = fragmentID;
    }

    /**
     * This method will clear all the values of the button state.
     */
    public void clearButtonsState(){
        for (int i = 0 ; i< buttonsState.length; i++) buttonsState[i]=false;
    }

    /**
     * This button will call the implemented method in the hosting activity to send the new update
     * of the Braille code
     */
    public void buttonChanged(){
        activityCommander.updateBrailleCode(buttonsState,fragmentID);
    }
}

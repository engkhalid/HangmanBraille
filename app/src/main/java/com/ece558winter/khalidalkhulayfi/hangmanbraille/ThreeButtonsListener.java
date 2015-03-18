package com.ece558winter.khalidalkhulayfi.hangmanbraille;

/**
 * Created by Khalid Alkhulayfi on 3/5/2015.
 */
public interface ThreeButtonsListener {
    /**
     * This method should be implemented by the hosting activity and used by the fragment
     * to update the braille code. It will take a boolean array of a three elements that has
     * the status of the three buttons. In addition, it will take the fragment ID number that
     * identify the left and the right fragments.
     *
     * @param changedButtonsState Boolean array of the three values that is changed in the fragment.
     * @param fragmentID The ID number of the fragment whose buttons was changed.
     */
    public void updateBrailleCode(boolean[] changedButtonsState,int fragmentID);
}

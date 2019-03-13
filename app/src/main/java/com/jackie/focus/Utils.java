package com.jackie.focus;

import android.content.Context;
import android.media.AudioManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import java.util.ArrayList;

public class Utils {

    /** Silences phone. */
    public static void mutePhone(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    /** Unmutes and restores phone to previous state. */
    public static void unmutePhone(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }

    /** Updates the layout view based on whether there are items in the RecyclerView.
     * @param v: View that uses the layouts.
     * @param arr: Array used to check whether there are items to display. */
    public static <T> void updateView(View v, ArrayList<T> arr) {
        ConstraintLayout _hasItemsLayout = v.findViewById(R.id.containsItems);
        ConstraintLayout _noItemsLayout = v.findViewById(R.id.containsNoItems);
        if (arr == null || arr.size() == 0) {
            // Displays a layout that encourages the user to add items.
            _noItemsLayout.bringToFront();
        } else {
            // Displays the items.
            _hasItemsLayout.bringToFront();
        }
    }
}

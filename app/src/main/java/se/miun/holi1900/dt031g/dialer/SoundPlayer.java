package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    //Labels for all the buttons on the dialpad
    private enum Dial {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO, HASH, STAR}

    //Amap that holds pairs for all the button label and their corresponding sound id  from SoundPool
    private Map<String, Integer> soundIds;

    // reference to single_instance of type SoundPlayer
    private static SoundPlayer single_instance = null;

    private SoundPool soundPool;

    private SoundPlayer(Context context){
        /*
         * From API level 21 the way to create a new SoundPool changed. Instead of using the
         * the constructor, we should now use SoundPool.Builder for level 21+. */

        // Create the SoundPool
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3) // number of simultaneous streams that can be played
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION) // use sonification since our sounds has to do with user interface sounds
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()) // build the audio attributes
                .build(); // build the sound pool

        //initialize soundIds
        soundIds = new HashMap<>();

        // Load the sounds ids and the sounds in the map replacing existing sound id if present
        //sound ids correspond to title of the dial buttons in the dialpad. The is a sound for each
        // possible title
        soundIds.put("1", soundPool.load(context, R.raw.one, 1));
        soundIds.put("2", soundPool.load(context, R.raw.two, 1));
        soundIds.put("3", soundPool.load(context, R.raw.three, 1));
        soundIds.put("4", soundPool.load(context, R.raw.four, 1));
        soundIds.put("5", soundPool.load(context, R.raw.five, 1));
        soundIds.put("6", soundPool.load(context, R.raw.six, 1));
        soundIds.put("7", soundPool.load(context, R.raw.seven, 1));
        soundIds.put("8", soundPool.load(context, R.raw.eight, 1));
        soundIds.put("9", soundPool.load(context, R.raw.nine, 1));
        soundIds.put("0", soundPool.load(context, R.raw.zero, 1));
        soundIds.put("*", soundPool.load(context, R.raw.star, 1));
        soundIds.put("#", soundPool.load(context, R.raw.pound, 1));
    }

    // Static method to create instance of Singleton class
    public static SoundPlayer getInstance(Context context)
    {
        if (single_instance == null)
            single_instance = new SoundPlayer(context);

        return single_instance;
    }

    /**
     * Plays a sound in the soundPool whose id is the same as the title of the DialButtonView requesting sound
     * @param dialButton reference to the button DialButtonView requesting sound to be played
     */
    public void playSound(DialButtonView dialButton){

        try {
            String title = dialButton.getTitle();
            soundPool.play(soundIds.get(title), 1f, 1f, 1, 0, 1f);
        }
        catch(Exception e) {
            System.out.println("No sound for assigned to this button");

        }
    }

    /**
     * release resources and sets the soundPool object and current instance of this class to null
     */
    public void destroy(){
        Log.d("assignment4", "SoundPlayer.onDestroy: releasing sound pool");
        soundPool.release();
        soundPool = null;
        single_instance = null;
    }
}

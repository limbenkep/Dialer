package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    //Amap that holds pairs for all the button label and their corresponding sound id  from SoundPool
    private final Map<String, Integer> soundIds;

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

        // Load the sounds ids for the sounds in the Default voice directory in internal storage
        // to the map replacing existing sound id if present sound ids correspond to title of
        // the dial buttons in the dialpad. The is a sound for each possible title
        for (Map.Entry<String, String> entry : Util.DEFAULT_VOICE_FILE_NAMES.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String prefKey = context.getString(R.string.voices_key);
            String fileName = Util.getPreferenceSummary(prefKey, Util.DEFAULT_VOICE, context);
            String path = Util.getVoiceFilePath(Util.getDirForVoice(context, fileName), value);
            soundIds.put(key, soundPool.load(path, 1));
        }
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

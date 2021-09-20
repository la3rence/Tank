package sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class Music implements Runnable {

    private String src;

    public Music(String file) {
        this.src = file;
    }

    public void play() {
        File file = new File(src);
        AudioClip ac;
        try {
            ac = Applet.newAudioClip(file.toURL());
            if (file.exists()) {
                ac.play();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        play();
    }
}

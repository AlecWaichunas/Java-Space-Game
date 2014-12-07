package ludum.audio;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	
	public static final Clip bullet_fireed = loadAudio("singleKnock"),
							explosion = loadAudio("Explosion"),
							rocket = loadAudio("rocket");

	public static Clip loadAudio(String name){
		Clip c = null;
		try {
			c = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
			          Audio.class.getResourceAsStream("/" + name + ".wav"));
			c.open(inputStream);
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static void playClip(Clip c, float sound){
		c.setFramePosition(0);
		FloatControl fc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		fc.setValue(sound);
		c.start();
	}
	
}

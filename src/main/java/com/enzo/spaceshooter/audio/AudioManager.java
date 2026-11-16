package com.enzo.spaceshooter.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioManager {
    private Clip clip;

    public void reproduzir() {
        try {

            URL arquivo = getClass().getResource("/audioefx/background.wav");

            if (arquivo == null) {
                throw new IllegalStateException("Áudio não encontrado!");
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            clip.open(audio);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (UnsupportedAudioFileException |
                 IOException |
                 LineUnavailableException e) {

            e.printStackTrace();

        }
    }
    public void parar(){
        clip.stop();
    }
    public void audioTriunfo(){
        parar();
        try {

            URL arquivo = getClass().getResource("/audioefx/missionpassed.wav");

            if (arquivo == null) {
                throw new IllegalStateException("Áudio não encontrado!");
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            clip.open(audio);

            clip.start();

        } catch (UnsupportedAudioFileException |
                 IOException |
                 LineUnavailableException e) {

            e.printStackTrace();

        }
    }
    public void audioDerrota(){
        parar();
        try {

            URL arquivo = getClass().getResource("/audioefx/missionfailedfah.wav");

            if (arquivo == null) {
                throw new IllegalStateException("Áudio não encontrado!");
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (UnsupportedAudioFileException |
                 IOException |
                 LineUnavailableException e) {

            e.printStackTrace();

        }
    }
}

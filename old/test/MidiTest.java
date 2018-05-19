package test;
/*
DEVELOPING GAME IN JAVA 

Caracteristiques

Editeur : NEW RIDERS 
Auteur : BRACKEEN 
Parution : 09 2003 
Pages : 972 
Isbn : 1-59273-005-1 
Reliure : Paperback 
Disponibilite : Disponible a la librairie 
*/


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * An example that plays a Midi sequence. First, the sequence is played once
 * with track 1 turned off. Then the sequence is played once with track 1 turned
 * on. Track 1 is the drum track in the example midi file.
 */
public class MidiTest implements MetaEventListener {

  // The drum track in the example Midi file
  private static final int DRUM_TRACK = 1;

  public static void main(String[] args) {
    new MidiTest().run();
  }

  private MidiPlayer player;

  public void run() {

    player = new MidiPlayer();

    // load a sequence
//    Sequence sequence = player.getSequence("test.mid");
    Sequence sequence = null;
	try {
		sequence = MidiSystem.getSequence(new File("test.mid"));
	} catch (InvalidMidiDataException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    // play the sequence
    player.play(sequence);

    // turn off the drums
//    System.out.println("Playing (without drums)...");
    Sequencer sequencer = player.getSequencer();
//    sequencer.setTrackMute(DRUM_TRACK, true);
    sequencer.addMetaEventListener(this);
  }

  /**
   * This method is called by the sound system when a meta event occurs. In
   * this case, when the end-of-track meta event is received, the drum track
   * is turned on.
   */
  public void meta(MetaMessage event) {
    if (event.getType() == MidiPlayer.END_OF_TRACK_MESSAGE) {
        System.out.println("Exiting...");
        player.close();
 //       System.exit(0);
    }
  }

}

class MidiPlayer {

  // Midi meta event
  public static final int END_OF_TRACK_MESSAGE = 47;

  private Sequencer sequencer;

  /**
   * Creates a new MidiPlayer object.
   */
  public MidiPlayer() {
    try {
      sequencer = MidiSystem.getSequencer();
      sequencer.open();
//      sequencer.addMetaEventListener(this);
    } catch (MidiUnavailableException ex) {
      sequencer = null;
    }
  }

  /**
   * Plays a sequence. This method returns immediately.
   * The sequence is not played if it is invalid.
   */
  public void play(Sequence sequence) {
    if (sequencer != null && sequence != null && sequencer.isOpen()) {
      try {
        sequencer.setSequence(sequence);
        sequencer.start();
      } catch (InvalidMidiDataException ex) {
        ex.printStackTrace();
      }
    }
  }


  /**
   * Closes the sequencer.
   */
  public void close() {
    if (sequencer != null && sequencer.isOpen()) {
      sequencer.close();
    }
  }

  /**
   * Gets the sequencer.
   */
  public Sequencer getSequencer() {
    return sequencer;
  }

}

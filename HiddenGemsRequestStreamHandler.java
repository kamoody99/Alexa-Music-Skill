package main.java;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.Arrays;
import java.util.HashSet;

/**
 * For the purposes of this project, this class is just boiler plate.
 * This is the class given to lambda for the alexa skill (full package - main.java.HiddenGemsRequestStreamHandler)
 *
 * Look at SpeechletMain for logic.
 */
public class HiddenGemsRequestStreamHandler extends SpeechletRequestStreamHandler {

    public HiddenGemsRequestStreamHandler() {
        super(new SpeechletMain(),
                new HashSet<>(Arrays.asList("")));
    }
}

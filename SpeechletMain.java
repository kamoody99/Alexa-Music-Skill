package main.java;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class SpeechletMain implements Speechlet {

    /** ny times api wrapper */
    private SpotifyAPI spotifyAPI = new SpotifyAPI();

    /**
     * Initialize values for the skill here
     * @param sessionStartedRequest
     * @param session
     * @throws SpeechletException
     */
    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {
        // This is said by the device when you first open the skill
        // Not going to do anything here
        System.out.println("Session started with session id=" + session.getSessionId());
    }

    /**
     * Run when the skill is opened
     * @param launchRequest
     * @param session
     * @return
     * @throws SpeechletException
     */
    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        System.out.println("Got launch request " + launchRequest.toString());
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("Hello World from Alexa");

        System.out.println("Sending text to user: " + speech.getText());

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    /**
     * Called to perform some kind of action
     * @param request The request (contains slots, intent, etc)
     * @param session Session info (session id, attributes, etc)
     * @return Response sent to device, alexa will say this.
     */
    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session) {
        Optional<String> intentNameOpt = getIntentName(request);
        if(intentNameOpt.isPresent()) {
            String intentName = intentNameOpt.get();
            System.out.println("Got intent name " + intentName);
            if ("ListIntent".equals(intentName)) {
                PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                try {
                    speech.setText("access token = " + spotifyAPI.getSpotifyKey("national"));
                } catch (Exception e) {
                    speech.setText("Error " + e);
                    throw new RuntimeException(e);
                }
//                speech.setText("Valid categories are home, arts, automobiles, books, business, fashion, " +
//                        "food, health, insider, magazine, movies, national, nyregion, obituaries, opinion, " +
//                        "politics, realestate, science, sports, sundayreview, technology, theater, tmagazine, " +
//                        "travel, upshot, and world");
                return SpeechletResponse.newTellResponse(speech);
            /*} else if("NYTimesIntent".equals(intentName)) {
                try {
                    // Get top story in a New York Times category provided by the user.
                    String topStory = spotifyAPI.getSpotifyKey(request.getIntent().getSlot("Section").getValue());
                    System.out.println("Top story is " + topStory);
                    PlainTextOutputSpeech out = new PlainTextOutputSpeech();
                    out.setText("The top new york times story  in " +
                            request.getIntent().getSlot("Section").getValue() + " is " + topStory);
                    return SpeechletResponse.newTellResponse(out);
                } catch(Exception e) {
                    // Tell the user that something went wrong
                    System.err.println("Error getting nytimes article title");
                    e.printStackTrace();
                    PlainTextOutputSpeech errorOut = new PlainTextOutputSpeech();
                    errorOut.setText("An error happened, please try again");
                    return SpeechletResponse.newTellResponse(errorOut);
                }
            }          FindGenreArtistIntent  */
            }else if ("FindMultipleArtistIntent".equals(intentName))
            {
                try
                {

                    String endpointUrl = "https://api.spotify.com/v1/search?q=" +getRandomLetters() + "&type=artist&limit=20&offset=" + getRandomOffset();
                    String Artistrequest        = endpointUrl;
                    URL url = new URL( Artistrequest );
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setInstanceFollowRedirects( false );
                    conn.setRequestMethod( "GET" );
                    conn.setRequestProperty("Authorization","Bearer " + SpotifyAPI.getSpotifyAccess());
                    conn.setUseCaches( false );
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String tmp;
                    while((tmp = br.readLine()) != null) {
                        sb.append(tmp);
                    }

                    System.out.println("From Spotify got " + sb.toString());

                    // Create a JsonObject from the JSON the API gives back
                    // Basically just a map of either maps or lists

                    //return sb.toString();
                    PlainTextOutputSpeech artistNames = new PlainTextOutputSpeech();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONObject artists = jsonObject.getJSONObject("artists");
                    JSONArray arrayOfArtists = artists.getJSONArray("items");
                    StringBuilder promptText = new StringBuilder();
                    int validArtists = 0;
                    for (int i = 0; i < arrayOfArtists.length(); ++i) {
                      JSONObject Individualartist = arrayOfArtists.getJSONObject(i);
                      int popularity = Individualartist.getInt("popularity");
                      if (popularity <= 60)
                      {
                          if(validArtists < 5)
                          {
                              String name = Individualartist.getString("name");
                              promptText.append(name).append(", ");
                              validArtists++;
                          }
                      }

                         //jsonObject.getString("name")
                    }
                    if(promptText.length() > 0)
                    {
                        artistNames.setText("I found these artists: " + promptText.toString());
                    }
                    else
                        {
                            artistNames.setText("I couldn't find any hidden gems this time. Ask me to search again.");
                        }
//                    PlainTextOutputSpeech artistName = new PlainTextOutputSpeech();
//                    artistName.setText(jsonObject.getString("name"));
                    //arrayOfArtists.length()
                    return SpeechletResponse.newTellResponse(artistNames);

                }
                catch(Exception e)
                {
                    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                    speech.setText("Error " + e + ", " + Arrays.asList(e.getStackTrace()));
                    return SpeechletResponse.newTellResponse(speech);
                    // throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Intent isn't present");
        PlainTextOutputSpeech error = new PlainTextOutputSpeech();
        error.setText("Unknown intent, exiting, request = " + request + ", name = " + intentNameOpt);
        return SpeechletResponse.newTellResponse(error);
    }

    /**
     * Handles intent or intent name being null
     * @param request Request to extract entent name from
     * @return Optional of the intent name
     */
    private Optional<String> getIntentName(IntentRequest request) {
        Intent intent = request.getIntent();
        Optional<String> intentName;
        if(null == intent.getName()) {
            return Optional.empty();
        } else {
            return Optional.of(intent.getName());
        }
    }

    /**
     * Perform cleanup here
     * @param sessionEndedRequest
     * @param session
     * @throws SpeechletException
     */
    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {
        System.out.println("Session ended with session id=" + session.getSessionId());

    }

    private String getRandomLetters()
    {
        String consanants = "bcdfghjklmnpqrstvwxyz";
        String vowels = "aeiou";

        String returnString = "";
        int randomNumber = (int) (Math.random() * 19);
        String randConsanant = "" + consanants.charAt(randomNumber);
        randomNumber = (int) (Math.random() * 4);
        String randVowel = "" + vowels.charAt(randomNumber);
        if(((int)(Math.random() * 2) + 1) == 1)
        {
            returnString = randConsanant + randVowel;
        }
        else {
            returnString = randVowel + randConsanant;
        }

        return returnString;

    }

    private int getRandomOffset()
    {
        return (int) (Math.random() * 50);
    }
}

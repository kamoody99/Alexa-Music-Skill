package main.java;

import com.amazonaws.util.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Simple REST demo on getting data
 *
 * Probably better to use a library if this was a real world thing, but for a demo this is fine
 * Client ID found on spotify
 * Client Secret found on spotify
 */
public class SpotifyAPI {
    private static final String API_KEY = "";



    /**
     * Get the top article from a specific new york times category using REST (GET)
     * @param category The category
     * @return Title of the article
     * @throws Exception On errors w/ REST
     */
    public static String getSpotifyKey(String category) throws Exception {

        System.out.println("Test 1");

        String idSecret = "Client ID" + ":" + "Client Secret" ;
        String idSecretEncoded = new String(Base64.encodeBase64(idSecret.getBytes()));
        String endpointUrl = "https://accounts.spotify.com/api/token";
        String urlParameters  = "grant_type=client_credentials";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = endpointUrl;
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty("Authorization", "Basic " + idSecretEncoded);
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
        }

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
        JSONObject jsonObject = new JSONObject(sb.toString());
        return (jsonObject.getString("access_token"));
    }

    public static String getSpotifyAccess() throws Exception {

        System.out.println("Test 1");

        String idSecret = "Client ID" + ":" + "Client Secret" ;
        String idSecretEncoded = new String(Base64.encodeBase64(idSecret.getBytes()));
        String endpointUrl = "https://accounts.spotify.com/api/token";
        String urlParameters  = "grant_type=client_credentials";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = endpointUrl;
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty("Authorization", "Basic " + idSecretEncoded);
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
        }

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
        JSONObject jsonObject = new JSONObject(sb.toString());
        return (jsonObject.getString("access_token"));
    }
}

//make a method that returns access token
//make methods that use access tokens to get information

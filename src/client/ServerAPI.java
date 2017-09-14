package client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import core.Command;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Anders on 13/09/2017.
 */
public class ServerAPI {

    private String gameID;
    private final String baseAPI = "http://localhost:8080/";
    private JSONObject moves;
    /**
     * Initializes the game and returns the content of the level file.
     * @param levelFile The name of the level file.
     * @return The level as a a string
     */
    String initGame(String levelFile){
        this.moves = new JSONObject();
        this.moves.put("transitions", new JSONArray());

        try{
            JSONObject result = Unirest
                    .get(this.baseAPI + "game/init/" + levelFile)
                    .asJson()
                    .getBody()
                    .getObject();

            //Set initial state and gameID
            this.gameID = result.getString("gameID");
            this.moves.put("initial", result.get("state"));

            //Return the content of the file
            return result.getString("map");

        } catch(Exception e){
            e.printStackTrace();
        }
        //TODO: Error handling?
        return "";
    }

    /**
     * Sends the action to the given gameID via the server.
     * @param action
     * @return
     */
    boolean performAction(Command action){

        try{
            JSONObject result  = Unirest
                    .get(this.baseAPI + "game/" + this.gameID + "/" + action.toString())
                    .asJson()
                    .getBody()
                    .getObject();


            //result is a full transition: Add it to the trajectory
            this.moves
                    .getJSONArray("transitions")
                    .put(result);

            return result.getBoolean("success");

        } catch(Exception e){
            e.printStackTrace();
        }

        //TODO: Error handling?
        return true;
    }

    /**
     * Terminates a game
     */
    public void terminateGame(boolean completed){

        // Connect api and kill game
        Unirest.get(this.baseAPI + "game/" + this.gameID + "terminate");

        //Only write out if completed
        if(completed){
            try{
                new PrintWriter(this.gameID)
                        .println(this.moves.toString(2));
            } catch(FileNotFoundException e){
                e.printStackTrace();
                //TODO: Error handling?
            }
        }

        this.gameID = null;
        this.moves = null;

    }




}

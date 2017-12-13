package client;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import core.Command;
import core.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 13/09/2017.
 */
public class ServerAPI {

    private String gameID;
    private static final String BASE_API = "http://localhost:5000/api/";
    private JSONObject lastMoveResult;

    /**
     * Initializes the game and returns the content of the level file.
     * @param levelFile The name of the level file.
     * @return The level as a a string
     */
    String initGame(String levelFile){

        try{
            JSONObject result = Unirest
                    .post(BASE_API + "pokoban/" + levelFile.replaceAll("/","_"))
                    .asJson()
                    .getBody()
                    .getObject();

            //Set initial state and gameID
            this.gameID = result.getString("gameID");

            Logger.global(this.gameID);

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
        if(this.gameID == null) {
            System.err.println("Game already terminated on performAction. Illegal move...");
            return false;
        }
        try{
            this.lastMoveResult  = Unirest
                    .put(BASE_API + "pokoban/" + this.gameID + "/" + action.toString().replace("Push","Move"))
                    .asJson()
                    .getBody()
                    .getObject();

            return this.lastMoveResult.getBoolean("success");

        } catch(Exception e){
            e.printStackTrace();
            System.out.println(BASE_API + this.gameID + "/" + action.toString());
            return false;
        }
    }

    boolean isCompleted(){
        return this.lastMoveResult.getBoolean("done");
    }

    /**
     * Terminates a game
     */
    public void terminateGame(boolean completed, String description){
        if(this.gameID == null) {
            return;
        }

        // Connect api and kill game
        try {
            JSONObject obj = Unirest.delete(BASE_API + "pokoban/" + this.gameID
                    + "?store=true&is_planner=true&description=" + URLEncoder.encode(description, "UTF-8"))
                    .asJson()
                    .getBody()
                    .getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.gameID = null;
    }

    public static List<String> getLevels(int skip, int limit) throws Exception{

        List<String> list = new ArrayList<>();

        JSONObject obj = Unirest.get(BASE_API + "levels/supervised?skip="+skip+"&limit=" + limit)
                .asJson()
                .getBody()
                .getObject();

        JSONArray levels = obj.getJSONArray("data");

        for (int i = 0; i < levels.length(); i++) {
            list.add(levels.getJSONObject(i).getString("relativePath"));
        }

        return list;

    }




}

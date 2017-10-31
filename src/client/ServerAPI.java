package client;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import core.Command;
import core.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 13/09/2017.
 */
public class ServerAPI {

    private String gameID;
    private static final String BASE_API = "http://localhost:8080/pokoban-server/api/";
    private JSONObject lastMoveResult;

    /**
     * Initializes the game and returns the content of the level file.
     * @param levelFile The name of the level file.
     * @return The level as a a string
     */
    String initGame(String levelFile){

        try{
            JSONObject result = Unirest
                    .post(BASE_API + "pokoban/supervised/" + levelFile)
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

        try{
            this.lastMoveResult  = Unirest
                    .put(BASE_API + "pokoban/" + this.gameID + "/" + action.toString())
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
        if(this.gameID == null) return;

        // Connect api and kill game
        try {
            JSONObject obj = Unirest.delete(BASE_API + "pokoban/" + this.gameID + "?store=true&is_planner=true&description=" + description)
                    .asJson()
                    .getBody()
                    .getObject();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        this.gameID = null;
    }

    public static List<String> getLevels() throws Exception{

        List<String> list = new ArrayList<>();

        JSONArray array = Unirest.get(BASE_API + "levels/supervised")
                .asJson()
                .getBody()
                .getArray();

        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }

        return list;

    }




}

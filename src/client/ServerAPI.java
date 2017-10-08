package client;

import com.mashape.unirest.http.Unirest;
import core.Command;
import core.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Anders on 13/09/2017.
 */
public class ServerAPI {

    private String gameID;
    private final String baseAPI = "http://localhost:8080/pokoban-server/api/";
    private JSONObject moves;
    private JSONObject lastMoveResult;
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
                    .post(this.baseAPI + levelFile)
                    .asJson()
                    .getBody()
                    .getObject();

            //Set initial state and gameID
            this.gameID = result.getString("gameID");
            this.moves.put("initial", result.get("state"));
            this.moves.put("level", levelFile);
            this.moves.put("id", this.gameID);

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
                    .post(this.baseAPI + this.gameID + "/" + action.toString())
                    .asJson()
                    .getBody()
                    .getObject();

            //result is a full transition: Add it to the trajectory
            this.moves
                    .getJSONArray("transitions")
                    .put(this.lastMoveResult);

            return this.lastMoveResult.getBoolean("success");

        } catch(Exception e){
            e.printStackTrace();
            System.out.println(this.baseAPI + this.gameID + "/" + action.toString());
            return false;
        }
    }

    boolean isCompleted(){
        return this.lastMoveResult.getBoolean("done");
    }

    /**
     * Terminates a game
     */
    public void terminateGame(boolean completed){
        if(this.gameID == null) return;

        // Connect api and kill game
        Unirest.delete(this.baseAPI + this.gameID);

        //Only write out if completed
        if(completed){
            try(PrintWriter pw = new PrintWriter("expert-moves/"
                    + this.moves.getString("level") + "-" + this.gameID + ".json")){
                pw.println(this.moves.toString(2));

            } catch(FileNotFoundException e){
                e.printStackTrace();
                //TODO: Error handling?
            }
        }

        this.gameID = null;
        this.moves = null;
        this.lastMoveResult = null;

    }




}

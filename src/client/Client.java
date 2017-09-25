package client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.SystemGuardian;
import akka.actor.Terminated;
import com.sun.corba.se.spi.activation.Server;
import core.*;
import enums.Color;
import map.Level;
import merging.MergeActor;
import merging.PlanMerger;
import planning.GoalPrioritizer;
import planning.PlannerActor;
import scala.concurrent.Await;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Anders on 24/04/16.
 */
public class Client {

    public Level level;
    public List<Agent> agents;
    public List<Box> boxes;

    public Client(String fileContent) throws Exception {
        Map< Character, Color> colors = new HashMap<>();

        String[] lines = fileContent.split("\n");
        int row = 0;
        int index;
        String line = lines[0];

        // Read lines specifying colors
        for(index = 0; index < lines.length; index++){
            line = lines[index];

            //Stop if line is not color defining
            if(!line.matches("^[a-z]+:\\s*[0-9A-Z](,\\s*[0-9A-Z])*\\s*$")) break;

            line = line.replaceAll( "\\s", "" );
            String[] colonSplit = line.split( ":" );
            Color color = mapColor(colonSplit[0].trim());

            for ( String id : colonSplit[1].split( "," ) ) {
                colors.put( id.trim().charAt( 0 ), color );
            }
        }
//
//        while ( ( line = serverMessages.readLine() ).matches( "^[a-z]+:\\s*[0-9A-Z](,\\s*[0-9A-Z])*\\s*$" ) ) {
//            line = line.replaceAll( "\\s", "" );
//            String[] colonSplit = line.split( ":" );
//            Color color = mapColor(colonSplit[0].trim());
//
//            for ( String id : colonSplit[1].split( "," ) ) {
//                colors.put( id.trim().charAt( 0 ), color );
//            }
//        }

        ArrayList<String> aList = new ArrayList<>();

        int maxCol = 0;
        int maxRow;


        for(/* index already set from previously */; index < lines.length; index++){

            line = lines[index];

            //Validate that the line is not empty
            if(line.equals("")) break;

            maxCol = Math.max(maxCol, lines.length);
            aList.add(line);
        }

        maxRow = aList.size();

        boolean[][] walls = new boolean[maxRow][maxCol];
        char[][] goals = new char[maxRow][maxCol];
        boxes = new ArrayList<>();
        agents = new ArrayList<>();

        for(String mapLine : aList){
            for ( int col = 0; col < mapLine.length(); col++ ) {

                //Character and if-variables
                char chr = mapLine.charAt( col );
                boolean isWall = '+' == chr;
                boolean isAgent = '0' <= chr && chr <= '9';
                boolean isBox =  'A' <= chr && chr <= 'Z';
                boolean isGoal = 'a' <= chr && chr <= 'z';

                if (isWall) { // Walls
                    walls[row][col] = true;

                } else if (isAgent) { // Agents
                    agents.add(new Agent(row, col, Character.getNumericValue(chr), colors.get(chr)));
                    //As long as we don't work on other states

                } else if (isBox) { // Boxes
                    boxes.add(new Box(row, col, chr, colors.get(chr)));

                } else if (isGoal) { // Goal cells
                    goals[row][col] = chr;
                }
            }
            row++;
        }

        Logger.global("Used commands:");
        Arrays.stream(Command.every).forEach(cmd -> Logger.global(cmd.toString()));

        //This part is needed always. Sets the level
        Logger.global("Creating level...");
        level = new Level(maxRow, maxCol, walls, goals, colors);
        Logger.global("Creating level completed");
    }

    private Color mapColor(String input){
        switch(input){
            case "blue": return Color.blue;
            case "red": return Color.red;
            case "green": return Color.green;
            case "cyan": return Color.cyan;
            case "magenta": return Color.magenta;
            case "orange": return Color.orange;
            case "pink": return Color.pink;
            case "yellow": return Color.yellow;
            default: return Color.blue;
        }
    }

    public static void main( String[] args ) throws Exception {

        if(args.length == 0) {
            System.out.println("No level file given");
            System.exit(1);
        }
        ServerAPI server = new ServerAPI();
        String level = server.initGame(args[0]);

        // Use stderr to print to console
        Logger.global("SearchClient initializing. I am sending this using the error output stream.");
        
        Client client = new Client(level);

        Logger.global(Memory.stringRep());
        ActorSystem system = ActorSystem.create("aimuffins");
        system.actorOf(PlannerActor.props(client.agents, client.level, client.boxes, new ServerClient(server)));

        system.whenTerminated().wait();
    }
}


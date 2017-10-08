package client;

import core.Command;
import core.InvalidMoveException;
import core.Logger;
import enums.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Anders on 02/04/16.
 */
public class ServerClient {
    private final ServerAPI server;
    private static int count = 0;

    public ServerClient(ServerAPI server) throws InvalidMoveException {
        this.server = server;
    }

    public boolean move(Command[] commands) throws InvalidMoveException {

        //Assume that the first action is the right one

        boolean response = server.performAction(commands[0]);

        if (!response) {
            System.out.println("Move failed : time: " + count);
            throw new InvalidMoveException();
        }
        count++;

        return server.isCompleted();


    }

    public int getCount(){
        return count;
    }

    public void terminate(boolean completed){
        this.server.terminateGame(completed);
    }
}

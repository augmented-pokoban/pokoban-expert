package core;

/**
 * Created by Anders on 27/04/16.
 */
public class Logger {

    private String name;
    private static boolean plan = false;
    private static boolean global = false;
    private static boolean info = false;
    private static boolean error = false;
    private static boolean goal = false;

    public Logger(String name){
        this.name = name;
    }

    public static void global(String output){
        if(global) System.err.println(output);
    }
    public static void goalCount(String output){
        if(goal) System.err.println(output);
    }

    public void plan(String output){
        if(plan) System.err.println("PLAN[" + name + "]: " + output);
    }

    public void info(String output){
        if(info) System.err.println("INFO[" + name + "]: " + output);
    }

    public void error(String output){
        if(error) System.err.println("ERROR[" + name + "]: " + output);
    }
}

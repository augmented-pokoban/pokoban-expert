import client.Client;

import java.util.Arrays;
import java.util.List;

public class RunExpert {

    public static void main(String[] args){

        List<String> files = Arrays.asList("test", "test2", "test3");

        files.forEach(file -> {
            try{
                System.out.println("Executing map: " + file);
                Client.main(new String[]{file});
            } catch(Exception e){
                e.printStackTrace();
            }
        });

        System.out.println("Completed");

    }
}

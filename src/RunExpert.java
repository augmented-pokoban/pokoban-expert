import client.Client;
import client.ServerAPI;

import java.util.Arrays;
import java.util.List;

public class RunExpert {

    public static void main(String[] args) {

        //TODO: Use endpoint to retrieve file names
//        List<String> files = Arrays.asList("easy_1_box_1", "easy_1_box_2", "easy_1_box_3", "easy_1_box_4",
//                "easy_1_box_5", "easy_1_box_6", "lab_1_box_1", "lab_1_box_2", "lab_1_box_3", "lab_1_box_4");
        try {
            List<String> files = ServerAPI.getLevels();

            files.forEach(file -> {
                try {
                    System.out.println("Executing map: " + file);
                    Client.main(new String[]{file});

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Completed");

    }
}

package skyjo.mainPackage;

import skyjo.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFunction extends Application{
    Controller controller ;
    @Override
    public void start(Stage primaryStage) throws Exception {
         controller = new Controller();
        controller.start(primaryStage);
    }
    
    @Override
    public void stop(){
        controller.closeConnexion();
        
    }
}

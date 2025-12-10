package com.game.mario;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pixel Heroes - A Platformer Adventure");
        GameFonts.initialize();
        
        MainMenuUI menuUI = new MainMenuUI(primaryStage);
        primaryStage.setScene(menuUI.createWelcomeScreen());
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

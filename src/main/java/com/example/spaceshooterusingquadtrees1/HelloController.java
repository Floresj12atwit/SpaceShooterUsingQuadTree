package com.example.spaceshooterusingquadtrees1;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class HelloController {

    static boolean mouseClicked;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void createPointsOnMouseClick(Scene scene, Pane root){
        scene.setOnMouseClicked(mouseEvent -> {
        if(mouseEvent.getButton()== MouseButton.PRIMARY){

        }
        });

        //mouseEvent.getButton()== MouseButton.PRIMARY

    }
    public static boolean moveLeft;
    public static boolean moveRight;
    public static boolean moveUp;
    public static boolean moveDown;

    public void moveRectangleOnKeyPress(Scene scene, Sprite rect){
        scene.setOnKeyPressed(e->{

            switch (e.getCode()){
                case UP:     //ship.moveUp();
                    moveUp  = true;
                    break;
                case RIGHT:  //ship.moveRight();
                    moveRight =  true;
                    break;
                case DOWN:   //ship.moveDown();
                    moveDown =  true;
                    break;
                case LEFT:   //ship.moveLeft();
                    moveLeft =  true;
                    break;
                case SPACE:  HelloController.shoot(rect);
                    break;
            }
        });

        scene.setOnKeyReleased(e->{
            switch (e.getCode()){
                case UP:     moveUp = false;
                    break;
                case RIGHT:  moveRight = false;
                    break;
                case DOWN:   moveDown =  false;
                    break;
                case LEFT:   moveLeft = false;
                    break;
                case SPACE:  HelloController.shoot(rect);
                    break;
            }

        });
    }
    public static void shoot(Sprite shooter){
        Sprite s;
            if(shooter.type.equals("Enemy")) {
                s = new Sprite((int) (shooter.getX() + shooter.getTranslateX() + shooter.getWidth() / 2), (int) (shooter.getY() + shooter.getTranslateY() + shooter.getHeight()), 5, 20, shooter.type + "bullet", Color.BLACK, HelloApplication.ENEMY_MISSILE_IMG);
                s.setStroke(Color.WHITE);
                HelloApplication.root.getChildren().add(s);

            } else if(shooter.type.equals("player")) {
            s = new Sprite((int) (shooter.getX() + shooter.getTranslateX() + shooter.getWidth() / 2), (int) (shooter.getY() + shooter.getTranslateY()), 5, 20, shooter.type + "bullet", Color.BLACK, HelloApplication.MISSILE_IMG);
            s.setStroke(Color.WHITE);
            HelloApplication.root.getChildren().add(s);
        }
        //Bullet b = new Bullet((int) (shooter.getTranslateX() + 40), (int) shooter.getTranslateY(), 5, 20, Color.BLACK);
        //HelloApplication.root.getChildren().add(s);
    }

}
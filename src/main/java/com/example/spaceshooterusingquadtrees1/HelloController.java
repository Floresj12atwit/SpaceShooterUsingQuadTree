package com.example.spaceshooterusingquadtrees1;

import java.io.InputStream;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;



public class HelloController {


    //These variables were made to reference the images for the missiles in the static context where they are needed 

    static InputStream ENEMY_MISSILE_IMG_Stream = HelloController.class.getResourceAsStream("/com/example/spaceshooterusingquadtrees1/GameDecals/ENEMY_SHIP1.png");
    static InputStream MISSILE_IMG_Stream = HelloController.class.getResourceAsStream("/com/example/spaceshooterusingquadtrees1/GameDecals/ENEMY_SHIP1.png");

    static Image ENEMY_MISSILE = new Image(ENEMY_MISSILE_IMG_Stream);
    static Image MISSILE_IMG = new Image(MISSILE_IMG_Stream);


    public static boolean moveLeft;
    public static boolean moveRight;
    public static boolean moveUp;
    public static boolean moveDown;

    /*
     * This method defines the actions the user can do with the player (moving and shooting missiles)
     */
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
                s = new Sprite((int) (shooter.getX() + shooter.getTranslateX() + shooter.getWidth() / 2), (int) (shooter.getY() + shooter.getTranslateY() + shooter.getHeight()), 5, 20, shooter.type + "bullet", Color.BLACK, HelloController.ENEMY_MISSILE);
                s.setStroke(Color.WHITE);
                HelloApplication.root.getChildren().add(s);

            } else if(shooter.type.equals("player")) {
            s = new Sprite((int) (shooter.getX() + shooter.getTranslateX() + shooter.getWidth() / 2), (int) (shooter.getY() + shooter.getTranslateY()), 5, 20, shooter.type + "bullet", Color.BLACK, HelloController.MISSILE_IMG);
            s.setStroke(Color.WHITE);
            HelloApplication.root.getChildren().add(s);
        }
        
    }

}


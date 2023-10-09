package com.example.spaceshooterusingquadtrees1;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
/*
 * This class is used to define the base class that all of the objects in the game are 
 */
public class Sprite extends Rectangle{
    boolean isDead = false;
    public String type;
    private int velocity = 5;
    public static int xSize = 50;
    public static int ySize = 50;
    public static int startPosX = 265;
    public static int startPosY = 500;

    public static Rectangle createShip(){
        return new Rectangle(startPosX, startPosY, xSize,ySize);
    }

    Sprite(int x, int y, int w, int h, String type, Color color, Image decal){

        super(w, h, color);
        this.type = type;
        setX(x);
        setY(y);
        setFill(new ImagePattern(decal));
    }

    /*
     * These methods are used to move the sprites 
     */
    void moveLeft(){
        setTranslateX(getTranslateX()-velocity);
    }
    void moveRight(){
        setTranslateX(getTranslateX()+velocity);
    }
    void moveUp(){
        setTranslateY(getTranslateY()-velocity);
    }
    void moveDown(){
        setTranslateY(getTranslateY()+velocity);
    }

    public void setVelocity(int newVelocity){
        velocity = newVelocity;
    }




}


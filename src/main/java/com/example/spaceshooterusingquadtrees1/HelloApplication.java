package com.example.spaceshooterusingquadtrees1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;



import static com.example.spaceshooterusingquadtrees1.Sprite.xSize;
import static com.example.spaceshooterusingquadtrees1.Sprite.ySize;

public class HelloApplication extends Application {

    public static Pane root = new Pane();
    
    //Importing the resources for the game 
    final Image BACKGROUND_IMAGE = new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/SpaceShooterBackGround.jpg")));
    final Image PLAYER_IMG = new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/PlayerShip.png")));
    final Image ENEMY_IMG = new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/ENEMY_SHIP1.png")));
    final Image[] BOMBS_IMG = {
            new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/ENEMY_SHIP1.png"))),
            new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/ENEMY_SHIP2.png"))),
            new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/ENEMY_SHIP3.png")))
    };
    final Image EXPLOSION_IMG = new Image(String.valueOf(this.getClass().getResource("/com/example/spaceshooterusingquadtrees1/GameDecals/Explosion.png")));
    //End of resources importing

    //Defining the size of the game window
    final int GAME_WINDOW_LENGTH = 600;
    final int GAME_WINDOW_HEIGHT = 600;
    final int leftBoundary=0;
    final int rightBoundary=GAME_WINDOW_LENGTH-xSize;
    final int topBoundary=0;
    final int bottomBoundary=GAME_WINDOW_HEIGHT-ySize;

    //Creates the instance of the quadtree
    Rectangle baseRectangle = new Rectangle(0,0,GAME_WINDOW_LENGTH,GAME_WINDOW_HEIGHT);
    QuadTree qt = new QuadTree(0, baseRectangle);
    final int bottomBorder=600;
    final int topBorder = 0-20;

    private double t =0;
    private Sprite player = new Sprite(265, 500, xSize, ySize, "player", Color.BLUE, PLAYER_IMG);

    int count=0;
    public int numberOfEnemies=11;
    public Text scoreLabel;
    public Text actualScore;
    int score=0; //This will score number of enemies killed
    boolean gameOver=false;
    BackgroundImage deepSpace = new BackgroundImage(BACKGROUND_IMAGE, BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(600,600,true,true,true,true));

    Background background = new Background(deepSpace);


    /*
     * Creates the gameboard where the game is played
     */

    private Parent createBoard(){
        root.setPrefSize(600,600);
        root.getChildren().add(player);
        root.setBackground(background);
        scoreLabel = new Text(20,20,"Score: ");
        actualScore = new Text(75,20,"0");
        scoreLabel.setFont(Font.font(20));
        actualScore.setFont(Font.font(20));
        scoreLabel.setFill(Color.WHITE);
        actualScore.setFill(Color.WHITE);
        root.getChildren().addAll(scoreLabel,actualScore);




        AnimationTimer timer1 = new AnimationTimer(){
            @Override
            public void handle (long l){
                    update1();
            }
        };




        timer1.start();

        return root;

    }
    /*
     * Code below is the level progession 
     */
    boolean level1=false;
    boolean level2=false;
    boolean level3=false;

    private void levelTransition(){
            if(level2&&!level3) {
                levelChange = new Text(270, 250, "Level 2!");
                levelChange.setScaleX(8);
                levelChange.setScaleY(8);
                levelChange.setFill(Color.BLACK);
                root.getChildren().add(levelChange);
            }

            else if(!level2&&level3){
            levelChange = new Text(270, 250, "Level 3!");
            levelChange.setScaleX(8);
            levelChange.setScaleY(8);
            levelChange.setFill(Color.BLACK);
            root.getChildren().add(levelChange);
        }

    }
    private void removeTransition(Text levelChange){
        root.getChildren().remove(levelChange);
    }
    /*
    * Game over screen
    */
    private void gameOver(){
        Text gameOver = new Text(180,200, "Game OVER! :'(\nYour score was: "+score+" =)");
        Font f = Font.font("Impact", FontWeight.LIGHT, 33);
        gameOver.setFont(f);
        gameOver.setScaleX(2);
        gameOver.setScaleY(2);
        root.getChildren().add(gameOver);
    }
    /*
    * Game Win screen
    */
    private void winScreen(){
        Text winScreen = new Text(180,200, "YOU WON!! :)\nYour score was: "+score+" =)");
        Font f = Font.font("Impact", FontWeight.LIGHT, 30);
        winScreen.setFont(f);
        winScreen.setScaleX(2);
        winScreen.setScaleY(2);
        root.getChildren().add(winScreen);
    }


    int round=1;
    int numEnemR1=22;
    private void firstLevel() {
        int yOffScreen=0;
        if(round==1){
            yOffScreen=-150;
        }
        else if(round == 2){
            yOffScreen=-250;
        }
        int xOffSet = 0;

        for (int i = 0; i < 11; i++) {
            if(i%2==0) {
                Sprite s = new Sprite(40 + i * 50, 100+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[generateRandomEnemyShips()]);
                qt.insert(s);
                root.getChildren().add(s);
            }

            else {
                Sprite s = new Sprite(40 + i * 50, 70 + yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[generateRandomEnemyShips()]);

                qt.insert(s);
                root.getChildren().add(s);
            }

        }
        round++;
    }


    /*
    This successfully spawns various clusters for level 2
     */
    int cluster1=6;
    int clusters2_3_4=5;
    private void createClusterOfSmallShips() {
        int whichCluster = (int) (Math.random() * 4) + 1;
        int clusterShipType=generateRandomEnemyShips();
        int clusterSize = 0;
        int xOffSet = 0;
        int yOffScreen = -150;
        int[] clusterRows = {25, -150, -250};
        

        if (whichCluster == 1 || whichCluster == 2) {
            if (whichCluster == 1) {
                clusterSize = cluster1;
                xOffSet = generateRandomOffSet(xOffSet);
            } else {
                clusterSize = clusters2_3_4;
                xOffSet = generateRandomOffSet(xOffSet);
            }

            for (int i = 0; i <= clusterSize; i++) {
                if (i < 2) {
                    Sprite s = new Sprite((i * 50) + 25 + xOffSet, 70 + yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                } else if (i > 2 && i <= 5) {
                    Sprite s = new Sprite((i * 50) - 150 + xOffSet, 100 + yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                } else if (i == 6) {
                    Sprite s = new Sprite((i * 50) - 250 + xOffSet, 140 + yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                }

            }
        } else if(whichCluster == 3){
            clusterSize = clusters2_3_4;
            xOffSet = generateRandomOffSet(xOffSet);

            //This is going to be the second level enemies
            //First cluster done just make clusters 2 3 and 4
            //Make the cluster first and then change where its going to go by adding or subtracting x values
            for (int i = 0; i <= clusterSize; i++) {
                if (i == 0) {
                    Sprite s = new Sprite((i * 50)+50 + xOffSet, 70+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                } else if (i > 1 && i <= 3) {
                    Sprite s = new Sprite((i * 50) -75 + xOffSet, 100+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                } else if (i>=4&&i<=5) {
                    Sprite s = new Sprite((i * 100) -400 + xOffSet, 130+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                }


            }
    }
        else if(whichCluster==4){
            clusterSize = clusters2_3_4;
            xOffSet = generateRandomOffSet(xOffSet);
            for (int i = 0; i <= clusterSize; i++) {
                if (i >= 0 && i<2) {
                    Sprite s = new Sprite((i * 100) + xOffSet, 70+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                } else if (i >= 2 && i < 4) {
                    Sprite s = new Sprite((i * 50) -75  + xOffSet, 100+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                } else if (i==5) {
                    Sprite s = new Sprite((i * 50) -200 + xOffSet, 130+yOffScreen, 30, 30, "Enemy", Color.RED, BOMBS_IMG[clusterShipType]);
                    qt.insert(s);
                    root.getChildren().add(s);
                }


            }
        }
        round++;
    }

    /*
     * Generates the final boss for the game 
     */
    private void generateBoss(){
        int yOffScreen=-300;
        int xOffSet = 0;
        int whichShip = generateRandomEnemyShips();
        for (int i = 1; i < 20; i++) {
            if(i<3) {
                Sprite s = new Sprite( (i * 70)+170, 90+yOffScreen, 45, 80, "Enemy", Color.RED, BOMBS_IMG[whichShip]);
                qt.insert(s);
                root.getChildren().add(s);
            }
            if(i>3&&i<6){
                Sprite s = new Sprite((i*450)-1740,130+yOffScreen, 30,60,"Enemy", Color.RED, BOMBS_IMG[whichShip]);
                qt.insert(s);
                root.getChildren().add(s);
            }
            if(i>6&&i<15){
                Sprite s = new Sprite((i*70)-455,200+yOffScreen, 40,40,"Enemy", Color.RED, BOMBS_IMG[whichShip]);
                qt.insert(s);
                root.getChildren().add(s);
            }
            if(i>=15){
                Sprite s = new Sprite((i*60)-740,250+yOffScreen, 40, 30, "Enemy", Color.RED, BOMBS_IMG[whichShip]);
                qt.insert(s);
                root.getChildren().add(s);
            }

        }
    }



    //*These are used for updating quadtree rectangles\\

    ArrayList<Rectangle> quadTreeNodes = new ArrayList<>();
    int removedObjects=0;

    int spritesSearched=0;

    Text levelChange;
    private void update1() {
        t += 0.010;
        


        ArrayList<Sprite> returned = new ArrayList<>();
        
        final boolean atRightBorder = player.getTranslateX() == rightBoundary-265;
        final boolean atLeftBorder = player.getTranslateX() == leftBoundary-265;
        final boolean atTopBorder = player.getTranslateY() == topBoundary-500;
        final boolean atBottomBorder = player.getTranslateY() == bottomBoundary-500;



        if(HelloController.moveUp){
            if(atTopBorder){
                player.setTranslateY(topBoundary-500);
            }
            else {
                player.moveUp();
            }
        }
        if(HelloController.moveRight){
            if(atRightBorder){
                player.setTranslateX(rightBoundary-265);
            }
            else {
                player.moveRight();
            }
        }
        if(HelloController.moveDown){
            if(atBottomBorder){
                player.setTranslateY(bottomBoundary-500);
            }
            else {
                player.moveDown();
            }
        }

        if(HelloController.moveLeft){
            if(atLeftBorder){
                player.setTranslateX(leftBoundary-265);
            }
            else {
                player.moveLeft();
            }
        }




        //Putting this outside update seems to make everything happen instantly
        ArrayList<Sprite> gameObjects = new ArrayList<>();


            for (int i = 0; i < root.getChildren().size(); i++) {
                if (root.getChildren().get(i) instanceof Sprite) { //Brian is the GOAT (Definitely better way to avoid using this)
                     gameObjects.add((Sprite) root.getChildren().get(i));


                } else if (root.getChildren().get(i) instanceof Rectangle) {
                    quadTreeNodes.add((Rectangle) root.getChildren().get(i));
                }
            }


            for (Sprite s : gameObjects) {

                switch (s.type) {
                    case "Enemybullet":
                        s.setVelocity(2);
                        s.moveDown();
                        

                        if(s.getTranslateY() + s.getY() > bottomBorder) {
                            //System.out.println("I reached the bottom of the screen");
                            s.isDead = true;
                        }

                        if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                            player.isDead = true;
                            s.isDead = true;
                        }


                    case "Enemy":

                        //Randomize which enemies move down
                        s.setVelocity(1);
                        s.moveDown();




                        if(level1||level3) {
                            if ((s.getTranslateY() + s.getY()) <= 260) {
                                if (s.getTranslateY() == 250) {
                                    s.moveUp();//s.setTranslateY(230);
                                    s.setVelocity(0);
                                }
                            }
                        }





                        if(t > 4){
                            if(Math.random() < 0.3){
                                HelloController.shoot(s);
                            }
                        }

                        if(s.getBoundsInParent().intersects(player.getBoundsInParent())){
                            s.isDead=true;
                            player.isDead=true;
                        }

                        if (s.getTranslateY() + s.getY() > bottomBorder) {
                            //System.out.println("I reached the bottom of the screen");
                            s.isDead = true;
                        }



                        break;

                    case "playerbullet":
                        s.moveUp();


                        if (s.getTranslateY() + s.getY() < topBorder) {
                            //System.out.print(s.getTranslateY());
                            //System.out.println("I have left the screen");
                            s.isDead = true;
                        }


                        spritesSearched = 0;
                        //qt.retrieve(returned, s);

                            for (Sprite enemy : gameObjects) {
                                if (enemy.type.equals("Enemy")||enemy.type.equals("Enemybullet")) {
                                    //enemy.setFill(Color.BLUE);
                                    //This is checking how many objects EACH missile is detecting for collision
                                    spritesSearched++;

                                    if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {


                                        //enemy.setFill(Color.GREEN);
                                        //List<Sprite> returned =  new ArrayList<>();
                                        enemy.isDead = true;
                                        enemy.setFill(new ImagePattern(EXPLOSION_IMG));
                                        s.isDead = true;
                                        increaseScore();

                                        if(level2){
                                            qt.retrieve(returned, enemy);
                                            for (Sprite n : returned) {
                                                n.setFill(new ImagePattern(EXPLOSION_IMG));
                                                n.isDead=true;
                                                increaseScore();
                                            }
                                        }



                                        removedObjects++;


                                    }
                                }
                            }



                        break;
                }

                if (s.isDead) {
                    root.getChildren().remove(s);
                    //s.setFill(new ImagePattern(EXPLOSION_IMG));
                }
                if(player.isDead){
                    gameOver();
                    s.isDead=true;
                }


            }




            if(t>1&& t< 1.018){
                //Use ifs to detect what level user is going to be generated
                if(round<=2) {
                    level1=true;
                    firstLevel();
                    t=2;
                }
                if(round==3 && !level1 && level2){
                    levelTransition();
                    round++;
                    t=2;
                }

                if(round>3&&round<=22){
                    level1=false;
                    level2=true;
                    createClusterOfSmallShips();
                    createClusterOfSmallShips();
                }
                if(round==23){

                    round++;

                }if(round==24&&gameObjects.size()==1){
                    level2=false;
                    level3=true;
                    levelTransition();
                    round++;
                    t=2;
                }
                if(round==25){
                    generateBoss();
                    round++;
                }

               
            }
            
            if (t > 2 && t < 2.2) {


                qt.show();
                qt.clear();

                for (int i = 0; i < gameObjects.size(); i++) {
                    if (gameObjects.get(i).type.equals("playerbullet") || gameObjects.get(i).type.equals("player")||gameObjects.get(i).type.equals("Enemybullet")) {
                        continue;
                    }
                    qt.insert(gameObjects.get(i));

                }


            }
            if (t > 4 && t < 4.2) {

                removeTransition(levelChange);

                //First level is based on killing all enemies second level is not
                if(level1&&(removedObjects>=numEnemR1)){
                    level1=false;
                    level2=true;
                }
                if(level3&&gameObjects.size()==1){
                    level3=false;
                    round++;
                    winScreen();
                }


                System.out.println(removedObjects);
                System.out.println(numEnemR1);

                System.out.println("GameObjects Size: " + gameObjects.size());
                //This is a psuedo method of clearing space
                gameObjects.clear();
                gameObjects.trimToSize();

                //System.out.println(spritesSearched);
                //System.out.println("GameObjects Size: " + gameObjects.size());
                //System.out.println("GameObject contents: " + gameObjects);
                System.out.println("Detected for collision within QuadtreeNode: "+returned.size());

                returned.clear();
                returned.trimToSize();
                //Removes QuadtreeNodes from Pane to show updates
                for (Rectangle i : quadTreeNodes) {
                    root.getChildren().remove(i);
                }


                quadTreeNodes.clear();
                quadTreeNodes.trimToSize();


                t = 0;
            }


        }






    @Override
    public void start(Stage stage) {


        HelloController control = new HelloController();
        Scene scene = new Scene(createBoard());
        control.moveRectangleOnKeyPress(scene, player);

        stage.setTitle("QuadTree test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    public int fillRandomCoordinate(){
        return (int) (Math.random()*(600))+1;
    }

    public boolean checkIfAllSpritesDead(ArrayList<Sprite> gameObjects){
        int numberOfSprites=0;
        for(Sprite n: gameObjects){
            if(n.isDead && n.type.equals("Enemy")){
                numberOfSprites++;
                System.out.print(numberOfSprites);
            }

        }
        return numberOfSprites==numberOfEnemies;
    }

    public void increaseScore(){
        score++;
        actualScore.setText(String.valueOf(score));
    }
    public int generateRandomOffSet(int offSet){
            int randomLocation= (int) ((Math.random()*6)+1);
            offSet=randomLocation*75;
            return offSet;
    }
    public int generateRandomEnemyShips(){
        return (int) (Math.random()*3);
    }

    }

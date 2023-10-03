package com.example.spaceshooterusingquadtrees1;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.example.spaceshooterusingquadtrees1.HelloApplication.root;
//import static com.example.quadtreevisual.HelloApplication.root2;

public class QuadTree {

    private int MAX_OBJECTS = 5;
    private int MAX_LEVELS = 5;

    private int level;
    public List<Sprite> objects;
    public Rectangle bounds;
    private QuadTree[] nodes;
    boolean isDivided;

    //Constructor
    public QuadTree(int pLevel, Rectangle pBounds) {
        level = pLevel;
        objects = new ArrayList<>();
        bounds = pBounds;
        nodes = new QuadTree[4];
    }

    /*
        Clears the quadtree
     */
    //Clears the quadtree recursively by clearing all objects from all nodes
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    /*
    Splits the node into 4 subnodes (rectangle into 4 rectangles(squares))
     */
    private void split() {

        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        //Coordinates start at top Left point of window
        nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subWidth, subWidth, subHeight));
        isDivided=true;
    }

/*
    Determines which node the object belongs to. -1 means object cannot completely fit within
    a child and is part of the parent node
 */
    int getIndex(Sprite pRect) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        //Boolean to check if object can completely fit within top quadrants
        boolean topQuadrant = (pRect.getY() <= horizontalMidpoint && pRect.getY() + pRect.getHeight() <= horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

        //Checks if object can completely fit within the left quadrants
        if (pRect.getX() <= verticalMidpoint && pRect.getX() + pRect.getWidth() <= verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        else if (pRect.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        return index;
    }

    /*
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(Sprite pRect) {
        if (nodes[0] != null) {
            int index = getIndex(pRect);

            if (index != -1) {
                nodes[index].insert(pRect);

                return;
            }
        }

        objects.add(pRect);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex((Sprite) objects.get(i));
                if (index != -1) {
                    nodes[index].insert((Sprite) objects.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }

    /*
     * Return all objects that could collide with the given object
     */
    public List<Sprite> retrieve(List<Sprite> returnObjects, Rectangle pRect) {
        int index = getIndex((Sprite) pRect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, pRect);
        }

        returnObjects.addAll(objects);

        return returnObjects;
    }

    void draw(ArrayList<Rectangle> quadtrees){
        quadtrees.add(this.bounds);
        for(int i =0; i < nodes.length; i++){
            QuadTree node =  nodes[i];
            if(node != null){
                node.draw(quadtrees);
            }
        }
    }

    void show(){
        Rectangle displayedNodes = new Rectangle(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        //Sprite displayedNodes = new Sprite((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight(),  "quadTreeNode", Color.BLACK);
        displayedNodes.setStroke(Color.BLACK);
        displayedNodes.setFill(Color.TRANSPARENT);
        root.getChildren().add(displayedNodes);
        //&&nodes[0]!=null
        if(this.isDivided&&nodes[0]!=null){
            this.nodes[0].show();
            this.nodes[1].show();
            this.nodes[2].show();
            this.nodes[3].show();

        }
    }





    void clearShow(ObservableList<Node> qtRects){
        qtRects.removeIf(n -> n instanceof Rectangle&& !(n instanceof Sprite));
        //root.getChildren().clear();
        /*
        if(this.isDivided&&nodes[0]!=null){
            this.nodes[0].clearShow();
            this.nodes[1].clearShow();
            this.nodes[2].clearShow();
            this.nodes[3].clearShow();

        }

         */
    }

}
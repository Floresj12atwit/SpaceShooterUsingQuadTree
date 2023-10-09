# Space Shooter Using a QuadTree #
The goal of this project was to use a quadtree to make collision detection more efficient in the context of a galaga-like space shooter game.
Instead of holding all the objects in a structure like an array list, they are held in a quadtree.  This data structure divides up the 2D plane into four quadrants which holds the objects on screen.
If many objects are added to a specific quadrant that quadrant will subdivide into 4 more quadrants.  

-When missiles hit enemies in this game the quadtree is called to see what ships are near and those
will be destroyed as well to create a area of effect damage concept except against the boss.  When enemies are hit, others that are in the same cluster will explode as well.

-The quadtree is visualized at each frame to show it working in real time.  Some enemies may exist on the border of a quadrant leading them to be 
destroyed when they are not near a proper collision.  

-This game currently has three levels and a boss fight but can be edited to have more if so desired.   

The game is run from the HelloApplication.java file found in the directory src/main -> java -> com/example/spaceshooterusingquadtrees1 

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class WindmillAnimation extends PApplet {



Windmill windmill;
ArrayList<Point> points = new ArrayList<Point>();
boolean paused = false;

public void setup(){
  
  background(50);
  //loads the sound effect
  SoundFile click = new SoundFile(this, "Click.wav");
  //creates the actual windmill object
  windmill = new Windmill(new int[] {width/2,height/2}, PI/512, click);
  //populatePoints(4);
}

public void draw(){
  if(paused == false){
    background(50);
    windmill.rotate(points);
    drawPoints();
  }
}

//generates a given number of points in random positions
public void populatePoints(int pointNum){
  for(int i = 0; i < pointNum; i++){
    int c = color(random(50, 255), random(50, 255), random(50, 255));
    Point point = new Point(new int[] {(int)random(20, width-20), (int)random(20, height-20)}, 10, c);
    points.add(point);
  }
}

public void drawPoints(){
  for(Point point : points){
    point.drawPoint();
  }
}

//allows you to add and remove points by clicking on them
public void mouseClicked(){
  int size = points.size();
  for(int i = 0; i < points.size(); i++){
    int x = points.get(i).getPos()[0];
    int y = points.get(i).getPos()[1];
    if(x+10 > mouseX && x-10 < mouseX && y+10 > mouseY && y-10 < mouseY){
      points.remove(i);
    }
  }
  if(size == points.size()){
    int c = color(random(50, 255), random(50, 255), random(50, 255));
    Point point = new Point(new int[] {(int)mouseX, (int)mouseY}, 10, c);
    points.add(point);
  }
  
}

//alows you to pause the animation
public void keyPressed(){
  if(key == 'p') paused = !paused;
}
/*This class is used to conveniently store and access a 2d coordinate
while also adding some other functionality
*/
public class Point{
  int[] coordinates;
  int size;
  int pointColor;
  boolean isFlagged = false;
  
  Point(int[] coordinates, int size, int pointColor){
    this.coordinates = coordinates;
    this.size = size;
    this.pointColor = pointColor;
  }
  
  public void drawPoint(){
    fill(this.pointColor);
    stroke(this.pointColor);
    circle(coordinates[0], coordinates[1], size);
  }
  
  public int[] getPos(){
    return this.coordinates;
  }
  
  
  //when a point is flagged it wont be checked for collisions by the RotLine class
  public void flag(){
    this.isFlagged = true;
  }
  
  public void unflag(){
    this.isFlagged = false;
  }
  
  public boolean isFlagged(){
    return this.isFlagged;
  }
}


public class Windmill{
  //the point the line rotates around
  int[] rotationalPoint;
  //rotation rate is in radians per frame
  float rotationRate;
  //the current angle of the line in radians
  float angle = 0;
  //The sound effect is played whenever a collision is detected
  SoundFile effect;
  
  Windmill(int[] initPoint, float rotationRate, SoundFile effect){
    this.rotationalPoint = initPoint;
    this.rotationRate = rotationRate;
    this.effect = effect;
  }
  
  //draws the windmill
  public void drawLine(){
    stroke(255);
    line(rotationalPoint[0], rotationalPoint[1], cos(angle)*10000+rotationalPoint[0], sin(angle)*10000+rotationalPoint[0]);
    line(rotationalPoint[0], rotationalPoint[1], cos(angle+PI)*10000+rotationalPoint[0], sin(angle+PI)*10000+rotationalPoint[0]);
  }
  
  //updates the angle of the line
  public void updateAngle(){
    this.angle = this.angle+rotationRate;
    while(this.angle > 2*PI) this.angle -= 2*PI;
  }
  
  //calls all the necessary functions to draw and change the line's position
  public void rotate(ArrayList<Point> points){
    this.updateAngle();
    this.detectCollision(points);
    this.unflagPoints(points);
    this.drawLine();
  }
  
  //detects if the line has collided with any points
  public void detectCollision(ArrayList<Point> points){
    for(Point point : points){
      int x1 = rotationalPoint[0];
      int y1 = rotationalPoint[1];
      int x2 = point.getPos()[0];
      int y2 = point.getPos()[1];
      
      //skips the point if it's the same as the pivot point
      if(point.isFlagged == false){
        //calculate the angle from the pivot point to a given point
        float theta = atan((float)(y2-y1)/((float)(x2-x1)));
        
        /* These two lines compensate for the fact that atan() as a range from PI/2 to -PI/2
        they give the atan function a range from 0 to 2PI
        */
        if(x2-x1 < 0) theta += PI;
        if(x2-x1 > 0 && y2-y1 < 0) theta = 2*PI-abs(theta);
        
        /*checks to see if the current line's angle is close  to the angle from the pivot point to the given point
        if it is then it will make the given point the new pivot point
        */
        if(angle<theta+rotationRate/2 && angle > theta-rotationRate/2){
          this.rotationalPoint[0] = x2;
          this.rotationalPoint[1] = y2;
          //flags the point so it wont be checked in the next frame
          point.flag();
          //plays a sound effect
          effect.play();
        }
        else{
          float alternateAngle = angle-PI;
          while(alternateAngle < 0) alternateAngle += 2*PI;
          if(alternateAngle < theta+rotationRate/2 && alternateAngle > theta-rotationRate/2){
            this.rotationalPoint[0] = x2;
            this.rotationalPoint[1] = y2;
            //flags the point so it wont be checked in the next frame
            point.flag();
            //plays a sound effect
            effect.play();
          }
        }
      }
    }
  }
  
  //unflags all the points that the line is currently not pivoting on
  public void unflagPoints(ArrayList<Point> points){
    for(Point point : points){
      if(point.getPos()[0] != rotationalPoint[0] && point.getPos()[1] != rotationalPoint[1]) point.unflag();
    }
  }
}
  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "WindmillAnimation" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

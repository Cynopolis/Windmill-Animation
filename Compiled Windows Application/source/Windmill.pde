import processing.sound.*;

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
  void drawLine(){
    stroke(255);
    line(rotationalPoint[0], rotationalPoint[1], cos(angle)*10000+rotationalPoint[0], sin(angle)*10000+rotationalPoint[0]);
    line(rotationalPoint[0], rotationalPoint[1], cos(angle+PI)*10000+rotationalPoint[0], sin(angle+PI)*10000+rotationalPoint[0]);
  }
  
  //updates the angle of the line
  void updateAngle(){
    this.angle = this.angle+rotationRate;
    while(this.angle > 2*PI) this.angle -= 2*PI;
  }
  
  //calls all the necessary functions to draw and change the line's position
  void rotate(ArrayList<Point> points){
    this.updateAngle();
    this.detectCollision(points);
    this.unflagPoints(points);
    this.drawLine();
  }
  
  //detects if the line has collided with any points
  void detectCollision(ArrayList<Point> points){
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
  void unflagPoints(ArrayList<Point> points){
    for(Point point : points){
      if(point.getPos()[0] != rotationalPoint[0] && point.getPos()[1] != rotationalPoint[1]) point.unflag();
    }
  }
}

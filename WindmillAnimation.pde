import processing.sound.*;

Windmill windmill;
ArrayList<Point> points = new ArrayList<Point>();
boolean paused = false;

void setup(){
  size(1000, 1000);
  background(50);
  //loads the sound effect
  SoundFile click = new SoundFile(this, "Click.wav");
  //creates the actual windmill object
  windmill = new Windmill(new int[] {width/2,height/2}, PI/512, click);
  //populatePoints(4);
}

void draw(){
  if(paused == false){
    background(50);
    windmill.rotate(points);
    drawPoints();
  }
}

//generates a given number of points in random positions
void populatePoints(int pointNum){
  for(int i = 0; i < pointNum; i++){
    color c = color(random(50, 255), random(50, 255), random(50, 255));
    Point point = new Point(new int[] {(int)random(20, width-20), (int)random(20, height-20)}, 10, c);
    points.add(point);
  }
}

void drawPoints(){
  for(Point point : points){
    point.drawPoint();
  }
}

//allows you to add and remove points by clicking on them
void mouseClicked(){
  int size = points.size();
  for(int i = 0; i < points.size(); i++){
    int x = points.get(i).getPos()[0];
    int y = points.get(i).getPos()[1];
    if(x+10 > mouseX && x-10 < mouseX && y+10 > mouseY && y-10 < mouseY){
      points.remove(i);
    }
  }
  if(size == points.size()){
    color c = color(random(50, 255), random(50, 255), random(50, 255));
    Point point = new Point(new int[] {(int)mouseX, (int)mouseY}, 10, c);
    points.add(point);
  }
  
}

//alows you to pause the animation
void keyPressed(){
  if(key == 'p') paused = !paused;
}

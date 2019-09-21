/*This class is used to conveniently store and access a 2d coordinate
while also adding some other functionality
*/
public class Point{
  int[] coordinates;
  int size;
  color pointColor;
  boolean isFlagged = false;
  
  Point(int[] coordinates, int size, color pointColor){
    this.coordinates = coordinates;
    this.size = size;
    this.pointColor = pointColor;
  }
  
  void drawPoint(){
    fill(this.pointColor);
    stroke(this.pointColor);
    circle(coordinates[0], coordinates[1], size);
  }
  
  int[] getPos(){
    return this.coordinates;
  }
  
  
  //when a point is flagged it wont be checked for collisions by the RotLine class
  void flag(){
    this.isFlagged = true;
  }
  
  void unflag(){
    this.isFlagged = false;
  }
  
  boolean isFlagged(){
    return this.isFlagged;
  }
}

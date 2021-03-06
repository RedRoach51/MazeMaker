import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.Scanner;

public class MazeMaker extends Application{
    Pane pane = new Pane();
    int pointX = 10;
    int pointY = 10;
    int input;
    double cX = 15 + pointX*20;
    double cY = 15 + pointY*20;
    boolean [][] points;
    Circle[][] circles;
    Line background;    

    Circle selection = new Circle(cX,cY,8, new Color(100.0/255.0, 20.0/255.0, 180.0/255.0,.6));
    Random random = new Random();
    
    public void start(Stage primaryStage){
        
        System.out.println("This code allows you to create a drawing or maze on this grid.");
        System.out.println("When you activate the line walker by pressing SPACE, the program will randomly move to available points and drawing lines until it is no longer able to.");
        System.out.println("Once this happens, the program will stop moving to points and drawing until activated again through user input.");
        System.out.println("The program can start lines even from already drawn ones, as long as there is an available point adjacent to it.");
        System.out.println("However, the user can manually draw lines before and after activating the line walker using WASD keys, even to points that are no longer available. ");
        
        points = new boolean[pointX*2][pointY*2];
        circles = new Circle[pointX*2][pointY*2];        
        
        pane.getChildren().add(selection);
        for (boolean[] column : points){
            for (boolean point : column){
                point = true;
            }
        }
        for (int x = 0; x < points.length; x++){
            for (int y = 0; y < points[x].length; y++){
                points[x][y] = false;
            }
        }
        for (int x = 0; x < circles.length; x++){
            for (int y = 0; y < circles[x].length; y++){
                Circle circle = new Circle((x+1) * 20 - 5,(y+1) * 20 - 5,2);
                circle.setFill(new Color(0,0,0,.2));
                circles[x][y] = circle;
                pane.getChildren().add(circle);
            }
        }

        for (int x = 0; x < points.length * 20; x += 20){
//            System.out.println(x);
            background = new Line(15 + x,15,15+x,15 + 20*(points[x/20].length - 1));
            background.setStroke(new Color(0,0,0,.2));
            pane.getChildren().add(background);
        }
        for (int y = 0; y < points[0].length * 20; y += 20){
//            System.out.println(y);
            background = new Line(15,15 + y,15+ 20*(points.length-1),15+y);
            background.setStroke(new Color(0,0,0,.2));
            pane.getChildren().add(background);                
        }
        
        Timeline animation = new Timeline(
            new KeyFrame(Duration.millis(100), e-> {
                moveRandom();
            }));
        Timeline animationControl = new Timeline(
            new KeyFrame(Duration.millis(50), e->{
                refreshPoints();
                if (!movesAvailable(pointX,pointY)){
                    animation.pause();
                }
            }));
        pane.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP: if (pointY != 0) { selectUp(); } break;
                case DOWN: if (pointY < points[0].length-1) { selectDown(); } break;
                case LEFT: if (pointX != 0) { selectLeft(); } break;
                case RIGHT: if (pointX < points.length-1){ selectRight(); } break;
                
                case W: if (pointY != 0) { moveUp(true); } break;
                case S: if (pointY != points[0].length-1) { moveDown(true); } break;
                case A: if (pointX != 0) { moveLeft(true); } break;
                case D: if (pointX != points[0].length-1){ moveRight(true); } break;
                
                case Q: getData(pointX,pointY); break;
                case SPACE: animation.play(); animationControl.play(); break;
                case F: animation.pause(); animationControl.pause(); break;
                case R: System.out.println(movesAvailable(pointX,pointY)); break;
            }
        });
            
        animation.setCycleCount(Timeline.INDEFINITE);
        animationControl.setCycleCount(Timeline.INDEFINITE);
        animationControl.play();
        
        primaryStage.setScene(new Scene(pane,cX*2-20,cY*2-20));
        primaryStage.setTitle("I'm really proud of this one actually");
        primaryStage.show();
        pane.requestFocus();
    }
    private void selectUp(){
        {
            cY -= 20;
            pointY -= 1;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);                  
        }
    }
    private void selectDown(){
        {
            pane.getChildren().remove(selection);
            cY += 20;
            pointY += 1;
            pane.getChildren().remove(selection);
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);                  
        }
    }
    private void selectLeft(){
        {
            cX -= 20;
            pointX -= 1;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);            
        }
    }
    private void selectRight(){
        {
            cX += 20;
            pointX += 1;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);      
        }
    }
    
    private void moveUp(boolean player){
        points[pointX][pointY] = true;
        if (!points[pointX][pointY - 1]||player){
            pane.getChildren().add(new Line(cX, cY, cX, cY - 20));
            cY -= 20;
            pointY -= 1;
            points[pointX][pointY] = true;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);  
        }
    }
    private void moveDown(boolean player){
        points[pointX][pointY] = true;
        if (!points[pointX][pointY + 1] || player){
            pane.getChildren().add(new Line(cX, cY, cX, cY + 20));
            cY += 20;
            pointY += 1;
            points[pointX][pointY] = true;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);  
        }
    }
    private void moveLeft(boolean player){
        points[pointX][pointY] = true;
        if (!points[pointX - 1][pointY] || player){
            pane.getChildren().add(new Line(cX, cY, cX - 20, cY));
            cX -= 20;
            pointX -= 1;
            points[pointX][pointY] = true;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);  
        }
    }
    private void moveRight(boolean player){
        points[pointX][pointY] = true;
        if (!points[pointX + 1][pointY] || player){
            pane.getChildren().add(new Line(cX, cY, cX + 20, cY));
            cX += 20;
            pointX += 1;
            points[pointX][pointY] = true;
            pane.getChildren().remove(selection);            
            selection.setCenterX(cX);
            selection.setCenterY(cY);
            pane.getChildren().add(selection);  
        }
    }
    
    private void moveRandom(){
        int choice = random.nextInt(5);
        if (choice == 0 && pointY != 0){
            moveUp(false);
        }
        if (choice == 1 && pointY != points[pointX].length-1){
            moveDown(false);
        }
        if (choice == 2 && pointX != 0){
            moveLeft(false);
        }
        if (choice == 3 && pointX != points[pointY].length-1){
            moveRight(false);
        }
    }
    
    private boolean movesAvailable(int x, int y){
        boolean available = false;
        if (pointY!= 0 && !points[x][y-1]){
            return true;
        }
        if (pointY != points[pointX].length-1 && !points[x][y+1]){
            return true;
        }
        if (pointX!= 0 && !points[x-1][y]){
            return true;
        }
        if (pointX!= points[pointY].length-1 && !points[x+1][y]){
            return true;
        }
        return false;
    }

    public void clear(int x, int y){
        points[x][y] = true;
    }
    
    private void refreshPoints(){
        for (int x = 0; x < points.length; x++){
            for (int y = 0; y < points[x].length; y++){
                pane.getChildren().remove(circles[x][y]);
                paintCircle(x,y);
                pane.getChildren().add(circles[x][y]);
            }
        }
        
    }

        
    
    private void getData(int x, int y){
        System.out.println(x + ", " + y + ", " + points[x][y]);
    }
    
    public void paintCircle(int x, int y){
        if(points[x][y]){
            circles[x][y].setFill(new Color(200.0/255.0,0,0,.6));
            circles[x][y].setRadius(3);
        }
        else{
            circles[x][y].setFill(new Color(0,0,0,.2));
            circles[x][y].setRadius(1);
        }
    }
    
    public static void main(String[] args){
        
//        Application.launch(args);
    }
}

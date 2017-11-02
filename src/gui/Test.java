package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Test extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				if((i + j)%2 == 1) {
					gridPane.add(redRectangle(50, 50), i, j);
				} else {
					gridPane.add(blueRectangle(50, 50), i, j);
				}
			}
		}
		
		Scene scene = new Scene(gridPane, 200, 200);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public Rectangle blueRectangle(int height, int width) {
		Rectangle blueRectangle = new Rectangle();
		blueRectangle.setFill(Color.BLUE);
		blueRectangle.setHeight(50);
		blueRectangle.setWidth(50);
		blueRectangle.setOnMouseClicked(e -> {
			System.out.println("Click BLUE!!");
		});
		blueRectangle.setOnMouseEntered(e -> {
			blueRectangle.setFill(Color.CYAN);
		});
		blueRectangle.setOnMouseExited(e -> {
			blueRectangle.setFill(Color.BLUE);
		});
		return blueRectangle;
	}
	
	public Rectangle redRectangle(int height, int width) {
		Rectangle redRectangle = new Rectangle();
		redRectangle.setFill(Color.RED);
		redRectangle.setHeight(50);
		redRectangle.setWidth(50);
		redRectangle.setOnMouseClicked(e -> {
			System.out.println("Click RED!!");
		});
		redRectangle.setOnMouseEntered(e -> {
			redRectangle.setFill(Color.PINK);
		});
		redRectangle.setOnMouseExited(e -> {
			redRectangle.setFill(Color.RED);
		});
		return redRectangle;
	}
}
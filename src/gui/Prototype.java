package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

public class Prototype extends Application {
	
	private int posx = 0;
	private int posy = 0;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Rectangle rectangle = new Rectangle(15, 15);
		rectangle.setFill(Color.CYAN);
		
		GridPane gridPane = new GridPane();
		gridPane.add(rectangle, posx, posy);
		gridPane.setPadding(new Insets(5));
		gridPane.setVgap(2);
		gridPane.setHgap(2);
		
		for(int i = 0; i < 20; ++i) {
			gridPane.getColumnConstraints().add(new ColumnConstraints(15));
			gridPane.getRowConstraints().add(new RowConstraints(15));
		}
		
		for(int i = 0; i < 20; ++i) {
			for(int j = 0; j < 20; ++j) {
				if(i == 0 && j == 0) {
					continue;
				}
				gridPane.add(getPinkRectangle(), i, j);
			}
		}
		
		Scene scene = new Scene(gridPane, 348, 348);
		scene.setOnKeyPressed(e -> {
			if(e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN &&
					e.getCode() != KeyCode.LEFT && e.getCode() != KeyCode.RIGHT) {
				return;
			}
			
			gridPane.getChildren().remove(rectangle);
			gridPane.add(getPinkRectangle(), posx, posy);
			
			if(e.getCode() == KeyCode.UP) {
				posy = Math.max(posy - 1, 0);
			} else if(e.getCode() == KeyCode.DOWN) {
				posy = Math.min(posy + 1, 19);
			} else if(e.getCode() == KeyCode.LEFT) {
				posx = Math.max(posx - 1, 0);
			} else if(e.getCode() == KeyCode.RIGHT) {
				posx = Math.min(posx + 1, 19);
			}
			
			for(Node node: gridPane.getChildren()) {
				if(GridPane.getColumnIndex(node) == posx && GridPane.getRowIndex(node) == posy) {
					gridPane.getChildren().remove(node);
					break;
				}
			}
			gridPane.add(rectangle, posx, posy);
		});
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	 
	public Rectangle getPinkRectangle() {
		Rectangle pinkRect = new Rectangle(15, 15);
		pinkRect.setFill(Color.LIGHTPINK);
		return pinkRect;
	}
}

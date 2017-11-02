package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.Random;
import java.util.LinkedList;

public class SnakeGame extends Application {
	private final int TABLE_SIZE = 20;
	private final int RECT_SIZE = 15;
	private final int FOOD_SCORE = 10;
	private final int UPDATE_TIME = 100;
	
	private Scene scene;
	private VBox vbox;
	private HBox hbox;
	private StackPane stackpane;
	private GridPane gridpane;
	private Rectangle[][] table;
	private Button resetButton;
	private Text text;
	private Text promptText;
	private Pair<Integer, Integer> food;
	private Timeline timeline;
	private Rectangle promptRect;
	
	private int length;
	private int direction;	// 0 = UP | 1 = RIGHT | 2 = DOWN | 3 = LEFT
	private int score;
	private int oldDirection;
	private boolean isDead;
	private boolean isPlaying;
	private boolean hasMoved;
	private LinkedList<Pair<Integer, Integer>> trace;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		gridpane = new GridPane();
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(2);
		gridpane.setVgap(2);
		
		promptRect = new Rectangle(338, 338);
		promptRect.setFill(Color.DARKGRAY);
		promptRect.setOpacity(0.8);
		
		promptText = new Text("Press \"Enter\" to Start!");
		promptText.setFill(Color.WHITE);
		promptText.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 32));
		
		stackpane = new StackPane();
		stackpane.getChildren().addAll(gridpane, promptRect, promptText);
		
		initializeTable();
		
		resetButton = new Button("Reset");
		resetButton.setAlignment(Pos.CENTER);
		resetButton.setOnAction(e -> {
			timeline.stop();
			
			initializeTable();
			
			stackpane.getChildren().clear();
			stackpane.getChildren().addAll(gridpane, promptRect, promptText);
			
			vbox.getChildren().clear();
			vbox.getChildren().addAll(stackpane, hbox);
			
			text.setText("Score: " + score);
		});
		
		text = new Text("Score: " + score);
		
		hbox = new HBox(50);
		hbox.getChildren().addAll(resetButton, text);
		hbox.setAlignment(Pos.CENTER);

		vbox = new VBox(10);
		vbox.getChildren().addAll(stackpane, hbox);
		
		scene = new Scene(vbox, 348, 400);
		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER && !isPlaying) {
				isPlaying = true;
				stackpane.getChildren().removeAll(promptRect, promptText);
				timeline.play();
			} else if(!isDead) {
				changeDirection(event);
			}
		});
		
		timeline = new Timeline(new KeyFrame(Duration.millis(UPDATE_TIME), 
				event -> {
					move();
				}));
		timeline.setCycleCount(Animation.INDEFINITE);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
		primaryStage.setOnCloseRequest(e -> {
			primaryStage.close();
		});
		
		primaryStage.show();
	}
	
	private void initializeTable() {
		score = 0;
		oldDirection = 1;
		direction = 1;
		length = 5;
		isDead = false;
		isPlaying = false;
		
		trace = new LinkedList<Pair<Integer, Integer>>();
		
		for(int i = 0; i < length; ++i) {
			trace.addFirst(new Pair<Integer, Integer>(8, 4 + i));
		}
		
		table = new Rectangle[TABLE_SIZE][TABLE_SIZE];
		for(int i = 0; i < TABLE_SIZE; ++i) {
			for(int j = 0; j < TABLE_SIZE; ++j) {
				table[i][j] = getBackground();
			}
		}
		
		int tmp = 0;
		for(Pair<Integer, Integer> pair: trace) {
			if(tmp == 0) {
				table[pair.getKey()][pair.getValue()] = getHead();
			} else {
				table[pair.getKey()][pair.getValue()] = getBody();
			}
			++tmp;
		}
		
		for(int i = 0; i < TABLE_SIZE; ++i) {
			for(int j = 0; j < TABLE_SIZE; ++j) {
				gridpane.add(table[i][j], j, i);
			}
		}
		
		food = randomize();
	}
	
	private Rectangle getBackground() {
		Rectangle background = new Rectangle(RECT_SIZE, RECT_SIZE);
		background.setFill(Color.PINK);
		return background;
	}
	
	private Rectangle getBody() {
		Rectangle body = new Rectangle(RECT_SIZE, RECT_SIZE);
		body.setFill(Color.CYAN);
		return body;
	}
	
	private Rectangle getHead() {
		Rectangle head = new Rectangle(RECT_SIZE, RECT_SIZE);
		head.setFill(Color.BLUE);
		return head;
	}
	
	private Rectangle getFood() {
		Rectangle food = new Rectangle(RECT_SIZE, RECT_SIZE);
		food.setFill(Color.RED);
		return food;
	}
	
	private Rectangle getDeadBackground() {
		Rectangle deadbg = new Rectangle(RECT_SIZE, RECT_SIZE);
		deadbg.setFill(Color.ORANGE);
		return deadbg;
	}
	
	private void changeDirection(KeyEvent event) {
		if(event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN &&
				event.getCode() != KeyCode.LEFT && event.getCode() != KeyCode.RIGHT) {
			return;
		}
		
		int newDirection;
		if(event.getCode() == KeyCode.UP) {
			newDirection = 0;
		} else if (event.getCode() == KeyCode.RIGHT) {
			newDirection = 1;
		} else if(event.getCode() == KeyCode.DOWN) {
			newDirection = 2;
		} else {
			newDirection = 3;
		}
		
		if(!hasMoved) {
			direction = oldDirection;
		}
		
		if(/*direction == newDirection || */Math.abs(direction - newDirection) == 2) {
			return;
		}

		oldDirection = direction;
		direction = newDirection;
		hasMoved = false;
		if(!movable()) {
			direction = oldDirection;
		}
	}
	
	private boolean movable() {
		int posx = trace.getFirst().getKey();
		int posy = trace.getFirst().getValue();
		
		if(direction == 0 && posx > 0) {
			--posx;
		} else if(direction == 1 && posy < TABLE_SIZE - 1) {
			++posy;
		} else if(direction == 2 && posx < TABLE_SIZE - 1) {
			++posx;
		} else if(direction == 3 && posy > 0) {
			--posy;
		} else {
			snakeDies();
			return false;
		}
		
		if(collide(new Pair<Integer, Integer>(posx, posy))) {
			snakeDies();
			return false;
		}
		
		return true;
	}
	
	private void move() {
		int posx = trace.getFirst().getKey();
		int posy = trace.getFirst().getValue();
		
		if(!movable()) {
			return;
		}
		
		hasMoved = true;
		
		if(direction == 0) {
			--posx;
		} else if(direction == 1) {
			++posy;
		} else if(direction == 2) {
			++posx;
		} else if(direction == 3) {
			--posy;
		}
		
		boolean hasEaten = false;
		
		if(food.equals(new Pair<Integer, Integer>(posx, posy))) {
			hasEaten = true;
			++length;
			score += FOOD_SCORE;
			text.setText("Score: " + score);
		}
		
		gridpane.getChildren().remove(table[trace.getFirst().getKey()][trace.getFirst().getValue()]);
		table[trace.getFirst().getKey()][trace.getFirst().getValue()] = getBody();
		gridpane.add(table[trace.getFirst().getKey()][trace.getFirst().getValue()], 
				trace.getFirst().getValue(), trace.getFirst().getKey());
		
		if(trace.size() == length) {
			gridpane.getChildren().remove(table[trace.getLast().getKey()][trace.getLast().getValue()]);
			table[trace.getLast().getKey()][trace.getLast().getValue()] = getBackground();
			gridpane.add(table[trace.getLast().getKey()][trace.getLast().getValue()], 
					trace.getLast().getValue(), trace.getLast().getKey());
			trace.removeLast();
		}
		
		gridpane.getChildren().remove(table[posx][posy]);
		table[posx][posy] = getHead();
		trace.push(new Pair<Integer, Integer>(posx, posy));
		gridpane.add(table[posx][posy], posy, posx);
		
		if(hasEaten) {
			food = randomize();
		}
	}
	
	private boolean collide(Pair<Integer, Integer> pair) {
		for(Pair<Integer, Integer> each: trace) {
			if(pair.equals(each) && !each.equals(trace.getLast())) {
				return true;
			}
		}
		return false;
	}
	
	private Pair<Integer, Integer> randomize() {
		Random random = new Random();
		int x;
		int y;
		
		do {
			x = random.nextInt(TABLE_SIZE);
			y = random.nextInt(TABLE_SIZE);
		} while(trace.indexOf(new Pair<Integer, Integer>(x, y)) != -1);
		
		gridpane.getChildren().remove(table[x][y]);
		table[x][y] = getFood();
		gridpane.add(table[x][y], y, x);
		return new Pair<Integer, Integer>(x, y);
	}
	
	private void snakeDies() {
		isDead = true;
		timeline.stop();
		for(int i = 0; i < TABLE_SIZE; ++i) {
			for(int j = 0; j < TABLE_SIZE; ++j) {
				if(table[i][j].getFill().equals(Color.BLUE) ||
						table[i][j].getFill().equals(Color.CYAN)) {
					continue;
				} else {
					gridpane.getChildren().remove(table[i][j]);
					table[i][j] = getDeadBackground();
					gridpane.add(table[i][j], j, i);
				}
			}
		}
	}
}

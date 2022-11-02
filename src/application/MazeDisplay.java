package application;
import javafx.util.Duration;
import searches.BFS;
import searches.DFS;
import searches.Greedy;
import searches.Magic;
import searches.RandomWalk;

import java.awt.Point;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
 * @author Shannon Duvall
 * 
 * This class is the "Main" class to run - holds the Java FX application
 */

public class MazeDisplay extends Application {

	/*
	 * GUI settings
	 */
	private final int MILLISECOND_DELAY = 15;	// speed of animation
	private final int EXTRA_VERTICAL = 100; 	// GUI area allowance when making the scene width
	private final int EXTRA_HORIZONTAL = 150; 	// GUI area allowance when making the scene width
	private final int BLOCK_SIZE = 12;     		// size of each cell in pixels

	private Scene myScene;						// the container for the GUI
	private boolean paused = false;		
	private Button pauseButton;

	private Rectangle[][] mirrorMaze;	// the Rectangle objects that will get updated and drawn.  It is 
	// called "mirror" maze because there is one entry per square in 
	// the maze.

	/*
	 * Maze color settings
	 */
	private Color[] color  = new Color[] {
			Color.rgb(200,0,0),		// wall color
			Color.rgb(128,128,255),	// path color
			Color.WHITE,			// empty cell color
			Color.rgb(200,200,200)	// visited cell color
	};  		// the color of each of the states  

	MazeController mazeController;
	

	// Start of JavaFX Application
	public void start(Stage stage) {
		
		mazeController = new MazeController(this);
		
		// Initializing the gui
		myScene = setupScene();
		stage.setScene(myScene);
		stage.setTitle("aMAZEing");
		stage.show();

		// Makes the animation happen.  Will call "step" method repeatedly.
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(MILLISECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	// Create the scene - Controls and Maze areas
	private Scene setupScene () {
		// Make three container 
		Group mazeDrawing = setupMaze();
		HBox searches = setupSearchButtons();
		HBox controls = setupControlButtons();

		VBox root = new VBox();
		root.setAlignment(Pos.TOP_CENTER);
		root.setSpacing(10);
		root.setPadding(new Insets(10, 10, 10, 10));
		root.getChildren().addAll(searches,mazeDrawing,controls);

		Scene scene = new Scene(root, mazeController.getNumCols()*BLOCK_SIZE+ EXTRA_HORIZONTAL, 
				mazeController.getNumRows()*BLOCK_SIZE + EXTRA_VERTICAL, Color.ANTIQUEWHITE);

		return scene;
	}
	
	/*
	 * Setup the maze part for drawing. In particular,
	 * make the mirrorMaze.
	 */
	private Group setupMaze(){
		Group drawing = new Group();
		mirrorMaze = new Rectangle[mazeController.getNumRows()][mazeController.getNumCols()];
		for(int i = 0; i< mazeController.getNumRows(); i++){
			for(int j =0; j < mazeController.getNumCols(); j++){
				Rectangle rect = new Rectangle(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				rect.setFill(color[mazeController.getCellState(new Point(i,j))]);
				mirrorMaze[i][j] = rect;
				drawing.getChildren().add(rect);
			}	
		}
		return drawing;
	}
	
	/*
	 * Does a step in the search only if not paused.
	 */
	public void step(double elapsedTime){
		if(!paused) {
			mazeController.doOneStep(elapsedTime);
		}
	}

	private HBox setupControlButtons(){
		// Make the controls part
		HBox controls = new HBox();
		controls.setAlignment(Pos.BASELINE_CENTER);
		controls.setSpacing(10);

		Button newMazeButton = new Button("New Maze");
		newMazeButton.setOnAction(value ->  {
			mazeController.newMaze();
		});
		controls.getChildren().add(newMazeButton);

		pauseButton = new Button("Pause");
		pauseButton.setOnAction(value ->  {
			pressPause();
		});
		controls.getChildren().add(pauseButton);

		Button stepButton = new Button("Step");
		stepButton.setOnAction(value ->  {
			mazeController.doOneStep(MILLISECOND_DELAY);
		});
		controls.getChildren().add(stepButton);
		return controls;
	}

	private HBox setupSearchButtons(){
		HBox searches = new HBox();
		searches.setAlignment(Pos.BASELINE_CENTER);
		searches.setSpacing(5);

		Button dfsButton = new Button("Depth-First Search");
		dfsButton.setOnAction(value ->  {
			mazeController.startSearch("DFS");
		});
		searches.getChildren().add(dfsButton);

		Button bfsButton = new Button("Breadth-First Search");
		bfsButton.setOnAction(value ->  {
			mazeController.startSearch("BFS");
		});
		searches.getChildren().add(bfsButton);

		Button greedyButton = new Button("Greedy");
		greedyButton.setOnAction(value ->  {
			mazeController.startSearch("Greedy");
		});
		searches.getChildren().add(greedyButton);

		Button randButton = new Button("Random Walk");
		randButton.setOnAction(value ->  {
			mazeController.startSearch("RandomWalk");
		});
		searches.getChildren().add(randButton);

		Button magicButton = new Button("Magic!");
		magicButton.setOnAction(value ->  {
			mazeController.startSearch("Magic");
		});
		searches.getChildren().add(magicButton);
		return searches;
	}
	
	/*
	 * resets all the rectangle colors according to the 
	 * current state of that rectangle in the maze.  This 
	 * method assumes the display maze matches the model maze
	 */
	public void redraw(){
		for(int i = 0; i< mirrorMaze.length; i++){
			for(int j =0; j < mirrorMaze[i].length; j++){
				mirrorMaze[i][j].setFill(color[mazeController.getCellState(new Point(i,j))]);
			}
		}
	}


	/*
	 * Toggle the pause button
	 */
	public void pressPause(){
		this.paused = !this.paused;
		if(this.paused){
			pauseButton.setText("Resume");
		}
		else{
			pauseButton.setText("Pause");
		}
	}

	/*
	 * Pause the animation (regardless of current state of pause button)
	 */
	public void pauseIt(){
		this.paused = true;
		pauseButton.setText("Resume");
	}


	public static void main(String[] args) {
		launch(args);
	}
}
package application;

import java.awt.Point;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import searches.BFS;
import searches.DFS;
import searches.Greedy;
import searches.Magic;
import searches.RandomWalk;

public class MazeController {

	private final int NUM_ROWS = 31; 
	private final int NUM_COLUMNS = 41;

	/* 
	 * Logic of the program
	 */
	// The search algorithms
	private Greedy greedy;				
	private BFS bfs;
	private DFS dfs;
	private RandomWalk rand;
	private Magic magic;
	private String search = "";		// This string tells which algorithm is currently chosen.  Anything other than 
	// the implemented search class names will result in no search happening.

	// Where to start and stop the search
	private Point start;
	private Point goal;

	// The maze to search
	private Maze maze;
	private MazeDisplay mazeDisplay;

	public MazeController(MazeDisplay view) {
		// Initializing logic state
		int numRows = NUM_ROWS;
		int numColumns = NUM_COLUMNS;
		start = new Point(1,1);
		goal = new Point(numRows-2, numColumns-2);
		maze = new Maze(numRows, numColumns);
		mazeDisplay = view;
	}

	public Point getMazeDimensions() {
		return new Point(NUM_ROWS, NUM_COLUMNS);
	}

	public int getNumRows() {
		return NUM_ROWS;
	}
	
	public int getNumCols() {
		return NUM_COLUMNS;
	}

	/*
	 * Re-create the maze from scratch.
	 * When this happens, we should also stop the search.
	 */
	public void newMaze() {
		maze.createMaze(maze.getNumRows(),maze.getNumCols());
		search = "";
		mazeDisplay.redraw();
	}

	/*
	 * Does a step in the search regardless of pause status
	 */
	public void doOneStep(double elapsedTime){
		if(search.equals("DFS")) dfs.step();
		else if (search.equals("BFS")) bfs.step();
		else if (search.equals("Greedy")) greedy.step();
		else if (search.equals("RandomWalk")) rand.step();
		else if (search.equals("Magic")) magic.step();
		mazeDisplay.redraw();
	}

	public void startSearch(String searchType) {
		maze.reColorMaze();
		search = searchType;

		// Restart the search.  Since I don't know 
		// which one, I'll restart all of them.

		bfs = new BFS(maze, start, goal);	// start in upper left and end in lower right corner
		dfs = new DFS(maze, start, goal);
		greedy = new Greedy(maze, start, goal);
		rand = new RandomWalk(maze, start, goal);
		magic = new Magic(maze, start, goal);
	}


	public int getCellState(Point position) {
		return maze.get(position);
	}
}

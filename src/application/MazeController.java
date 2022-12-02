package application;

import java.awt.Point;
import searches.SearchAlgorithm;
import searches.SearchFactory;

public class MazeController {

	/* 
	 * Logic of the program
	 */
	// The search algorithms
	//private Greedy greedy;				
	//private BFS bfs;
	//private DFS dfs;
	//private RandomWalk rand;
	//private Magic magic;
	//private String search = "";		// This string tells which algorithm is currently chosen.  Anything other than 
	// the implemented search class names will result in no search happening.
	private SearchAlgorithm search;
	
	// Where to start and stop the search
	private Point start;
	private Point goal;

	// The maze to search
	private Maze maze;
	private MazeDisplay mazeDisplay;
	
	private SearchFactory searchFactory;

	public MazeController(int rows, int cols, MazeDisplay view) {
		// Initializing logic state
		int numRows = rows;
		int numColumns = cols;
		start = new Point(1,1);
		goal = new Point(numRows-2, numColumns-2);
		maze = new Maze(numRows, numColumns);
		mazeDisplay = view;
		searchFactory = new SearchFactory();
	}

	/*
	 * Re-create the maze from scratch.
	 * When this happens, we should also stop the search.
	 */
	public void newMaze() {
		maze.createMaze(maze.getNumRows(),maze.getNumCols());
		mazeDisplay.redraw();
	}

	/*
	 * Does a step in the search regardless of pause status
	 */
	public void doOneStep(double elapsedTime){
		if (search != null) {
			search.step();
		}
		mazeDisplay.redraw();
	}

	public void startSearch(String searchType) {
		maze.reColorMaze();

		// Restart the search.  Since I don't know 
		// which one, I'll restart all of them.
		search = searchFactory.makeSearch(searchType, maze, goal, goal);
	}


	public int getCellState(Point position) {
		return maze.get(position);
	}
}


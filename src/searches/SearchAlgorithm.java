package searches;

import java.awt.Point;
import java.util.Collection;
import java.util.Queue;

import application.Maze;

public abstract class SearchAlgorithm {

	private Maze maze;					// The maze being solved
	private Point goal;					// The goal Point - will let us know when search is successful
	private Collection<Point> data;		// Data structure used to keep "fringe" points
	private boolean searchOver = false;	// Is search done?
	private boolean searchResult = false;	// Was it successful?
	private Point current;				// Current point being explored

	// Search algorithm constructor
	public SearchAlgorithm(Maze mazeBlocks, Point startPoint, Point goalPoint) {
		maze = mazeBlocks;
		goal = goalPoint;
		current = startPoint;
		maze.markPath(current);
	}

	public boolean step() {
		// Don't keep computing after goal is reached or determined impossible.
		checkSearchIsOver();
		/* if(searchOver){
			colorPath();
			return searchResult;
		} */
		// Find possible next steps
		Collection<Point> neighbors = getNeighbors();
		// Choose one to be a part of the path
		Point next = chooseNeighbor(neighbors);
		// mark the next step
		if(next!=null){
			maze.markPath(next);
			recordLink(next);
		}
		// if no next step is found, mark current 
		// state "visited" and take off queue.
		else {	
			nextStateIsNull();
			/* maze.markVisited(current);
			Queue<Point> queue = (Queue<Point>) data;
			queue.remove(); */
		}
		resetCurrent();
		checkSearchOver();
		return searchResult;
	}

	public abstract boolean checkSearchIsOver();
	
	public abstract void nextStateIsNull();

}

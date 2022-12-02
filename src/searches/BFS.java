package searches;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import application.Maze;

public class BFS extends SearchAlgorithm{	

	// Keeps up with the child-parent trail so we can recreate the chosen path
	HashMap<Point,Point> childParent;	

	public BFS(Maze mazeBlocks, Point startPoint, Point goalPoint){
		super(mazeBlocks, startPoint, goalPoint);

		data = new LinkedList<>();
		data.add(startPoint);
		childParent = new HashMap<>();
	}

	@Override
	protected boolean whenSearchIsOver() {
		if (searchOver) {
			colorPath();
			return searchResult;
		}
		return searchOver;
	}

	@Override
	protected void whenNextStateNull(Point next) {
		Queue<Point> queue = (Queue<Point>) data;
		queue.remove();
	}

	/*
	 * In addition to putting the new node on the data structure, 
	 * we need to remember who the parent is.
	 */
	@Override
	protected void recordLink(Point next){	
		Queue<Point> queue = (Queue<Point>) data;
		queue.add(next);
		childParent.put(next,current);
	}

	/*
	 * The new node is the one next in the queue
	 */
	@Override
	protected void resetCurrent(){
		Queue<Point> queue = (Queue<Point>) data;
		current = queue.peek();
	}


	/*
	 * Use the trail from child to parent to color the actual chosen path
	 */
	protected void colorPath(){
		Point step = goal;
		maze.markPath(step);
		while(step!=null){
			maze.markPath(step);
			step = childParent.get(step);
		}
	}
}
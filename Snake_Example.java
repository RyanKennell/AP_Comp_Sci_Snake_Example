package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Snake_Example extends JPanel {
	
	//The pixel size of each of the squares on the map
	private final int SIZE = 20;
	
	//The dimensions of the screen
	private final static int SCREEN_WIDTH = 450;
	private final static int SCREEN_HEIGHT = 475;
	
	//Instance of Random, used to generate the apple location
	private final Random rand = new Random();
	//Instance of the snake that will slither across our program 
	private final Snake snake = new Snake();
	
	/*
	 * 25x25 int array that will be representative of our map
	 * 
	 * values chart
	 * -1  = apple at this index
	 * 0   = empty square
	 * > 0 = anything greater than zero is a snake part
	 * 
	 * The snake is represented by assigning a value to the map 
	 * for how many frames that square is to be colored. If the snake
	 * is 3 squares long, the square the snakes head travels over will be 
	 * assigned green for 3 frames. Every frame all snake parts are reduced by 
	 * one until they return to zero.
	 */
	private int[][] map = new int[25][25];
	
	//The number of apples the snake has eaten
	private int score = 0;

	/*
	 * This boolean is used to ensure only one key input
	 * is registered per frame. Since each frame ticks at
	 * 120 milliseconds, it is possible to run into yourself
	 * by entering for example, up and than right, when your snake
	 * is traveling left. After the first move is made, this boolean
	 * is set to false. Prior to any key inputs being executed, we test 
	 * to see if we have already moved in the current frame
	 */
	private boolean canMove = true;	

	/*
	 * While this variable is false, the game will continue 
	 * to run. This variable is changed by the "moveSnakeHead()"
	 * method which tests for collisions with the head and the
	 * body
	 * 
	 * @see moveSnakeHead()
	 */
	private static boolean gameover = false;
	
	/*
	 * Instantiates the Keyboard class used
	 * to take key inputs
	 */
	public Snake_Example() {
		Keyboard keyboard = new Keyboard();
		addKeyListener(keyboard);
		setFocusable(true);
	}
	
	/*
	 * This is the main method of the game
	 * 
	 * The main game loop is within this method. The
	 * purpose of this method is to execute the logic
	 * of the program from within that loop after setting
	 * up the game environment.
	 */
	public static void main(String args[]) {
		//Creating the JFrame and adding our instance as the panel
		JFrame frame = new JFrame("Snake Game");
		Snake_Example panel = new Snake_Example();
		frame.add(panel);
		
		//Setting JFrame variables
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
	    frame.setResizable(false);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    
	    //Generating the first apple on the map
	    panel.generateApple();
	    
	    /*
	     * The main game loop that will run as long 
	     * as our game is not over.
	     */
		while(!gameover) {
			//Carry out the logic of the game every frame
			panel.moveSnakeHead();
			panel.moveSnakeBody();
			panel.testAppleCollision();
			frame.repaint();
			
			/*
			 * Make sure this loop iterates once
			 * every 120 milliseconds
			 */
			try {
				Thread.sleep(120);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Handles the movement of the snake head
	 * 
	 * We take the direction the snake is moving
	 * and move the snake head appropriately. This
	 * is also where we test to see if the snake has
	 * hit a wall. If it has, we set 'gameover' to true
	 * 
	 * @see main()
	 */
	private void moveSnakeHead() {
		if(snake.isMovingLeft()) {
			snake.setHeadX(snake.getHeadX() - 1);
			if(snake.getHeadX() < 1) {
				gameover = true;
			}
		}
		
		if(snake.isMovingRight()) {
			snake.setHeadX(snake.getHeadX() + 1);
			if(snake.getHeadX() > 20) {
				gameover = true;
			}
		}
		
		if(snake.isMovingUp()) {
			snake.setHeadY(snake.getHeadY() - 1);
			if(snake.getHeadY() < 1) {
				gameover = true;
			}
		}
		
		if(snake.isMovingDown()) {
			snake.setHeadY(snake.getHeadY() + 1);
			if(snake.getHeadY() > 20) {
				gameover = true;
			}
		}
		
		/*
		 * Tests if the snake has collided with itself,
		 * if it has, the game will end
		 */
		if(map[snake.getHeadX()][snake.getHeadY()] > 0) {
			gameover = true;
		}
		
		//Denoting that the action that has been queue up has been performed
		canMove = true;
	}
	
	/*
	 * Tests to see if the snake head has collided
	 * with a apple. If it has, the score is incremented
	 * and the number of snake joints is increased by one.
	 * A new apple is then generated.
	 * 
	 * @see main()
	 */
	private void testAppleCollision() {
		if(map[snake.getHeadX()][snake.getHeadY()] == -1) {
			score ++;
			snake.setJoints(snake.getJoints() + 1);
			generateApple();
		}
	}
	
	/*
	 * Iterates through the map[][] array and
	 * lowers the number of frames a snake body
	 * part is needed to be displayed by one
	 */
	private void moveSnakeBody() {
		for(int r = 0; r < map.length; r ++) {
			for(int c = 0; c < map[r].length; c ++) {
				if(map[r][c] > 0) {
					map[r][c] --;
				}
			}
		}
		
		//Adding the new snakes position to the map
		int time = snake.getJoints();
		map[snake.getHeadX()][snake.getHeadY()] = time;
	}
	
	/*
	 * Generates a new coord for the next apple,
	 * if that coord is already occupied by a snake
	 * piece, we continue to generate a location 
	 * until we find a untaken spot for the apple
	 * 
	 * @see main()
	 * @see testAppleCollision()
	 */
	private void generateApple() {
		int appleX = rand.nextInt(SIZE) + 1;
		int appleY = rand.nextInt(SIZE) + 1;
		
		//If the first spot is taken, generate spots until one is free
		while(map[appleX][appleY] != 0) {
			appleX = rand.nextInt(SIZE) + 1;
			appleY = rand.nextInt(SIZE) + 1;
		}
		//sets the new apple on the map
		map[appleX][appleY] = -1;
	}
	
	/*
	 * The paint method, which is inherited through
	 * the Java Swing Package is used to draw all 
	 * the graphics
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
    public void paint ( Graphics g ){
    	
    	/*
    	 * the screen is painted differently if the
    	 * game is over or not, so we add a if statement
    	 * to determine what the state of our game is
    	 */
    	if(!gameover) {
    		//Drawing the black border around the screen
    		g.setColor(Color.BLACK);
        	g.fillRect(0, 0, 420, 20);
        	g.fillRect(0, 0, 20, 420);
        	g.fillRect(0, 420, 420, 30);
        	g.fillRect(420, 0, 30, 475);
        	
        	//Drawing the snake parts and the apple
        	for(int r = 0; r < map.length; r ++) {
        		for(int c = 0; c < map[r].length; c ++) {
        			//Drawing the snake
        			if(map[r][c] > 0) {
        				g.setColor(Color.GREEN);
        				g.fillRect(r * SIZE, c * SIZE, SIZE, SIZE);
        			}
        			
        			//Drawing the apple
        			if(map[r][c] == -1) {
        				g.setColor(Color.RED);
        				g.fillRect(r * SIZE, c * SIZE, SIZE, SIZE);
        			}
        		}
        	}
        	
        	//Displaying the score in the top left
        	g.setColor(Color.WHITE);
        	g.drawString("Score :: " + score, 15, 15);
    	} else {
    		
    		//Drawing the 'Game Over' Screen
    		g.setColor(Color.WHITE);
    		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    		
    		g.setColor(Color.RED);
    		g.setFont(new Font("Arial", 50, 50));
    		g.drawString("GAME OVER!", 75 , SCREEN_HEIGHT / 3);
    		
    		g.setFont(new Font("Arial", 25, 25));
    		g.drawString("Score: " + score, 175, SCREEN_HEIGHT / 2);
    	}
    }
    
    /*
     * The keyboard class retrieves input from the 
     * player. This class is a inner class to 'Snake_Example'
     * so it can have access to the Snake instance
     */
    public class Keyboard implements KeyListener {

    	@Override
    	public void keyPressed(KeyEvent e) {
    		
    		/*
    		 * Used to make sure no more than one action
    		 * is registered per frame
    		 */
    		if(!canMove) {
    			return;
    		}
    		
    		//Handling the pressing of 'w'
    		if( KeyEvent.getKeyText(e.getKeyCode()).equals("W")){
    			if(!snake.isMovingDown()) {
    				snake.reset_direction();
        			snake.setMovingUp(true);
        			canMove = false;
    			}
    		}
    		
    		//Handling the pressing of 's'
    		else if ( KeyEvent.getKeyText(e.getKeyCode()).equals("S")){
    			if(!snake.isMovingUp()) {
    				snake.reset_direction();
        			snake.setMovingDown(true);
        			canMove = false;
    			}
    		}
    		
    		//Handling the pressing of 'a'
    		else if( KeyEvent.getKeyText(e.getKeyCode()).equals("A")){
    			if(!snake.isMovingRight()) {
    				snake.reset_direction();
        			snake.setMovingLeft(true);
        			canMove = false;
    			}
    		}
    		
    		//Handling the pressing of 'd'
    		else if ( KeyEvent.getKeyText(e.getKeyCode()).equals("D")){
    			if(!snake.isMovingLeft()) {
    				snake.reset_direction();
        			snake.setMovingRight(true);
        			canMove = false;
    			}
    		}
    	}
    	
    	@Override
    	public void keyReleased(KeyEvent arg0) {
    	}
    	
    	@Override
    	public void keyTyped(KeyEvent e) {
    	}
    	
    }
}

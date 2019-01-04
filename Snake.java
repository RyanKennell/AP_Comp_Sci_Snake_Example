
public class Snake {
	
	private int headX = 10;
	private int headY = 10;
	
	private boolean isMovingLeft = true;
	private boolean isMovingRight = false;
	private boolean isMovingUp = false;
	private boolean isMovingDown = false;
	
	private int joints = 3;
	
	public void reset_direction() {
		isMovingLeft = false;
		isMovingRight = false;
		isMovingUp = false;
		isMovingDown = false;
	}
	
	public int getHeadX() {
		return headX;
	}

	public void setHeadX(int headX) {
		this.headX = headX;
	}

	public int getHeadY() {
		return headY;
	}

	public void setHeadY(int headY) {
		this.headY = headY;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public void setMovingLeft(boolean isMovingLeft) {
		this.isMovingLeft = isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public void setMovingRight(boolean isMovingRight) {
		this.isMovingRight = isMovingRight;
	}

	public boolean isMovingUp() {
		return isMovingUp;
	}

	public void setMovingUp(boolean isMovingUp) {
		this.isMovingUp = isMovingUp;
	}

	public boolean isMovingDown() {
		return isMovingDown;
	}

	public void setMovingDown(boolean isMovingDown) {
		this.isMovingDown = isMovingDown;
	}

	public int getJoints() {
		return joints;
	}

	public void setJoints(int joints) {
		this.joints = joints;
	}
}

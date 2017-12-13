package map;

public class Wall extends Square {

	public Wall(int row, int col, Level level){
		super(row,col, level);
	}

	@Override
	public Boolean isPassable() {
		return false;
	}

}

package map;

public class Field extends Square {

    public Field(int row, int col, Level level){
        super(row,col, level);
    }

	@Override
	public Boolean isPassable() {
		return true;
	}
}

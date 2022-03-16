package io.runon.trading.strategy;
/**
 * @author macle
 */
public class PositionIndex {
    private final Position position;
    private final int index;


    public PositionIndex(Position position, int index){
        this.position = position;
        this.index = index;
    }

    public Position getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }
}

package main.jcells.engine.parser.expression;

public class CellIndex
{
    public int x,y;

    @Override
    public String toString()
    {
        return "("+x+","+y+")";
    }
}

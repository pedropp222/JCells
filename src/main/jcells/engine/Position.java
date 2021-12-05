package main.jcells.engine;

public class Position
{
    private int x = 0;
    private int y = 0;

    public void AddressToPosition(String address)
    {
        char column = address.substring(0,1).toCharArray()[0];

        x = column-65; //convert A to 0, B to 1, etc.
        y = Integer.parseInt(address.substring(1))-1;
    }

    public Position(String address)
    {
        AddressToPosition(address);
    }

    public Position(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }

    @Override
    public String toString()
    {
        return "("+x+","+y+")";
    }

    public Position(){};

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}

package main.jcells.engine;

public class Cell
{
    private CellType type;

    private Position position;
    private String cellContent;
    private CellExpr asExpr;

    public Cell(CellType t, String content, Position p)
    {
        type = t;
        cellContent = content;
        position = p;
        asExpr = new CellExpr();
    }

    public Cell(CellType t, Position p)
    {
        type = t;
        cellContent = "";
        position = p;
        asExpr = new CellExpr();
    }

    public Cell(Position p)
    {
        type = CellType.TEXT;
        cellContent = "";
        position = p;
        asExpr = new CellExpr();
    }

    public CellExpr getExpression()
    {
        if (type == CellType.EXPR)
        {
            return asExpr;
        }
        return null;
    }

    public String getCellContent()
    {
        return cellContent;
    }

    public CellType GetType()
    {
        return type;
    }

    public int getCellContentParsed()
    {
        if (type != CellType.NUMBER)
        {
            System.out.println("Cell ("+position.getX()+","+position.getY()+") is not a number.");
            return  0;
        }
        return Integer.parseInt(cellContent);
    }

    public void setCellContent(String content)
    {
        String cnt = content.replace("\"","");

        char[] a = cnt.toCharArray();

        boolean number = true;

        for (char c : a)
        {
            if (!Character.isDigit(c))
            {
                number = false;
                break;
            }
        }

        if (a.length == 0)
        {
            number=false;
        }

        if (number)
        {
            int v = Integer.parseInt(cnt);
            cellContent = Integer.toString(v);
            type = CellType.NUMBER;
        }
        else
        {
            if (cnt.startsWith("="))
            {
                type = CellType.EXPR;
                cellContent = cnt;
            } else
            {
                type = CellType.TEXT;
                cellContent = content;
            }
        }
    }



    public Position getPosition()
    {
        return position;
    }
}
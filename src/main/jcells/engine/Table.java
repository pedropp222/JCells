package main.jcells.engine;

import main.jcells.engine.parser.Parser;
import main.jcells.engine.parser.expression.ExprStatus;
import main.jcells.engine.parser.expression.Expression;

import java.util.ArrayList;

public class Table
{
    ArrayList<TableRow> rows;

    public Table()
    {
        rows = new ArrayList<>();
    }

    public TableRow AddRow()
    {
        TableRow a = new TableRow();
        rows.add(a);
        return a;
    }

    public boolean RemoveRow(int index)
    {
        if (index >= rows.size()) return false;

        rows.remove(index);
        return true;
    }

    public Cell GetCell(Position pos)
    {
        if (!PositionExists(pos))
        {
            System.out.println("Position does not exist on table");
            return null;
        }

        return rows.get(pos.getY()).cells.stream()
                .filter(p -> p.getPosition().getX() == pos.getX())
                .findFirst()
                .orElse(null);
    }

    public int InitializeExprs()
    {
        int num = 0;

        for(TableRow tr : rows)
        {
            for (Cell c : tr.cells)
            {
                String x = c.getCellContent();

                if (c.GetType() == CellType.EXPR)
                {
                    GenerateExpression(c.getPosition());
                    num++;
                }
            }
        }

        return  num;
    }

    String GetLineNumber(int l)
    {
        if (l == 0)
        {
            return " "+" "+(" ").repeat(rows.size()-String.valueOf(l).length());
        }
        return " "+l+(" ").repeat(rows.size()-String.valueOf(l).length());
    }

    public void PrintTable(boolean expr)
    {
        int charWidth = 0;
        int maxCells = 0;

        //Normalize cell widths
        for(TableRow tr : rows)
        {
            if(tr.cells.size()>maxCells) maxCells = tr.cells.size();
            for(Cell c : tr.cells)
            {
                int l = (" "+c.getCellContent()+"|").length();
                if (l > charWidth) charWidth=l;
            }
        }

        String space = " ";
        for(int i = 0; i < maxCells; i++)
        {

            String c;
            if (i == 0)
            {
                c = GetLineNumber(0)+"|"+Character.toString('A'+i)+space.repeat(charWidth-2);
            }
            else
            {
                c = "|"+Character.toString('A'+i)+space.repeat(charWidth-1);
            }

            if (i==maxCells-1) c+="|";
            System.out.print(c);
        }
        System.out.println();

        int cellN = 0;
        int rowN = 0;

        for(TableRow tr : rows)
        {
            cellN = 0;
            for(Cell c : tr.cells)
            {
                String x;

                if (cellN == 0)
                {
                    System.out.print(GetLineNumber(rowN+1));
                   x = "| "+c.getCellContent();
                }
                else
                {
                    x = " "+c.getCellContent();
                }

                if (c.GetType()==CellType.EXPR&&expr)
                {
                    EvaluateCell(c);
                    if (cellN==0)
                    {
                        x = "| " + c.getExpression().currentExpr.as.integerValue;
                    }
                    else
                    {
                        x = " " + c.getExpression().currentExpr.as.integerValue;
                    }
                    if (c.getExpression().status == ExprStatus.ERROR)
                    {
                        x = "| <ERROR>";
                    }
                }

                if (x.length() < charWidth)
                {
                    x+= space.repeat((charWidth)-x.length());
                }

                x+="|";
                System.out.print(x);
                cellN++;
            }
            System.out.print("\n");
            rowN++;
        }
    }

    public int EvaluateExpression(Expression expr)
    {
        switch (expr.type)
        {
            case NUMBER:
                return expr.as.integerValue;
            case CELL:
                Position x = new Position(expr.as.cellValue.x,expr.as.cellValue.y);
                Cell wc = GetCell(x);
                if (wc!=null)
                {
                    switch (wc.GetType())
                    {
                        case NUMBER:
                            return wc.getCellContentParsed();
                        case TEXT:
                            System.out.println("Error: can't use text cells in expression");
                            return -1;
                        case EXPR:

                            if (wc.getExpression().status == ExprStatus.EVALUATED)
                            {
                                return wc.getExpression().currentExpr.as.integerValue;
                            }
                            else
                            {
                                GenerateExpression(x);
                                EvaluateCell(wc);

                                return wc.getExpression().currentExpr.as.integerValue;
                            }

                    }
                }
            case ADD:
                int lhs = EvaluateExpression(expr.lhs);
                int rhs = EvaluateExpression(expr.rhs);
                return lhs+rhs;
        }

        return -1;
    }


    public void EvaluateCell(Cell x)
    {
        //System.out.println("Evaluating cell "+x.getPosition().toString());
        if (x.GetType() == CellType.EXPR)
        {
            if(x.getExpression().status == ExprStatus.UNEVALUATED)
            {
                x.getExpression().status = ExprStatus.ONGOING;
                x.getExpression().currentExpr.as.integerValue = EvaluateExpression(x.getExpression().currentExpr);
                x.getExpression().status = ExprStatus.EVALUATED;
            }
            else if(x.getExpression().status == ExprStatus.ONGOING)
            {
                System.out.println("Error: circular dependency detected");
                x.getExpression().status = ExprStatus.ERROR;
            }
        }
    }

    public boolean GenerateExpression(Position pos)
    {
        Cell c = GetCell(pos);

        if (c==null){
            System.out.println("Could not find cell at "+pos.toString());
            return false;
        }

        if (c.GetType()!=CellType.EXPR)
        {
            System.out.println("Error: cell at "+pos.toString()+" is not expression");
            return false;
        }

        Parser p = new Parser();

        boolean s = p.ParseExpression(this,c);

        //c.getExpression().currentExpr.as.integerValue = EvaluateExpression(c.getExpression().currentExpr);
        return true;
    }

    public void ChangeCellValue(Position p, String cont)
    {
        if (!PositionExists(p))
        {
            System.out.println("Position does not exist");
            return;
        }

        Cell x = GetCell(p);
        if(x!=null)
        {
            x.setCellContent(cont);
        }
    }

    public boolean PositionExists(Position p)
    {
        return p.getY() <= rows.size()-1 && p.getX() <= rows.get(p.getY()).cells.size()-1;
    }
}
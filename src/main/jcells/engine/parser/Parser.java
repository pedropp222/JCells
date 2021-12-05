package main.jcells.engine.parser;

import main.jcells.engine.Cell;
import main.jcells.engine.Position;
import main.jcells.engine.Table;
import main.jcells.engine.parser.expression.ExprType;
import main.jcells.engine.parser.expression.Expression;

public class Parser
{
    Tokenizer tokenizer;
    Table currentTable;
    Cell currentCell;

    public boolean ParseExpression(Table table, Cell cell)
    {
        currentTable = table;
        currentCell = cell;

        tokenizer = new Tokenizer();

        if (!tokenizer.ParseTokens(currentCell.getCellContent()))
        {
            System.out.println("Error parsing expression");
        }
        else
        {
            Expression p = ParsePlus();

            if (p!=null)
            {
                currentCell.getExpression().currentExpr = p;
            }
            else
            {
                return false;
            }

            //DumpExpression(p,0);

            //System.out.println("cell "+cell.getCellContent()+" = ");
            return true;
        }
        return false;
    }

    void DumpExpression(Expression exp, int level)
    {
        for(int i = 0; i < level; i++)
        {
            System.out.print("\t");
        }
        switch (exp.type)
        {
            case NUMBER:
                System.out.print("Number: "+exp.as.integerValue+"\n");
                break;
            case CELL:
                System.out.print("Cell "+exp.as.cellValue.toString()+"\n");
                break;
            case ADD:
                System.out.print("PLUS: \n");
                DumpExpression(exp.lhs,level+1);
                DumpExpression(exp.rhs,level+1);
                break;
        }
    }

    Expression ParsePrimary()
    {
        Token n = tokenizer.NextToken();
        //System.out.println("parsing "+n.value);

        Expression exp = new Expression();

        if (n.type == TokenType.INTEGER)
        {
            //System.out.println(n.value+" is integer");
            exp.as.integerValue = Integer.parseInt(n.value);
            exp.type = ExprType.NUMBER;
            return exp;
        }
        else if (n.type == TokenType.ID)
        {
            Position p = new Position();
            p.AddressToPosition(n.value);
            //System.out.println(n.value+" is cell");
            exp.as.cellValue.x = p.getX();
            exp.as.cellValue.y = p.getY();
            exp.type = ExprType.CELL;
            return exp;
        }
        else
        {
            return null;
        }
    }

    Expression ParsePlus()
    {
        Expression lhs = ParsePrimary();

        if(lhs==null)
        {
            System.out.println("Error: expected lhs expression but found null");
            return null;
        }

        Token n = tokenizer.NextToken();

        if (n==null)
        {
            return lhs;
        }

        //System.out.println("Next token is "+n.value);

        if(n.type == TokenType.OPERATOR)
        {
            Expression rhs = ParsePlus();
            Expression exp = new Expression();
            exp.type = ExprType.ADD;
            exp.lhs = lhs;
            exp.rhs = rhs;
            return exp;
        }
        else
        {
            return lhs;
        }
    }
}

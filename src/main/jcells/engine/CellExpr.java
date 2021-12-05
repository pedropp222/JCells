package main.jcells.engine;

import main.jcells.engine.parser.expression.ExprStatus;
import main.jcells.engine.parser.expression.Expression;

public class CellExpr
{
    public Expression currentExpr;
    public ExprStatus status;

    public CellExpr()
    {
        status = ExprStatus.UNEVALUATED;
        currentExpr = new Expression();
    }
}

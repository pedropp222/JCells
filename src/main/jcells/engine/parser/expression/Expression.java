package main.jcells.engine.parser.expression;

public class Expression
{
    public Expression lhs, rhs;
    public ExprType type;
    public ExpressionValue as;

    public Expression()
    {
        as = new ExpressionValue();
    }
}
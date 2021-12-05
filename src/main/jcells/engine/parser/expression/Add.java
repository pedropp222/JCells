package main.jcells.engine.parser.expression;

public class Add extends Expression
{
    public Add(Expression l, Expression r)
    {
        lhs = l;
        rhs = r;
    }
}


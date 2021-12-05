package main.jcells.engine.parser;

public class Token
{
    public String value;
    public TokenType type;

    public Token(String s, TokenType t)
    {
        value = s;
        type = t;
    }
}



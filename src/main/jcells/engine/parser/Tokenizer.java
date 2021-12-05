package main.jcells.engine.parser;

import java.util.ArrayList;

public class Tokenizer
{
    ArrayList<Token> tokens =  new ArrayList<>();
    int currentToken = 0;


    public boolean ParseTokens(String cont)
    {
        char[] input = cont.toCharArray();

        String currentToken = "";

        for(int i = 0; i< input.length; i++)
        {
            if(Character.isWhitespace(input[i])||input[i]=='=') continue;

            if (input[i]=='+'||input[i]=='-')
            {
                if (currentToken.length()!=0)
                {
                    if (EvaluateToken(currentToken))
                    {
                        tokens.add(new Token(currentToken, TryParseInt(currentToken) ? TokenType.INTEGER : TokenType.ID));
                        currentToken = "";
                    }
                    else
                    {
                        return false;
                    }
                }
                tokens.add(new Token(Character.toString(input[i]),TokenType.OPERATOR));
                continue;
            }

            if (Character.isAlphabetic(input[i]))
            {
                currentToken += input[i];
            }
            else if (Character.isDigit(input[i]))
            {
                currentToken += input[i];
            }
            else
            {
                System.out.println("Error: unknown character "+input[i]+" in tokenizer");
            }
        }

        if (currentToken.length()!=0)
        {
            if (EvaluateToken(currentToken))
            {
                tokens.add(new Token(currentToken, TryParseInt(currentToken) ? TokenType.INTEGER : TokenType.ID));
                currentToken = "";
            }
            else
            {
                return false;
            }
        }

        /*System.out.println(tokens.size()+" tokens in expression");
        for(Token a : tokens)
        {
            System.out.print(a.value+" ");
        }*/

        return true;
    }

    public boolean TryParseInt(String value)
    {
        try
        {
            int a = Integer.parseInt(value);
        } catch (NumberFormatException e)
        {
            return false;
        }

        return true;
    }

    public boolean EvaluateToken(String value)
    {
        if (TryParseInt(value)) return true;

        char[] v = value.toCharArray();

        if (TryParseInt(String.valueOf(v[0])))
        {
            System.out.println("Error: invalid token starts with number and contains other characters after");
            return false;
        }
        else
        {
            return true;
        }
    }

    public Token NextToken()
    {
        if (currentToken != tokens.size())
        {
            Token t = tokens.get(currentToken);
            ScanToken();
            return t;
        }
        else
        {
            return null;
        }
    }

    public void ScanToken()
    {
        currentToken++;
    }

}

package main.jcells.engine.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalUtils
{
    public static List<String> GetParams(String input)
    {
        List<String> params = new ArrayList<String>();

        Pattern pattern = Pattern.compile(" [A-Z][0-9]+| -?[0-9]+| [a-zA-Z0-9 ]*");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find())
        {
            params.add(matcher.group(0).substring(1));
        }

        return  params;
    }
}

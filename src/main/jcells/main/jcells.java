package main.jcells.main;

import main.jcells.engine.*;
import main.jcells.engine.utils.TerminalUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class jcells
{
    public static final String version = "0.0.1";

    static Table mainTable;
    static File mainFile;

    public static void main(String[] args) throws FileNotFoundException
    {
        mainTable = new Table();

        if (args == null || args.length == 0|| args[0].length() == 0)
        {
            System.out.println("USAGE: minicel file_path");
        }
        else
        {
            System.out.println("Minicel "+version);
            System.out.println("Starting minicel on path "+args[0]);

            mainFile = new File(args[0]);

            if(!mainFile.exists())
            {
                System.out.println("File "+mainFile.getAbsolutePath()+" does not exist.");
            }
            else
            {
                PopulateTable();
                programLoop();
            }
        }
    }

    public static void PopulateTable() throws FileNotFoundException
    {
        Scanner sc = new Scanner(mainFile);

        int rowNum = 0;
        int cellNum = 0;

        while(sc.hasNextLine())
        {
            String line = sc.nextLine();

            TableRow row = mainTable.AddRow();

            String[] cells = line.split(",");
            if (cells.length > cellNum)
            {
                cellNum = cells.length;
            }
            for(int i = 0; i < cells.length; i++)
            {
                Cell c = new Cell(new Position(i,rowNum));

                c.setCellContent(cells[i]);

                row.cells.add(c);
            }

            rowNum++;
        }

        int n  = mainTable.InitializeExprs();

        System.out.println("Loaded table "+rowNum+"*"+cellNum);
        System.out.println("Number of expressions: "+n);
    }

    public static void programLoop()
    {
        Scanner sc = new Scanner(System.in);

        boolean running = true;

        while (running)
        {
            System.out.print("Operation: ");

            String op = sc.nextLine();

            switch (op)
            {
                case "quit":
                    running = false;
                    return;
                case "print":
                    mainTable.PrintTable(false);
                    break;
                case "printe":
                    mainTable.PrintTable(true);
                    break;
            }

            if (op.startsWith("cv"))
            {
                List<String> params = new ArrayList<String>();

                params = TerminalUtils.GetParams(op);

                if (params.size()<2)
                {
                    System.out.println("Usage: cv x y content or cv coordinate content");
                }

                if (params.size() == 3)
                {
                    mainTable.ChangeCellValue(new Position(Integer.parseInt(params.get(0)),Integer.parseInt(params.get(1))),params.get(2));
                }
                else if (params.size() == 2)
                {
                    mainTable.ChangeCellValue(new Position(params.get(0)), params.get(1));
                }
                else
                {
                    System.out.println("Error: too many params?");
                }
            }

            if (op.startsWith("expr"))
            {
                List<String> params;

                params = TerminalUtils.GetParams(op);

                if (params.size()<1)
                {
                    System.out.println("Usage: expr coordinate");
                }
                else
                {
                    Position p = new Position(params.get(0));
                    mainTable.EvaluateCell(mainTable.GetCell(p));
                    System.out.println("VALUE IS: "+mainTable.GetCell(p).getExpression().currentExpr.as.integerValue);
                }
            }

        }
    }


}

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    enum CommandType {
        C_ARITHMETIC,
        C_PUSH,
        C_POP,
        C_LABEL,
        C_GOTO,
        C_IF,
        C_FUNCTION,
        C_RETURN,
        C_CALL
    }

    private ArrayList<String> commands;
    private List<String> arithmetics;
    private int pointer;
    private String currentCommand;

    public Parser(String inFilePath) {
        commands = new ArrayList<String>();
        arithmetics = Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");
        pointer = 0;

        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(inFilePath));
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.indexOf("//" ) == 0) continue;
                String[] splitLine = line.split("//");
                commands.add(splitLine[0]);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Boolean hasMoreCommands() {
        return pointer < commands.size();
    }

    public void advance() {
        currentCommand = commands.get(pointer++);
    }

    public CommandType commandType() {
        if (arithmetics.contains(currentCommand)) {
            return CommandType.C_ARITHMETIC;
        } else if (currentCommand.startsWith("push")) {
            return CommandType.C_PUSH;
        } else if (currentCommand.startsWith("pop")) {
            return CommandType.C_POP;
        }
        return null;
    }

    public String arg1() {
        if (commandType() == CommandType.C_ARITHMETIC) {
            return currentCommand;
        }
        return currentCommand.split(" ")[1];
    }

    public int arg2() {
        return Integer.parseInt(currentCommand.split(" ")[2]);
    }

}

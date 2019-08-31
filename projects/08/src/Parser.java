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

    private ArrayList<String> commands = new ArrayList<>();
    private List<String> arithmetics;
    private int pointer;
    private String currentCommand;

    public Parser(File inFile) {
        arithmetics = Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");
        pointer = 0;

        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(inFile));
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.indexOf("//" ) == 0) continue;
                String[] splitLine = line.split("//");
                commands.add(splitLine[0].trim());
            }
            in.close();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public boolean hasMoreCommands() {
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
        } else if (currentCommand.startsWith("label")) {
            return CommandType.C_LABEL;
        } else if (currentCommand.startsWith("goto")) {
            return CommandType.C_GOTO;
        } else if (currentCommand.startsWith("if-goto")) {
            return CommandType.C_IF;
        } else if (currentCommand.startsWith("function")) {
            return CommandType.C_FUNCTION;
        } else if (currentCommand.startsWith("return")) {
            return CommandType.C_RETURN;
        } else if (currentCommand.startsWith("call")) {
            return CommandType.C_CALL;
        }
        System.out.println("ERROR: Comando Desconocido " + currentCommand);
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

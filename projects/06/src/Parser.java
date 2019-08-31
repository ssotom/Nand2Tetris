import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private ArrayList<String> instructions = new ArrayList<String>();
    int pointer = 0;
    String currentInstruction;
    enum CommandType {A_COMMAND, C_COMMAND, L_COMMAND}

    public Parser(String filePath) {
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            while ((line = in.readLine()) != null) {
                line = line.replaceAll(" ", "");
                if (line.equals("") || line.indexOf("//") == 0) continue;
                line = line.split("//")[0];
                instructions.add(line);
            }
            in.close();
        } catch(IOException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public boolean hasMoreCommands() {
        return pointer < instructions.size();
    }

    public void advance() {
        currentInstruction = instructions.get(pointer++);
    }

    public CommandType commandType() {
        if (currentInstruction.startsWith("@")) {
            return CommandType.A_COMMAND;
        } else if (currentInstruction.startsWith("(") && currentInstruction.endsWith(")")) {
            pointer--;
            instructions.remove(pointer);
            return CommandType.L_COMMAND;
        } else {
            return CommandType.C_COMMAND;
        }
    }

    public String symbol() {
        return currentInstruction.substring(1).replace(")","");
    }

    public String dest() throws IOException {
        if(!currentInstruction.contains("="))
            return "";
        String instruction = currentInstruction.split("=")[0];
        if(instruction.contains("A") || instruction.contains("D") || instruction.contains("M"))
            return instruction;
        throw new IOException("Comando "+ pointer);
    }

    public String comp() throws IOException {
        if (currentInstruction.contains("=") && currentInstruction.contains(";")) {
            int i = currentInstruction.indexOf(";");
            int f = currentInstruction.indexOf("=");
            return currentInstruction.substring(i + 1, f);
        }else if (currentInstruction.contains("=")) {
            return currentInstruction.split("=")[1];
        } else if (currentInstruction.contains(";")){
            return currentInstruction.split(";")[0];
        }
        throw new IOException("Comando "+ pointer);
    }

    public String jump() {
        if (currentInstruction.contains(";"))
            return currentInstruction.split(";")[1];
        return "";
    }

    public  int getPointer() {
        return pointer;
    }

    public void reset() {
        pointer = 0;
    }

}

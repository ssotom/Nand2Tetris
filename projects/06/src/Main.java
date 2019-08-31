import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String args[]) {
        if (args.length > 0) {
            Parser parser = new Parser(args[0]);
            SymbolTable symbolTable = new SymbolTable();
            Code code = new Code();

            while (parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType() == Parser.CommandType.L_COMMAND)
                    symbolTable.addEntry(parser.symbol(), parser.getPointer());
            }
            parser.reset();
            try {   
                BufferedWriter out = new BufferedWriter(new FileWriter(args[0].replace(".asm", ".hack")));
                while (parser.hasMoreCommands()) {
                    parser.advance();
                    switch (parser.commandType()) {
                        case A_COMMAND:
                            String AInstruction = parser.symbol();
                            char c = AInstruction.charAt(0);
                            if (Character.isLetter(c) || c == ':' || c == '_' || c == '$' || c == '.') {
                                if (!symbolTable.contains(AInstruction)) symbolTable.addEntry(AInstruction);
                                AInstruction = getBinary(symbolTable.getAddress(AInstruction));
                            }else {
                                AInstruction = getBinary(Integer.parseInt(AInstruction));
                            }
                            out.write(AInstruction);
                            break;
                        case C_COMMAND:
                            String dest = code.dest(parser.dest());
                            String comp = code.comp(parser.comp());
                            String jump = code.jump(parser.jump());
                            out.write("111" + comp + dest + jump);
                            break;
                    }
                    out.newLine();
                    
                }
                out.close();
            }catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }else{
            System.out.println("ERROR: No hay archivo de entrada");
        }
        return;
    }

    public static String getBinary(int num) {
        String binary = Integer.toBinaryString(num);
        while(binary.length() < 16) {
            binary = 0 + binary;
        }
        return binary;
    }

}

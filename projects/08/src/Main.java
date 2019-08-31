import java.io.File;

public class Main {
    private static void traslator(File _file, CodeWriter c) {
        Parser p = new Parser(_file);
        c.setFileName(_file);

        while (p.hasMoreCommands()) {
            p.advance();
            switch (p.commandType()) {
                case C_ARITHMETIC:
                    c.writeArithmetic(p.arg1());
                    break;
                case C_PUSH:
                    c.writePush(p.arg1(), p.arg2());
                    break;
                case C_POP:
                    c.writePop(p.arg1(), p.arg2());
                    break;
                case C_LABEL:
                    c.writeLabel(p.arg1());
                    break;
                case C_GOTO:
                    c.writeGoto(p.arg1());
                    break;
                case C_IF:
                    c.writeIf(p.arg1());
                    break;
                case C_CALL:
                    c.writeCall(p.arg1(), p.arg2());
                    break;
                case C_RETURN:
                    c.writeReturn();
                    break;
                case C_FUNCTION:
                    c.writeFunction(p.arg1(), p.arg2());
                    break;
                default:
                    System.out.println("ERROR: en archivo " + _file);
            }
        }
    }

    public static void main(String[] args) {
    	if(args.length == 1) {
    		String fileName =  args[0];
            File file = new File(fileName);
            File[] files;
            String outputName;
    
            if (file.isFile() && file.getName().contains(".vm")) {
                files = new File[1];
                files[0] = file;
                outputName = fileName.split("[.]")[0] + ".asm";
            } else if (file.isDirectory()) {
                outputName = file.getPath() + "/" + file.getName() + ".asm";
                files = file.listFiles();
            } else {
                System.out.println("ERROR: En el archivo de entrada");
                return;
            }
    
            CodeWriter c = new CodeWriter(outputName);
    
            for (short i = 0; i < files.length; i++) {
                String _file = files[i].getName();
                if (_file.equals("Sys.vm")) {
                    traslator(files[i], c);
                    files[i] = new File("");
                }
            }
    
            for (File _file : files) {
                if (_file.getName().contains(".vm")) traslator(_file, c);
            }
            c.close();
        } else {
                System.out.println("ERROR: En los argumentos");
        }
    }
        
}

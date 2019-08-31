public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            Parser p = new Parser(args[0]);
            CodeWriter c = new CodeWriter(args[0]);
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
                    default:
                        System.out.println("ERROR: en archivo " + args[0]);
                }
            }
            c.close();
        }else {
            System.out.println("ERROR: No hay archivo de entrada");
        }
    }
}

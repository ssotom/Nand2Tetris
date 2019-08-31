import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    private BufferedWriter out;
    private short labelCount;
    private short returnCount;
    private String fileName;
    private String functionName;

    public CodeWriter(String outFilePath) {
        try {
            File file = new File(outFilePath.replace(".vm", ".asm"));
            out = new BufferedWriter(new FileWriter(file));
            labelCount = 0;
            fileName = file.getName();
        } catch (IOException e) { System.out.println("ERROR: " + e.getMessage()); }
    }

    public void setFileName(File file) {
        fileName = file.getName();
        functionName = fileName;
        if (fileName.equals("Sys.vm")) { writeInit(); }
    }

    public void writeInit() {
        write("@256");
        write("D=A");
        write("@SP");
        write("M=D");
        writeCall("Sys.init" ,0);
    }

    public void writeArithmetic(String command) {
        switch (command) {
            case "add":
                binaryOperation1("+");
                break;
            case "sub":
                binaryOperation1("-");
                break;
            case "neg":
                write("@SP");
                write("AM=M-1");
                write("M=-M");
                write("@SP");
                write("M=M+1");
                break;
            case "eq":
                binaryOperation1("-");
                binaryOperation2("JEQ");
                break;
            case "gt":
                binaryOperation1("-");
                binaryOperation2("JGT");
                break;
            case "lt":
                binaryOperation1("-");
                binaryOperation2("JLT");
                break;
            case "and":
                binaryOperation1("&");
                break;
            case "or":
                binaryOperation1("|");
                break;
            case "not":
                write("@SP");
                write("AM=M-1");
                write("M=!M");
                write("@SP");
                write("M=M+1");
                break;
            default:
                System.out.println("ERROR: Arithmetic " + command);
        }
    }

    public void writePush(String segment, int index) {
        switch (segment) {
            case "constant":
                write("@" + index);
                write("D=A");
                write("@SP");
                write("A=M");
                write("M=D");
                write("@SP");
                write("M=M+1");
                break;
            case "local":
                push("LCL", index);
                break;
            case "argument":
                push("ARG", index);
                break;
            case "this":
                push("THIS", index);
                break;
            case "that":
                push("THAT", index);
                break;
            case "temp":
                write("@R" + (index+5));
                write("D=M");
                write("@SP");
                write("A=M");
                write("M=D");
                write("@SP");
                write("M=M+1");
                break;
            case "pointer":
                if (index == 0) write("@THIS");
                else write("@THAT");
                write("D=M");
                write("@SP");
                write("A=M");
                write("M=D");
                write("@SP");
                write("M=M+1");
                break;
            case "static":
                write("@" + fileName + index);
                write("D=M");
                write("@SP");
                write("A=M");
                write("M=D");
                write("@SP");
                write("M=M+1");
                break;
            default:
                System.out.println("ERROR: PUSH " + segment + " " + index);
        }
    }

    public void writePop(String segment, int index) {
        switch (segment) {
            case "local":
                pop("LCL", index);
                break;
            case "argument":
                pop("ARG", index);
                break;
            case "this":
                pop("THIS", index);
                break;
            case "that":
                pop("THAT", index);
                break;
            case "temp":
                write("@SP");
                write("M=M-1");
                write("A=M");
                write("D=M");
                write("@R" + (index+5));
                write("M=D");
                break;
            case "pointer":
                write("@SP");
                write("M=M-1");
                write("A=M");
                write("D=M");
                if (index == 0) write("@THIS");
                else write("@THAT");
                write("M=D");
                break;
            case "static":
                write("@SP");
                write("M=M-1");
                write("A=M");
                write("D=M");
                write("@" + fileName + index);
                write("M=D");
                break;
            default:
                System.out.println("ERROR: POP " + segment + " " + index);
        }
    }

    private void binaryOperation1(String arithmetic) {
        write("@SP");
        write("AM=M-1");
        write("D=M");
        write("A=A-1");
        write("M=M" + arithmetic + "D");
    }

    private void binaryOperation2(String arithmetic) {
        write("D=M");
        write("@SI" + labelCount);
        write("D;" + arithmetic);
        write("@SP");
        write("A=M-1");
        write("M=0");
        write("@END" + labelCount);
        write("0;JMP");
        write("(SI" + labelCount + ")");
        write("@SP");
        write("A=M-1");
        write("M=-1");
        write("(END" + (labelCount++) + ")");
    }

    private void push(String pointer, int index) {
        write("@" + pointer);
        write("D=M");
        write("@" + index);
        write("A=D+A");
        write("D=M");
        write("@SP");
        write("A=M");
        write("M=D");
        write("@SP");
        write("M=M+1");
    }

    private void pop(String pointer, int index) {
            write("@" + pointer);
            write("D=M");
            write("@" + index);
            write("D=D+A");
            write("@R13");
            write("M=D");
            write("@SP");
            write("AM=M-1");
            write("D=M");
            write("@R13");
            write("A=M");
            write("M=D");
    }

    public void writeLabel(String label) {
        write("("+ functionName + "$" + label + ")");
    }

    public void writeGoto(String label) {
        write("@" + functionName + "$" + label);
        write("0;JMP");
    }

    public void writeIf(String label) {
            write("@SP");
            write("AM=M-1");
            write("D=M");
            write("@" + functionName + "$" + label);
            write("D;JNE");
    }

    public void writeCall(String functionName, int numArgs) {
        String returnName = "RETURN"+ "_" + (returnCount++);
        // Push return address
        write("@" + returnName);
        write("D=A");
        write("@SP");
        write("A=M");
        write("M=D");
        write("@SP");
        write("M=M+1");

        String segments[] = {"LCL","ARG","THIS","THAT"};
        for (String segment : segments) {
            write("@" + segment);
            write("D=M");
            write("@SP");
            write("A=M");
            write("M=D");
            write("@SP");
            write("M=M+1");
        }
        //ARG = SP - n - 5
        write("@SP");
        write("D=M");
        write("@" + numArgs);
        write("D=D-A");
        write("@5");
        write("D=D-A");
        write("@ARG");
        write("M=D");
        // LCL = SP
        write("@SP");
        write("D=M");
        write("@LCL");
        write("M=D");
        // goto f
        write("@" + functionName);
        write("0;JMP");
        // (return-address)
        write("(" + returnName + ")");
    }

    public void writeReturn() {
        // frame = LCL
        write("@LCL");
        write("D=M");
        write("@FRAME");
        write("M=D");
        // RET = *(FRAME-5)
        write("@5");
        write("A=D-A");
        write("D=M");
        write("@RET");
        write("M=D");
        // *ARG = pop()
        writePop("argument", 0);
        // SP = ARG + 1
        write("@ARG");
        write("D=M+1");
        write("@SP");
        write("M=D");
        //Restore THAT THIS ARG LCL
        byte i = 1;
        String segments[] = {"THAT","THIS","ARG","LCL"};
        for (String segment : segments) {
            write("@" + (i++));
            write("D=A");
            write("@FRAME");
            write("A=M-D");
            write("D=M");
            write("@" + segment);
            write("M=D");
        }
        // goto RET
        write("@RET");
        write("A=M");
        write("0;JMP");
    }

    public void writeFunction(String functionName, int numLocals) {
        this.functionName = functionName;
        write("(" + functionName + ")");
        for (int i = 0; i < numLocals; i++)
            writePush("constant", 0);
    }

    private void write (String line) {
        try {
            out.write(line + "\n");
        } catch (IOException e) { System.out.println("ERROR: Escribiendo archivo"); }
    }

    public void close() {
        try {
            write("(END)");
            write("@END");
            write("0;JMP");
            out.close();
        } catch (IOException e) { System.out.println("ERROR: En archivo de entrada "); }
    }

}

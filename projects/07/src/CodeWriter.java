import java.io.*;

public class CodeWriter {
    private BufferedWriter out;
    private short label;

    public CodeWriter(String outFilePath) {
        try {
            File file = new File(outFilePath.replace(".vm", ".asm"));
            out = new BufferedWriter(new FileWriter(file));
            label = 0;
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void writeArithmetic(String command) {
        try {
            switch (command) {
                case "add":
                    binaryOperation1("+");
                    break;
                case "sub":
                    binaryOperation1("-");
                    break;
                case "neg":
                    out.write("@SP\n");
                    out.write("AM=M-1\n");
                    out.write("M=-M\n");
                    out.write("@SP\n");
                    out.write("M=M+1\n");
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
                    out.write("@SP\n");
                    out.write("AM=M-1\n");
                    out.write("M=!M\n");
                    out.write("@SP\n");
                    out.write("M=M+1\n");
                    break;
                default:
                    System.out.println("ERROR: Arithmetic " + command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writePush(String segment, int index) {
        try {
            switch (segment) {
                case "constant":
                    out.write("@" + index  +"\n");
                    out.write("D=A\n");
                    out.write("@SP\n");
                    out.write("A=M\n");
                    out.write("M=D\n");
                    out.write("@SP\n");
                    out.write("M=M+1\n");
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
                    out.write("@R" + (index+5)  +"\n");
                    out.write("D=M\n");
                    out.write("@SP\n");
                    out.write("A=M\n");
                    out.write("M=D\n");
                    out.write("@SP\n");
                    out.write("M=M+1\n");
                    break;
                case "pointer":
                    if (index == 0) out.write("@THIS\n");
                    else out.write("@THAT\n");
                    out.write("D=M\n");
                    out.write("@SP\n");
                    out.write("A=M\n");
                    out.write("M=D\n");
                    out.write("@SP\n");
                    out.write("M=M+1\n");
                    break;
                case "static":
                    out.write("@" + (index+16)  +"\n");
                    out.write("D=M\n");
                    out.write("@SP\n");
                    out.write("A=M\n");
                    out.write("M=D\n");
                    out.write("@SP\n");
                    out.write("M=M+1\n");
                    break;
            }
        } catch (IOException e) {
            System.out.println("ERROR: Push " + segment + index);
        }
    }
    public void writePop(String segment, int index) {
        try {
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
                    out.write("@SP\n");
                    out.write("M=M-1\n");
                    out.write("A=M\n");
                    out.write("D=M\n");
                    out.write("@R" + (index+5)  +"\n");
                    out.write("M=D\n");
                    break;
                case "pointer":
                    out.write("@SP\n");
                    out.write("M=M-1\n");
                    out.write("A=M\n");
                    out.write("D=M\n");
                    if (index == 0) out.write("@THIS\n");
                    else out.write("@THAT\n");
                    out.write("M=D\n");
                    break;
                case "static":
                    out.write("@SP\n");
                    out.write("M=M-1\n");
                    out.write("A=M\n");
                    out.write("D=M\n");
                    out.write("@" + (index+16)  +"\n");
                    out.write("M=D\n");
                    break;
            }
        } catch (IOException e) {
            System.out.println("ERROR: Pop " + segment + index);
        }
    }

    private void binaryOperation1(String arithmetic) {
        try {
            out.write("@SP\n");
            out.write("AM=M-1\n");
            out.write("D=M\n");
            out.write("A=A-1\n");
            out.write("M=M" + arithmetic + "D\n");
        } catch (IOException e) {
            System.out.println("ERROR: Binary Operation " + arithmetic);
        }
    }

    private void binaryOperation2(String arithmetic) {
        try {
            out.write("D=M\n");
            out.write("@SI" + label + "\n");
            out.write("D;" + arithmetic + "\n");
            out.write("@SP\n");
            out.write("A=M-1\n");
            out.write("M=0\n");
            out.write("@END" + label + "\n");
            out.write("0;JMP\n");
            out.write("(SI" + label + ")\n");
            out.write("@SP\n");
            out.write("A=M-1\n");
            out.write("M=-1\n");
            out.write("(END" + (label++) + ")\n");
        } catch (IOException e) {
            System.out.println("ERROR: Binary Operation " + arithmetic);
        }
    }

    private void push(String pointer, int index) {
        try {
            out.write("@" + pointer + "\n");
            out.write("D=M\n");
            out.write("@" + index + "\n");
            out.write("A=D+A\n");
            out.write("D=M\n");
            out.write("@SP\n");
            out.write("A=M\n");
            out.write("M=D\n");
            out.write("@SP\n");
            out.write("M=M+1\n");
        } catch (IOException e) {
            System.out.println("ERROR: Push " + pointer + index);
        }
    }

    private void pop(String pointer, int index) {
        try {
            out.write("@" + pointer + "\n");
            out.write("D=M\n");
            out.write("@" + index + "\n");
            out.write("D=D+A\n");
            out.write("@R13\n");
            out.write("M=D\n");
            out.write("@SP\n");
            out.write("AM=M-1\n");
            out.write("D=M\n");
            out.write("@R13\n");
            out.write("A=M\n");
            out.write("M=D\n");
        } catch (IOException e) {
            System.out.println("ERROR: PoP " + pointer + index);
        }
    }

    public void close() {
        try {
            out.write("(END)\n");
            out.write("@END\n");
            out.write("0;JMP\n");
            out.close();
        } catch (IOException e) {
            System.out.println("ERROR: En archivo de entrada");
        }
    }
}

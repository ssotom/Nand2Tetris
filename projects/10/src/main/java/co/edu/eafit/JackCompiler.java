// Comando para ejecutar este proyecto: java -cp "$CLASSPATH;target/classes" co.edu.eafit.JackCompiler example.jack
package co.edu.eafit;

import java_cup.runtime.*;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
Primera Versión del Compilador para el lenguaje Jack
En esta versión Genera un archivo .xml que se deriva de la gramática de este lenguaje.
También se genera un archivo .xml con lo Tokens obtenidos.
Este compilador está desarrollado utilizando JFlex para el scanner y con JCUP para el parser.
Esta es la clase principal en la cual se pasa los archivos de entrada al scanner y 
la salida de este se le pasa al parser.
Posteriormente del parser se obtiene el árbol sintáctico si se procede a imprimirlo en un archivo .xml
*/
public class JackCompiler{

    public static void main(String[] args) {
        File f = null;
        JackScanner scanner = null;
        try {
           if(args.length == 1) { // Se verifica que allá un argumento
                String fileName =  args[0];
                File file = new File(fileName);
                File files[];
    
                if (file.isFile()) { // Revisamos si es un archivo o un directorio
                    if(!file.getName().contains(".jack")) return;
                    files = new File[1];
                    files[0] = file;
                } else if (file.isDirectory()) {
                    files = file.listFiles();
                } else {
                    System.out.println("ERROR: En el archivo de entrada");
                    return;
                }
  
                for (File _file : files) {
                    if (!_file.getName().contains(".jack")) continue;
                    f = _file;
                    scanner = new JackScanner(new FileReader(f)); // Pasamos los archivos al scanner
                    JackParser parser = new JackParser(scanner);  // Pasamos tokens al parser
                    parser.parse();
                    File fileOut = new File(f.getPath().replace(".jack", ".xml"));
                    BufferedWriter out = new BufferedWriter(new FileWriter(fileOut));
                    short t = 0;
                    for(String str: parser.n.getList()) { // Imprimos al arbol del parser en un xml
                        if(str.startsWith("/")) {t--;}
                        for(short i = 0; i < t; i++) { out.write("  ");}
                        if(str.startsWith("<")) {
                            out.write(str);out.newLine();
                        }else if(str.startsWith("/")) {
                            out.write("<"+str+">");out.newLine();
                        }else{
                            out.write("<"+str+">");out.newLine();t++;
                        }   
                    }
                    out.close();

                    fileOut = new File(f.getPath().replace(".jack", "T.xml"));
                    BufferedWriter out2 = new BufferedWriter(new FileWriter(fileOut));
                    out2.write("<tokens>");out2.newLine();
                    for(String str: parser.n.getList()) { // Se imprimen los tokens en un xml
                        if(!str.startsWith("<")) continue;
                        out2.write(str);out2.newLine();
                    }
                    out2.write("</tokens>");out2.newLine();
                    out2.close();

                }
            } else {
                System.out.println("ERROR: En los argumentos");
            } 
        }catch(IOException e) {
            System.out.println("Error input file: "+f.getName());
        }catch(Exception e) {
            System.out.println("at line "+(scanner.getLine()+1)+" : "+scanner.getString()+ " in: "+f.getName());
        }    
    }

}

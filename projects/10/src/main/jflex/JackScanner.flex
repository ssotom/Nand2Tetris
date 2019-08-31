package co.edu.eafit;

import java_cup.runtime.Symbol;
/**
Esta clase obtienes los tokens de un archivo que recibe como parámetro,
esto lo hace utilizando expresiones regulares.
*/
%%

%class JackScanner
%cup
%unicode
%line
%column
%state STRING

%{

    String string = "";

    public int getLine(){
        return yyline;
    }

    public int getColumn(){
        return yycolumn;
    }

    public String getString(){
        return yytext();
    }

    private void error(String message) {
        System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
    }

%}

Identifier     = [:jletter:] ([:jletterdigit:] | "_")*
Integer        = [0-9]+
LineTerminator = \r|\n|\r\n|\n\r
WhiteSpace     = {LineTerminator} | [ \t\v\f]
EndOfLineComment     = "//" [^\r\n]*
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
DocumentationComment = "/**" ( [^*] | \*+ [^/*] )* "*"+ "/"
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

%%

<YYINITIAL> {


    //Keyword

    "class"        { return new Symbol(JackSymbols.CLASS); }
    "constructor"  { return new Symbol(JackSymbols.CONSTRUCTOR); }
    "function"     { return new Symbol(JackSymbols.FUNCTION); }
    "method"       { return new Symbol(JackSymbols.METHOD); }
    "field"        { return new Symbol(JackSymbols.FIELD); }
    "static"       { return new Symbol(JackSymbols.STATIC); }
    "var"          { return new Symbol(JackSymbols.VAR); }
    "int"          { return new Symbol(JackSymbols.INT); }
    "char"         { return new Symbol(JackSymbols.CHAR); }
    "boolean"      { return new Symbol(JackSymbols.BOOLEAN); }
    "void"         { return new Symbol(JackSymbols.VOID); }
    "true"         { return new Symbol(JackSymbols.TRUE); }
    "false"        { return new Symbol(JackSymbols.FALSE); }
    "null"         { return new Symbol(JackSymbols.NULL); }
    "this"         { return new Symbol(JackSymbols.THIS); }
    "let"          { return new Symbol(JackSymbols.LET); }
    "do"           { return new Symbol(JackSymbols.DO); }
    "if"           { return new Symbol(JackSymbols.IF); }
    "else"         { return new Symbol(JackSymbols.ELSE); }
    "while"        { return new Symbol(JackSymbols.WHILE); }
    "return"       { return new Symbol(JackSymbols.RETURN); }

    //Symbol

    "{"          { return new Symbol(JackSymbols.LCBRACKET); }
    "}"          { return new Symbol(JackSymbols.RCBRACKET); }
    "("          { return new Symbol(JackSymbols.LPAREN); }
    ")"          { return new Symbol(JackSymbols.RPAREN); }
    "["          { return new Symbol(JackSymbols.LBRACKET); }
    "]"          { return new Symbol(JackSymbols.RBRACKET); }
    "."          { return new Symbol(JackSymbols.PERIOD); }
    ","          { return new Symbol(JackSymbols.COMMA); }
    ";"          { return new Symbol(JackSymbols.SEMICOLON); }
    "+"          { return new Symbol(JackSymbols.ADD); }
    "-"          { return new Symbol(JackSymbols.SUB); }
    "*"          { return new Symbol(JackSymbols.TIM); }
    "/"          { return new Symbol(JackSymbols.DIV); }
    "&"          { return new Symbol(JackSymbols.AMPERSAND); }
    "|"          { return new Symbol(JackSymbols.PLECA);}
    "<"          { return new Symbol(JackSymbols.LESS); }
    ">"          { return new Symbol(JackSymbols.GREAT); }
    "="          { return new Symbol(JackSymbols.EQUAL); }
    "~"         { return new Symbol(JackSymbols.DIFERENT); }


    {Integer}    { return new Symbol(JackSymbols.INTEGERCONSTANT,yytext()); }

    {Identifier} { return new Symbol(JackSymbols.IDENTIFIER,yytext()); }

    //WhiteSpace

    {Comment}     {  }
    {WhiteSpace}  {  }

    \"            { yybegin(STRING); }

}

<STRING> {

    \"            { yybegin(YYINITIAL); String aux = string; string = ""; return new Symbol(JackSymbols.STRINGCONSTANT, aux); }

   [^\n\r\"]+     { string = getString(); }
}

<<EOF>>       { if (zzLexicalState == YYINITIAL) return new Symbol(JackSymbols.EOF);
              else            return new Symbol(51);}

.             { error("Illegal character <"+ yytext()+">"); return new Symbol(JackSymbols.error); }
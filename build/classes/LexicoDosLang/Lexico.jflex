package LexicoDosLang;
import java_cup.runtime.*;
import SintacticoDosLang.sym;
import Excepciones.Excepcion;
import Excepciones.Excepcion.TIPOERROR;
import doslang.DosLang;
%%

%class Lexer
%unicode
%cup
%line
%column
%public
%ignorecase
%state STRING, CHARACTER
%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
    StringBuilder NuevoString = new StringBuilder();
    StringBuilder NuevoChar = new StringBuilder();
%}

FinLinea        =   \r|\n|\r\n
InputCharacter  =   [^\r\n]
WhiteSpace      =   {FinLinea} | [ \t\f]
numero          =   [0-9] [0-9]*
real            =   [0-9]+\.[0-9]+
Letra           =   [a-zA-ZñÑ]
Identificador   =   [_a-zA-ZñÑ]([_a-zA-ZñÑ]|{numero})*
ComentarioLinea =   "(*" ~"*)"
ComentarioMulti =   "{" ~"}"
%%

<YYINITIAL> {
    {numero}            { return symbol(sym.numero, yytext().toLowerCase());  }
    {real}              { return symbol(sym.decimal, yytext().toLowerCase());  }
    "integer"           { return symbol(sym.integer, yytext().toLowerCase());  }
    "real"              { return symbol(sym.real,  yytext().toLowerCase());  }
    "char"              { return symbol(sym.character, yytext().toLowerCase());  }
    "boolean"           { return symbol(sym.bool, yytext().toLowerCase());  }
    "word"              { return symbol(sym.word, yytext().toLowerCase());  }
    "string"            { return symbol(sym.string, yytext().toLowerCase());  }
    "record"            { return symbol(sym.record, yytext().toLowerCase());  }
    "nil"               { return symbol(sym.nil, yytext().toLowerCase());  }
    "type"              { return symbol(sym.type, yytext().toLowerCase()); }
    "array"             { return symbol(sym.array, yytext().toLowerCase()); }
    "of"                { return symbol(sym.of, yytext().toLowerCase()); }
    "sizeof"            { return symbol(sym.sizeof, yytext().toLowerCase()); }
    "malloc"            { return symbol(sym.malloc, yytext().toLowerCase()); }
    "free"              { return symbol(sym.free, yytext().toLowerCase()); }
    
    "begin"             { return symbol(sym.begin, yytext().toLowerCase()); }
    "case"              { return symbol(sym.caso, yytext().toLowerCase()); }
    "const"             { return symbol(sym.constante, yytext().toLowerCase()); }
    "do"                { return symbol(sym.hacer, yytext().toLowerCase()); }
    "downto"            { return symbol(sym.downto, yytext().toLowerCase()); }
    "else"              { return symbol(sym.sino, yytext().toLowerCase()); }
    "end"               { return symbol(sym.end, yytext().toLowerCase()); }
    "for"               { return symbol(sym.para, yytext().toLowerCase()); }
    "function"          { return symbol(sym.function, yytext().toLowerCase()); }
    "if"                { return symbol(sym.si, yytext().toLowerCase()); }
    "and"               { return symbol(sym.and, yytext().toLowerCase()); }
    "or"                { return symbol(sym.or, yytext().toLowerCase()); }
    "nand"              { return symbol(sym.nand, yytext().toLowerCase()); }
    "nor"               { return symbol(sym.nor, yytext().toLowerCase()); }
    "not"               { return symbol(sym.not, yytext().toLowerCase()); }
    "procedure"         { return symbol(sym.procedure, yytext().toLowerCase()); }
    "program"           { return symbol(sym.program, yytext().toLowerCase()); }
    "repeat"            { return symbol(sym.repeat, yytext().toLowerCase()); }
    "then"              { return symbol(sym.then, yytext().toLowerCase()); }
    "to"                { return symbol(sym.to, yytext().toLowerCase()); }
    "until"             { return symbol(sym.until, yytext().toLowerCase()); }
    "var"               { return symbol(sym.var, yytext().toLowerCase()); }
    "while"             { return symbol(sym.mientras, yytext().toLowerCase()); }
    "with"              { return symbol(sym.con, yytext().toLowerCase()); }
    "uses"              { return symbol(sym.uses, yytext().toLowerCase()); }
    "true"              { return symbol(sym.verdadero, yytext().toLowerCase()); }
    "false"             { return symbol(sym.falso, yytext().toLowerCase()); }
    "break"             { return symbol(sym.detener, yytext().toLowerCase()); }
    "continue"          { return symbol(sym.continuar, yytext().toLowerCase()); }
    "exit"              { return symbol(sym.salir, yytext().toLowerCase()); }
    "write"             { return symbol(sym.escribir, yytext().toLowerCase()); }
    "writeln"           { return symbol(sym.escribirln, yytext().toLowerCase()); }
    "read"              { return symbol(sym.leer, yytext().toLowerCase()); }
    
    "="                 { return symbol(sym.igual, "="); }
    "+"                 { return symbol(sym.mas, "+"); }
    "-"                 { return symbol(sym.menos, "-"); }
    "*"                 { return symbol(sym.por, "*"); }
    "/"                 { return symbol(sym.division, "/"); }
    ";"                 { return symbol(sym.pComa, ";"); }
    ":"                 { return symbol(sym.dosPuntos, ":"); }
    "("                 { return symbol(sym.parenI, "("); }
    ")"                 { return symbol(sym.parenD, ")"); }
    "["                 { return symbol(sym.corI, "["); }
    "]"                 { return symbol(sym.corD, "]"); }
    "..."               { return symbol(sym.tresP, "..."); }
    ".."                { return symbol(sym.dosP, ".."); }
    "."                 { return symbol(sym.punto, "."); }
    ","                 { return symbol(sym.coma, ","); }
    "^"                 { return symbol(sym.potencia, "^"); }
    "%"                 { return symbol(sym.modulo, "%"); }
    "<"                 { return symbol(sym.menorque, "<"); }
    "<="                { return symbol(sym.menorigual, "<="); }
    ">"                 { return symbol(sym.mayorque, ">"); }
    ">="                { return symbol(sym.mayorigual, ">="); }
    "=="                { return symbol(sym.igualacion, "=="); }
    "<>"                { return symbol(sym.diferente, "<>"); }
    \"                  { yybegin(STRING); NuevoString.setLength(0);}
    \'                  { yybegin(CHARACTER); NuevoChar.setLength(0);}
    {Identificador}     { return symbol(sym.identificador, yytext().toLowerCase());  }
    {ComentarioLinea}   { /* ignore */}
    {ComentarioMulti}   { /* ignore */}
    {WhiteSpace}        {}
    /* Cualquier Otro */
    .                   { yybegin(YYINITIAL); 
                          DosLang.errores.add(new Excepcion(TIPOERROR.LEXICO, 
                                                        "El caracter '"+yytext()+"' no pertenece al lenguaje", 
                                                        yyline, 
                                                        yycolumn));  }
}


<STRING> {
    \"                           { yybegin(YYINITIAL); return symbol(sym.cadena, NuevoString.toString());}
    \\'                          { NuevoString.append('\''); }
    \\\"                         { NuevoString.append('\"'); }
    "\?"                         { NuevoString.append('?'); }
    \\                           { NuevoString.append('\\'); }
    \\0                          { NuevoString.append('\0'); }
    \\a                          { NuevoString.append('a'); }
    \\b                          { NuevoString.append('\b'); }
    \\f                          { NuevoString.append('\f'); }
    \\n                          { NuevoString.append('\n'); }
    \\r                          { NuevoString.append('\r'); }
    \\t                          { NuevoString.append('\t'); }
    \\v                          { NuevoString.append('v'); }
    {FinLinea}                   { yybegin(YYINITIAL);
                                   DosLang.errores.add(new Excepcion(TIPOERROR.LEXICO, 
                                                                 "String sin finalizar", 
                                                                 yyline, 
                                                                 yycolumn)); }
    .                            { NuevoString.append(yytext()); }
}

<CHARACTER> {
    \'                           { yybegin(YYINITIAL); 
                                   if(NuevoChar.toString().length() > 2){
                                        DosLang.errores.add(new Excepcion(TIPOERROR.LEXICO, 
                                                            "El tipo de dato char solo debe tener un caracter", 
                                                            yyline, 
                                                            yycolumn));
                                   }else{
                                        return symbol(sym.caracter, NuevoChar.toString());}
                                   }
    \\t                          { NuevoString.append('\t'); }
    \\n                          { NuevoString.append('\n'); }
    \\r                          { NuevoString.append('\r'); }
    \\\"                         { NuevoString.append('\"'); }
    \\                           { NuevoString.append('\\'); }
    {FinLinea}                   { yybegin(YYINITIAL);
                                   DosLang.errores.add(new Excepcion(TIPOERROR.LEXICO, 
                                                                 "Char sin finalizar", 
                                                                 yyline, 
                                                                 yycolumn)); }
    .                            { NuevoChar.append( yytext() ); }
}

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
%state STRING
%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
    StringBuilder NuevoString = new StringBuilder();
%}

FinLinea        =   \r|\n|\r\n
InputCharacter  =   [^\r\n]
WhiteSpace      =   {FinLinea} | [ \t\f]
numero          =   [0-9] [0-9]*
decimal         =   [0-9]+(\.[0-9]+)?
Letra           =   [a-zA-ZñÑ]
Identificador   =   [_a-zA-ZñÑ]([_a-zA-ZñÑ]|{numero})*
ComentarioLinea =   "(*" ~"*)"
ComentarioMulti =   "{" ~"}"
%%

<YYINITIAL> {
    {numero}            { return symbol(sym.numero, yytext().toLowerCase());  }
    {decimal}           { return symbol(sym.decimal, yytext().toLowerCase());  }
    "tipo"              { return symbol(sym.tipo, yytext().toLowerCase()); }
    "="                 { return symbol(sym.igual, "="); }
    \"                  { yybegin(STRING); NuevoString.setLength(0);}
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
    [^\n\r\"\\]+                 { NuevoString.append( yytext() ); }
    \\t                          { NuevoString.append('\t'); }
    \\n                          { NuevoString.append('\n'); }
    \\r                          { NuevoString.append('\r'); }
    \\\"                         { NuevoString.append('\"'); }
    \\                           { NuevoString.append('\\'); }
    {FinLinea}                   { yybegin(YYINITIAL);
                                   DosLang.errores.add(new Excepcion(TIPOERROR.LEXICO, 
                                                                 "String sin finalizar", 
                                                                 yyline, 
                                                                 yycolumn)); }
}

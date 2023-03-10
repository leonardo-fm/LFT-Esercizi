package Esercizio_2_2;

import java.io.*;
import java.util.*;
public class Lexer {
    public static int line = 1;
    private char peek = ' ';
    private Boolean endOfVariables = false;
    private String notVarChar = "|&!()[]{}+-*/;,<>= \t\n\r";

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = System.getProperty("user.dir") + "\\src\\Esercizio_2_2\\Inputs\\Input4.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public Token lexical_scan(BufferedReader br) {

        if ("|&!()[]{}+-*/;,<>=".contains(Character.toString(peek)) == false) {
            while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
                if (peek == '\n') line++;
                readch(br);
            }
        } else {
            endOfVariables = false;
        }

        switch (peek) {

            /* === Token === */
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                peek = ' ';
                return Token.div;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;

            /* === Word === */
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek );
                    return null;
                }
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek );
                    return null;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : " + peek );
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else if (peek == ' ') {
                    return Word.lt;
                } else {
                    System.err.println("Erroneous character"
                            + " after < : " + peek );
                    return null;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else if (peek == ' ') {
                    return Word.gt;
                } else {
                    System.err.println("Erroneous character"
                            + " after > : " + peek );
                    return null;
                }

            /* === Keyword === */
            case (char) -1:
                return new Token(Tag.EOF);
            default:
                if (Character.isLetter(peek) || peek == '_') {
                    String word = "";
                    while (notVarChar.contains(Character.toString(peek)) == false && peek != (char) -1) {
                        word += peek;
                        readch(br);
                    }
                    endOfVariables = true;

                    switch (word) {
                        case "assign":
                            return Word.assign;
                        case "to":
                            return Word.to;
                        case "conditional":
                            return Word.conditional;
                        case "option":
                            return Word.option;
                        case "do":
                            return Word.dotok;
                        case "else":
                            return Word.elsetok;
                        case "while":
                            return Word.whiletok;
                        case "begin":
                            return Word.begin;
                        case "end":
                            return Word.end;
                        case "print":
                            return Word.print;
                        case "read":
                            return Word.read;
                        default:
                            if (isIdentifier(word))
                                return new Word(Tag.ID, word);
                            else {
                                System.err.println("Erroneous character: " + peek );
                                return null;
                            }
                    }

                } else if (Character.isDigit(peek)) {

                    String num = "";
                    while (notVarChar.contains(Character.toString(peek)) == false && peek != (char) -1) {
                        num += peek;
                        readch(br);
                    }
                    endOfVariables = true;

                    if (isNumber(num))
                        return new NumberTok(num);
                    else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                    }

                } else {
                    System.err.println("Erroneous character: "
                            + peek );
                    return null;
                }
        }
    }

    private boolean isIdentifier(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                        state = 1;
                    else if (ch == '_')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".contains(Character.toString(ch)) == true)
                        state = 1;
                    else
                        state = -1;
                    break;
                case 2:
                    if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".contains(Character.toString(ch)) == true)
                        state = 1;
                    else if (ch == '_')
                        state = 2;
                    else
                        state = -1;
                    break;
            }
        }

        return state == 1;
    }

    private boolean isNumber(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if ("123456789".contains(Character.toString(ch)) == true)
                        state = 1;
                    else if (ch == '0')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if ("0123456789".contains(Character.toString(ch)) == true)
                        state = 1;
                    else
                        state = -1;
                    break;
                case 2:
                    state = -1;
                    break;
            }
        }

        return state == 1 || state == 2;
    }

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
}

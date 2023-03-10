package Esercizio_3_1;

import Esercizio_3_1.Lexer.*;
import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = System.getProperty("user.dir") + "\\src\\Esercizio_3_1\\Inputs\\Input4.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }
    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
    public void start() {
        expr();
        match(Tag.EOF);
    }
    private void expr() {
        term();
        exprp();
    }
    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                fact();
                exprp();
                break;
            case '-':
                match('-');
                fact();
                exprp();
                break;
            default:
                break;
        }
    }
    private void term() {
        fact();
        termp();
    }
    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
            default:
                break;
        }
    }
    private void fact() {
        switch (look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error("syntax error");
        }
    }
}

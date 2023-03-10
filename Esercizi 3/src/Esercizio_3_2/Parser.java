package Esercizio_3_2;

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
        String path = System.getProperty("user.dir") + "\\src\\Esercizio_3_2\\Inputs\\Input4.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
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

    private void prog() {
        statlist();
        match(Tag.EOF);
    }

    private void statlist() {
        start();
        statlistp();
    }

    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                start();
                statlistp();
                break;
            default:
                break;
        }
    }

    public void start() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist();
                match(']');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                start();
                break;
            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist();
                match(']');

                switch (look.tag) {
                    case Tag.END:
                        match(Tag.END);
                        break;
                    case Tag.ELSE:
                        match(Tag.ELSE);
                        start();
                        match(Tag.END);
                        break;
                    default:
                        error("syntax error");
                }
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default:
                error("syntax error");
        }
    }

    private void idlist() {
        switch (look.tag) {
            case Tag.ID:
                match(Tag.ID);
                idlistp();
                break;
            default:
                error("syntax error");
        }
    }

    private void idlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
                break;
            default:
                break;
        }
    }

    private void optlist() {
        optitem();
        optlistp();
    }

    private void optlistp() {
        if(look.tag == Tag.OPTION) {
            optitem();
            optlistp();
        }
    }

    private void optitem() {
        switch (look.tag) {
            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                bexpr();
                match(')');
                match(Tag.DO);
                start();
                break;
            default:
                error("syntax error");
        }
    }

    private void bexpr() {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("syntax error");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                error("syntax error");
        }
    }

    private void exprlist() {
        expr();
        exprlistp();
    }

    private void exprlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                exprlistp();
                break;
            default:
                break;
        }
    }
}

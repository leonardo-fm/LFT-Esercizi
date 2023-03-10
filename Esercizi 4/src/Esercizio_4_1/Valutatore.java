package Esercizio_4_1;

import Esercizio_4_1.Lexer.Lexer;
import Esercizio_4_1.Lexer.NumberTok;
import Esercizio_4_1.Lexer.Tag;
import Esercizio_4_1.Lexer.Token;

import java.io.*;

import static java.lang.Integer.parseInt;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = System.getProperty("user.dir") + "\\src\\Esercizio_4_1\\Inputs\\Input4.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
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
        int expr_val;

        expr_val = expr();
        match(Tag.EOF);

        System.out.println(expr_val);
    }

    private int expr() {
        int term_val, exprp_val;

        term_val = term();
        exprp_val = exprp(term_val);

        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;

        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            default:
                exprp_val = exprp_i;
                break;
        }

        return exprp_val;
    }

    private int term() {
        int fac_val, termp_val;

        fac_val = fact();
        termp_val = termp(fac_val);

        return termp_val;
    }

    private int termp(int termp_i) {
        int fact_val, termp_val = 0;

        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            default:
                termp_val = termp_i;
                break;
        }

        return termp_val;
    }

    private int fact() {
        int fact_val = 0;

        switch (look.tag) {
            case '(':
                match('(');
                fact_val = expr();
                match(')');
                break;
            case Tag.NUM:
                fact_val = parseInt(((NumberTok) look).lexeme);
                match(Tag.NUM);
                break;
            default:
                error("syntax error");
                break;
        }

        return fact_val;
    }
}
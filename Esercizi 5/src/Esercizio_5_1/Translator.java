package Esercizio_5_1;

import Esercizio_5_1.Bytecode.CodeGenerator;
import Esercizio_5_1.Bytecode.OpCode;
import Esercizio_5_1.Bytecode.SymbolTable;
import Esercizio_5_1.Lexer.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = System.getProperty("user.dir") + "\\src\\Esercizio_5_1\\Inputs\\Input6.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
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
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
            code.toJasmin();
        }
        catch(java.io.IOException e) {
            System.out.println("IO error\n");
        }
    }

    private void statlist(int lnext_prog) {
        start(lnext_prog);
        statlistp();
        int n_label = code.newLabel();
        code.emit(OpCode.GOto, n_label);
        code.emitLabel(n_label);
        code.emit(OpCode.GOto, lnext_prog); // TODO rivedere qui mi sembra na cazzata
    }

    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                int n_label = code.newLabel();
                code.emit(OpCode.GOto, n_label);
                code.emitLabel(n_label);
                start(n_label);
                statlistp();
                break;
            default:
                break;
        }
    }

    public void start(int lnext_prog) {
        int n_label;

        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(new Token(Tag.ASSIGN));
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist(null);
                match(']');
                code.emit(OpCode.invokestatic, 1);
                break;
            case Tag.READ:
                code.emit(OpCode.invokestatic, 0);
                match(Tag.READ);
                match('[');
                idlist(new Token(Tag.READ));
                match(']');
                break;
            case Tag.WHILE:
                n_label = code.newLabel();
                match(Tag.WHILE);
                match('(');
                bexpr(n_label);
                code.emit(OpCode.GOto, lnext_prog);
                match(')');
                code.emit(OpCode.GOto, n_label);
                code.emitLabel(n_label);
                start(n_label);
                break;
            case Tag.COND:
                n_label = code.newLabel();
                match(Tag.COND);
                match('[');
                optlist(n_label);
                match(']');

                switch (look.tag) {
                    case Tag.END:
                        match(Tag.END);
                        code.emitLabel(n_label);
                        break;
                    case Tag.ELSE:
                        match(Tag.ELSE);
                        code.emit(OpCode.GOto, n_label);
                        code.emitLabel(n_label);
                        start(n_label);
                        match(Tag.END);
                        break;
                    default:
                        error("syntax error");
                }
                break;
            case '{':
                n_label = code.newLabel();
                match('{');
                statlist(n_label);
                match('}');
                break;
            default:
                error("syntax error");
        }
    }

    private void idlist(Token ref_tok) {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = addInMemory();
                code.emit(OpCode.istore, id_addr);
                match(Tag.ID);
                idlistp(ref_tok);
                break;
            default:
                error("syntax error");
        }
    }

    private void idlistp(Token ref_tok) {
        switch (look.tag) {
            case ',':
                match(',');
                switch (ref_tok.tag) {
                    case Tag.ASSIGN:
                        code.emit(OpCode.iload, count);
                        break;
                    case Tag.READ:
                        code.emit(OpCode.invokestatic, 0);
                        break;
                }
                int id_addr = addInMemory();
                code.emit(OpCode.istore, id_addr);
                match(Tag.ID);
                idlistp(ref_tok);
                break;
            default:
                break;
        }
    }

    private void optlist(int lnext_prog) {
        optitem(lnext_prog);
        optlistp(lnext_prog);
    }

    private void optlistp(int lnext_prog) {
        if(look.tag == Tag.OPTION) {
            optitem(lnext_prog);
            optlistp(lnext_prog);
        }
    }

    private void optitem(int lnext_prog) {
        switch (look.tag) {
            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                bexpr(lnext_prog);
                match(')');
                match(Tag.DO);
                code.emit(OpCode.GOto, lnext_prog);
                code.emitLabel(lnext_prog);
                start(lnext_prog);
                break;
            default:
                error("syntax error");
        }
    }

    private void bexpr(int lnext_prog) {
        if (look.tag != Tag.RELOP)
            error("syntax error");

        switch (((Word) look).lexeme) {
            case "==":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpeq, lnext_prog);
                break;
            case ">":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpgt, lnext_prog);
                break;
            case "<":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmplt, lnext_prog);
                break;
            case ">=":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpge, lnext_prog);
                break;
            case "<=":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmple, lnext_prog);
                break;
            case "<>":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpne, lnext_prog);
                break;
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist(OpCode.iadd);
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                match('(');
                exprlist(OpCode.imul);
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, parseInt(((NumberTok) look).lexeme));
                match(Tag.NUM);
                break;
            case Tag.ID:
                code.emit(OpCode.iload, getIdFromMemory());
                match(Tag.ID);
                break;
            default:
                error("syntax error");
        }
    }

    private void exprlist(OpCode op_code) {
        expr();
        exprlistp(op_code);
    }

    private void exprlistp(OpCode op_code) {
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                exprlistp(op_code);
                if (op_code != null)
                    code.emit(op_code);
                break;
            default:
                break;
        }
    }

    private int addInMemory() {
        int id_addr = st.lookupAddress(((Word) look).lexeme);
        if (id_addr == -1) {
            id_addr = count;
            st.insert(((Word) look).lexeme, count++);
        }
        return id_addr;
    }

    private int getIdFromMemory() {
        int id_addr = st.lookupAddress(((Word) look).lexeme);
        if (id_addr == -1) {
            error("Variable not loaded");
        }

        return id_addr;
    }
}

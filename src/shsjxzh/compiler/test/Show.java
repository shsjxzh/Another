package shsjxzh.compiler.test;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.FrontEnd.*;
import shsjxzh.compiler.Parser.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class Show {
    private ProgramNode ast;

    private InputStream printIn;
    private PrintStream printOut;

    public Show(InputStream printIn, PrintStream printOut) {
        this.printIn = printIn;
        this.printOut = printOut;
    }

    private void printAST() {
        ast.accept(new ASTPrinter(printOut));
    }

    private void buildAST() throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(printIn);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();  //建语法树

        //System.out.println(tree.toStringTree(parser));//看下语法树的样子

        ASTBuilder astBuilder = new ASTBuilder();
        ast = (ProgramNode) astBuilder.visit(tree);
    }

    public void run() throws Exception{
        buildAST();
        printAST();
    }

    public static void main(String[] args) throws Exception{
        InputStream in = System.in;
        PrintStream out = System.out;
        new Show(in, out).run();
    }
}

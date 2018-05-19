package shsjxzh.compiler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.ErrorHandle.ParseTreeErrorListener;
import shsjxzh.compiler.FrontEnd.*;
import shsjxzh.compiler.Parser.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class MidTerm {
    private ProgramNode ast;

    private InputStream printIn;
    private PrintStream printOut;

    public MidTerm(InputStream printIn, PrintStream printOut) {
        this.printIn = printIn;
        this.printOut = printOut;
    }

    private void printAST() {
        ast.accept(new ASTPrinter(printOut));
    }

    private void semanticAnalysis() {
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        ast.accept(semanticAnalyzer);
    }

    private void buildAST() throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(printIn);
        MxLexer lexer = new MxLexer(input);

        lexer.removeErrorListeners();
        lexer.addErrorListener(ParseTreeErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        MxParser parser = new MxParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(ParseTreeErrorListener.INSTANCE);


        ParseTree tree = parser.program();  //build cst

        ASTBuilder astBuilder = new ASTBuilder();
        ast = (ProgramNode) astBuilder.visit(tree);
    }

    public void run() throws Exception{
        buildAST();
        //printAST();
        semanticAnalysis();
    }

    public static void main(String[] args) throws Exception{
        PrintStream out = System.out;
        //InputStream in = System.in;
        InputStream in = new FileInputStream("program.txt");
        new MidTerm(in, System.out).run();
    }
}

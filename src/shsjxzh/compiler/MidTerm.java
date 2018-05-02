package shsjxzh.compiler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.ErrorHandle.ParseTreeErrorListener;
import shsjxzh.compiler.FrontEnd.*;
import shsjxzh.compiler.Parser.*;

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

    private void semanticCheck() {
        //preprocess
        //ScopeTreePrePross scopeTreePrePross = new ScopeTreePrePross();
        //ast.accept(scopeTreePrePross);
        ReferenceResolver referenceResolver = new ReferenceResolver();
        ast.accept(referenceResolver);
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


        ParseTree tree = parser.program();  //build ast

        //System.out.println(tree.toStringTree(parser));

        ASTBuilder astBuilder = new ASTBuilder();
        ast = (ProgramNode) astBuilder.visit(tree);
    }

    public void run() throws Exception{
        buildAST();
        //printAST();
        //semanticCheck();
    }

    public static void main(String[] args) throws Exception{
        InputStream in = System.in;
        PrintStream out = System.out;
        new MidTerm(in, out).run();
        //new MidTerm(in, out).run();
    }
}

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
        //预处理
        ScopeTreePrePross scopeTreePrePross = new ScopeTreePrePross();
        ast.accept(scopeTreePrePross);
        //引用消除
        ReferenceResolver referenceResolver = new ReferenceResolver(scopeTreePrePross.getGlobalScope());
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


        ParseTree tree = parser.program();  //建语法树

        //System.out.println(tree.toStringTree(parser));//看下语法树的样子

        ASTBuilder astBuilder = new ASTBuilder();
        ast = (ProgramNode) astBuilder.visit(tree);
    }

    public void run() throws Exception{
        buildAST();
        //printAST();
    }

    public static void main(String[] args) throws Exception{
        InputStream in = System.in;
        PrintStream out = System.out;
        new MidTerm(in, out).run();
        //new MidTerm(in, out).run();
    }
}

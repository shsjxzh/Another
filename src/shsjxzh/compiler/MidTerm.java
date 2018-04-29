package shsjxzh.compiler;

//import shsjxzh.compiler.AST.Program;
import shsjxzh.compiler.FrontEnd.*;
import shsjxzh.compiler.Parser.MxLexer;
import shsjxzh.compiler.Parser.MxParser;
//import shsjxzh.compiler.Symbol.GlobalSymbolTable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.InputStream;

public class MidTerm {
    static public void main(String[] argv) {
        try {
            ANTLRInputStream input = new ANTLRInputStream(System.in);
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);

            ParseTree tree = parser.program();  //构建语法树

            ParseTreeWalker walker = new ParseTreeWalker();
            /*
            ASTBuilder astBuilder = new ASTBuilder();
            walker.walk(astBuilder, tree);
            ProgramNode program = astBuilder.getProgram();

            CompilationError ce = new CompilationError();
            GlobalSymbolTable sym = new GlobalSymbolTable();
            StructSymbolScanner structSymbolScanner = new StructSymbolScanner(sym, ce);
            StructFunctionDeclarator structFunctionDeclarator = new StructFunctionDeclarator(sym, ce);
            SemanticChecker semanticChecker = new SemanticChecker(sym, ce);

            program.accept(structSymbolScanner);
            program.accept(structFunctionDeclarator);
            program.accept(semanticChecker);
            //program.accept(printer);*/
        } catch (Exception e) {
            //e.printStackTrace(System.err);
            System.exit(1);
        }

    }
}
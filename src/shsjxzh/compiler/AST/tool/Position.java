package shsjxzh.compiler.AST.tool;

import org.antlr.runtime.Token;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Position {
    public final int line;
    public final int column;

    public Position(int line, int column){
        this.line = line;
        this.column = column;
    }

    public Position(Token token){
        this.line = token.getLine();
        this.column = token.getCharPositionInLine();
    }


    @Override
    public String toString() {
        return "Line " + line + " Column " + column;
    }
    /*
    public Position(ParserRuleContext ctx) {
        this(ctx.start);
    }

    public Position(TerminalNode terminal) {
        this(terminal.getSymbol());
    }
    */
    public Position(ParserRuleContext ctx){
        this.line = ctx.start.getLine();
        this.column = ctx.start.getCharPositionInLine();
    }


    public Position(TerminalNode terminal){
        this.line = terminal.getSymbol().getLine();
        this.column = terminal.getSymbol().getCharPositionInLine();
    }
}


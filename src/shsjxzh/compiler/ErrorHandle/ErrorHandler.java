package shsjxzh.compiler.ErrorHandle;

import shsjxzh.compiler.AST.tool.Position;

public class ErrorHandler extends RuntimeException{
    public ErrorHandler(String message) {
        super(message);
    }
}

package shsjxzh.compiler.ErrorHandle;

import shsjxzh.compiler.AST.tool.Position;

//用于以自定义方式输出错误
public class ErrorHandler extends RuntimeException{
    public ErrorHandler(String message, Position pos) {
        super(message + pos.toString());
    }
}

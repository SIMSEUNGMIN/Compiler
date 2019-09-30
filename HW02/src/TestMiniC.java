import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TestMiniC {

	public static void main(String[] args) throws Exception {
		
		MiniCLexer lexer = new MiniCLexer(new ANTLRFileStream("C:/Users/user/Desktop/test.c"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniCParser parser = new MiniCParser(tokens);
		ParseTree tree = parser.program();
	}

}

package listener;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import generated.*;

public class MiniCPrintListener extends MiniCBaseListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();

	boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 &&
				ctx.getChild(1) != ctx.expr();
	}

	String printSpace(int depth) {
		String space = "";
		for(int i = 4; i <= depth; i++) {
			space += "....";
		}
		return space;
	}

	//
	@Override public void enterProgram(MiniCParser.ProgramContext ctx) {}

	//완성
	@Override public void exitProgram(MiniCParser.ProgramContext ctx) {

		//decl+들을 하나씩 newText에 넣은 다음... 머머함 (추가 필요)
		for(int i = 0; i < ctx.getChildCount(); i++) {
			newTexts.put(ctx, ctx.getChild(i).getText());
			System.out.print(newTexts.get(ctx.getChild(i)));
		}
	}

	//
	@Override public void enterDecl(MiniCParser.DeclContext ctx) {}

	//완성
	@Override public void exitDecl(MiniCParser.DeclContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}

	//전역 변수라 앞에 ....이 들어갈 일이 없음
	@Override public void enterVar_decl(MiniCParser.Var_declContext ctx) {
	}

	//완성
	@Override public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String spec= newTexts.get(ctx.getChild(0));

		//type_spec IDENT ';' 
		if(ctx.getChildCount() == 3) {
			String id = ctx.IDENT().getText();
			String semicol = ctx.getChild(2).getText();

			newTexts.put(ctx, spec + " " + id + semicol + "\n");
		}
		//type_spec IDENT '=' LITERAL ';'
		else if(ctx.getChildCount() == 5) {
			String id = ctx.IDENT().getText();
			String equal = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String semicol = ctx.getChild(4).getText();

			newTexts.put(ctx, spec + " " + id + " " + equal + " " + literal + semicol + "\n");
		}
		//type_spec IDENT '[' LITERAL ']' ';'
		else if(ctx.getChildCount() == 6) {
			String id = ctx.IDENT().getText();
			String open = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String close = ctx.getChild(4).getText();
			String semicol = ctx.getChild(5).getText();

			newTexts.put(ctx, spec + " " + id + open + literal + close + semicol + "\n");
		}
	}

	@Override public void enterType_spec(MiniCParser.Type_specContext ctx) {
		//System.out.println("깊이 : " + ctx.depth());
	}

	//완성
	@Override public void exitType_spec(MiniCParser.Type_specContext ctx) {
		if(ctx.getChild(0).getText().equals("int")) {
			newTexts.put(ctx, ctx.INT().getText());
		}
		else if(ctx.getChild(0).getText().equals("void")) {
			newTexts.put(ctx, ctx.VOID().getText());
		}
	}

	@Override public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완성
	@Override public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		String head = newTexts.get(ctx.getChild(0)) + " " + ctx.IDENT().getText() 
				+ ctx.getChild(2).getText() + newTexts.get(ctx.params()) + ctx.getChild(4).getText();

		String stmt = newTexts.get(ctx.compound_stmt());

		newTexts.put(ctx, head + "\n" + stmt);
	}


	@Override public void enterParams(MiniCParser.ParamsContext ctx) {
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완료
	@Override public void exitParams(MiniCParser.ParamsContext ctx) {

		//빈칸인 경우
		if(ctx.getChildCount() == 0) {
			newTexts.put(ctx, "");
		}
		//void인 경우, param (',' param)*인 경우를 포함함
		else {
			//void만 나오는 경우를 거름
			if(ctx.getChild(0).getText().equals("void")) {
				newTexts.put(ctx, ctx.VOID().getText());
			}
			else {
				//param(list)의 index
				int count = 0;

				String startParam = newTexts.get(ctx.param(count++));
				String next = "";

				for(int i = 1; i < ctx.getChildCount(); i++) {
					if(ctx.getChild(i).getText().equals(",")) {
						next += ctx.getChild(i).getText() + " ";
					}
					else {
						next += newTexts.get(ctx.param(count++));
					}
				}

				newTexts.put(ctx, startParam + next);
			}
		}
	}


	@Override public void enterParam(MiniCParser.ParamContext ctx) {
	}

	//완료
	@Override public void exitParam(MiniCParser.ParamContext ctx) { 
		if(ctx.getChildCount() == 2) {
			newTexts.put(ctx, newTexts.get(ctx.type_spec()) + " " + ctx.IDENT().getText());
		}
		else {
			//ctx.getChildCount() == 4인 경우
			newTexts.put(ctx, newTexts.get(ctx.type_spec()) + " " + ctx.IDENT().getText()
					+ ctx.getChild(2).getText() + ctx.getChild(3).getText());
		}
	}

	@Override public void enterStmt(MiniCParser.StmtContext ctx) {
	}

	//완료
	@Override public void exitStmt(MiniCParser.StmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}

	@Override public void enterExpr_stmt(MiniCParser.Expr_stmtContext ctx) { 
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완료
	@Override public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.expr()) + ctx.getChild(1).getText());
	}

	@Override public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) { 
		//System.out.print(18);
		//System.out.println();
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완료
	@Override public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		newTexts.put(ctx, ctx.WHILE().getText() + ctx.getChild(1).getText()
				+ newTexts.get(ctx.expr()) +  ctx.getChild(3).getText() + "\n" + newTexts.get(ctx.stmt()));
	}

	@Override 
	public void enterCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		//System.out.print(20);
		//System.out.println("값 : ");
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완료..?
	@Override 
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {

		String open = ctx.getChild(0).getText() + "\n";
		String localDecl = "";
		String nextStmt = "";
		String close = ctx.getChild(1 + ctx.local_decl().size() + ctx.stmt().size()).getText();

		for(int i = 0; i < ctx.local_decl().size(); i++) {
			localDecl += newTexts.get(ctx.local_decl(i));
		}

		for(int i = 0; i < ctx.stmt().size(); i++) {
			localDecl += newTexts.get(ctx.stmt(i));
		}

		newTexts.put(ctx, open + localDecl + nextStmt + close);
	}

	@Override 
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
		//		System.out.println("개수 : " + ctx.getChildCount());
		//		newTexts.put(ctx, printSpace(ctx.depth()));
		//		System.out.println(newTexts.get(ctx));	
	}

	//완료
	@Override 
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String spec= newTexts.get(ctx.getChild(0));

		//type_spec IDENT ';' 
		if(ctx.getChildCount() == 3) {
			String id = ctx.IDENT().getText();
			String semicol = ctx.getChild(2).getText();

			newTexts.put(ctx, spec + " " + id + semicol + "\n");
		}
		//type_spec IDENT '=' LITERAL ';'
		else if(ctx.getChildCount() == 5) {
			String id = ctx.IDENT().getText();
			String equal = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String semicol = ctx.getChild(4).getText();

			newTexts.put(ctx, spec + " " + id + " " + equal + " " + literal + semicol + "\n");
		}
		//type_spec IDENT '[' LITERAL ']' ';'
		else if(ctx.getChildCount() == 6) {
			String id = ctx.IDENT().getText();
			String open = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String close = ctx.getChild(4).getText();
			String semicol = ctx.getChild(5).getText();

			newTexts.put(ctx, spec + " " + id + open + literal + close + semicol + "\n");
		}
	}

	@Override public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
		//		System.out.print(24);
		//		System.out.println();
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완료
	@Override public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		//IF '(' expr ')' stmt인 경우
		if(ctx.getChildCount() == 5) {
			newTexts.put(ctx, ctx.IF().getText() + ctx.getChild(1).getText()
					+ newTexts.get(ctx.expr()) + ctx.getChild(3).getText() + "\n"
					+ newTexts.get(ctx.stmt(0)));
		}
		//IF '(' expr ')' stmt ELSE stmt인 경우
		else {
			newTexts.put(ctx, ctx.IF().getText() + ctx.getChild(1).getText()
					+ newTexts.get(ctx.expr()) + ctx.getChild(3).getText() + "\n"
					+ newTexts.get(ctx.stmt(0)) + ctx.ELSE().getText() + "\n"
					+ newTexts.get(ctx.stmt(1)) + "\n");
		}
	}

	@Override public void enterReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	//완료
	@Override public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		//RETURN ';'인 경우
		if(ctx.getChildCount() == 2) {
			newTexts.put(ctx, ctx.RETURN().getText() + ctx.getChild(1).getText() + "\n");
		}
		//RETURN expr ';'인 경우
		else {
			newTexts.put(ctx, ctx.RETURN().getText() + " " 
					+ newTexts.get(ctx.expr()) + ctx.getChild(2).getText() + "\n");
		}
	}

	@Override public void enterExpr(MiniCParser.ExprContext ctx) { 


	}

	@Override public void exitExpr(MiniCParser.ExprContext ctx) { 
		String s1 = null, s2 = null, op = null;

		if(isBinaryOperation(ctx)) {
			//예 : expr '+' expr
			s1 = newTexts.get(ctx.expr(0));
			s2 = newTexts.get(ctx.expr(1));
			op = ctx.getChild(1).getText();
			newTexts.put(ctx, s1 + " " + op + " " + s2);

			//System.out.println("s1" + s1);
		}
	}

	@Override public void enterArgs(MiniCParser.ArgsContext ctx) { 
		//		System.out.print(30);
		//		System.out.println();
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	@Override public void exitArgs(MiniCParser.ArgsContext ctx) {
		//빈칸인 경우
		if(ctx.getChildCount() == 0) {
			newTexts.put(ctx, "");
		}
		else {
			//expr(list)의 index
			int count = 0;

			String startParam = newTexts.get(ctx.expr(count++));
			String next = "";

			for(int i = 1; i < ctx.getChildCount(); i++) {
				if(ctx.getChild(i).getText().equals(",")) {
					next += ctx.getChild(i).getText() + " ";
				}
				else {
					next += newTexts.get(ctx.expr(count++));
				}
			}

			newTexts.put(ctx, startParam + next);
		}
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) {
		//		for(int i = 0; i < ctx.getChildCount(); i++) {
		//			newTexts.put(ctx.getChild(i), ctx.getChild(i).getText());
		//		}
	}

	@Override public void exitEveryRule(ParserRuleContext ctx) {
	}

	@Override
	public void visitTerminal(TerminalNode node) {}

	@Override public void visitErrorNode(ErrorNode node) { 
		System.out.println("Error");
	}
}

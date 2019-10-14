package listener;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import generated.*;
import generated.MiniCParser.If_stmtContext;

//201702034 심승민
public class MiniCPrintListener extends MiniCBaseListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	
	//문장 깊이
	int depth = -1;
	
	//IDENT '=' expr 또한 이항 연산자에서 인식하는 경우가 있음
	//따라서 밑에 '='을 제외하는 조건을 추가
	//이항 연산자를 판단하는 함수
	boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 &&
				ctx.getChild(1) != ctx.expr() &&
				!ctx.getChild(1).getText().equals("=");
	}
	
	//if문에서 if else문인지 아니면 if에서 여러 stmt를 가진 조건문인지 확인하는 함수
	boolean isElse(If_stmtContext ctx) {
		for(int i = 0; i < ctx.getChildCount(); i++) {
			if(ctx.getChild(i).getText().equals("else")) {
				return true;
			}
		}
		return false;
	}
	
	//깊이에 따른 인덴트를 만들어주는 함수
	String plusSpace(int depth) {
		String space = "";
		
		for(int i = 0; i < depth; i++) {
			space += "....";
		}
		
		return space;
	}
	
	
	@Override public void exitProgram(MiniCParser.ProgramContext ctx) {

		//decl+들을 돌면서 나온 결과를 출력함 (decl 단위로 들어감)
		for(int i = 0; i < ctx.getChildCount(); i++) {
			newTexts.put(ctx, ctx.getChild(i).getText());
			System.out.print(newTexts.get(ctx.getChild(i)));
		}
	}

	//decl의 결과를 집어넣음
	@Override public void exitDecl(MiniCParser.DeclContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
	//전역 변수 선언
	@Override public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String spec= newTexts.get(ctx.getChild(0));

		//type_spec IDENT ';' 경우
		if(ctx.getChildCount() == 3) {
			String id = ctx.IDENT().getText();
			String semicol = ctx.getChild(2).getText();

			newTexts.put(ctx, spec + " " + id + semicol + "\n");
		}
		//type_spec IDENT '=' LITERAL ';' 경우
		else if(ctx.getChildCount() == 5) {
			String id = ctx.IDENT().getText();
			String equal = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String semicol = ctx.getChild(4).getText();

			newTexts.put(ctx, spec + " " + id + " " + equal + " " + literal + semicol + "\n");
		}
		//type_spec IDENT '[' LITERAL ']' ';' 경우
		else if(ctx.getChildCount() == 6) {
			String id = ctx.IDENT().getText();
			String open = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String close = ctx.getChild(4).getText();
			String semicol = ctx.getChild(5).getText();

			newTexts.put(ctx, spec + " " + id + open + literal + close + semicol + "\n");
		}
	}

	//함수 앞 반환형 체크
	@Override public void exitType_spec(MiniCParser.Type_specContext ctx) {
		//자식 노드가 1개인 경우 뿐인데 자식 노드의 text가 int면 INT를 집어넣고
		//void면 VOID를 집어넣음
		if(ctx.getChild(0).getText().equals("int")) {
			newTexts.put(ctx, ctx.INT().getText());
		}
		else if(ctx.getChild(0).getText().equals("void")) {
			newTexts.put(ctx, ctx.VOID().getText());
		}
	}

	//함수일 경우
	@Override public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		//함수의 헤더
		String head = newTexts.get(ctx.getChild(0)) + " " + ctx.IDENT().getText() 
				+ ctx.getChild(2).getText() + newTexts.get(ctx.params()) + ctx.getChild(4).getText();
		
		//함수 안쪽 부분
		String stmt = newTexts.get(ctx.compound_stmt());
		
		//\n을 추가하여 넣음
		newTexts.put(ctx, head + "\n" + stmt);
	}

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
				//param (',' param)*인 경우
				
				//param(list)의 index
				int count = 0;

				String startParam = newTexts.get(ctx.param(count++));
				String next = "";
				//(, param)*이므로 두개가 계속 나올 수 있으므로 더이상 나오지 않을 때까지
				//String에 추가하여 합한 다음 넣어줌
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

	@Override public void exitParam(MiniCParser.ParamContext ctx) {
		//자식 노드의 개수가 2개이면 type_spec IDENT의 경우
		if(ctx.getChildCount() == 2) {
			newTexts.put(ctx, newTexts.get(ctx.type_spec()) + " " + ctx.IDENT().getText());
		}
		else {
			//ctx.getChildCount() == 4일때
			//type_spec IDENT '[' ']'인 경우
			newTexts.put(ctx, newTexts.get(ctx.type_spec()) + " " + ctx.IDENT().getText()
					+ ctx.getChild(2).getText() + ctx.getChild(3).getText());
		}
	}

	@Override public void exitStmt(MiniCParser.StmtContext ctx) {
		//자식 노드가 항상 1개 뿐이므로 0번을 넣어줌
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}
	
	//compound_stmt를 거치지 않기 때문에 (거칠때마다 depth 증가)
	//거치지 않을 경우에는 (if() 한줄) 깊이 증가가 이루어지지 않기 때문에
	//여기서 깊이를 증가 시킴
	@Override public void enterExpr_stmt(MiniCParser.Expr_stmtContext ctx) { 
		depth++;
	}

	//Expr문장을 끝내고 나면 깊이가 다시 줄어들기 때문에 깊이를 줄임
	@Override public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.expr()) + ctx.getChild(1).getText() + "\n");
		depth--;
	}
	
	//WHILE '(' expr ')' stmt인 경우
	@Override public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		newTexts.put(ctx, ctx.WHILE().getText() + ctx.getChild(1).getText()
				+ newTexts.get(ctx.expr()) +  ctx.getChild(3).getText() + "\n" + newTexts.get(ctx.stmt()));
	}
	
	//{}의 indent를 위한 깊이 증가
	@Override public void enterCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		depth++;
		String space = plusSpace(depth);
		//space를 불러서 사용할 수 있도록 넣어줌
		newTexts.put(ctx, space);
	}

	@Override public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		//{}안의 내용을 위한 깊이 증가
		depth++;
		//{}안의 indent
		String space = plusSpace(depth);
		//원하는 indent를 구하고 나면 깊이를 감소시킴
		depth--;
		
		//{부분
		String open = newTexts.get(ctx) + ctx.getChild(0).getText() + "\n";
		//중간에 들어갈 내용
		String localDecl = "";
		String nextStmt = "";
		//}부분
		String close = newTexts.get(ctx) 
				+ ctx.getChild(1 + ctx.local_decl().size() + ctx.stmt().size()).getText() + "\n";
		
		//local_decl* stmt* 이므로 개수만큼 반복문을 수행
		//space 인텐트를 앞에 넣어준다.
		for(int i = 0; i < ctx.local_decl().size(); i++) {
			localDecl += space + newTexts.get(ctx.local_decl(i));
		}

		for(int i = 0; i < ctx.stmt().size(); i++) {
			nextStmt += space + newTexts.get(ctx.stmt(i));
		}

		newTexts.put(ctx, open + localDecl + nextStmt + close);
		
		depth--;
	}


	//지역 변수, 전역변수와 같음
	@Override public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		
		String spec= newTexts.get(ctx.getChild(0));

		//type_spec IDENT ';' 의 경우
		if(ctx.getChildCount() == 3) {
			String id = ctx.IDENT().getText();
			String semicol = ctx.getChild(2).getText();

			newTexts.put(ctx, spec + " " + id + semicol + "\n");
		}
		//type_spec IDENT '=' LITERAL ';' 의 경우
		else if(ctx.getChildCount() == 5) {
			String id = ctx.IDENT().getText();
			String equal = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String semicol = ctx.getChild(4).getText();

			newTexts.put(ctx, spec + " " + id + " " + equal + " " + literal + semicol + "\n");
		}
		//type_spec IDENT '[' LITERAL ']' ';' 의 경우
		else if(ctx.getChildCount() == 6) {
			String id = ctx.IDENT().getText();
			String open = ctx.getChild(2).getText();
			String literal = ctx.LITERAL().getText();
			String close = ctx.getChild(4).getText();
			String semicol = ctx.getChild(5).getText();

			newTexts.put(ctx, spec + " " + id + open + literal + close + semicol + "\n");
		}
	}
	
	//if가 시작될 경우 else를 위한 indent를 만들어 줌
	@Override public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
		depth++;
		
		String space = plusSpace(depth);

		newTexts.put(ctx, space);
		
		depth--;
	}

	@Override public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		
		//IF '(' expr ')' stmt인 경우 (stmt가 1줄일 경우)
		if(ctx.getChildCount() == 5) {
			newTexts.put(ctx, ctx.IF().getText() + ctx.getChild(1).getText()
					+ newTexts.get(ctx.expr()) + ctx.getChild(3).getText() + "\n"
					+ newTexts.get(ctx.stmt(0)));
		}
		else {
			//노드 안에 else가 있다면
			//IF '(' expr ')' stmt ELSE stmt인 경우
			//없으면 IF '(' expr ')' stmt인 경우에서 다수의 stmt를 가진 경우
			if(isElse(ctx)) {
				newTexts.put(ctx, ctx.IF().getText() + ctx.getChild(1).getText()
						+ newTexts.get(ctx.expr()) + ctx.getChild(3).getText() + "\n"
						+ newTexts.get(ctx.stmt(0)) + newTexts.get(ctx) + ctx.ELSE().getText() + "\n"
						+ newTexts.get(ctx.stmt(1)));
			}
			else {
				//IF '(' expr ')' stmt인 경우
				if(ctx.getChildCount() >= 5
						&& ctx.stmt().size() >= 0) {
					newTexts.put(ctx, ctx.IF().getText() + ctx.getChild(1).getText()
							+ newTexts.get(ctx.expr()) + ctx.getChild(3).getText() + "\n"
							+ newTexts.get(ctx.stmt(0)));
				}
			}
		}
	}

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
	
	//if() 한 줄 과 같은 경우 한줄 부분을 위한 indent를 만듦
	@Override public void enterExpr(MiniCParser.ExprContext ctx) { 
		depth++;
		String space = plusSpace(depth);
		depth--;
		newTexts.put(ctx, space);
	}

	@Override public void exitExpr(MiniCParser.ExprContext ctx) { 

		//이항 연산자일 경우
		if(isBinaryOperation(ctx)) {
			String s1 = null, s2 = null, op = null;
			s1 = newTexts.get(ctx.expr(0));
			s2 = newTexts.get(ctx.expr(1));
			op = ctx.getChild(1).getText();
			newTexts.put(ctx, s1 + " " + op + " " + s2);
		}
		//LITERAL 또는 INDENT의 경우
		else if(ctx.getChildCount() == 1) {
			if(ctx.getChild(0) == ctx.IDENT()) {
				newTexts.put(ctx, ctx.IDENT().getText());
			}
			else {
				newTexts.put(ctx, ctx.LITERAL().getText());
			}
		}
		//'(' expr ')'의 경우
		else if(ctx.getChildCount() == 3 && 
				ctx.getChild(1) == ctx.expr(0)) {
			newTexts.put(ctx, ctx.getChild(0).getText() + newTexts.get(ctx.expr(0))
			+ ctx.getChild(2));
		}
		//IDENT '[' expr ']'인 경우
		else if(ctx.getChildCount() == 4 &&
				ctx.getChild(2) == ctx.expr(0)) {
			newTexts.put(ctx, newTexts.get(ctx) + ctx.IDENT().getText() + ctx.getChild(1).getText()
					+ newTexts.get(ctx.expr(0)) + ctx.getChild(3).getText());
		}
		//IDENT '(' args ')' 인 경우
		else if(ctx.getChildCount() == 4 &&
				ctx.getChild(2) == ctx.args()) {
			newTexts.put(ctx, newTexts.get(ctx) +ctx.IDENT().getText() + ctx.getChild(1).getText()
					+ newTexts.get(ctx.args()) + ctx.getChild(3).getText());
		}
		//단항연산자일 경우
		else if(ctx.getChildCount() == 2
				&& ctx.getChild(1) == ctx.expr(0)) {
			newTexts.put(ctx, ctx.getChild(0).getText() + newTexts.get(ctx.expr(0)));
		}
		//IDENT '=' expr의 경우
		else if(ctx.getChildCount() == 3
				&& ctx.getChild(0) == ctx.IDENT()
				&& ctx.getChild(2) == ctx.expr(0)) {

			newTexts.put(ctx, ctx.IDENT().getText() + " " + ctx.getChild(1).getText()
					+ " "  + newTexts.get(ctx.expr(0)));
		}
		//DENT '[' expr ']' '=' expr의 경우
		else if(ctx.getChildCount() == 6) {
			String array = ctx.IDENT().getText() + ctx.getChild(1).getText()
					+ newTexts.get(ctx.expr(0)) + ctx.getChild(3).getText();
			newTexts.put(ctx, array + " " + ctx.getChild(4) + " " +newTexts.get(ctx.expr(1)));	

		}
	}

	@Override public void exitArgs(MiniCParser.ArgsContext ctx) {
		//빈칸인 경우
		if(ctx.getChildCount() == 0) {
			newTexts.put(ctx, "");
		}
		else {
			//expr (',' expr)*의 경우
			//expr(list)의 index
			int count = 0;

			String startArgs = newTexts.get(ctx.expr(count++));
			String next = "";
			
			//(',' expr)*이기 때문에 개수만큼 next 문자열에 추가하여 집어 넣어줌
			for(int i = 1; i < ctx.getChildCount(); i++) {
				if(ctx.getChild(i).getText().equals(",")) {
					next += ctx.getChild(i).getText() + " ";
				}
				else {
					next += newTexts.get(ctx.expr(count++));
				}
			}

			newTexts.put(ctx, startArgs + next);
		}
	}
}

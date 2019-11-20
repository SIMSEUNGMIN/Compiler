package listener.main;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import generated.MiniCParser;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.ParamsContext;
import generated.MiniCParser.Type_specContext;
import generated.MiniCParser.Var_declContext;
import listener.main.SymbolTable.Type;
import static listener.main.BytecodeGenListenerHelper.*;


public class SymbolTable {

	//함수 반환형 타입(?)
	enum Type {
		INT, INTARRAY, VOID, ERROR
	}

	//변수
	static public class VarInfo {
		Type type; // 변수 타입
		int id; // 변수 타입에 해당하는 id
		int initVal; // 변수 초기화 값

		// 생성자
		public VarInfo(Type type, int id, int initVal) {
			this.type = type;
			this.id = id;
			this.initVal = initVal;
		}
		public VarInfo(Type type,  int id) {
			this.type = type;
			this.id = id;
			this.initVal = 0;
		}
	}

	//함수
	static public class FInfo {
		public String sigStr;
	}

	private Map<String, VarInfo> _lsymtable = new HashMap<>();	// local v.
	private Map<String, VarInfo> _gsymtable = new HashMap<>();	// global v.
	private Map<String, FInfo> _fsymtable = new HashMap<>();	// function 


	private int _globalVarID = 0;
	private int _localVarID = 0;
	private int _labelID = 0;
	private int _tempVarID = 0;

	SymbolTable(){
		initFunDecl();
		initFunTable();
	}

	//처음에 symbolTable 생성시 초기화
	void initFunDecl(){		// at each func decl
		this._lsymtable.clear();
		_localVarID = 0;
		_labelID = 0;
		_tempVarID = 32;		
	}

	///////////////////////////////////////////
	void putLocalVar(String varname, Type type){
		this._lsymtable.put(varname, new VarInfo(type, this._localVarID++));
	}

	////////////////////////////////////////////
	void putGlobalVar(String varname, Type type){
		this._gsymtable.put(varname, new VarInfo(type, this._globalVarID++));
	}

	///////////////////////////////////////////
	void putLocalVarWithInitVal(String varname, Type type, int initVar){
		this._lsymtable.put(varname, new VarInfo(type, this._localVarID++, initVar));
	}

	////////////////////////////////////////////
	void putGlobalVarWithInitVal(String varname, Type type, int initVar){
		this._gsymtable.put(varname, new VarInfo(type, this._globalVarID++, initVar));
	}
	
	/////////////////////////////////////////////////
	void putParams(MiniCParser.ParamsContext params) {
		for(int i = 0; i < params.param().size(); i++) {
			//파라미터 변수 타입 (0번)
			String varTypeString = params.param().get(i).children.get(0).getText();
			Type type;
			//System.out.println("varType : " + type);
			
			//어느 변수 타입인지 확인
			if(varTypeString.equals("int"))
				type = Type.INT;
			else if(varTypeString.equals("int[]"))
				type = Type.INTARRAY;
			else if(varTypeString.equals("void"))
				type = Type.VOID;
			else 
				type = Type.ERROR;
			
			//파라미터 변수 이름(1번)
			String varname = params.param().get(i).children.get(1).getText();
			//System.out.println("varname : " + varname);
			
			//파라미터 변수 타입과 이름을 넣어줌
			this._lsymtable.put(varname, new VarInfo(type, this._localVarID++));
		}
	}

	private void initFunTable() {
		FInfo printlninfo = new FInfo();
		printlninfo.sigStr = "java/io/PrintStream/println(I)V";

		FInfo maininfo = new FInfo();
		maininfo.sigStr = "main([Ljava/lang/String;)V";
		_fsymtable.put("_print", printlninfo);
		_fsymtable.put("main", maininfo);
	}

	/////////////////////////////////////////
	public String getFunSpecStr(String fname) {		
		String funStr = this._fsymtable.get(fname).sigStr;
//		System.out.println("SymbolTable getFunSpecStr1: " + funStr);
		return funStr;
	}

	/////////////////////////////////////////////////
	public String getFunSpecStr(Fun_declContext ctx) {
		String funStr = ctx.IDENT().getText();
//		System.out.println("SymbolTable getFunSpecStr2: " + funStr);
		return funStr;
	}

	//채워야 함 ?????????????????????????????????????????????????????????????
	public String putFunSpecStr(Fun_declContext ctx) {
		String fname = getFunName(ctx);
		String argtype = "";	
		String rtype = "";
		String res = "";

		// <Fill here>	

		res =  fname + "(" + argtype + ")" + rtype;

		FInfo finfo = new FInfo();
		finfo.sigStr = res;
		_fsymtable.put(fname, finfo);

		return res;
	}

	/////////////////////////////
	String getVarId(String name){
		String sname = null;

		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		if(lvar != null) {
			sname += this._lsymtable.get(name).id;
		}

		VarInfo gvar = (VarInfo) _gsymtable.get(name);
		if(lvar != null) {
			sname += this._gsymtable.get(name).id;
		}

		return sname;
	}

	Type getVarType(String name){
		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		if (lvar != null) {
			return lvar.type;
		}

		VarInfo gvar = (VarInfo) _gsymtable.get(name);
		if (gvar != null) {
			return gvar.type;
		}

		return Type.ERROR;	
	}

	String newLabel() {
		return "label" + _labelID++;
	}

	String newTempVar() {
		String id = "";
		return id + _tempVarID--;
	}

	// global ???????????????????????????????????????????????????????????????????????
	public String getVarId(Var_declContext ctx) {
		// <Fill here>	
		String sname = "";
		sname += getVarId(ctx.IDENT().getText());
		return sname;
	}

	// local
	public String getVarId(Local_declContext ctx) {
		String sname = "";
		sname += getVarId(ctx.IDENT().getText());
		return sname;
	}

}

grammar MiniC;

program	: decl+			{System.out.println("201702034 Rule 0");};
decl		: var_decl	{System.out.println("201702034 Rule 1-1");}	
		| fun_decl		{System.out.println("201702034 Rule 1-2");};
var_decl	:  type_spec IDENT ';'	{System.out.println("201702034 Rule 2-1");}
		| type_spec IDENT '=' LITERAL ';'	{System.out.println("201702034 Rule 2-2");}
		| type_spec IDENT '[' LITERAL ']' ';'	{System.out.println("201702034 Rule 2-3");};
type_spec	: VOID	{System.out.println("201702034 Rule 3-1");}		
		| INT	{System.out.println("201702034 Rule 3-2");};
fun_decl	: type_spec IDENT '(' params ')' compound_stmt {System.out.println("201702034 Rule 4-1");};
params		: param (',' param)*	{System.out.println("201702034 Rule 5-1");}
		| VOID		{System.out.println("201702034 Rule 5-2");}	
		|		{System.out.println("201702034 Rule 5-3");};
param		: type_spec IDENT	{System.out.println("201702034 Rule 6-1");}	
		| type_spec IDENT '[' ']'	{System.out.println("201702034 Rule 6-2");};
stmt		: expr_stmt		{System.out.println("201702034 Rule 7-1");}	
		| compound_stmt		{System.out.println("201702034 Rule 7-2");}	
		| if_stmt		{System.out.println("201702034 Rule 7-3");}	
		| while_stmt	{System.out.println("201702034 Rule 7-4");}		
		| return_stmt	{System.out.println("201702034 Rule 7-5");};
expr_stmt	: expr ';'	{System.out.println("201702034 Rule 8-1");};
while_stmt	: WHILE '(' expr ')' stmt	{System.out.println("201702034 Rule 9-1");};
compound_stmt: '{' local_decl* stmt* '}'	{System.out.println("201702034 Rule 10-1");};
local_decl	: type_spec IDENT ';'	{System.out.println("201702034 Rule 11-1");}
		| type_spec IDENT '=' LITERAL ';'	{System.out.println("201702034 Rule 11-2");}
		| type_spec IDENT '[' LITERAL ']' ';'	{System.out.println("201702034 Rule 11-3");};
if_stmt		: IF '(' expr ')' stmt		{System.out.println("201702034 Rule 12-1");}
		| IF '(' expr ')' stmt ELSE stmt 	{System.out.println("201702034 Rule 112-2");};
return_stmt	: RETURN ';'	{System.out.println("201702034 Rule 13-1");}		
		| RETURN expr ';'	{System.out.println("201702034 Rule 13-2");};
expr	:  LITERAL		{System.out.println("201702034 Rule 14-1");}		
	| '(' expr ')'	{System.out.println("201702034 Rule 14-2");}			 
	| IDENT			{System.out.println("201702034 Rule 14-3");}	 
	| IDENT '[' expr ']'	{System.out.println("201702034 Rule 14-4");}	 
	| IDENT '(' args ')'	{System.out.println("201702034 Rule 14-5");}		
	| '-' expr		{System.out.println("201702034 Rule 14-6");}		 
	| '+' expr		{System.out.println("201702034 Rule 14-7");}		 
	| '--' expr		{System.out.println("201702034 Rule 14-8");}		 
	| '++' expr		{System.out.println("201702034 Rule 14-9");}		 
	| expr '*' expr		{System.out.println("201702034 Rule 14-10");}			 
	| expr '/' expr		{System.out.println("201702034 Rule 14-11");}		 
	| expr '%' expr		{System.out.println("201702034 Rule 14-12");}		 
	| expr '+' expr		{System.out.println("201702034 Rule 14-13");}		 
	| expr '-' expr		{System.out.println("201702034 Rule 14-14");}		 
	| expr EQ expr		{System.out.println("201702034 Rule 14-15");}		
	| expr NE expr		{System.out.println("201702034 Rule 14-16");}		 
	| expr LE expr		{System.out.println("201702034 Rule 14-17");}		 
	| expr '<' expr		{System.out.println("201702034 Rule 14-18");}		 
	| expr GE expr		{System.out.println("201702034 Rule 14-19");}		 
	| expr '>' expr		{System.out.println("201702034 Rule 14-20");}		 
	| '!' expr			{System.out.println("201702034 Rule 14-21");}		 
	| expr AND expr		{System.out.println("201702034 Rule 14-22");}		 
	| expr OR expr		{System.out.println("201702034 Rule 14-23");}		
	| IDENT '=' expr	{System.out.println("201702034 Rule 14-24");}		
	| IDENT '[' expr ']' '=' expr	{System.out.println("201702034 Rule 14-25");};
args	: expr (',' expr)*		{System.out.println("201702034 Rule 15-1");}	 
	|		{System.out.println("201702034 Rule 15-2");} ;

VOID: 'void'	{System.out.println("201702034 Rule 16-1");};
INT: 'int'	{System.out.println("201702034 Rule 17-1");};

WHILE: 'while'	{System.out.println("201702034 Rule 18-1");};
IF: 'if'	{System.out.println("201702034 Rule 19-1");};
ELSE: 'else'	{System.out.println("201702034 Rule 20-1");};
RETURN: 'return'	{System.out.println("201702034 Rule 21-1");};
OR: 'or'	{System.out.println("201702034 Rule 22-1");};
AND: 'and'	{System.out.println("201702034 Rule 23-1");};
LE: '<='	{System.out.println("201702034 Rule 24-1");};
GE: '>='	{System.out.println("201702034 Rule 25-1");};
EQ: '=='	{System.out.println("201702034 Rule 26-1");};
NE: '!='	{System.out.println("201702034 Rule 27-1");};

IDENT  : [a-zA-Z_]
        (   [a-zA-Z_]
        |  [0-9]	
        )*	{System.out.println("201702034 Rule 28-1");};


LITERAL:   DecimalConstant	{System.out.println("201702034 Rule 29-1");}
		|   OctalConstant   {System.out.println("201702034 Rule 29-2");} 
		|   HexadecimalConstant    {System.out.println("201702034 Rule 29-3");};


DecimalConstant
    :   '0'	{System.out.println("201702034 Rule 30-1");}
	|   [1-9] [0-9]*	{System.out.println("201702034 Rule 30-2");}
    ;

OctalConstant
    :   '0'[0-7]*	{System.out.println("201702034 Rule 31-1");}
    ;

HexadecimalConstant
    :   '0' [xX] [0-9a-fA-F] +	{System.out.println("201702034 Rule 32-1");}
    ;

WS  :   (   ' '
        |   '\t'	
        |   '\r'	
        |   '\n'	
        )+	{System.out.println("201702034 Rule 33-1");}
	-> channel(HIDDEN)	 
    ;

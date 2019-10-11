package noo2c;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by eschough on 2019-09-02.
 * SIMSEUNGMIN 201702034
 */

public class NooToC {
    CmdExtractor cmdExtractor;
    FileWriter fw;
    String nooPgm;
    //파일에 쓸 문자열을 초기화
    StringBuilder str = new StringBuilder();

	// init
    public NooToC (FileWriter fw, String nooPgm) throws IOException {
		this.fw = fw;
		this.nooPgm = nooPgm;
		//들어온 파일의 입력에 대한 CmdExtractor객체를 생성
		this.cmdExtractor = new CmdExtractor(this.nooPgm);
		//파일에 쓸 앞 부분
		str.append("#include<stdio.h>\n"
				+ "int main(){\n"
				+ "int r, t1;\n");
    }

	// translate cmd to C code for each case.
    public void translateRecursively(CmdExtractor.Cmds cmd) {
    	//해당 CMD에 따른 필요한 문자열을 str에 붙이고 필요시 재귀를 돌린다.
    	//재귀는 CMD3일 경우 더이상 돌지 않고 되돌아 간다.
    	
    	//CMD1의 경우 다음 CMD를 확인할 필요가 있기 때문에 다음 CMD에 대한 재귀를 돌리고
    	//그 다음 print문을 str에 붙인다.
    	if(cmd == CmdExtractor.Cmds.CMD1) {
    		translateRecursively(next());
    		str.append("print(\"%d\", r);\n");
    	}
    	//CMD2의 경우 다음 CMD를 확인할 필요가 있기 때문에 다음 CMD에 대한 재귀를 돌려야 한다.
    	//그 다음 1을 추가하는 문자열을 str에 붙인다.
    	else if(cmd == CmdExtractor.Cmds.CMD2) {
    		translateRecursively(next());
    		str.append("t1 = r;\n"
    				+ "r = t1 + 1;\n");
    	}
    	//CMD3는 다음 CMD를 확인할 필요가 없다. (재귀의 종료 조건)
    	//따라서 CMD3에 해당하는 선언문(r=0)만 str에 붙이고 해당 CMD3에 대한 재귀를 종료한다.
    	else if(cmd == CmdExtractor.Cmds.CMD3) {
    		str.append("r = 0;\n");
    		return;
    	}
    	//CMD4는 다음 CMD 2개를 확인할 필요가 있다. (두 개의 조건을 다 실행해야 하기 때문)
    	//따라서 CMD4 다음 2개의 CMD에 대한 재귀를 돌린다.
    	else if(cmd == CmdExtractor.Cmds.CMD4) {
    		translateRecursively(next());
    		translateRecursively(next());
    	}
    	//CMD5는 다음 CMD3개를 확인 해야한다. (조건이 3개 붙기 때문)
    	//먼저 첫번 째 CMD를 확인하는 재귀를 돌린 다음 if문에 대한 문자열(if문 시작)을 str에 붙인다.
    	//그 다음 두번 째 CMD를 확인하는 재귀를 돌리고 else에 대한 문자열(else문 시작)을 str에 붙인다.
    	//마지막으로 세번 째 CMD를 확인하는 재귀를 돌리고 else문을 종료하는 문자를 str에 붙인다.
    	else if(cmd == CmdExtractor.Cmds.CMD5) {
    		translateRecursively(next());
    		str.append("t1 = r;\n"
    				+ "if(t1 != 0)\n"
    				+ "{\n");
    		
    		translateRecursively(next());
    		
    		str.append("}\n"
    				+ "else\n"
    				+ "{\n");

    		translateRecursively(next());

    		str.append("}\n");
    	}
    }
    
    public void translate(CmdExtractor.Cmds cmd) {
    	//들고온 CMD를 가지고 재귀를 돌리는 함수 호출
    	translateRecursively(cmd);
    	
    	//재귀를 다 돌리고 나면 파일에 쓸 뒷 부분인 return 1;과 }을 추가해서 파일에 쓴다.
    	try {
    		fw.write(str.toString() + "return 1;\n" + "}\n");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public CmdExtractor.Cmds next() {
    	//CmdExtractor에서 순서대로 CMD 하나를 가져와서 리턴 
    	return CmdExtractor.checkTheCmd();
    }
}

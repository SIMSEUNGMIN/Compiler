package noo2c;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by eschough on 2019-09-02.
 */

public class NooToC {
    CmdExtractor cmdExtractor;
    FileWriter fw;
    String nooPgm;
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
    public void translate(CmdExtractor.Cmds cmd) {
    	
    	if(cmd == CmdExtractor.Cmds.CMD1) {
    		translate(next());
    		str.append("print(\"%d\", r);\n");
    	}
    	else if(cmd == CmdExtractor.Cmds.CMD2) {
    		translate(next());
    		str.append("t1 = r;\n"
    				+ "r = t1 + 1;\n");
    	}
    	else if(cmd == CmdExtractor.Cmds.CMD3) {
    		str.append("r = 0;\n");
    		return;
    	}
    	else if(cmd == CmdExtractor.Cmds.CMD4) {
    		translate(next());
    		translate(next());
    	}
    	else if(cmd == CmdExtractor.Cmds.CMD5) {
    		translate(next());
    		str.append("t1 = r;\n"
    				+ "if(t1 != 0)\n"
    				+ "{\n");
    		
    		translate(next());
    		
    		str.append("}\n"
    				+ "else\n"
    				+ "{\n");

    		translate(next());

    		str.append("}\n");
    	}
    }
    
    public void print() {
    	try {
    		fw.write(str.toString() + "return 1;\n" + "}\n");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public CmdExtractor.Cmds next() {
    	return CmdExtractor.checkTheCmd();
    }
}

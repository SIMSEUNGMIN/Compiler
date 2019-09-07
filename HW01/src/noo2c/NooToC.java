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
    //���Ͽ� �� ���ڿ��� �ʱ�ȭ
    StringBuilder str = new StringBuilder();

	// init
    public NooToC (FileWriter fw, String nooPgm) throws IOException {
		this.fw = fw;
		this.nooPgm = nooPgm;
		//���� ������ �Է¿� ���� CmdExtractor��ü�� ����
		this.cmdExtractor = new CmdExtractor(this.nooPgm);
		//���Ͽ� �� �� �κ�
		str.append("#include<stdio.h>\n"
				+ "int main(){\n"
				+ "int r, t1;\n");
    }

	// translate cmd to C code for each case.
    public void translateRecursively(CmdExtractor.Cmds cmd) {
    	//�ش� CMD�� ���� �ʿ��� ���ڿ��� str�� ���̰� �ʿ�� ��͸� ������.
    	//��ʹ� CMD3�� ��� ���̻� ���� �ʰ� �ǵ��� ����.
    	
    	//CMD1�� ��� ���� CMD�� Ȯ���� �ʿ䰡 �ֱ� ������ ���� CMD�� ���� ��͸� ������
    	//�� ���� print���� str�� ���δ�.
    	if(cmd == CmdExtractor.Cmds.CMD1) {
    		translateRecursively(next());
    		str.append("print(\"%d\", r);\n");
    	}
    	//CMD2�� ��� ���� CMD�� Ȯ���� �ʿ䰡 �ֱ� ������ ���� CMD�� ���� ��͸� ������ �Ѵ�.
    	//�� ���� 1�� �߰��ϴ� ���ڿ��� str�� ���δ�.
    	else if(cmd == CmdExtractor.Cmds.CMD2) {
    		translateRecursively(next());
    		str.append("t1 = r;\n"
    				+ "r = t1 + 1;\n");
    	}
    	//CMD3�� ���� CMD�� Ȯ���� �ʿ䰡 ����. (����� ���� ����)
    	//���� CMD3�� �ش��ϴ� ����(r=0)�� str�� ���̰� �ش� CMD3�� ���� ��͸� �����Ѵ�.
    	else if(cmd == CmdExtractor.Cmds.CMD3) {
    		str.append("r = 0;\n");
    		return;
    	}
    	//CMD4�� ���� CMD 2���� Ȯ���� �ʿ䰡 �ִ�. (�� ���� ������ �� �����ؾ� �ϱ� ����)
    	//���� CMD4 ���� 2���� CMD�� ���� ��͸� ������.
    	else if(cmd == CmdExtractor.Cmds.CMD4) {
    		translateRecursively(next());
    		translateRecursively(next());
    	}
    	//CMD5�� ���� CMD3���� Ȯ�� �ؾ��Ѵ�. (������ 3�� �ٱ� ����)
    	//���� ù�� ° CMD�� Ȯ���ϴ� ��͸� ���� ���� if���� ���� ���ڿ�(if�� ����)�� str�� ���δ�.
    	//�� ���� �ι� ° CMD�� Ȯ���ϴ� ��͸� ������ else�� ���� ���ڿ�(else�� ����)�� str�� ���δ�.
    	//���������� ���� ° CMD�� Ȯ���ϴ� ��͸� ������ else���� �����ϴ� ���ڸ� str�� ���δ�.
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
    	//���� CMD�� ������ ��͸� ������ �Լ� ȣ��
    	translateRecursively(cmd);
    	
    	//��͸� �� ������ ���� ���Ͽ� �� �� �κ��� return 1;�� }�� �߰��ؼ� ���Ͽ� ����.
    	try {
    		fw.write(str.toString() + "return 1;\n" + "}\n");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public CmdExtractor.Cmds next() {
    	//CmdExtractor���� ������� CMD �ϳ��� �����ͼ� ���� 
    	return CmdExtractor.checkTheCmd();
    }
}

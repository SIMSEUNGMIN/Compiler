package noo2c;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by eschough on 2019-09-02.
 * SIMSEUNGMIN 201702034
 */
 
// find command pattern from input, and convert it to cmd
// CmdExtractor�� input���κ��� CMD pattern�� ã�� parsing

public class  CmdExtractor {
	public enum Cmds {
		// pattern (��ɾ ���ȭ)
		CMD1("'\""), //����ϰ� ����
		CMD2("'\"\""), //+1�ϰ� ����
		CMD3("'\"\"\""), //0�� ����
		CMD4("'\"\"\"\""), //x ����, ���� y ����, y��� ����
		CMD5("'\"\"\"\"\""); //x��  0�� �ƴҶ� y���, ���� ,0�̸� z���, ����
		
		String matchedStr;
		
		//enum ������
		private Cmds(String recvString){
			//this.matchedStr = recvString;
		}
    }
	
	//���Ͽ��� �о�� ���ڿ�
	String testString = null;
	//���ڿ����� pattern�� ��ġ�ϴ� ���ڿ��� ã�� matcher
	static Matcher matcher = null;
	
	//��ü�� �����Ǿ��� �� �Է¹��� ���ڿ��� ���Խ��� ��ġ��Ű�� run�Լ� ȣ��.
	public CmdExtractor(String recv) {
		this.testString = recv;
		run();
	}
	
	//���Խ��� ����� matcher�� testString�� ���Խ��� ��ġ��Ų��.
	public void run() {
		//���Խ��� �������� ����� (String�߿��� '�� �����ϰ� "�� 1�� �̻��� Cmd)
		Pattern pattern = Pattern.compile("'\"+");
		//������ tesstString�� ��ġ��Ų��.
		matcher = pattern.matcher(testString);
	}
	
	//ȣ��� ��� �´� find�� ����Ͽ� ��ġ�Ǵ� ���ڿ��� �ϳ� ã��
	//group���� ���ڿ��� ȣ���� ���� �ش��ϴ� CMD�� enum ���·� ��ȯ�Ѵ�.
	//ȣ��� ������ �� ������ �ش��ϴ� ���ڿ��� ã�Ƴ���.
	public static Cmds checkTheCmd() {

		if(matcher.find()) {

			switch(matcher.group()) {
			case "'\"" :
				return Cmds.CMD1;
			case "'\"\"" :
				return Cmds.CMD2;
			case "'\"\"\"" :
				return Cmds.CMD3;
			case "'\"\"\"\"" :
				return Cmds.CMD4;
			case "'\"\"\"\"\"" :
				return Cmds.CMD5;
			default :
				break;
			}
		}
		else {
			return null;
		}
		
		return null;
	}
}

package noo2c;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by eschough on 2019-09-02.
 */
 
// find command pattern from input, and convert it to cmd
// CmdExtractor�� input���κ��� CMD pattern�� ã�� parsing

public class  CmdExtractor {
	public enum Cmds {
		// pattern (���ɾ ���ȭ)
		CMD1("'\""), //����ϰ� ����
		CMD2("'\"\""), //+1�ϰ� ����
		CMD3("'\"\"\""), //0�� ����
		CMD4("'\"\"\"\""), //x ����, ���� y ����, y��� ����
		CMD5("'\"\"\"\"\""); //x��  0�� �ƴҶ� y���, ���� ,0�̸� z���, ����
		
		String matchedStr;
		
		private Cmds(String recvString){
			this.matchedStr = recvString;
		}
    }
	
	String testString = null;
	static Matcher matcher = null;
	//static List<Cmds> cmdsList = new ArrayList<Cmds>();
	
	//��ü�� ���� �Ǿ��� �� �Է¹��� �����͸� ������ Cmd�� ã�� run�Լ� ����
	public CmdExtractor(String recv) {
		this.testString = recv;
		run();
	}
	
	public void run() {
		//���Ϸκ��� �о�� String�߿��� '�� �����ϰ� "�� 1�� �̻��� Cmd�� ã�� ����.
		Pattern pattern = Pattern.compile("'\"+");
		matcher = pattern.matcher(testString);
	}
	
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
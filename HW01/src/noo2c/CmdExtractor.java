package noo2c;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by eschough on 2019-09-02.
 */
 
// find command pattern from input, and convert it to cmd
// CmdExtractor는 input으로부터 CMD pattern을 찾아 parsing

public class  CmdExtractor {
	public enum Cmds {
		// pattern (명령어를 상수화)
		CMD1("'\""), //출력하고 리턴
		CMD2("'\"\""), //+1하고 리턴
		CMD3("'\"\"\""), //0을 리턴
		CMD4("'\"\"\"\""), //x 수행, 다음 y 수행, y결과 리턴
		CMD5("'\"\"\"\"\""); //x가  0이 아닐때 y출력, 리턴 ,0이면 z출력, 리턴
		
		String matchedStr;
		
		private Cmds(String recvString){
			this.matchedStr = recvString;
		}
    }
	
	String testString = null;
	static Matcher matcher = null;
	//static List<Cmds> cmdsList = new ArrayList<Cmds>();
	
	//객체가 생성 되었을 때 입력받은 데이터를 가지고 Cmd를 찾는 run함수 실행
	public CmdExtractor(String recv) {
		this.testString = recv;
		run();
	}
	
	public void run() {
		//파일로부터 읽어온 String중에서 '로 시작하고 "가 1개 이상인 Cmd를 찾아 낸다.
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

package noo2c;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by eschough on 2019-09-02.
 * SIMSEUNGMIN 201702034
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
		
		//enum 생성자
		private Cmds(String recvString){
			//this.matchedStr = recvString;
		}
    }
	
	//파일에서 읽어온 문자열
	String testString = null;
	//문자열에서 pattern과 일치하는 문자열을 찾는 matcher
	static Matcher matcher = null;
	
	//객체가 생성되었을 때 입력받은 문자열과 정규식을 매치시키는 run함수 호출.
	public CmdExtractor(String recv) {
		this.testString = recv;
		run();
	}
	
	//정규식을 만드고 matcher에 testString과 정규식을 매치시킨다.
	public void run() {
		//정규식을 패턴으로 만들고 (String중에서 '로 시작하고 "가 1개 이상인 Cmd)
		Pattern pattern = Pattern.compile("'\"+");
		//패턴을 tesstString과 매치시킨다.
		matcher = pattern.matcher(testString);
	}
	
	//호출될 경우 맞는 find를 사용하여 매치되는 문자열을 하나 찾고
	//group으로 문자열을 호출한 다음 해당하는 CMD를 enum 형태로 반환한다.
	//호출될 떄마다 그 다음번 해당하는 문자열을 찾아낸다.
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

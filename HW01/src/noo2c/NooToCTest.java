package noo2c;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by eschough on 2019-09-02.
 */

public class NooToCTest {
    public static void main(String[] args) {
    	String s = "";
    	
		//String filePath = "C:/Users/user/Desktop/PLASLAB/test1.noo";
		String filePath = "C:/Users/user/Desktop/test1.noo";
    	try (FileInputStream fstream= new FileInputStream(filePath);){		
    		byte[] rb = new byte[fstream.available()];
    		while(fstream.read(rb) != -1) {}
    		fstream.close();
    		s = new String(rb);
    		
    		//읽어온 값 출력
    		System.out.println("input : " + s);
    		
    	}catch(Exception e) {
    		e.getStackTrace();
    	}
    	
    	try (FileWriter fw = new FileWriter("test.c");){
       		NooToC ntc = null;
    		ntc = new NooToC(fw,s);
    		ntc.translate(ntc.next());
			fw.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
}

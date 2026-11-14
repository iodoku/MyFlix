package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakeRandomPw {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 5; i++) {
			String cngPw = MakeRandom();
			System.out.println("자릿수 : " + cngPw.length());
			System.out.println(cngPw);
		}
	}
	
	public static String MakeRandom() {
		String lower = "abcdefghijklmnopqrstuvwxyz";
		String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String digits = "1234567890";
		String specials = "?!@#$%^&*";
		
		List<Character> charlist = new ArrayList<>();
		
		charlist.add(lower.charAt((int)(Math.random() * lower.length())));
		charlist.add(upper.charAt((int)(Math.random() * upper.length())));
		charlist.add(digits.charAt((int)(Math.random() * digits.length())));
		charlist.add(specials.charAt((int)(Math.random() * specials.length())));
		
		int maxlen = (int)(Math.random() * 10) + 6;
		
		String totalseed = lower + upper + digits + specials;
		while(charlist.size() < maxlen) {
			int randidx = (int)(Math.random() * totalseed.length());
			charlist.add(totalseed.charAt(randidx));
		}
		
		Collections.shuffle(charlist);
		
		StringBuilder code = new StringBuilder();
		for (char c : charlist) {
			code.append(c);
		}
		
		return code.toString();
	}

}

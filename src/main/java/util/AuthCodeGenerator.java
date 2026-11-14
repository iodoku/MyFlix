package util;

import java.util.Random;

public class AuthCodeGenerator 
{
    
    public static String generateAuthCode() 
    {
        Random random = new Random();
        // 0부터 999999 사이의 무작위 숫자 생성
        int num = random.nextInt(1000000); 
      
        // %06d: 6자리 숫자로 만들되, 빈 자리는 앞쪽에 0으로 채움 (예: 123 -> "000123")
        return String.format("%06d", num);
    }
}
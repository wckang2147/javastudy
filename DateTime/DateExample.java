import java.util.Calendar;
import java.util.Date;
 
public class DateExample {
  public static void main(String[] args){
    new DateExample();
  }
  public DateExample(){
    //비교일
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
 
    //기준일
    Date d = new Date();
    
    //현재날짜 : 현재날짜
    String out = Check(d,c.getTime());
    System.out.println(out);
    
    //현재날짜 : 현재날짜 2달전
    c.add(Calendar.MONTH,-2);
    out = Check(d,c.getTime());
    System.out.println(out);
    
    //현재날짜 : 현재날짜 1달후
    c.add(Calendar.MONTH, 3);
    out = Check(d,c.getTime());
    System.out.println(out);
    
    //예상데이터
    //날짜가 같습니다.
    //a가 b보다 날짜가 느립니다.
    //a가 b보다 날짜가 빠릅니다.
  }
  protected String Check(Date a,Date b){
    if(a.compareTo(b) > 0){
      return "a가 b보다 날짜가 느립니다.";
    }else if(a.compareTo(b) < 0){
      return "a가 b보다 날짜가 빠릅니다.";
    }else{
      return "날짜가 같습니다.";
    }
  }
}

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
public class DateExample2 {
  public static void main(String... args){
    try{
      new DateExample2();
    }catch(Throwable e){
      e.printStackTrace();
    }
  }
  public DateExample2() throws ParseException{
    
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    //문자를 데이터형으로 변경
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date d = df.parse("2015-01-16");
    System.out.println(d);
             
    //데이터형에서 문자형으로 변경
    String buf = df.format(c.getTime());
    System.out.println(buf);
    
  }
}

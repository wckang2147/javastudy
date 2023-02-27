package com.lgcns.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MyDateTime {

	public static void main(String[] args) throws Exception {
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusSeconds(2);

		System.out.println("time1 = " + time1 + ", time2 = " + time2);
		
		while (true)
		{
			LocalTime now = LocalTime.now();
			if ( now.isAfter(time2) )
			{
				System.out.println("After 2 sec");
				break;
			}
			else {
				System.out.println("now = " + now);
				Thread.sleep(900);
			}
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd hh:mm-ss");
		LocalDateTime startDT = LocalDateTime.of(2023,2,27,9,0,0);
		LocalDateTime secondDT = LocalDateTime.of(2023,3,1,9,0,0);
		
		System.out.println( "formatted date = " +  startDT.format(formatter));
		
		Period period = Period.between(startDT.toLocalDate(), secondDT.toLocalDate());
		System.out.println("period = " + period.getDays());
		
		LocalDateTime nextDT = LocalDateTime.now().plusSeconds(3);
		System.out.println("next 2 sec = " + nextDT);

		while (true) {
			
			LocalDateTime now2 = LocalDateTime.now();
			long diff = now2.until(nextDT, ChronoUnit.SECONDS);
			System.out.println(now2 + " - " + nextDT + " = " + diff);
			if (diff <= 0 )
			{
				System.out.println("time over");
				break;
			}
			Thread.sleep(900);
			long between = ChronoUnit.SECONDS.between(now2, nextDT );
			System.out.println("remained = " + between);
		}
		
		
	}

}

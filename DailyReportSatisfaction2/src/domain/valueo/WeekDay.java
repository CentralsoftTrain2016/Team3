package domain.valueo;

public class WeekDay extends Value{

	private String weekDay;		// 曜日

	/* コンストラクタ */
	public WeekDay(String weekDay){
		super();
		String week = weekDay;
		//０と６（土日）を５（金曜）に置き換える
		if(week.equals("0") || week.equals("6")){
			week = "5";
		}
		this.weekDay = week;
	}

	// 曜日を取得
	public String getWeekDay(){
		return weekDay;
	}

}

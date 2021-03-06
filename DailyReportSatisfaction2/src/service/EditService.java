
package service;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import db.ConfigDao;
import db.DraftDao;
import db.TemplateDao;
import db.TemplateRuleDao;
import db.UserDao;
import domain.Config;
import domain.Draft;
import domain.Template;
import domain.TemplateRule;
import domain.User;
import domain.valueo.DepartmentID;
import domain.valueo.DraftID;
import domain.valueo.TemplateID;
import domain.valueo.Text;
import domain.valueo.UserID;
import domain.valueo.WeekDay;

public class EditService extends Service {

	// private TemplateDao tdao;
	private ConfigDao cdao;
	private DraftDao ddao;
	private TemplateDao tdao;
	private TemplateRuleDao trdao;
	private UserDao udao;

	@Override
	void prePare() {

		// this.tdao = new TemplateDao(this.con);
		this.cdao = new ConfigDao(this.con);

		this.ddao = new DraftDao(this.con);
		this.tdao = new TemplateDao(this.con);
		this.trdao = new TemplateRuleDao(this.con);
		this.udao = new UserDao(this.con);
	}


	// 設定の読み込み
	public List<Config> getConfig(UserID userID) {
		List<Config> configList;
		try {
			configList = cdao.getConfig(userID);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return configList;
	}


	// 下書き更新
	public void updateDraft(List<Draft> draftList) {
		try {
			for (Draft draft : draftList) {
				ddao.updateDraft(new DraftID(draft.getDraftID()), new Text(draft.getText()));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ルールとテンプレートを特定し、下書きを読み込む
	public List<Draft> getDraft(UserID userID, DepartmentID departmentID) {
		List<Draft> drafts = null;
		TemplateID templateID = null;
		// 曜日取得
		// 1～7の数字で返ってくる(1が日曜)
		Calendar cal = Calendar.getInstance();
		int week = cal.get(Calendar.DAY_OF_WEEK);
		week--;
		String weekStr = String.valueOf(week);
		WeekDay weekDay = new WeekDay(weekStr);

		// ルールを特定し、テンプレートを決定
		try {
			TemplateRule tmp = trdao.getTemplateRuleID(weekDay, departmentID);
			if(tmp == null){
				templateID = new TemplateID("0001");
			}else{
			templateID = new TemplateID(tmp.getTEMPLATEID());
			}
			drafts = ddao.getAndInsertDraft(templateID, userID);

			//下書き読み込み
			drafts = ddao.getDraft(templateID, userID);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		return drafts;
	}

	// テンプレート手動選択
	public List<Draft> getSelectDraft(UserID userID, TemplateID templateID) {
		List<Draft> drafts = null;
		// 下書き読み込み
		try {
			drafts = ddao.getAndInsertDraft(templateID, userID);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		return drafts;
	}

	/* 件名自動生成メソッド */
	public String createMailTitle(User user) {
		String autoTitle = "【日報】"; // 自動生成する件名
		String userName = ""; // ユーザ名
		String trainer = ""; // トレーナ名
		String date = ""; // 日付

		// 今日の日付を取得
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		date = sdf.format(d);

		Date date1 = null;
		Date date2 = null;

		try {
			date1 = DateFormat.getDateInstance().parse(date); // 現在の日付
			date2 = DateFormat.getDateInstance().parse("2016/05/16"); // トレーニング開始日
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// 現在の日付とトレーニング開始日の差を算出
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		long one_date_time = 1000 * 60 * 60 * 24;
		long diffDays = (datetime1 - datetime2) / one_date_time;
		int diff = (int) diffDays;

		// 曜日取得
		// 1～7の数字で返ってくる(1が日曜)
		Calendar cal = Calendar.getInstance();
		int week = cal.get(Calendar.DAY_OF_WEEK);

		// TrainingStartday ts = new TrainingStartday("5/16");
		// String trainingStartday = ts.toString();

		boolean flg = false;

		// int diff = trainingStartday.compareTo(date);

		// trueがトレーナA
		// 開始日が今日かつ月曜
		if (diff == 0 && week == 2) {
			flg = true;
		} else if ((diff == 1) || (diff == 2) || (diff == 3) || (diff == 4) || (diff == 14) || (diff == 15)
				|| (diff == 16) || (diff == 17) || (diff == 18) || (diff == 28) || (diff == 29) || (diff == 30)
				|| (diff == 31) || (diff == 32)) {
			flg = true;
		}

		// ユーザ名を取得
		userName = user.getName().getName(); // DBからユーザIDを使ってユーザ名を持ってくる

		// トレーナ名を取得
		try {
			if (flg == true) { // トレーナABどっちか判断する処理
				trainer = udao.getTrainerAName(user.getTrainerAID().getTrainerAID(), user.getUserID().getUserID());
			} else {
				trainer = udao.getTrainerBName(user.getTrainerBID().getTrainerBID(), user.getUserID().getUserID());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// 日付を取得してdateに格納
		Date d2 = new Date();
		SimpleDateFormat sdf2 = new SimpleDateFormat("M/d");
		date = sdf2.format(d2);

		// 件名を生成する
		autoTitle = autoTitle + userName + " → " + trainer + " " + date;

		return autoTitle;
	}

	public List<Template> getTemplates() {
		List<Template> templates = null;
		try {
			templates = tdao.getAllTemplate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return templates;
	}

	// サニタイジング
	public List<Draft> convertSanitize(List<Draft> drafts) {
		if (drafts == null) {
			return drafts;
		}
		for (Draft draft : drafts) {
			String item = draft.getItemName();
			String text = draft.getText();
			if (item != null) {
				item = item.replaceAll("&", "&amp;");
				item = item.replaceAll("<", "&lt;");
				item = item.replaceAll(">", "&gt;");
				item = item.replaceAll("\"", "&quot;");
				item = item.replaceAll("'", "&#39;");
			}
			draft.setItemName(item);
			if (text != null) {
				text = text.replaceAll("&", "&amp;");
				text = text.replaceAll("<", "&lt;");
				text = text.replaceAll(">", "&gt;");
				text = text.replaceAll("\"", "&quot;");
				text = text.replaceAll("'", "&#39;");
			}
			draft.setText(text);
		}
		return drafts;
	}


}

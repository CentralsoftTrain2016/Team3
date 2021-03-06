package service;
import java.sql.SQLException;

import db.UserDao;
import domain.User;
public class LoginService extends Service{
	private UserDao udao;


	@Override
	void prePare() {
		this.udao = new UserDao( this.con);
	}
//ユーザーの取得
	public User getUser( String id,String password)
	{
	User user = null;

	try {
		user = udao.getUser(id, password);
	} catch (SQLException e)
	{
		throw new RuntimeException(e);
	}
	return user;
}

//ユーザーの新規作成
	//public void createUser(UserID userID,Password password, Name name, DepartmentID departmentID
	//		,TrainerAID trainerAID,TrainerBID trainerBID,Mailaddress mailAddress){

	public void createUser(User user) throws AlreadyUsedException{
		try {
			udao.createUser(user);
		}

		catch (SQLException e) {
			int code = e.getErrorCode();
			if(code == 1)
			{
				//throw new RuntimeException(e);
				throw new AlreadyUsedException(e);
			}
			throw new RuntimeException(e);
		}
	}

}

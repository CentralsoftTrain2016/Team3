package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.ReceiveMail;
import domain.valueo.UserID;

public class MailDao extends Dao {

	public MailDao(Connection con) {
		super(con);
	}

	// --------------------------------------------------------
	// DBにあるメールを受け取る
	private static final String GET_MAIL_SQL =
			"select"
			+ " title , text , receiveMailID "
			+ " from"
			+ " ReceiveMailTABLE "
			+ " Where "
			+ " UserID = ? ";

	public List<ReceiveMail> getReceiveMail(UserID userID) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		ReceiveMail receiveMail;
		List<ReceiveMail> mailList = new ArrayList<ReceiveMail>();
		try {

			/* Statementの作成 */
			stmt = con.prepareStatement(GET_MAIL_SQL);
			stmt.setString(1, userID.getUserID());

			/* SQL実行 */
			rset = stmt.executeQuery();
			/* セット */
			while (rset.next()) {
				receiveMail = new ReceiveMail();
				receiveMail.setTitle(rset.getString(1));
				receiveMail.setText(rset.getString(2));
				receiveMail.setUserId(userID.getUserID());
				receiveMail.setRecieveMailId(rset.getString(3));

				mailList.add(receiveMail);
			}
		}

		catch (SQLException e) {
			throw e;

		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
				stmt = null;
			}
			if (rset != null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
				rset = null;
			}
		}
		return mailList;
	}

	// DBにあるメールを受け取る
		private static final String GET_MAIL_TEXT_SQL =
				"select"
				+ " title , text , UserID "
				+ " from"
				+ " ReceiveMailTABLE "
				+ " Where "
				+ " receiveMailID = ? ";

		public List<ReceiveMail> getMailText(String receiveMailID) throws SQLException {
			PreparedStatement stmt = null;
			ResultSet rset = null;
			ReceiveMail receiveMail;
			List<ReceiveMail> mailList = new ArrayList<ReceiveMail>();
			try {

				/* Statementの作成 */
				stmt = con.prepareStatement(GET_MAIL_TEXT_SQL);
				stmt.setString(1, receiveMailID);

				/* SQL実行 */
				rset = stmt.executeQuery();
				/* セット */
				while (rset.next()) {
					receiveMail = new ReceiveMail();
					receiveMail.setTitle(rset.getString(1));
					receiveMail.setText(rset.getString(2));
					receiveMail.setUserId(rset.getString(3));
					receiveMail.setRecieveMailId(receiveMailID);

					mailList.add(receiveMail);
				}
			}

			catch (SQLException e) {
				throw e;

			} finally {

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
						throw e;
					}
					stmt = null;
				}
				if (rset != null) {
					try {
						rset.close();
					} catch (SQLException e) {
						e.printStackTrace();
						throw e;
					}
					rset = null;
				}
			}
			return mailList;
		}
}
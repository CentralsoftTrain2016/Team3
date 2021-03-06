package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.TemplateRule;
import domain.valueo.DepartmentID;
import domain.valueo.WeekDay;


public class TemplateRuleDao extends Dao {

	public TemplateRuleDao(Connection con) {
		super(con);
	}

	private static final String GET_RULE_SQL =
			  " select "
			+ "  templateID "
			+ " FROM "
			+ " TEMPLATERULETABLE "
			+ " where "
			+ " weekday = ? "
			+ " AND "
			+ " DEPARTMENTID = ? "
			+ " AND "
			+ " TO_CHAR(SYSDATE,'YYYY/MM/DD') >= TO_CHAR(STARTDATE) "
			+ " AND "
			+ " TO_CHAR(SYSDATE,'YYYY/MM/DD') <= TO_CHAR(ENDDATE) ";


	public TemplateRule getTemplateRuleID(WeekDay weekDay,DepartmentID departmentID) throws  SQLException
	{
		 PreparedStatement stmt = null;
		  ResultSet rset = null;

		  TemplateRule  trID = null;

		  try{
				stmt = con.prepareStatement(GET_RULE_SQL);
				stmt.setString(1, weekDay.getWeekDay());
				stmt.setString(2, departmentID.getDepartmentID());

				rset = stmt.executeQuery();

				while (rset.next())
					{
						trID = new TemplateRule();


						trID.setTEMPLATEID(rset.getString(1));
					}
			}catch (SQLException e) {
				throw e;
			}
			catch ( Exception e){
				throw e;
			}
			finally{
				if(stmt != null){
				  stmt.close();
				  stmt = null;
				}
				if(rset != null){
				  rset.close();
				  rset = null;
				}
			}
		return trID;
	}


}

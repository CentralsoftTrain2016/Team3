package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.User;
import domain.valueo.Communication;
import domain.valueo.DepartmentID;
import domain.valueo.InputRequiredException;
import domain.valueo.InvalidMailAddressFormat;
import domain.valueo.MailHostUrl;
import domain.valueo.MailPassword;
import domain.valueo.Mailaddress;
import domain.valueo.Name;
import domain.valueo.Password;
import domain.valueo.Technology;
import domain.valueo.TrainerAID;
import domain.valueo.TrainerBID;
import domain.valueo.UserID;
import domain.valueo.Work;
import service.AlreadyUsedException;
import service.LoginService;
import service.bean.RegistBean;

/**
 * Servlet implementation class RegistUserServlet
 */
@WebServlet("/RegistUserServlet")
public class RegistUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		LoginService service = null;

		try{
			service = dodoGet(request, response);
		}catch( Throwable t){
			if(service != null)
				service.rollebackEnd();
			throw t;
		}finally{
			if(service != null){
				service.end();
			}
		}
	}

	@SuppressWarnings("finally")
	private LoginService dodoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		LoginService service = null;
		String userID        = request.getParameter("userID");
		String password      = request.getParameter("password");
		String name          = request.getParameter("name");
		String trainerAID    = request.getParameter("trainerAID");
		String trainerBID    = request.getParameter("trainerBID");
		String departmentID  = request.getParameter("departmentID");
		String mailAddress   = request.getParameter("mailAddress");
		String atto = "@";

		//バリューオブジェクトを使ったサンプル実装
		MailHostUrl mailHostUrlo;
		MailPassword mailPasswordo;
		try {
			mailHostUrlo = MailHostUrl.getInstanceWithMaintain(
					request.getParameter(MailHostUrl.class.getName())
					);
			mailPasswordo = MailPassword.getInstanceWithMaintain(
					request.getParameter(MailPassword.class.getName())
					);
			mailPasswordo.encriptPassword();

		} catch (InputRequiredException e1) {
			//必須入力チェックもバリューオブジェクトで行う。

			String message = "空欄があります。";
			RegistBean bean = new RegistBean();
			bean.setMessage(message);
			request.setAttribute("bean", bean);
			RequestDispatcher disp = request.getRequestDispatcher("/RegistServlet");
			disp.forward(request, response);
			return service;
		}

		UserID userIDo;
		Password passwordo;
		Name nameo;
		TrainerAID trainerAIDo;
		TrainerBID trainerBIDo;
		DepartmentID departmentIDo;
		Mailaddress mailAddresso;


		userIDo        = new UserID       (new String(userID       .getBytes("ISO8859-1"),"UTF-8") );
		passwordo      = new Password     (new String(password     .getBytes("ISO8859-1"),"UTF-8") );
		nameo          = new Name         (new String(name         .getBytes("ISO8859-1"),"UTF-8") );
		trainerAIDo    = new TrainerAID   (new String(trainerAID   .getBytes("ISO8859-1"),"UTF-8") );
		trainerBIDo    = new TrainerBID   (new String(trainerBID   .getBytes("ISO8859-1"),"UTF-8") );
		departmentIDo  = new DepartmentID (new String(departmentID .getBytes("ISO8859-1"),"UTF-8") );

		//	メールアドレスフォーマットチェックをバリューオブジェクトに移行
		try {
			mailAddresso   = new Mailaddress(
					new String(mailAddress  .getBytes("ISO8859-1"),"UTF-8")
					);
		} catch (InvalidMailAddressFormat e1) {
			RegistBean bean = new RegistBean();
			bean.setMessage(e1.getMessage());
			request.setAttribute("bean", bean);
			RequestDispatcher disp = request.getRequestDispatcher("/RegistServlet");
			disp.forward(request, response);

			return service;
		}


		//入力がない場合は、同じ画面に戻る
		if(		userID == null
			|| 	userID.equals("")
			|| 	password == null
			|| 	password.equals("")
			||  name == null
			||  name.equals("")
			||  trainerAID == null
			||  trainerAID.equals("")
			||  trainerBID == null
			||  trainerBID.equals("")
			||  departmentID == null
			||  departmentID.equals("")
			||  mailAddress == null
			||  mailAddress.equals("")
//			||  mailHostUrlo.toString().equals("")
//			||  mailPasswordo.toString().equals("")
			){
			String message = "空欄があります。";
			RegistBean bean = new RegistBean();
			bean.setMessage(message);
			request.setAttribute("bean", bean);
			RequestDispatcher disp = request.getRequestDispatcher("/RegistServlet");
			disp.forward(request, response);
			return service;
		}

//メールアドレスフォーマットチェックをバリューオブジェクトに移行
//		if(mailAddress.indexOf(atto) == -1){
//			String message = "メールアドレスの形式が正しくありません。";
//			RegistBean bean = new RegistBean();
//			bean.setMessage(message);
//			request.setAttribute("bean", bean);
//			RequestDispatcher disp = request.getRequestDispatcher("/RegistServlet");
//			disp.forward(request, response);
//
//			return service;
//		}


		//サービスからユーザーを取得
		service = new LoginService();
		service.start();

		try{

			User user = new User(
					userIDo,
					passwordo,
					nameo,
					departmentIDo,
					trainerAIDo,
					trainerBIDo,
					mailAddresso,
					new Communication(""),
					new Work(""),
					new Technology(""),
					mailPasswordo,
					mailHostUrlo
					);

			service.createUser(user);

//		}catch(RuntimeException e){
//			e.printStackTrace();
//			String message = "そのIDは使用されています。";
//			RegistBean bean = new RegistBean();
//			bean.setMessage(message);
//			request.setAttribute("bean", bean);
//			RequestDispatcher disp = request.getRequestDispatcher("/RegistServlet");
//			disp.forward(request, response);
//			return service;
		} catch (AlreadyUsedException e) {
			String message = "そのIDは使用されています。";
			RegistBean bean = new RegistBean();
			bean.setMessage(message);
			request.setAttribute("bean", bean);
			RequestDispatcher disp = request.getRequestDispatcher("/RegistServlet");
			disp.forward(request, response);
			return service;
		}

		// セッションにセット
		request.getSession().setAttribute("userID", userID);
		// リクエストにセット
		request.setAttribute("userName", nameo.getName());

		// ログイン画面に遷移
		RequestDispatcher disp = request.getRequestDispatcher("/LoginServlet");
		disp.forward(request, response);

		return service;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

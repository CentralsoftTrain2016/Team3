package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.User;
import domain.valueo.Point;
import service.EditService;
import service.PointService;

@WebServlet("/EditPointAddServlet")
public class EditPointAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditPointAddServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// user持ってくる
		User user = (User) request.getSession().getAttribute("user");
		// サービス開始
		EditService service = null;
		PointService pointService = null;
		service = new EditService();
		pointService = new  PointService();
		service.start();
		pointService.start(service);

		String point = request.getParameter("point");

		if (point == null || point.equals("")) {

		}else{

		//文字化けのおまじない
		point =new String(point.getBytes("ISO8859-1"),"UTF-8");
		Point pointWrite = new Point(point);

		// 指摘保存
		pointService.setPoint(user.getUserID(), pointWrite);
		// EditServletへ飛ぶ
		//RequestDispatcher disp = request.getRequestDispatcher("/EditServlet");
		//disp.forward(request, response);
		}

		response.sendRedirect("MailTextServlet");

	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

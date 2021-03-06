package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Draft;
import domain.User;
import domain.valueo.Destination;
import domain.valueo.InvalidMailAddressFormat;
import domain.valueo.Mailaddress;
import domain.valueo.Sender;
import domain.valueo.TemplateID;
import domain.valueo.Title;
import service.PreviewService;
import service.bean.PreviewBean;

/**
 * Servlet implementation class PreviewServlet
 */
@WebServlet("/PreviewServlet")
public class PreviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PreviewServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
																throws ServletException, IOException {
		PreviewService ps = null;

		try
		{
			TemplateID templateID = new TemplateID(request.getParameter("templateID"));				// テンプレートID
			//String trainerAddress = request.getParameter("trainerAddress");		// 宛先

			User user = (User)request.getSession().getAttribute("user");

			// プレビューサービスを生成
			ps = new PreviewService();
			ps.start();

			Sender sender = (Sender)request.getAttribute("sender");			// 送信者
			Mailaddress senderAddress = null;

			try
			{
				senderAddress = new Mailaddress(user.getMailAddress().getMailAddress());
			}
			catch (InvalidMailAddressFormat e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			//送信アドレス
			Destination destination = (Destination)request.getAttribute("destination");		// 宛先

			Title title = (Title)request.getAttribute("title");								// 件名
			//Title title = new Title( request.getParameter("title"));								// 件名

			List<Draft> drafts = ps.getIndentDraft(templateID, user.getUserID());	// 本文

			//再編集用に下書きをセッションに入れる
			request.getSession().setAttribute("drafts",drafts);

			// PreviewBeanに値をセット
			PreviewBean bean = new PreviewBean();
			bean.setSender(sender);
			bean.setSenderAddress(senderAddress);
			bean.setDestination(destination);
			bean.setTitle(title);
			bean.setDraft(drafts);
			StringBuffer textBuffer = ps.createText(drafts);
			StringBuffer sendTextBuffer = ps.createSendText(drafts);

			bean.setText(textBuffer.toString());
			bean.setSendText(sendTextBuffer.toString());

			// プレビュー画面に値を渡して遷移
			request.setAttribute("bean", bean);
			RequestDispatcher disp = request.getRequestDispatcher("Preview.jsp");
			disp.forward(request, response);
		}
		catch( Throwable t )
		{
			ps.rollebackEnd();
			throw t;
		}
		finally
		{
			if( ps!= null )
				ps.end();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}

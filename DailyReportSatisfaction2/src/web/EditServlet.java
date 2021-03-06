package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Config;
import domain.Draft;
import domain.Template;
import domain.User;
import domain.valueo.TemplateID;
import domain.valueo.Title;
import service.EditService;
import service.GoogleSheetService;
import service.MailService;
import service.bean.EditBean;

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// user持ってくる
		User user = (User) request.getSession().getAttribute("user");

		// EditBeanつくり！
		EditBean editBean = new EditBean();

		// サービス開始
		EditService service = null;
		GoogleSheetService gService = null;
		MailService  mailService =null;
		service = new EditService();
		gService = new GoogleSheetService();
		mailService =new MailService();
		service.start();
		gService.start(service);
		mailService.start(service);

		//遷移元によって読み込む下書きを変える
		List<Draft> drafts = null;
		String referer = (String)request.getAttribute("referer");

		if(referer != null && referer.equals("EditSelectTemplateServlet"))
		{
			TemplateID templateID = (TemplateID)request.getAttribute("templateID");
			drafts = service.getSelectDraft(user.getUserID(), templateID);
		}
		else
		{
			drafts = service.getDraft(user.getUserID(), user.getDepartmentID());
		}

		// 目標目的の適用
		drafts = gService.setGoal(drafts, user.getUserID());

		//最後に表示したメール本文
		String lastMail = (String)request.getSession(true).getAttribute(MailTextServlet.LAST_DISP_MAIL_SESSION_KEY);

		//引用の適用
		drafts = mailService.setLastMail(drafts, user.getUserID() ,lastMail );

		editBean.setDrafts(drafts);

		//無害化する
		drafts = service.convertSanitize(drafts);

		//テンプレートゲット！
		List<Template> templates = service.getTemplates();
		editBean.setTemplates(templates);

		// 設定取り出し
		List<Config> configList = service.getConfig(user.getUserID());
		editBean.setConfigList(configList);
		// 名前セット
		editBean.setName(user.getName());

		// 件名を自動生成 String autoTitle =
		//service.createMailTitle(user.getUserID());

		// 件名を自動生成
		String autoTitle = service.createMailTitle(user);
		Title title = new Title(autoTitle);
		editBean.setTitle(title);

		request.setAttribute("bean", editBean);
		RequestDispatcher disp = request.getRequestDispatcher("/Edit.jsp");
		disp.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request, response);
	}

}
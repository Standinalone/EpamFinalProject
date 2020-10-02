package com.epam.project.tag;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class CustomTag extends SimpleTagSupport {
	public static final ResourceBundle rb = ResourceBundle.getBundle("info");
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println(rb.getString("info"));

//	      PageContext context = (PageContext) getJspContext();
//	      HttpServletRequest request = (HttpServletRequest) context.getRequest();
//	      StringBuilder sb = new StringBuilder();
//	      String localeCurr = request.getSession().getAttribute("locale");
//	      sb.append("Your locale is :")
	}
}

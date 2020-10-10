package com.epam.project.tag;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Custom tag used to retrieve parameters from the ResourceBundle
 *
 */
public class CustomTag extends SimpleTagSupport {
	private String param;

	public void setParam(String param) {
		this.param = param;
	}

	private static final ResourceBundle rb = ResourceBundle.getBundle("info");

	/**
	 * Returns value for the given key in a resources file
	 */
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println(rb.getString(param));
	}
}

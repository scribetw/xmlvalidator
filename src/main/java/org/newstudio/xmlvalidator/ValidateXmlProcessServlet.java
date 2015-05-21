package org.newstudio.xmlvalidator;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 * ValidateXmlProcessServlet.
 *
 * @author Scribe Huang
 */
public class ValidateXmlProcessServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			return;
		}

		ServletFileUpload upload = new ServletFileUpload();
		ErrorHandler e = new ErrorHandler(false);
		String charset = "UTF-8";
		String ftype = "";
		String strXML = "";
		InputStream isXSD = null;

		try {
			// Parse the request
			FileItemIterator iter = upload.getItemIterator(req);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				InputStream stream = item.openStream();

				if ("charset".equals(name)) {
					charset = IOUtils.toString(stream);
				}
				if ("filetype".equals(name)){
					ftype = IOUtils.toString(stream);
				}
				if ("fileXML".equals(name)){
					strXML = IOUtils.toString(stream, charset);
				}
				if ("fileXSD".equals(name)){
					isXSD = IOUtils.toInputStream(
						IOUtils.toString(stream, charset), "UTF-8"
					);
				}
			}
		} catch (Exception ee) {
		}

		if ("xsd".equals(ftype)) {
			XsdXmlValidator vXSD = new XsdXmlValidator(e);
			vXSD.setSchema(isXSD);
			resp.setContentType("text/xml; charset=utf-8");
			resp.getWriter().println(vXSD.validate(strXML));
		} else { // DTD, XPath mode
			DtdXmlValidator vDTD = new DtdXmlValidator(e);
			resp.setContentType("text/xml; charset=utf-8");
			if ("xpath".equals(ftype)) {
				vDTD.disableValidating();
			}
			resp.getWriter().println(vDTD.validate(strXML));
		}

		// Clean up
		IOUtils.closeQuietly(isXSD);
	}
}

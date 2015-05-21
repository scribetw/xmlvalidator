package org.newstudio.xmlvalidator;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 * Use SAX Parser 驗證 XML (使用 W3C XML DTD)
 *
 * @author Scribe Huang
 */
public class DtdXmlValidator implements XmlValidator {
	/** Error Handler */
	private ErrorHandler e;
	/** SAXParserFactory 物件 */
	private SAXParserFactory spf = SAXParserFactory.newInstance();
	/**  */
	private boolean isValidating = true;

	public DtdXmlValidator(ErrorHandler e) {
		setErrorHandler(e);
	}

	@Override
	public void setErrorHandler(ErrorHandler e) {
		this.e = e;
	}

	/**
	 * Disable the validator. For XPath.
	 */
	public void disableValidating() {
		isValidating = false;
	}

	@Override
	public String validate(String fileXML) {
		try {
			spf.setValidating(isValidating);
			SAXParser sp = spf.newSAXParser();
			sp.parse(
				IOUtils.toInputStream(fileXML, "UTF-8"),
				e != null ? e : null
			);

			// 遇錯不停止處理錯誤訊息方式 (因為無throws Exception)
			if (e != null && e.caughtError()){
				return toXML("-ERR", fileXML, e.flushErrorMessage());
			}
			return toXML("+OK", fileXML, "驗證成功!");
		} catch (ParserConfigurationException pe) { // parser 設定錯誤
			return toXML("-ERR", fileXML, "剖析器設定錯誤!");
		} catch (IOException ie) { // IO 錯誤
			return toXML("-ERR", fileXML, "檔案處理錯誤!" + ie.getMessage());
		} catch (SAXException se) { // SAX 驗證錯誤
			return toXML("-ERR", fileXML, "<errors>" + se.getMessage() + "</errors>");
		}
	}

	private String toXML(String status, String xml, String errormsg) {
		StringBuffer body = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		body.append("<result>\n");
		body.append("\t<status>" + status + "</status>\n");
		body.append("\t<xml><![CDATA[" + xml + "]]></xml>\n");
		body.append("\t<message>\n" + errormsg + "</message>\n");
		body.append("</result>\n");
		return body.toString();
	}
}

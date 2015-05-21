package org.newstudio.xmlvalidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * 使用 JAXP 新類別 (javax.xml.validation) 驗證 XML (使用 W3C XML Schema Definition)
 *
 * @author Scribe Huang
 */
public class XsdXmlValidator implements XmlValidator {
	/** Error handler */
	private ErrorHandler e;
	/** schemaFactory 物件 */
	private SchemaFactory schemaFactory = SchemaFactory.newInstance(
		javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI
	);
	/** Schema */
	private Schema schema;

	public XsdXmlValidator(ErrorHandler e) {
		setErrorHandler(e);
	}

	@Override
	public void setErrorHandler(ErrorHandler e) {
		this.e = e;
	}

	// 設定 Schema
	public void setSchema(InputStream schema) {
		try {
			this.schema = schemaFactory.newSchema(new StreamSource(schema));
		} catch(Exception e) {
			this.schema = null;
		}
	}

	@Override
	public String validate(String fileXML) {
		try {
			// 預設驗證用 Schema (自 XML 檔案中判斷)
			if (schema == null) {
				schema = schemaFactory.newSchema();
			}

			Validator validator = schema.newValidator();
			validator.setErrorHandler(e);
			validator.validate(new StreamSource(new StringReader(fileXML)));

			// 遇錯不停止處理錯誤訊息方式 (因為無throws Exception)
			if (e != null && e.caughtError()) {
				return toXML("-ERR", fileXML, e.flushErrorMessage());
			}
			return toXML("+OK", fileXML, "驗證成功!");
		} catch (IOException ie) { // IO 錯誤
			ie.printStackTrace();
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

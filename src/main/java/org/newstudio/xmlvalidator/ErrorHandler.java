package org.newstudio.xmlvalidator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX 錯誤處理物件，顯示詳細驗證失敗資訊
 *
 * @author Scribe Huang
 */
public class ErrorHandler extends DefaultHandler {
	/** 是否在遇到驗證錯誤就停止 */
	private boolean stopOnError = false;
	/** 錯誤訊息緩衝區 */
	private StringBuffer errorMessage = new StringBuffer();

	// 標準建構元
	public ErrorHandler() {
		this(false);
	}

	// 帶設定的建構元:
	public ErrorHandler(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}

	// 格式化為易懂的錯誤訊息
	private String getMsg(SAXParseException spe) {
		String msg = String.format(
			"\t<line>%d</line>\n\t<column>%d</column>\n\t<message><![CDATA[ %s ]]></message>\n",
			spe.getLineNumber(), spe.getColumnNumber(), spe.getMessage());
		return msg;
	}

	// 是否捕捉到錯誤訊息
	public boolean caughtError() {
		return errorMessage.length() > 0;
	}

	// 回傳錯誤訊息
	public String flushErrorMessage() {
		String err = "<errors>\n" + errorMessage.toString() + "</errors>\n";
		errorMessage.setLength(0); // 清空
		return err;
	}

	// 錯誤攔截: 警告層級
	@Override
	public void warning(SAXParseException spe) throws SAXException {
		String err = "<error>\n\t<level>warning</level>\n" + getMsg(spe) + "</error>\n";
		errorMessage.append(err);
		if (stopOnError) {
			throw new SAXException(err);
		}
	}

	// 錯誤攔截: 錯誤層級
	@Override
	public void error(SAXParseException spe) throws SAXException {
		String err = "<error>\n\t<level>error</level>\n" + getMsg(spe) + "</error>\n";
		errorMessage.append(err);
		if (stopOnError) {
			throw new SAXException(err);
		}
	}

	// 錯誤攔截: 嚴重錯誤層級
	@Override
	public void fatalError(SAXParseException spe) throws SAXException {
		String err = "<error>\n\t<level>fatal</level>\n" + getMsg(spe) + "</error>\n";
		// 嚴重錯誤將會強制中斷驗證
		throw new SAXException(err);
	}
}

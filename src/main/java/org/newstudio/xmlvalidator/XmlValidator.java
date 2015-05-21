package org.newstudio.xmlvalidator;

/**
 * XML validator interface.
 *
 * @author Scribe Huang
 */
public interface XmlValidator {
	/**
	 * Set the error handler.
	 *
	 * @param e Error handler object
 	 */
	void setErrorHandler(ErrorHandler e);

	/**
	 * Validate the XML and get the result.
	 *
	 * @param fileXML XML content
	 * @return result
 	 */
	String validate(String fileXML);
}

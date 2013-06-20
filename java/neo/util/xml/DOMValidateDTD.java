package neo.util.xml;

import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

public class DOMValidateDTD {
  public static void main(String args[]) {  
    try{
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
        //Ignore the fatal errors
        public void fatalError(SAXParseException exception)throws SAXException { }
        //Validation errors 
        public void error(SAXParseException e)throws SAXParseException {
          System.out.println("Error at " +e.getLineNumber() + " line.");
          System.out.println(e.getMessage());
          System.exit(0);
        }
        //Show warnings
        public void warning(SAXParseException err)throws SAXParseException{
          System.out.println(err.getMessage());
          System.exit(0);
        }
      });
      Document xmlDocument = builder.parse(new FileInputStream("Employeexy.xml"));
      DOMSource source = new DOMSource(xmlDocument);
      StreamResult result = new StreamResult(System.out);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "Employee.dtd");
      transformer.transform(source, result);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
} 

/*
<?xml version = "1.0" ?>
<!DOCTYPE Employee SYSTEM "Employee.dtd">
<Employee>
<Emp_Id> E-001 </Emp_Id>
<Emp_Name> Vinod </Emp_Name>
<Emp_E-mail> Vinod1@yahoo.com </Emp_E-mail>
</Employee> 

Here is the DTD File: Employee.dtd

<!ELEMENT Employee (Emp_Id, Emp_Name, Emp_E-mail)>
<!ELEMENT Emp_Id (#PCDATA)>
<!ELEMENT Emp_Name (#PCDATA)>
<!ELEMENT Emp_E-mail (#PCDATA)> 
*/


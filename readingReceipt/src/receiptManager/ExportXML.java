package receiptManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ExportXML {
public void generateXML(MyReceipt myRcpt) {
		
		Document doc = new Document();  
		
		Element receipt = new Element("receipt");
		Element vendor = new Element("vendor");
		Element name = new Element("name");
		Element address = new Element("address");
		Element phone = new Element("phone");
		Element date = new Element("date");
		Element time = new Element("time");
		Element total = new Element("total");
		Element tax = new Element("tax");
		Element recognizedItem = new Element("recognizedItem");
		
		
		receipt.addContent(vendor);
			vendor.addContent(name);
			vendor.addContent(address);
			vendor.addContent(phone);
		receipt.addContent(date);
		receipt.addContent(time);
		receipt.addContent(total);
		receipt.addContent(tax);
		receipt.addContent(recognizedItem);
		
		Map<String, Element> n = new HashMap<String, Element>();
		
		for (int i = 0 ; i < myRcpt.rcgItem.size() ; i++) {
			n.put("item" + i, new Element("item"));
			n.get("item" + i).setAttribute("index", (i+1)+"");
			n.get("item" + i).addContent(new Element("name").setText(myRcpt.getRcgItem().get(i).get("name")));
			n.get("item" + i).addContent(new Element("total").setText(myRcpt.getRcgItem().get(i).get("total")));
			recognizedItem.addContent(n.get("item" + i));
		}
		
		name.setText(myRcpt.vendor);
		address.setText(myRcpt.address);
		phone.setText(myRcpt.phone);
		date.setText(myRcpt.date);
		time.setText(myRcpt.time);
		total.setText(myRcpt.total);
		tax.setText(myRcpt.tax);
		recognizedItem.setAttribute("count", myRcpt.rcgItem.size()+"");
		
		doc.setRootElement(receipt);
		
		try {                                                             
			FileOutputStream out = new FileOutputStream(myRcpt.xmlExportPath); 
			XMLOutputter serializer = new XMLOutputter();                 
			
			Format f = serializer.getFormat();                            
			f.setEncoding("UTF-8");
			f.setIndent(" ");                                             
			f.setLineSeparator("\r\n");                                   
			f.setTextMode(Format.TextMode.TRIM);                          
			serializer.setFormat(f);                                      
			
			// Save results to XML
			System.out.println( "Saving results..." );
			serializer.output(doc, out);                                  
			out.flush();                                                  
			out.close();    
			
			//String 으로 xml 출력
			// XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8")) ;
			// System.out.println(outputter.outputString(doc));
		} catch (IOException e) {                                         
			System.err.println(e);                                        
		}     
		
	}

}

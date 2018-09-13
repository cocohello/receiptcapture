package receiptManager;

import java.util.ArrayList;
import java.util.HashMap;

import com.abbyy.ReceiptCapture.Engine;
import com.abbyy.ReceiptCapture.EngineException;
import com.abbyy.ReceiptCapture.IEngine;
import com.abbyy.ReceiptCapture.IEngineLoader;
import com.abbyy.ReceiptCapture.IImageOpeningParams;
import com.abbyy.ReceiptCapture.IImagePreprocessingParams;
import com.abbyy.ReceiptCapture.IReceipt;
import com.abbyy.ReceiptCapture.IReceiptLineItems;
import com.abbyy.ReceiptCapture.IReceiptRecognizer;
import com.abbyy.ReceiptCapture.IReceiptSynthesisParams;
import com.abbyy.ReceiptCapture.ReceiptCountriesEnum;
import com.abbyy.ReceiptCapture.ThreeStatePropertyValueEnum;

public class ReceiptOCR {
	String dataDir = "C:\\ProgramData\\ABBYY\\SDK\\11\\ReceiptCapture";
	String tempDir = "C:\\Users\\user_bbn043\\AppData\\Local\\Temp\\ABBYY Receipt Capture SDK 1";
	String dllPath = dataDir+"\\ReceiptCapture.dll";
	String serialNumber = "SWAT11010006528417813217";
	IEngineLoader engineLoader = null;
	IEngine engn = null;
	IImageOpeningParams iop = null;
	IReceiptSynthesisParams rsp = null;
	IImagePreprocessingParams ipp = null;
	ThreeStatePropertyValueEnum treatPhotoFlag = ThreeStatePropertyValueEnum.TSPV_No;
	int processFlag = 0;
	MyReceipt myRcpt = new MyReceipt();
	
	public void loadEngn () {
		System.out.println("Initializing Engine");
				
		try {
			//Load the Engine
			engineLoader = Engine.GetEngineOutprocLoader();
			engn = engineLoader.GetEngineObjectEx(serialNumber, dataDir, tempDir, true, null, null);
			
			rsp = engn.CreateReceiptSynthesisParams();
			rsp.setCountriesOfOrigin(ReceiptCountriesEnum.RC_Japan.getValue());
			
			ipp = engn.CreateImagePreprocessingParams();
            ipp.setCorrectOrientation(true);
            ipp.setCorrectSkew(ThreeStatePropertyValueEnum.TSPV_Yes);
            
            iop = engn.CreateImageOpeningParams();
            iop.setTreatAsPhoto(treatPhotoFlag);
			
		} catch (EngineException eE) {
			// TODO: handle exception
			System.out.println("EngineExMessage = "+eE.getMessage());
			System.out.println("HResult = "+Integer.toString(eE.getHResult()));
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
	
	}
	
	public void processImage(String rcptPath) {
		//Create a ReceiptRecognizer object and process receipts
		IReceiptRecognizer recognizer =  engn.CreateReceiptRecognizer();
		String xmlExportPath = rcptPath.substring(0, rcptPath.lastIndexOf(".")) + ".xml";
		try {
			// Add image file to recognizer
			System.out.println( "Loading image" );
			recognizer.AddImageFile( rcptPath, iop );

			// Process images in the recognizer
			System.out.println( "Processing image" );
			recognizer.Process( ipp, rsp );
			
			if (recognizer.getReceipts().getCount() == 0) {
                throw new Exception( "No receipts in the file" );
            }

			IReceipt receiptData = recognizer.getReceipts().getElement(0);
			
			while( processFlag == 0 && recognizer.getReceipts().getCount() > 0 ) {

				processFlag++;
				treatPhotoFlag = ThreeStatePropertyValueEnum.TSPV_Yes;
				
				//1st save receipt
				setToMyReceipt(receiptData);
				
				if(myRcpt.getXmlExportPath()==null) {
					myRcpt.setXmlExportPath(xmlExportPath);
				}
				
				if (!receiptData.getIsRecognized()
					||receiptData.getVendorField()==null
					||receiptData.getPhoneField()==null
					||receiptData.getAddressField()==null
					||receiptData.getDateField()==null
					||receiptData.getTimeField()==null
					||receiptData.getTotalField()==null
					||receiptData.getTotalTaxField()==null
					||receiptData.getReceiptLineItems()==null
				) {
					loadEngn();
					break;
				}
				
			}
			
			while( processFlag == 1 ) {

				processFlag=-1;
				
				//2nd save receipt
				setToMyReceipt(receiptData);
				
			}
			
			//generate XML file
			ExportXML eXML = new ExportXML();
			eXML.generateXML(myRcpt);
			
		} catch( Exception ex ) {
			System.out.println( ex.getMessage() );
		} finally {
			// Close recognizer
			recognizer.Close();
			// Deinitialize Engine
			System.out.println("Deinitializing Engine");
			engn = null;
			System.runFinalization();
			engineLoader.ExplicitlyUnload();
			engineLoader = null;
			System.runFinalization();
		}
		
	}

	private void setToMyReceipt(IReceipt receiptData) {
		
		if(receiptData.getVendorField()!=null
				&& myRcpt.getVendor()==null) {
			myRcpt.setVendor(receiptData.getVendorField().getRecognizedText().getText());
		}
		if(receiptData.getAddressField()!=null
				&& myRcpt.getAddress()==null) {
			myRcpt.setAddress(receiptData.getAddressField().getRecognizedText().getText());
		}
		if(receiptData.getPhoneField()!=null
				&& myRcpt.getPhone()==null) {
			myRcpt.setPhone(receiptData.getPhoneField().getRecognizedText().getText());
		}
		if(receiptData.getDateField()!=null
				&& myRcpt.getDate()==null) {
			myRcpt.setDate(receiptData.getDateField().getYear()+"/"+receiptData.getDateField().getMonth()+"/"+receiptData.getDateField().getDay());
		}
		if(receiptData.getTimeField()!=null
				&& myRcpt.getTime()==null) {
			myRcpt.setTime(receiptData.getTimeField().getHour()+":"+receiptData.getTimeField().getMinute());
		}
		if(receiptData.getTotalField()!=null
				&& myRcpt.getTotal()==null) {
			myRcpt.setTotal(receiptData.getTotalField().getRecognizedText().getText());
		}
		if(receiptData.getTotalTaxField()!=null
				&& myRcpt.getTax()==null) {
			myRcpt.setTax(receiptData.getTotalTaxField().getRecognizedText().getText());
		}
		if(receiptData.getReceiptLineItems()!=null
				&& myRcpt.getRcgItem()==null) {
			myRcpt.setRcgItem(saveRecognizedItem(receiptData.getReceiptLineItems()));
		}
	}
	
	private ArrayList<HashMap<String, String>> saveRecognizedItem(IReceiptLineItems lineItem){
		ArrayList<HashMap<String, String>> rcgItem = new ArrayList<>();
		for(int i = 0 ; i <lineItem.getCount(); i++) {
			HashMap<String, String> rcpt = new HashMap<>();
			if ( lineItem.getCount() > 0 ) {
				rcpt.put("name", lineItem.Item(i).getItemName().toString().replaceAll("¥", ""));
				rcpt.put("total", (lineItem.Item(i).getTotal()/100)+"");
				rcgItem.add(rcpt);
			}
		}
		return rcgItem;
	}
		

}

package receiptManager;

import java.util.ArrayList;

public class OcrMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
try {
			
			ReceiptConfig rcCfg = new ReceiptConfig();
			ArrayList<String> receipts = rcCfg.GetReceiptFolder(args);
			
			for (String rcptPath : receipts) {
				ReceiptOCR rcOCR = new ReceiptOCR();
				rcOCR.loadEngn();
				rcOCR.processImage(rcptPath);
			}
		} catch( Exception ex ) {
			System.out.println(ex.getStackTrace());
		}
	}

}

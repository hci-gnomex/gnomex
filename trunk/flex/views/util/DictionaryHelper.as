package views.util
{
	import hci.flex.util.DictionaryManager;

	public class DictionaryHelper
	{
		private var dictionaryManager:DictionaryManager;
		
		public function DictionaryHelper(dictionaryManager:DictionaryManager) {
			this.dictionaryManager = dictionaryManager;
		}
		
		// Given the code application function returns the first active protocol associated with the application.
		// This is useful for Illumina applications which by process have only one active protocol associated with them.
		public function getProtocolFromApplication(codeApplication:String):XML {
			var returnProtocol:XML = null;
			if (codeApplication != '') {
				var protocols:XMLList = dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.SeqLibProtocolApplication').DictionaryEntry.(@value != '' && @codeApplication == codeApplication);
				for each (var p:XML in protocols) {
					var protocol:XMLList = dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.SeqLibProtocol').DictionaryEntry.(@value != '' && @idSeqLibProtocol == p.@idSeqLibProtocol.toString());
					if (protocol != null && protocol[0].@isActive.toString() == 'Y') {
						returnProtocol = protocol[0];
						break;
					}
				}
			}

			return returnProtocol;
		}
		
		public function getApplicationForProtocol(idSeqLibProtocol:String):XML {
			var returnApplication:XML = null;
			var appCodes:XMLList = dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.SeqLibProtocolApplication').DictionaryEntry.(@value != '' && @idSeqLibProtocol == idSeqLibProtocol);
			if (idSeqLibProtocol != '') {
				for each (var a:XML in appCodes) {
					var app:XMLList = dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.Application').DictionaryEntry.(@value != '' && @codeApplication == a.@codeApplication.toString());
					returnApplication = app[0];
				}
			}
			
			return returnApplication;
		}
	}
}
package views.util
{
	public class FileUtil
	{
		public static const  KB:Number = 1024;
		public static const  MB:Number = Math.pow(1024,2) ;
		public static const  GB:Number = Math.pow(1024,3);		

		public function FileUtil()
		{
		}
		
		public static function getFileSizeText(theFileSize:Number):String {
			var size:Number = 0;
			var sizeTxt:String = "";
			if (theFileSize > GB ) {
				// Round gigabyte to the one decimal place
				size = Math.round((theFileSize / GB) * 10) / 10;
				if (size == 0) {
					size = 1;
				} 
				sizeTxt = size + " GB";
			}  else if (theFileSize > MB ) {
				// Round megabyte to neareast whole number
				size = Math.round(theFileSize / MB);
				if (size == 0) {
					size = 1;
				}
				sizeTxt = size + " MB";
			} else if (theFileSize > KB ) {
				// Round kb to the nearest 100th place
				size = Math.round((theFileSize / KB) / 100) * 100;
				if (size == 0) {
					size = 1;
				}
				sizeTxt = size + " KB";
			} else {
				// Round bytes to nearest 100th place
				size = Math.round(theFileSize / 100) * 100;
				if (size == 0) {
					size = 1;
				}
				sizeTxt = size + " bytes";
			}
			return sizeTxt;
		}
		
		public static function getEstimatedCompressedSize(item:Object):Number {
			var compressionRatio:Number = 1; 
			if (item.@type == 'fep') {
				compressionRatio = 1.6; 
			} else if (item.@type.toString().toUpperCase() == 'PDF') {
				compressionRatio = 1;
			} else if (item.@type.toString().toUpperCase() == 'TIF' ||
				item.@type.toString().toUpperCase() == 'TIFF') {
				compressionRatio = 1.9;
			} else if (item.@type.toString().toUpperCase() == 'JPG') {
				compressionRatio = 1;
			} else if (item.@type.toString().toUpperCase() == 'TXT') {
				compressionRatio = 2.7;  
			} else if (item.@type.toString().toUpperCase() == 'RTF') {
				compressionRatio = 2.7;
			} else if (item.@type.toString().toUpperCase() == 'DAT') {
				compressionRatio = 1.6;
			} else if (item.@type.toString().toUpperCase() == 'CEL') { 
				compressionRatio = 2.8;
			} else if (item.@type.toString().toUpperCase() == 'ZIP') {
				compressionRatio = 1;
			}
			
			return (item.@fileSize / compressionRatio);
		}

		
	}
	
}
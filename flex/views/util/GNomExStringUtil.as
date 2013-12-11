package views.util
{
	import flash.utils.Dictionary;
	
	public class GNomExStringUtil
	{
		public static const  KB:Number = 1024;
		public static const  MB:Number = Math.pow(1024,2) ;
		public static const  GB:Number = Math.pow(1024,3);		
		public static var    unicodeToAsciiMap:Dictionary = null;

		public function GNomExStringUtil() {
		}

		public static function startsWith(string:String, pattern:String, caseSensitive:Boolean=true):Boolean {
			if (!caseSensitive) {
				string = string.toLowerCase();
				pattern = pattern.toLowerCase();
			}
			return pattern == string.substr(0, pattern.length);
		}
		
		public static function endsWith(string:String, pattern:String, caseSensitive:Boolean=true):Boolean {
			if (!caseSensitive) {
				string = string.toLowerCase();
				pattern = pattern.toLowerCase();
			}
			if (string.length < pattern.length) {
				return false;
			} else {
				return pattern == string.substr(string.length - pattern.length, pattern.length);
			}
		}
		
		public static function cleanRichTextHTML(htmlText:String):String {
			var pattern:RegExp = /<TEXTFORMAT.*?>/g;
			var str:String = htmlText.replace(pattern, "");
			pattern = /<FONT.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<\/FONT.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<\/TEXTFORMAT.*?>/g;
			str = str.replace(pattern, "");
			
			return str;		    
		}
		
		public static function stripHTMLText(htmlText:String):String {
			var pattern:RegExp = /<P.*?>/g;
			var str:String = htmlText.replace(pattern, "");
			pattern = /<\/P.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<B.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<\/B.*?>/g;
			str = str.replace(pattern, "");			
			pattern = /<U.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<\/U.*?>/g;
			str = str.replace(pattern, "");	
			pattern = /<LI.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<\/LI.*?>/g;
			str = str.replace(pattern, "");	
			pattern = /<I.*?>/g;
			str = str.replace(pattern, "");
			pattern = /<\/I.*?>/g;
			str = str.replace(pattern, "");	
			
			//pattern = /<U.*?>/g;
			//str = str.replace(pattern, "");
			//pattern = /<\/U.*?>/g;
			//str = str.replace(pattern, "");					
			return GNomExStringUtil.cleanRichTextHTML(str);
		}
		
		public static function sortAppUsers(obj1:Object, obj2:Object, fields:Array=null):int {
			if (obj1 == null && obj2 == null) {
				return 0;
			} else if (obj1 == null) {
				return 1;
			} else if (obj2 == null) {
				return -1;
			} else {
				var display1:String = obj1.@displayName;
				var display2:String = obj2.@displayName;
				
				if (display1.toLowerCase() < display2.toLowerCase()) {
					return -1;
				} else if (display1.toLowerCase() > display2.toLowerCase()) {
					return 1;
				} else {
					return 0;
				}
				
			}
		}
		
		// Altered escape function to exclude spaces and other characters
		// that are normally escaped for url but don't need to be escaped
		// here.
		public static function myEscape( str:String ):String
		{
			if(str == null || str.length == 0) {
				return str;
			}
			var a:Array  = str.split( "" );
			var i:Number = a.length;
			
			while( i -- )
			{
				var n:Number = a[ i ].charCodeAt( 0 );
				
				// TAB LF VT FF CR
				if( n >= 9 && n <= 13 )
					continue;
				
				// & ' ( ) * + , - . / 0 1 2 3 4 5 6 7 8 9 : ;
				if( n >= 38 && n <= 59 )
					continue;
				
				// ? @ A B C D E F G H I J K L M N O P Q R S T U V W X Y Z [ \ ] ^
				// _ ` a b c d e f g h i j k l m n o p q r s t u v w x y z { | } ~
				if( n >= 63 && n <= 126 )
					continue;
				
				// sp ! # $ = _ + 
				if(n == 32 || n == 33 || n == 35 || n == 36 || n == 61)
					continue;
				/*
				Lucene can't handle these characters once they've been escaped so just leave them alone (hopefully they won't be used)
				if(n >= 8192) {
				a[ i ] = "%u" + n.toString( 16 ).toUpperCase();
				} else {
				a[ i ] = "%" + n.toString( 16 ).toUpperCase();
				}
				*/
				a[ i ] = "%" + n.toString( 16 ).toUpperCase();
				
			}
			
			return a.join( "" );
		} 
		
		// Maps a unicode string to an ascii string.  It also replaces all non-printable ascii characters (including cr/lf) with ?.
		public static function unicodeToAscii( str:String ):String {
			if(str == null || str.length == 0) {
				return str;
			}
			
			initUnicodeToAsciiMap();
			
			var a:Array  = str.split( "" );
			var i:Number = a.length;
			
			while( i -- )
			{
				var n:Number = a[ i ].charCodeAt( 0 );

				if (n < 32) {
					a[i] = "?";
				} else if (n > 127) {
					if (unicodeToAsciiMap[n] != null) {
						a[i] = unicodeToAsciiMap[n];
					} else {
						a[i] = "?";
					}
				}
			}
			
			return a.join( "" );
		}
		
		// this maps specific unicode characters to ascii equivalents that look almost the same
		public static function initUnicodeToAsciiMap():void {
			if (unicodeToAsciiMap == null) {
				unicodeToAsciiMap = new Dictionary();
				unicodeToAsciiMap[0x00A0] = " ";
				unicodeToAsciiMap[0x00A1] = "!";
				unicodeToAsciiMap[0x00A3] = "#";
				unicodeToAsciiMap[0x00A6] = "|";
				unicodeToAsciiMap[0x00A8] = '"';
				unicodeToAsciiMap[0x00A9] = "C";
				unicodeToAsciiMap[0x00AB] = "<";
				unicodeToAsciiMap[0x00AE] = "R";
				unicodeToAsciiMap[0x00AF] = "-";
				unicodeToAsciiMap[0x00B4] = "'";
				unicodeToAsciiMap[0x00BB] = ">";
				unicodeToAsciiMap[0x00BF] = "?";
				unicodeToAsciiMap[0x00C0] = "A";
				unicodeToAsciiMap[0x00C1] = "A";
				unicodeToAsciiMap[0x00C2] = "A";
				unicodeToAsciiMap[0x00C3] = "A";
				unicodeToAsciiMap[0x00C4] = "A";
				unicodeToAsciiMap[0x00C5] = "A";
				unicodeToAsciiMap[0x00C7] = "C";
				unicodeToAsciiMap[0x00C8] = "E";
				unicodeToAsciiMap[0x00C9] = "E";
				unicodeToAsciiMap[0x00CA] = "E";
				unicodeToAsciiMap[0x00CB] = "E";
				unicodeToAsciiMap[0x00CC] = "I";
				unicodeToAsciiMap[0x00CD] = "I";
				unicodeToAsciiMap[0x00CE] = "I";
				unicodeToAsciiMap[0x00CF] = "I";
				unicodeToAsciiMap[0x00D0] = "D";
				unicodeToAsciiMap[0x00D1] = "N";
				unicodeToAsciiMap[0x00D2] = "O";
				unicodeToAsciiMap[0x00D3] = "O";
				unicodeToAsciiMap[0x00D4] = "O";
				unicodeToAsciiMap[0x00D5] = "O";
				unicodeToAsciiMap[0x00D6] = "O";
				unicodeToAsciiMap[0x00D9] = "U";
				unicodeToAsciiMap[0x00DA] = "U";
				unicodeToAsciiMap[0x00DB] = "U";
				unicodeToAsciiMap[0x00DC] = "U";
				unicodeToAsciiMap[0x00DD] = "Y";
				unicodeToAsciiMap[0x00DF] = "B";
				unicodeToAsciiMap[0x00E0] = "a";
				unicodeToAsciiMap[0x00E1] = "a";
				unicodeToAsciiMap[0x00E2] = "a";
				unicodeToAsciiMap[0x00E3] = "a";
				unicodeToAsciiMap[0x00E4] = "a";
				unicodeToAsciiMap[0x00E5] = "a";
				unicodeToAsciiMap[0x00E7] = "c";
				unicodeToAsciiMap[0x00E8] = "e";
				unicodeToAsciiMap[0x00E9] = "e";
				unicodeToAsciiMap[0x00EA] = "e";
				unicodeToAsciiMap[0x00EB] = "i";
				unicodeToAsciiMap[0x00EC] = "i";
				unicodeToAsciiMap[0x00ED] = "i";
				unicodeToAsciiMap[0x00EF] = "i";
				unicodeToAsciiMap[0x00F0] = "o";
				unicodeToAsciiMap[0x00F1] = "n";
				unicodeToAsciiMap[0x00F2] = "o";
				unicodeToAsciiMap[0x00F3] = "o";
				unicodeToAsciiMap[0x00F4] = "o";
				unicodeToAsciiMap[0x00F5] = "o";
				unicodeToAsciiMap[0x00F6] = "o";
				unicodeToAsciiMap[0x00F7] = "/";
				unicodeToAsciiMap[0x00F9] = "u";
				unicodeToAsciiMap[0x00FA] = "u";
				unicodeToAsciiMap[0x00FB] = "u";
				unicodeToAsciiMap[0x00FC] = "u";
				unicodeToAsciiMap[0x00FD] = "y";
				unicodeToAsciiMap[0x00FF] = "y";
				unicodeToAsciiMap[0x0100] = "A";
				unicodeToAsciiMap[0x0101] = "a";
				unicodeToAsciiMap[0x0102] = "A";
				unicodeToAsciiMap[0x0103] = "a";
				unicodeToAsciiMap[0x0104] = "A";
				unicodeToAsciiMap[0x0105] = "a";
				unicodeToAsciiMap[0x0106] = "C";
				unicodeToAsciiMap[0x0107] = "c";
				unicodeToAsciiMap[0x0108] = "C";
				unicodeToAsciiMap[0x0109] = "c";
				unicodeToAsciiMap[0x010A] = "C";
				unicodeToAsciiMap[0x010B] = "c";
				unicodeToAsciiMap[0x010C] = "C";
				unicodeToAsciiMap[0x010D] = "c";
				unicodeToAsciiMap[0x010E] = "D";
				unicodeToAsciiMap[0x010F] = "F";
				// Ok -- got tired of entering equivalences.  Can do more later.  The dash below is the one I know is needed.
				unicodeToAsciiMap[0x2212] = "-";
			}
		}
		
	}
	
}
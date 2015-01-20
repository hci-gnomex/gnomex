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
		
		// Escapes any XML special characters
		public static function makeXMLSafe( s:String ):String {
			var pattern:RegExp;
			var str:String = s;
			pattern = /&/;
			str = str.replace(pattern, "&amp;");
			pattern = /</;
			str = str.replace(pattern, "&lt;");
			pattern = />/;
			str = str.replace(pattern, "&gt;");
			pattern = /\'/;
			str = str.replace(pattern, "&apos;");
			pattern = /\"/;
			str = str.replace(pattern, "&quot;");
			
			return str;
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
				unicodeToAsciiMap[0x0110] = "D";
				unicodeToAsciiMap[0x0111] = "d";
				unicodeToAsciiMap[0x0112] = "E";
				unicodeToAsciiMap[0x0113] = "e";
				unicodeToAsciiMap[0x0114] = "E";
				unicodeToAsciiMap[0x0115] = "e";
				unicodeToAsciiMap[0x0116] = "E";
				unicodeToAsciiMap[0x0117] = "e";
				unicodeToAsciiMap[0x0118] = "E";
				unicodeToAsciiMap[0x0119] = "e";
				unicodeToAsciiMap[0x011A] = "E";
				unicodeToAsciiMap[0x011B] = "e";
				unicodeToAsciiMap[0x011C] = "G";
				unicodeToAsciiMap[0x011D] = "g";
				unicodeToAsciiMap[0x011E] = "G";
				unicodeToAsciiMap[0x011F] = "g";
				unicodeToAsciiMap[0x0120] = "G";
				unicodeToAsciiMap[0x0121] = "g";
				unicodeToAsciiMap[0x0122] = "G";
				unicodeToAsciiMap[0x0123] = "g";
				unicodeToAsciiMap[0x0124] = "H";
				unicodeToAsciiMap[0x0125] = "h";
				unicodeToAsciiMap[0x0126] = "H";
				unicodeToAsciiMap[0x0127] = "h";
				unicodeToAsciiMap[0x0128] = "I";
				unicodeToAsciiMap[0x0129] = "i";
				unicodeToAsciiMap[0x012A] = "I";
				unicodeToAsciiMap[0x012B] = "i";
				unicodeToAsciiMap[0x012C] = "I";
				unicodeToAsciiMap[0x012D] = "i";
				unicodeToAsciiMap[0x012E] = "I";
				unicodeToAsciiMap[0x012F] = "i";
				unicodeToAsciiMap[0x0130] = "I";
				unicodeToAsciiMap[0x0131] = "i";
				unicodeToAsciiMap[0x0134] = "J";
				unicodeToAsciiMap[0x0135] = "j";
				unicodeToAsciiMap[0x0136] = "K";
				unicodeToAsciiMap[0x0137] = "k";
				unicodeToAsciiMap[0x0138] = "K";
				unicodeToAsciiMap[0x0139] = "L";
				unicodeToAsciiMap[0x013A] = "l";
				unicodeToAsciiMap[0x013B] = "L";
				unicodeToAsciiMap[0x013C] = "l";
				unicodeToAsciiMap[0x013D] = "L";
				unicodeToAsciiMap[0x013E] = "l";
				unicodeToAsciiMap[0x013F] = "L";
				unicodeToAsciiMap[0x0140] = "l";
				unicodeToAsciiMap[0x0141] = "L";
				unicodeToAsciiMap[0x0142] = "l";
				unicodeToAsciiMap[0x0143] = "N";
				unicodeToAsciiMap[0x0144] = "n";
				unicodeToAsciiMap[0x0145] = "N";
				unicodeToAsciiMap[0x0146] = "n";
				unicodeToAsciiMap[0x0147] = "N";
				unicodeToAsciiMap[0x0148] = "n";
				unicodeToAsciiMap[0x0149] = "n";
				unicodeToAsciiMap[0x014A] = "N";
				unicodeToAsciiMap[0x014B] = "n";
				unicodeToAsciiMap[0x014C] = "O";
				unicodeToAsciiMap[0x014D] = "o";
				unicodeToAsciiMap[0x014E] = "O";
				unicodeToAsciiMap[0x014F] = "o";
				unicodeToAsciiMap[0x0150] = "O";
				unicodeToAsciiMap[0x0151] = "o";
				unicodeToAsciiMap[0x0154] = "R";
				unicodeToAsciiMap[0x0155] = "r";
				unicodeToAsciiMap[0x0156] = "R";
				unicodeToAsciiMap[0x0157] = "r";
				unicodeToAsciiMap[0x0158] = "R";
				unicodeToAsciiMap[0x0159] = "r";
				unicodeToAsciiMap[0x015A] = "S";
				unicodeToAsciiMap[0x015B] = "s";
				unicodeToAsciiMap[0x015C] = "S";
				unicodeToAsciiMap[0x015D] = "s";
				unicodeToAsciiMap[0x015E] = "S";
				unicodeToAsciiMap[0x015F] = "s";
				unicodeToAsciiMap[0x0160] = "S";
				unicodeToAsciiMap[0x0161] = "s";
				unicodeToAsciiMap[0x0162] = "T";
				unicodeToAsciiMap[0x0163] = "t";
				unicodeToAsciiMap[0x0164] = "T";
				unicodeToAsciiMap[0x0165] = "t";
				unicodeToAsciiMap[0x0166] = "T";
				unicodeToAsciiMap[0x0167] = "t";
				unicodeToAsciiMap[0x0168] = "U";
				unicodeToAsciiMap[0x0169] = "u";
				unicodeToAsciiMap[0x016A] = "U";
				unicodeToAsciiMap[0x016B] = "u";
				unicodeToAsciiMap[0x016C] = "U";
				unicodeToAsciiMap[0x016D] = "u";
				unicodeToAsciiMap[0x016E] = "U";
				unicodeToAsciiMap[0x016F] = "u";
				unicodeToAsciiMap[0x0170] = "U";
				unicodeToAsciiMap[0x0171] = "u";
				unicodeToAsciiMap[0x0172] = "U";
				unicodeToAsciiMap[0x0173] = "u";
				unicodeToAsciiMap[0x0174] = "W";
				unicodeToAsciiMap[0x0175] = "w";
				unicodeToAsciiMap[0x0176] = "Y";
				unicodeToAsciiMap[0x0177] = "y";
				unicodeToAsciiMap[0x0178] = "Y";
				unicodeToAsciiMap[0x0179] = "Z";
				unicodeToAsciiMap[0x017A] = "z";
				unicodeToAsciiMap[0x017B] = "Z";
				unicodeToAsciiMap[0x017C] = "z";
				unicodeToAsciiMap[0x017D] = "Z";
				unicodeToAsciiMap[0x017E] = "z";
				unicodeToAsciiMap[0x017F] = "f";
				unicodeToAsciiMap[0x0180] = "b";
				unicodeToAsciiMap[0x0181] = "B";
				unicodeToAsciiMap[0x0182] = "b";
				unicodeToAsciiMap[0x0183] = "b";
				unicodeToAsciiMap[0x0184] = "b";
				unicodeToAsciiMap[0x0185] = "b";
				unicodeToAsciiMap[0x0186] = "C";
				unicodeToAsciiMap[0x0187] = "C";
				unicodeToAsciiMap[0x0188] = "c";
				unicodeToAsciiMap[0x0189] = "D";
				unicodeToAsciiMap[0x018A] = "D";
				unicodeToAsciiMap[0x018B] = "d";
				unicodeToAsciiMap[0x018C] = "d";
				unicodeToAsciiMap[0x018E] = "E";
				unicodeToAsciiMap[0x018F] = "e";
				unicodeToAsciiMap[0x0191] = "F";
				unicodeToAsciiMap[0x0192] = "f";
				unicodeToAsciiMap[0x0193] = "G";
				unicodeToAsciiMap[0x0196] = "l";
				unicodeToAsciiMap[0x0197] = "I";
				unicodeToAsciiMap[0x0198] = "K";
				unicodeToAsciiMap[0x0199] = "k";
				unicodeToAsciiMap[0x01A0] = "O";
				unicodeToAsciiMap[0x01A1] = "o";
				unicodeToAsciiMap[0x01A4] = "P";
				unicodeToAsciiMap[0x01A5] = "p";
				unicodeToAsciiMap[0x01A6] = "R";
				unicodeToAsciiMap[0x01A7] = "S";
				unicodeToAsciiMap[0x01A8] = "s";
				unicodeToAsciiMap[0x01AB] = "t";
				unicodeToAsciiMap[0x01AC] = "T";
				unicodeToAsciiMap[0x01AD] = "f";
				unicodeToAsciiMap[0x01AE] = "T";
				unicodeToAsciiMap[0x01AF] = "U";
				unicodeToAsciiMap[0x01B0] = "u";
				unicodeToAsciiMap[0x01B5] = "Z";
				unicodeToAsciiMap[0x01B6] = "z";
				unicodeToAsciiMap[0x01C0] = "|";
				unicodeToAsciiMap[0x01C1] = "|";
				unicodeToAsciiMap[0x01C3] = "!";
				unicodeToAsciiMap[0x01CD] = "A";
				unicodeToAsciiMap[0x01CE] = "a";
				unicodeToAsciiMap[0x01CF] = "I";
				unicodeToAsciiMap[0x01D0] = "i";
				unicodeToAsciiMap[0x01D1] = "O";
				unicodeToAsciiMap[0x01D2] = "o";
				unicodeToAsciiMap[0x01D3] = "U";
				unicodeToAsciiMap[0x01D4] = "u";
				unicodeToAsciiMap[0x01D5] = "U";
				unicodeToAsciiMap[0x01D6] = "u";
				unicodeToAsciiMap[0x01D7] = "U";
				unicodeToAsciiMap[0x01D8] = "u";
				unicodeToAsciiMap[0x01D9] = "U";
				unicodeToAsciiMap[0x01DA] = "u";
				unicodeToAsciiMap[0x01DB] = "U";
				unicodeToAsciiMap[0x01DC] = "u";
				unicodeToAsciiMap[0x01DD] = "e";
				unicodeToAsciiMap[0x01DE] = "A";
				unicodeToAsciiMap[0x01DF] = "a";
				unicodeToAsciiMap[0x01E0] = "A";
				unicodeToAsciiMap[0x01E1] = "A";
				unicodeToAsciiMap[0x01E4] = "G";
				unicodeToAsciiMap[0x01E5] = "g";
				unicodeToAsciiMap[0x01E6] = "G";
				unicodeToAsciiMap[0x01E7] = "g";
				unicodeToAsciiMap[0x01E8] = "K";
				unicodeToAsciiMap[0x01E9] = "k";
				unicodeToAsciiMap[0x01F0] = "j";
				unicodeToAsciiMap[0x01F4] = "G";
				unicodeToAsciiMap[0x01F5] = "g";
				unicodeToAsciiMap[0x01F8] = "N";
				unicodeToAsciiMap[0x01F9] = "n";
				unicodeToAsciiMap[0x01FA] = "A";
				unicodeToAsciiMap[0x01FB] = "a";
				unicodeToAsciiMap[0x0200] = "A";
				unicodeToAsciiMap[0x0201] = "a";
				unicodeToAsciiMap[0x0202] = "A";
				unicodeToAsciiMap[0x0203] = "a";
				unicodeToAsciiMap[0x0204] = "E";
				unicodeToAsciiMap[0x0205] = "e";
				unicodeToAsciiMap[0x0206] = "E";
				unicodeToAsciiMap[0x0207] = "e";
				unicodeToAsciiMap[0x0208] = "I";
				unicodeToAsciiMap[0x0209] = "i";
				unicodeToAsciiMap[0x020A] = "I";
				unicodeToAsciiMap[0x020B] = "i";
				unicodeToAsciiMap[0x020C] = "O";
				unicodeToAsciiMap[0x020D] = "o";
				unicodeToAsciiMap[0x020E] = "O";
				unicodeToAsciiMap[0x020F] = "o";
				unicodeToAsciiMap[0x0210] = "R";
				unicodeToAsciiMap[0x0211] = "r";
				unicodeToAsciiMap[0x0212] = "R";
				unicodeToAsciiMap[0x0213] = "r";
				unicodeToAsciiMap[0x0214] = "U";
				unicodeToAsciiMap[0x0215] = "u";
				unicodeToAsciiMap[0x0216] = "U";
				unicodeToAsciiMap[0x0217] = "u";
				unicodeToAsciiMap[0x0218] = "S";
				unicodeToAsciiMap[0x0219] = "s";
				unicodeToAsciiMap[0x021A] = "T";
				unicodeToAsciiMap[0x021B] = "t";
				unicodeToAsciiMap[0x021E] = "H";
				unicodeToAsciiMap[0x021F] = "h";
				unicodeToAsciiMap[0x0226] = "A";
				unicodeToAsciiMap[0x0227] = "a";
				unicodeToAsciiMap[0x0228] = "E";
				unicodeToAsciiMap[0x0229] = "e";
				unicodeToAsciiMap[0x022A] = "O";
				unicodeToAsciiMap[0x022B] = "o";
				unicodeToAsciiMap[0x022C] = "O";
				unicodeToAsciiMap[0x022D] = "o";
				unicodeToAsciiMap[0x022E] = "O";
				unicodeToAsciiMap[0x022F] = "o";
				unicodeToAsciiMap[0x0230] = "O";
				unicodeToAsciiMap[0x0231] = "o";
				unicodeToAsciiMap[0x0232] = "Y";
				unicodeToAsciiMap[0x0233] = "y";
				unicodeToAsciiMap[0x0234] = "l";
				unicodeToAsciiMap[0x0235] = "n";
				unicodeToAsciiMap[0x0236] = "t";
				unicodeToAsciiMap[0x0237] = "j";
				unicodeToAsciiMap[0x023A] = "A";
				unicodeToAsciiMap[0x023B] = "C";
				unicodeToAsciiMap[0x023C] = "c";
				unicodeToAsciiMap[0x023D] = "L";
				unicodeToAsciiMap[0x023E] = "T";
				unicodeToAsciiMap[0x023F] = "S";
				unicodeToAsciiMap[0x0243] = "B";
				unicodeToAsciiMap[0x0244] = "U";
				unicodeToAsciiMap[0x0245] = "A";
				unicodeToAsciiMap[0x0246] = "E";
				unicodeToAsciiMap[0x0247] = "e";
				unicodeToAsciiMap[0x0248] = "J";
				unicodeToAsciiMap[0x0249] = "j";
				unicodeToAsciiMap[0x024A] = "Q";
				unicodeToAsciiMap[0x024B] = "q";
				unicodeToAsciiMap[0x024C] = "R";
				unicodeToAsciiMap[0x024D] = "r";
				unicodeToAsciiMap[0x024E] = "Y";
				unicodeToAsciiMap[0x024F] = "y";
				unicodeToAsciiMap[0x02B9] = "'";
				unicodeToAsciiMap[0x02BA] = '"';
				unicodeToAsciiMap[0x02BC] = "'";
				unicodeToAsciiMap[0x02BD] = "'";
				unicodeToAsciiMap[0x02BE] = "'";
				unicodeToAsciiMap[0x02BF] = "'";
				unicodeToAsciiMap[0x02C2] = "<";
				unicodeToAsciiMap[0x02C3] = ">";
				unicodeToAsciiMap[0x02C4] = "^";
				unicodeToAsciiMap[0x02C6] = "^";
				unicodeToAsciiMap[0x02C8] = "'";
				unicodeToAsciiMap[0x02C9] = "-";
				unicodeToAsciiMap[0x02CA] = "'";
				unicodeToAsciiMap[0x02CB] = "'";
				unicodeToAsciiMap[0x02CC] = ",";
				unicodeToAsciiMap[0x02CD] = "-";
				unicodeToAsciiMap[0x02CE] = ",";
				unicodeToAsciiMap[0x02CF] = ",";
				unicodeToAsciiMap[0x02D0] = ":";
				unicodeToAsciiMap[0x02ED] = "=";
				unicodeToAsciiMap[0x02EE] = '"';
				unicodeToAsciiMap[0x02F0] = "^";
				unicodeToAsciiMap[0x02F1] = "<";
				unicodeToAsciiMap[0x02F2] = ">";
				unicodeToAsciiMap[0x02F4] = "'";
				unicodeToAsciiMap[0x02F5] = '"';
				unicodeToAsciiMap[0x02F6] = '"';
				unicodeToAsciiMap[0x02F7] = "~";
				unicodeToAsciiMap[0x20F8] = ":";
				unicodeToAsciiMap[0x0300] = "'";
				unicodeToAsciiMap[0x0301] = "'";
				unicodeToAsciiMap[0x0302] = "^";
				unicodeToAsciiMap[0x0320] = "-";
				unicodeToAsciiMap[0x0330] = "~";
				unicodeToAsciiMap[0x0331] = "-";
				unicodeToAsciiMap[0x0332] = "-";
				unicodeToAsciiMap[0x0333] = "=";
				unicodeToAsciiMap[0x0334] = "~";
				unicodeToAsciiMap[0x0335] = "-";
				unicodeToAsciiMap[0x0336] = "-";
				unicodeToAsciiMap[0x0337] = "/";
				unicodeToAsciiMap[0x0338] = "/";
				unicodeToAsciiMap[0x0340] = "'";
				unicodeToAsciiMap[0x0341] = "'";
				unicodeToAsciiMap[0x0342] = "-";
				unicodeToAsciiMap[0x0343] = "'";
				unicodeToAsciiMap[0x0347] = "=";
				unicodeToAsciiMap[0x0350] = ">";
				unicodeToAsciiMap[0x0354] = ">";
				unicodeToAsciiMap[0x0355] = "<";
				unicodeToAsciiMap[0x0357] = "'";
				unicodeToAsciiMap[0x0358] = "'";
				unicodeToAsciiMap[0x035E] = "-";
				unicodeToAsciiMap[0x035F] = "-";
				// Ok -- got tired of entering equivalences.  Can do more later.  The dash below is the one I know is needed.
				unicodeToAsciiMap[0x2212] = "-";
			}
		}
		
	}
	
}
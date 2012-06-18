package views.util
{
	public class StringUtil
	{
		public static const  KB:Number = 1024;
		public static const  MB:Number = Math.pow(1024,2) ;
		public static const  GB:Number = Math.pow(1024,3);		

		public function StringUtil()
		{
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
		
		
		
		
		
	}
	
}
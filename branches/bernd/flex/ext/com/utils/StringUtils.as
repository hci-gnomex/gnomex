package ext.com.utils
{
	import mx.utils.StringUtil;
	
	public class StringUtils
	{
		/**
		 * This will convert a camel caps string to a pretty version
		 * ie, "firstName" becomes "First Name"
		 */
		public static function prettify( string:String ):String
		{
			var newString:String = "";
			
			for (var x:uint = 0; x < string.length; x++)
			{
				var char:String = string.charAt( x );
				
				// is letter upper case
				if (char.charCodeAt() <= 90 && newString.length > 0)
				{
					newString += " ";
				}
				
				newString += char;
				
			}
			
			return StringUtils.capitalizeWords( newString.toLowerCase() );
		}		
		
		public static function stripDown( string:String ):String
		{
			string = string.toLowerCase();
			string = StringUtil.trim( string );
			string = string.replace( / /g, "" );
			
			return string;				
		}
		
		public static function stripDownSpecial( string:String):String
		{
		//	string = string.toLowerCase();
			string = StringUtil.trim( string );
			string = string.replace( / /g, "" );
			string = string.replace(/[~%&\\;:"',<>?#\s]/g,"");
			string = string.charAt(0).toLowerCase() + string.substring( 1, string.length );
			return string;
		}

		public static function capitalizeWords( string:String ):String
		{
			var origWords:Array = string.split( " " );
			var newWords:Array = [];
			
			for each (var word:String in origWords)
			{
				newWords.push( StringUtils.capitalize( word ) ); 
			}

			return newWords.join( " " ); 
		}

		public static function capitalize( string:String ):String
		{
			return string.charAt(0).toUpperCase() + string.substring( 1, string.length );
		}
	}
}
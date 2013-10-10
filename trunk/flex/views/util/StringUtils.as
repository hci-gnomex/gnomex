package views.util
{
	public class StringUtils
	{
		public static function startsWith(string:String, pattern:String, caseSensitive:Boolean=true):Boolean {
			if (!caseSensitive) {
				string = string.toLowerCase();
				pattern = pattern.toLowerCase();
			}
			return pattern == string.substr(0, pattern.length);
		}
	}
}
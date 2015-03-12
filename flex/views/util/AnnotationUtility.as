package views.util
{
	import mx.utils.StringUtil;
	import views.util.AnnotationUtility;
	
	public class AnnotationUtility
	{
		public function AnnotationUtility()
		{
		}
		
		public static function isApplicableProperty(property:Object, reqCategory:Object, idOrganism:String, codeApplication:String=null):Boolean {
			if (property == null) {
				return false;
			}
			
			var filterByOrganism:Boolean = false;
			if (property.descendants("Organism").length() > 0) {
				filterByOrganism = true;	
			}
			
			var filterByPlatformApplication:Boolean = false;
			if (reqCategory != null) {
				if (( property.@forRequest != null && property.@forRequest == 'Y' ) ||
						property.descendants("PropertyPlatformApplication").length() > 0 || 
						reqCategory.@type == 'ISCAN' ||
						reqCategory.@type == 'SEQUENOM' ||
						reqCategory.@type == 'ISOLATION') {
					filterByPlatformApplication = true;	
				}
			}			
			var keep:Boolean = false;
			
			if (!filterByOrganism) {
				keep = true;
			} else {
				if (idOrganism != null) {
					for each(var o:XML in property.descendants("Organism")) {
						if (idOrganism == o.@idOrganism.toString()) {
							keep = true;
							break;
						}
					}
				}
			}
			
			if (keep) {
				if (!filterByPlatformApplication) {
					keep = true;
				} else {
					keep = false;
					if (reqCategory != null) {
						for each(var pa:XML in property.descendants("PropertyPlatformApplication")) {
							if (reqCategory.@codeRequestCategory.toString() == pa.@codeRequestCategory) {
								if(pa.@codeApplication == "" || codeApplication == pa.@codeApplication) {
									keep = true;
									break;
								}
							}
						}						
					}
				}
			}

			if (keep) {
				if (reqCategory.@idCoreFacility != property.@idCoreFacility) {
					keep = false;
				}
			}
			
			return keep;
		}
		
		public static function mapPropertyOptionEquivalents(option:String, equivalents:String):String {
			if (option == null) {
				return "";
			}
			option = StringUtil.trim(option);
			
			var eq:String = equivalents;
			if (eq == null || StringUtil.trim(eq).length == 0) {
				return option;
			}
			
			var opts:Array = eq.split(",");
			if (opts.length < 2) {
				return option;
			}
			
			for each(var opt:String in opts) {
				opt = StringUtil.trim(opt);
				if (opt.toUpperCase() == option.toUpperCase()) {
					return StringUtil.trim(opts[0]);
				}
			}
			
			return option;
		}
		
		/*
		 * Sort annotations by sort order then name
		 */
		public static function sortProperties(obj1:Object, obj2:Object, fields:Array=null):int {
			if (obj1 == null && obj2 == null) {
				return 0;
			} else if (obj1 == null) {
				return 1;
			} else if (obj2 == null) {
				return -1;
			} else {
				var so1:Number = (obj1.@sortOrder == '' || obj1.@sortOrder == null) ? Number(999999) : new Number(obj1.@sortOrder);
				var so2:Number = (obj2.@sortOrder == '' || obj2.@sortOrder == null) ? Number(999999) : new Number(obj2.@sortOrder);
				var sc1:String = obj1.@name;
				var sc2:String = obj2.@name;
				
				if (so1 < so2) {
					return -1;
				} else if (so1 > so2) {
					return 1;
				} else {
					if (sc1 == 'Other') {
						return 1;
					} else if (sc2 == 'Other') {
						return  -1;
					} else {
						if (sc1.toLowerCase() < sc2.toLowerCase()) {
							return -1;
						} else if (sc1.toLowerCase() > sc2.toLowerCase()) {
							return 1;
						} else {
							return 0;
						}
					}
				}
				
			}
		}	
	}

}
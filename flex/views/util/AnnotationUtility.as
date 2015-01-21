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
				if (property.descendants("PropertyPlatformApplication").length() > 0 || 
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
	}

}
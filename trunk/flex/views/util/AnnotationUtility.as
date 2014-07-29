package views.util
{
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
			
			
			return keep;
		}
	}
}
package views.experimentplatform
{
	import views.util.IconUtility;

	public class ExperimentPlatformService
	{
		public function ExperimentPlatformService()
		{
		}
		
		public function getIcon(item:Object):Class {
			if(item is XML) {
				// If icon path defined then use it
				var itemIcon:String = item.@icon;
				if(itemIcon != null && itemIcon.length > 0) {
					return IconUtility.getClass(item, itemIcon, 16, 16);
				}
			}				
			return null;				
		}	
	}
}
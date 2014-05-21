package views.renderers
{
	
	import mx.collections.*;
	import mx.controls.Label;
	import mx.controls.listClasses.*;
	import mx.controls.treeClasses.*;
	
	public class ExperimentPlatformListNodeRenderer extends ListItemRenderer {
		
		
		override public function set data(value:Object):void {
			if(value != null) { 
				super.data = value;
				var xVal:XML = XML(value);
				if (xVal.hasOwnProperty("@isActive") && xVal.@isActive == 'Y') {
					setStyle("fontStyle", "normal");
					setStyle("fontWeight", "normal");
				} else {
					setStyle("fontStyle", "italic");
					setStyle("fontWeight", "normal");
				}
			}
		}
	}
}
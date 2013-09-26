package views.renderers
{
	
	import mx.collections.*;
	import mx.controls.Label;
	import mx.controls.listClasses.*;
	import mx.controls.treeClasses.*;
	
	public class ExperimentPlatformApplicationTreeNodeRenderer extends TreeItemRenderer {
		
		
		override public function set data(value:Object):void {
			if(value != null) { 
				super.data = value;
				var xVal:XML = XML(value);
				if (xVal.hasOwnProperty("@isSelected") && xVal.@isSelected == 'Y') {
					setStyle("fontWeight", "bold");
					setStyle("fontStyle", "normal");
				} else if (xVal.name() == 'ApplicationTheme' || (xVal.hasOwnProperty("@isActive") && xVal.@isActive == 'Y')) {
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
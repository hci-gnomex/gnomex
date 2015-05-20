package views.renderers
{
	import hci.flex.controls.Label;
	import flash.display.Graphics;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;

	public class LabelFlowCellSampleConcentration extends hci.flex.controls.Label
	{		
		public static function create(dataField:String):IFactory {
				return RendererFactory.create(views.renderers.LabelFlowCellSampleConcentration, 
				{ _dataField: dataField});			
				  
		}			 
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          
          toolTip = data.@relatedFlowCellInfo;
          
	      var g:Graphics = graphics;
    	  g.clear();
       	  g.beginFill( data.name() == "WorkItem" && data[_dataField] == '' && data['@editable'] == 'true' ? missingRequiredFieldBackground : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
      }
	}
}
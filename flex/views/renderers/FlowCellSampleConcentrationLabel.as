package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;

	public class FlowCellSampleConcentrationLabel extends hci.flex.renderers.Label
	{		
		public static function create(dataField:String, missingRequiredFieldBackground:uint=0xffd8bb):IFactory {
				return RendererFactory.create(views.renderers.FlowCellSampleConcentrationLabel, 
				{ _dataField: dataField, missingRequiredFieldBackground: missingRequiredFieldBackground});			
				  
		}			 
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          
	      var g:Graphics = graphics;
    	  g.clear();
       	  g.beginFill( data.name() == "WorkItem" && data[_dataField] == '' ? missingRequiredFieldBackground : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
      }
	}
}
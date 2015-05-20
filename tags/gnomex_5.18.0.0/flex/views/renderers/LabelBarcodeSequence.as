package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;
	import hci.flex.renderers.RendererFactory;

	public class LabelBarcodeSequence extends Label
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null || parentDocument == null || parentDocument.parentDocument == null) {
          	return;
          } 
          if (!data.hasOwnProperty('@barcodeSequence')) {
          	return;
          }
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( !parentDocument.parentDocument.isEditState() && data.@barcodeSequence == '' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();

	     }
	}
}
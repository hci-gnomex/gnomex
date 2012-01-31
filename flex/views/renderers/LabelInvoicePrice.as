package views.renderers
{
	import flash.display.Graphics;
	
	import mx.controls.Label;

	public class LabelInvoicePrice extends Label
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          
          if (data.@other == 'Y') {
          	styleName = "other";
          } else if (data.@percentageDisplay != "" && data.@percentageDisplay != "100%") {
          	styleName = "normal";
          } else {
			var ipS:String = data.@invoicePrice;
			var tpS:String = data.@totalPrice;
			var ip:Number = Number(ipS.replace("$","").replace(",",""));
			var tp:Number = Number(tpS.replace("$","").replace(",",""));
			if (ip != tp) {
				styleName = "percentageEmphasis";
			} else {
          		styleName = "normal";
			}
          }
	    }
	}
}
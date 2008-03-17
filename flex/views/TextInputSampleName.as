package views
{
	import mx.controls.TextInput;
	import flash.display.Graphics;

	public class TextInputSampleName extends TextInput
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data.@name == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : "0xffffff");
	     }
	}
}
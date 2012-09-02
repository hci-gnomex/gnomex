package views.util
{

	import flash.text.TextLineMetrics; 
	import mx.controls.LinkButton; 

	import flash.display.DisplayObject;
 

public class LinkButtonTwoLines extends LinkButton
{
	

	public static var maxLinkBtnWidth:Number = 0;
	
	public function LinkButtonTwoLines()
	{
		super();
	}
	
	override protected function createChildren():void 
	{ 
		if (!textField) 
		{ 
			textField = new NoTruncationUITextField(); 
			textField.styleName = this; 
			addChild(DisplayObject(textField)); 
		} 
		super.createChildren(); 
		textField.wordWrap = true; 
		textField.multiline = true; 
	}  
		
	
	override public function measureText(s:String):TextLineMetrics
	{
		
		textField.text = s;
		// We only want to make the width the standard narrow size if we have a space or dash to wrap the text on
		if (parent == null  || (textField.text.length > 10 && textField.text.indexOf(" ") < 0 && textField.text.indexOf("-") < 0)) {			
		} else {
			textField.width = this.width;
		}
		
		
		
		
		var lineMetrics:TextLineMetrics = textField.getLineMetrics(0);
		lineMetrics.width = textField.textWidth;

		
		// for it to be 2 lines worth of text
		lineMetrics.height = 40;
		
		return lineMetrics;
		
	}
		
	override protected function measure():void
	{		
		super.measure();
		
	}
    
	
}

}
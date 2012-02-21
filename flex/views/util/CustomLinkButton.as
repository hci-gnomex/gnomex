package views.util
{

import flash.text.TextLineMetrics;

import mx.controls.Alert;
import mx.controls.LinkButton;

public class CustomLinkButton extends LinkButton
{
	

	public static var maxLinkBtnWidth:Number = 0;
	
	public function CustomLinkButton()
	{
		
		super();
		
	}
	
	override protected function createChildren():void
	{
		
		super.createChildren();
		if (textField){
			textField.wordWrap = true; // word wrap for the text in the link button
			textField.multiline = true;
			
		}
	}
		
	override public function measureText(s:String):TextLineMetrics
	{
		
		textField.text = s;
		// We only want to make the width the standard narrow size if we have a space or dash to wrap the text on
		if (parent == null || CustomLinkBar(parent).buttonWidth == 0 || (textField.text.length > 10 && textField.text.indexOf(" ") < 0 && textField.text.indexOf("-") < 0)) {			
		} else {
			textField.width = CustomLinkBar(parent).buttonWidth;			
		}
		var lineMetrics:TextLineMetrics = textField.getLineMetrics(0);
		lineMetrics.width = textField.textWidth;
		lineMetrics.height = textField.textHeight;
		return lineMetrics;
		
	}
		
	override protected function measure():void
	{		
		super.measure();
		
		/*
			// Whenever a link button is added it’s width is checked and if it is more than the maxWidth, the maxWidth is set to the new width value
			// to make sure at any given point of time ’maxLinkBtnWidth’ has the width of the widest link button.
			if ( CustomLinkButton.maxLinkBtnWidth > measuredWidth )
				measuredWidth = CustomLinkButton.maxLinkBtnWidth;
			else
				CustomLinkButton.maxLinkBtnWidth = measuredWidth;
		*/			
		
	}
		
}
	
}


package views.util
{
	
import mx.core.UITextField; 

public class NoTruncationUITextField extends UITextField 
{ 
	
	public function NoTruncationUITextField() 
	{ 
		super(); 
	} 
	
	override public function truncateToFit(s:String = null):Boolean 
	{ 
		return false; 
	} 
} 

}
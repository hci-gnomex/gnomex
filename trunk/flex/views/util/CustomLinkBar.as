package views.util
{

import mx.controls.LinkBar;
import mx.core.ClassFactory;
import mx.core.mx_internal;

use namespace mx_internal;

public class CustomLinkBar extends LinkBar
{
	
	public var buttonWidth:int = 0;
	
	public function CustomLinkBar()
	{
		
		super();
		navItemFactory = new ClassFactory(CustomLinkButton);
	}
	
}

}

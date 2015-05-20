package views.renderers
{

import mx.controls.Label; 
import mx.controls.listClasses.*; 

import mx.controls.treeClasses.*;
import mx.collections.*;

public class TreeNodeRenderer extends TreeItemRenderer {

		
    override public function set data(value:Object):void {
    	if(value != null) { 
            super.data = value;
        }
     }	
     protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void { 
     	// Draw a highlight background if we are to emphasize this tree node
     	if (data != null && data.hasOwnProperty("@emphasize") && data.@emphasize == "Y" ) { 
     		this.graphics.beginFill( 0xFFFFB9, 1 ); 
     		this.graphics.drawRect( 0, 0, this.width, this.height ); 
     	} else { 
     		this.graphics.clear(); 
     	} 
     	super.updateDisplayList( unscaledWidth, unscaledHeight ); }
     } 
 

}
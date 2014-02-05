package views.util
{
    import mx.core.FlexSprite;
    import mx.core.FlexShape;
    import flash.display.DisplayObject;
    import flash.display.Graphics;
    import flash.display.Shape;
    import mx.events.IndexChangedEvent;

    /**                                                                                                                                                                 
     *  @private                                                                                                                                                        
     */
    public class HorizontalResizeCursor extends FlexSprite
    {
	public function HorizontalResizeCursor()
	{
	    super();
            var xOff:Number = -6;
            var yOff:Number = -0.5;
	    var len:Number=9;
            var g:Graphics;

            cursor = new FlexShape();
            cursor.name = "cursor";
            g = cursor.graphics;

	    g.beginFill(0xFFFFFF);
            g.moveTo(xOff-2, yOff);
	    g.lineTo(xOff+len-1, yOff-len+2);
	    g.lineTo(xOff+2*len+1, yOff);
	    g.lineTo(xOff+len-1, yOff+len-2);
	    g.lineTo(xOff-2, yOff);
	    g.endFill();

            //Left Arrow with vertical edge
            drawRect(g,xOff-1, yOff-1, len-1, 2);
            drawRect(g,xOff+len-2, yOff-len+3, 1,2*len-6);
            drawTriangle(g, xOff-2, yOff,
			 xOff+len/2-1,yOff+len/2-2,
			 xOff+len/2-1,yOff-len/2+2);
            			 	
            //Right Arrow with vertical edge
            drawRect(g,xOff+len+1, yOff-1, len-1, 2);
            drawRect(g,xOff+len+1, yOff-len+3, -1,2*len-6);
            drawTriangle(g, xOff+2*len+1, yOff,
			 xOff+3*len/2,yOff+len/2-2,
			 xOff+3*len/2,yOff-len/2+2);

	    addChild(cursor);
	}

	protected var cursor:Shape;
  		
	private function drawRect(g:Graphics,x:int, y:int,
				  width:int, height:int):void
	{
	    g.beginFill(0x000000);
            g.moveTo(x, y);
            g.lineTo(x+width,  y);
            g.lineTo(x+width, y+height);
            g.lineTo(x, y+height);
            g.moveTo(x, y);
            g.endFill();
	}
	private function drawTriangle(g:Graphics,x1:int, y1:int,
				      x2:int, y2:int, x3:int, y3:int):void
	{
	    g.beginFill(0x000000);
            g.moveTo(x1, y1)
		g.lineTo(x2, y2);
            g.lineTo(x3, y3);
            g.lineTo(x1, y1);
            g.endFill();
	}
    }
}

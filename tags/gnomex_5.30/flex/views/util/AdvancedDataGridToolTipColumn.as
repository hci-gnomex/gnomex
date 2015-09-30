package views.util
{
	import flash.events.Event;
	
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	
	[Event(name="headerToolTipChanged", type="flash.events.Event")]
	[Event(name="headerToolTipPositionChanged", type="flash.events.Event")]
	[Event(name="headerTextTruncateChanged", type="flash.events.Event")]
	
	/**
	 * Extends AdvancedDataGridColumn to support a headerToolTip property.
	 * It uses a custom headerRenderer class (which extends Label)
	 * and sets the tooltip on that renderer.
	 * You can also position the tooltip to: above, below, to the left or right
	 * of the header, or inline (on top of the header).  
	 * Or if you don't specify a position it will use the default position 
	 * which is slightly below and to the right of the mouse cursor.
	 * You can also set the headerTextTruncate flag which will truncate the 
	 * headerText to fit the header, adding the ellipsis ("...") if necessary.
	 * Note that headerTextTruncate only works if headerTextWordWrap is false. 
	 * 
	 * @author Chris Callendar
	 * @date July 29th, 2009
	 */
	public class AdvancedDataGridToolTipColumn extends AdvancedDataGridColumn
	{
		
		public static const HEADER_TOOL_TIP_CHANGED:String 			= "headerToolTipChanged";
		public static const HEADER_TOOL_TIP_POSITION_CHANGED:String = "headerToolTipPositionChanged";
		public static const HEADER_TEXT_TRUNCATE_CHANGED:String 	= "headerTextTruncateChanged";
		
		private var _toolTip:String;
		private var _toolTipPosition:String;
		private var _truncate:Boolean;
		
		public function AdvancedDataGridToolTipColumn(columnName:String = null) {
			super(columnName);
			this.headerRenderer = new HeaderRenderer(this);
		}
		
		[Bindable("headerToolTipChanged")]
		[Inspectable(category="General")]
		/**
		 *  ToolTip for the header of this column. By default, the DataGrid
		 *  control uses the value of the <code>headerText</code> property 
		 *  as the header tooltip.
		 */
		public function get headerToolTip():String {
			return (_toolTip != null ? _toolTip : null);
		}
		
		public function set headerToolTip(value:String):void {
			if (_toolTip != value) {
				_toolTip = value;
				dispatchEvent(new Event(HEADER_TOOL_TIP_CHANGED));
			}
		}
		
		[Bindable("headerToolTipPositionChanged")]
		[Inspectable(category="General", enumeration="above,below,left,right,default,inline", type="String", defaultValue="default")]
		public function get headerToolTipPosition():String {
			return _toolTipPosition;
		}
		
		public function set headerToolTipPosition(pos:String):void {
			if (pos != null) {
				switch (pos.toLowerCase()) {
					case "above" :
					case "below" :
					case "left" :
					case "right" :
					case "default" :
					case "inline" :
						break;
					default :
						pos = "default";
						break;
				}
				if (_toolTipPosition != pos) {
					_toolTipPosition = pos;
					dispatchEvent(new Event(HEADER_TOOL_TIP_POSITION_CHANGED));
				}
			}
		}
		
		/**
		 * Only applicable when headerWordWrap is set to false.
		 */
		[Bindable("headerTextTruncateChanged")]
		[Inspectable(category="General", type="Boolean", defaultValue="false")]
		public function get headerTextTruncate():Boolean {
			return _truncate;
		}
		
		public function set headerTextTruncate(truncate:Boolean):void {
			if (truncate != _truncate) {
				this._truncate = truncate;
				dispatchEvent(new Event(HEADER_TEXT_TRUNCATE_CHANGED));
			}
		}
		
	}
	
}

import mx.core.IFactory;
import mx.events.ToolTipEvent;
import mx.core.IToolTip;
import flash.geom.Rectangle;
import views.util.AdvancedDataGridToolTipColumn;
import mx.controls.advancedDataGridClasses.AdvancedDataGridItemRenderer;

/**
 * This helper class is the renderer used by the above DataGridToolTipColumn.
 * It sets the toolTip value and handles positioning the toolTip.
 * It also performs the text truncation if necessary.
 */
internal class HeaderRenderer extends AdvancedDataGridItemRenderer implements IFactory
{
	
	private var column:AdvancedDataGridToolTipColumn;
	private var fullText:String;
	
	public function HeaderRenderer(column:AdvancedDataGridToolTipColumn) {
		this.column = column;
	}
	
	public function newInstance():* {
		return new HeaderRenderer(column);
	}
	
	override public function set data(value:Object):void {
		super.data = value;
		super.toolTip = column.headerToolTip;
	}
	
	override public function set toolTip(value:String):void {
		// do nothing
	}
	
	override protected function toolTipShowHandler(event:ToolTipEvent):void {
		// position the tooltip above, below, left, or right of the header
		// the default position is slightly below and to the right of the mouse cursor
		var position:String = column.headerToolTipPosition;
		if (position && (position != "default")) {
			var tt:IToolTip = event.toolTip;
			var bounds:Rectangle = getBounds(stage);
			bounds.y = bounds.y - 3;
			bounds.height = bounds.height + 4;
			switch (position) {
				case "above" :
					tt.move(bounds.x - 3, bounds.y - tt.height + 2);
					break;
				case "below" :
					tt.move(bounds.x - 3, bounds.bottom + 1);
					break;
				case "left" :
					tt.move(bounds.x - tt.width + 2, bounds.y);
					break;
				case "right" :
					tt.move(bounds.right - 1, bounds.y);
					break;
				case "inline" :
					super.toolTipShowHandler(event);
					break;
			}
		}
	}
	
	override public function set text(value:String):void {
		fullText = value;
		super.text = value;
		if (column.headerTextTruncate) {
			truncateToFit("...");
		}
	}
	
	override public function set wordWrap(value:Boolean):void {
		if (value != super.wordWrap) {
			super.wordWrap = value;
			// remove the "..."
			super.text = fullText;
		}
	}
	
}

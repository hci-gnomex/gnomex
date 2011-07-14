package views.util
{
	import mx.controls.AdvancedDataGrid;
	import mx.events.DragEvent;
	import flash.utils.clearInterval;
	import mx.managers.DragManager;
	import mx.events.ScrollEvent;
	import flash.utils.setInterval;
	import mx.events.ScrollEventDetail;
	import mx.events.ScrollEventDirection;
	import flash.events.MouseEvent;
	
	public class AdvancedDataGridAS extends AdvancedDataGrid
	{
		private var dragScrollingInterval:int = 0;
		
		public function AdvancedDataGridAS()
		{
			super();
		}		
		
		override protected function dragScroll():void
		{
			var slop:Number = 0;
			var scrollInterval:Number;
			var oldPosition:Number;
			var d:Number;
			var scrollEvent:ScrollEvent;
			
			// sometimes, we'll get called even if interval has been cleared
			if (dragScrollingInterval == 0)
				return;
			
			const minScrollInterval:Number = 30;
			
			if (DragManager.isDragging)
			{
				slop = viewMetrics.top
					+ (variableRowHeight ? getStyle("fontSize") / 4 : rowHeight);
			}
			
			clearInterval(dragScrollingInterval);
			
			if (mouseY < slop)
			{
				oldPosition = verticalScrollPosition;
				verticalScrollPosition = Math.max(0, oldPosition - 1);
				if (DragManager.isDragging)
				{
					scrollInterval = 100;
				}
				else
				{
					d = Math.min(0 - mouseY - 30, 0);
					// quadratic relation between distance and scroll speed
					scrollInterval = 0.593 * d * d + 1 + minScrollInterval;
				}
				
				dragScrollingInterval = setInterval(dragScroll, scrollInterval);
				
				if (oldPosition != verticalScrollPosition)
				{
					scrollEvent = new ScrollEvent(ScrollEvent.SCROLL);
					scrollEvent.detail = ScrollEventDetail.THUMB_POSITION;
					scrollEvent.direction = ScrollEventDirection.VERTICAL;
					scrollEvent.position = verticalScrollPosition;
					scrollEvent.delta = verticalScrollPosition - oldPosition;
					dispatchEvent(scrollEvent);
				}
			}
			else if (mouseY > (unscaledHeight - slop))
			{
				oldPosition = verticalScrollPosition;
				verticalScrollPosition = Math.min(maxVerticalScrollPosition, verticalScrollPosition + 1);
				if (DragManager.isDragging)
				{
					scrollInterval = 100;
				}
				else
				{
					d = Math.min(mouseY - unscaledHeight - 30, 0);
					scrollInterval = 0.593 * d * d + 1 + minScrollInterval;
				}
				
				dragScrollingInterval = setInterval(dragScroll, scrollInterval);
				
				if (oldPosition != verticalScrollPosition)
				{
					scrollEvent = new ScrollEvent(ScrollEvent.SCROLL);
					scrollEvent.detail = ScrollEventDetail.THUMB_POSITION;
					scrollEvent.direction = ScrollEventDirection.VERTICAL;
					scrollEvent.position = verticalScrollPosition;
					scrollEvent.delta = verticalScrollPosition - oldPosition;
					dispatchEvent(scrollEvent);
				}
			}
			else
			{
				dragScrollingInterval = setInterval(dragScroll, 15);
			}			
		}
		
		
		/**
		 *  @private
		 *  Position indicator bar that shows where an item will be placed in the list.
		 */
		override public function showDropFeedback(event:DragEvent):void
		{
			if (collection) {
				if (dragScrollingInterval == 0) {
					dragScrollingInterval = setInterval(dragScroll, 15);
				}	
			}
			super.showDropFeedback(event);
		}
		
		
		/**
		 *  @private
		 *  Blocks mouse events on items that are tweening away and are invalid for input
		 */
		override protected function mouseUpHandler(event:MouseEvent):void
		{
				super.mouseUpHandler(event);
				thisResetDragScrolling();
		}
		
		/**
		 *  Handles <code>DragEvent.DRAG_EXIT</code> events. This method hides
		 *  the UI feeback by calling the <code>hideDropFeedback()</code> method.
		 *
		 *  @param event The DragEvent object.
		 */
		override protected function dragExitHandler(event:DragEvent):void
		{
						
			thisResetDragScrolling();
			super.dragExitHandler(event);
		}
		
		
		/**
		 *  @private
		 *  Stop the drag scrolling callback.
		 */
		protected function thisResetDragScrolling():void
		{
			if (dragScrollingInterval != 0)
			{
				clearInterval(dragScrollingInterval);
				dragScrollingInterval = 0;
			}
		}		
		
		
	}
}
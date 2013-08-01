package views.util
{
import flash.events.MouseEvent;
import flash.ui.Keyboard;
import mx.controls.listClasses.IListItemRenderer;
import mx.controls.List;

/**
 *  A List that uses a disableFunction to determine if an item can be selected or not
 */
public class DisabledItemsList extends List
{
	public function DisabledItemsList() 
	{
		super();
	}

	/**
	 *  The function to apply that determines if the item is "disabled" or not.
	 *  Takes one argument, which is the data item
	 *  Returns true if item cannot be selected.
	 */
	public var disabledFunction:Function;

	override protected function mouseEventToItemRenderer(event:MouseEvent):IListItemRenderer
	{
		var listItem:IListItemRenderer = super.mouseEventToItemRenderer(event);
		if (listItem)
		{
			if (listItem.data)
			{
				if (disabledFunction(listItem.data))
				{
					return null;
				}
			}
		}
		return listItem;
	}

	private var selectionUpward:Boolean;

    override protected function moveSelectionVertically(code:uint, shiftKey:Boolean,
                                               ctrlKey:Boolean):void
    {
		if (code == Keyboard.DOWN)
			selectionUpward = false;
		else
			selectionUpward = true;
		super.moveSelectionVertically(code, shiftKey, ctrlKey);
	}

    override protected function finishKeySelection():void
	{
		super.finishKeySelection();
	
		var i:int;
        var uid:String;
        var rowCount:int = listItems.length;
        var partialRow:int = (rowInfo[rowCount-1].y + rowInfo[rowCount-1].height >
                                  listContent.height) ? 1 : 0;

		var listItem:IListItemRenderer;
        listItem = listItems[caretIndex - verticalScrollPosition][0];
        if (listItem)
        {
            if (listItem.data);
			{
				if (disabledFunction(listItem.data))
				{
					// find another visible item that is enabled
					// assumes there is one that is fully visible
					rowCount = rowCount - partialRow;
					var idx:int = caretIndex - verticalScrollPosition;
					if (selectionUpward)
					{
						// look up;
						for (i = idx - 1; i >= 0; i--)
						{
							listItem = listItems[i][0];
							if (!disabledFunction(listItem.data))
							{
								selectedIndex = i - verticalScrollPosition;
								return;
							}
						}
						for (i = idx + 1; i < rowCount; i++)
						{
							listItem = listItems[i][0];
							if (!disabledFunction(listItem.data))
							{
								selectedIndex = i - verticalScrollPosition;
								return;
							}
						}
					}
					else
					{
						// look down;
						for (i = idx + 1; i < rowCount; i++)
						{
							listItem = listItems[i][0];
							if (!disabledFunction(listItem.data))
							{
								selectedIndex = i - verticalScrollPosition;
								return;
							}
						}
						for (i = idx - 1; i >= 0; i--)
						{
							listItem = listItems[i][0];
							if (!disabledFunction(listItem.data))
							{
								selectedIndex = i - verticalScrollPosition;
								return;
							}
						}
					}
				}
			}
		}
	}
  
}

}
package views.util
{
import views.util.ResizableTitleWindow;
import flash.display.DisplayObject;
import mx.core.Container;
import mx.core.Application;
import mx.containers.ApplicationControlBar;
import flash.events.Event;
import mx.managers.ISystemManager;
import mx.core.IUIComponent;
import flash.geom.Rectangle;
import flash.utils.Dictionary;

/**
 *  The TaskBarManager class manages the ResiableTitleWindow in the application.
 *  A ResiableTitleWindow when minimized sits in a Task Bar managed by this class.
 *
 */                                                                                                                                    
public class TaskBarManager
{
    //--------------------------------------------------------------------------
    //
    //  Class Properties
    //
    //--------------------------------------------------------------------------


    /**                                                                                                                                                                 
     *  A reference to application control bar.
     */
    protected static var applicationControlBar:ApplicationControlBar = initialize();

    /**                                                                                                                                                                 
     *  A flag to indicate whether the task bar is visible or not.
     */
    public static var visible:Boolean = false;

    //--------------------------------------------------------------------------
    //
    //  Class Variables
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  Data-structure used to maintain information about all the minimizable windows in the Application
     */
    private static var itemsInfo:Dictionary;

    /**
     *  @private
     */
    private static const HEIGHT:uint = 25;

    /**
     *  @private
     */
    private static const GAP:uint = 5;

    /**
     *  @private
     *  Number of windows in the Task Bar.
     */
    private static var numMinimizedWindows:int=0;

    //--------------------------------------------------------------------------
    //
    //  Classs Methods
    //
    //--------------------------------------------------------------------------

    /**
     *  Adds the window to the Taskbar.
     *
     */
    public static function addToTaskBar(item:ResizableTitleWindow):void
    {			
	var i:uint;

	if(item && item.parent is Container)
	    {
		if(!o)
		    registerWindow(item);
		var o:TaskBarItemsData = itemsInfo[item];

		if(applicationControlBar.visible == false)
		    {
			applicationControlBar.visible = true;
			visible = true;
			Application.application.invalidateProperties();
			Application.application.invalidateSize();
		    }
		item.parent.removeChild(item);
		applicationControlBar.addChild(item);

		var lengthOccupied:uint = GAP;

		for(i=0; i< itemsInfo.length;i++)
		    lengthOccupied += 200+GAP;

		item.x = lengthOccupied;
		item.y = item.parent.stage.height - HEIGHT + 4;
		numMinimizedWindows++;
	    }	
    }

    /**
     *  Register the window in the TaskBarManager.
     *
     */
    public static function registerWindow(item:ResizableTitleWindow):void
    {
	if(item)
	    {
		if(!itemsInfo)
		    {
			itemsInfo = new Dictionary();
			var root:Application = Application.application as Application;
			root.addChild(applicationControlBar);

	        var sm:ISystemManager;
	        if ( item.parent is ISystemManager)
	         	sm = ISystemManager(item.parent);
	        else if(item.parent is IUIComponent)
	        	sm = IUIComponent(item.parent).systemManager;
	        if(sm)
//			const sm:ISystemManager = IUIComponent(item.parent).systemManager;
			sm.addEventListener(Event.RESIZE, resizeHandler);
		    }

		var o:TaskBarItemsData = new TaskBarItemsData();
		o.parent = item.parent;
		o.owner = item;
		o.index = item.parent.getChildIndex(item);
		itemsInfo[item] = o;
	    }
    }

    /**
     *  Removes the window in the TaskBarManager.
     *
     */
    public static function removeFromTaskBar(item:ResizableTitleWindow):void
    {
	var o:TaskBarItemsData = itemsInfo[item];
	var i:int;

	if(o)
	    {
		numMinimizedWindows--;
		if(numMinimizedWindows == 0)
		    {
			applicationControlBar.visible = false;
			visible = false;
		    }

		applicationControlBar.removeChild(item);
		Container(o.parent).addChild(item);

	    }
    }

    /**
     *  @private
     *  Initializes the Task Bar component;
     */
    private static function initialize():ApplicationControlBar
    {
	var applicationControlBar2:ApplicationControlBar;

	var root:Application = Application.application as Application;

	applicationControlBar2 = new ApplicationControlBar();
	applicationControlBar2.width = root.width;
	applicationControlBar2.height = 30;
	applicationControlBar2.y = root.height - 30;
	applicationControlBar2.alpha = 0.5;
	applicationControlBar2.visible = false;

	return applicationControlBar2;
    }

    /**
     *  @private
     *  Handles when the main Application window/Browser is resized
     */
    private static function resizeHandler(event:Event):void
    {
	const s:Rectangle = ISystemManager(DisplayObject(event.target).root).screen;

	if(applicationControlBar)
	    {
		applicationControlBar.width = s.width;
		applicationControlBar.y = s.height - 30;
	    }
    }
}
}
import views.util.ResizableTitleWindow;
import flash.display.DisplayObject;
import mx.controls.Button;
	
class TaskBarItemsData
{
    public function TaskBarItemsData()
    {
	super();
    }
    public var parent:DisplayObject;
    public var owner:ResizableTitleWindow;
    public var index:int;
    public var button:Button;
}

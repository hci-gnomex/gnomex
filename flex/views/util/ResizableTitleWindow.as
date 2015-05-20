////////////////////////////////////////////////////////////////////////////////////////////////
//
//  ResizableTitleWindow(6/11/06 Nisheet Jain)
//     
//  Ported from  -
//  Manish Jethani's ResizableTitleWindow
//  
//  This basic design for this class has been taken from Manish Jethani's ResizableTitleWindow
//  http://manish.revise.org/archives/2005/01/09/resizable-titlewindow-in-flex/
//  
////////////////////////////////////////////////////////////////////////////////////////////////
   
package views.util
{
import adobe.utils.CustomActions;

import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.FocusEvent;
import flash.events.MouseEvent;
import flash.geom.Point;
import flash.geom.Rectangle;

import mx.containers.TitleWindow;
import mx.controls.Button;
import mx.controls.ToggleButtonBar;
import mx.core.Application;
import mx.core.Container;
import mx.core.EdgeMetrics;
import mx.core.FlexVersion;
import mx.core.IUIComponent;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.mx_internal;
import mx.effects.Move;
import mx.effects.Parallel;
import mx.effects.Pause;
import mx.effects.Resize;
import mx.effects.Sequence;
import mx.effects.easing.Bounce;
import mx.effects.easing.Circular;
import mx.effects.easing.Linear;
import mx.events.CloseEvent;
import mx.events.DragEvent;
import mx.events.MoveEvent;
import mx.managers.CursorManager;
import mx.managers.CursorManagerPriority;
import mx.managers.ISystemManager;
import mx.managers.SystemManager;
import mx.states.SetProperty;
import mx.states.State;
import mx.states.Transition;
import mx.styles.CSSStyleDeclaration;
import mx.styles.StyleManager;
import mx.styles.StyleProxy;

import views.util.DiagonalResizeCursor;
import views.util.HorizontalResizeCursor;
import views.util.RotatedDiagonalResizeCursor;
import views.util.TaskBarManager;
import views.util.VerticalResizeCursor;

use namespace mx_internal;
//--------------------------------------
//  Events
//--------------------------------------

/**
*  Dispatched when the user selects the maximize button.
*/
[Event(name="maximize", type="flash.events.Event")]

/**
*  Dispatched when the user selects the maximize button.
*/
[Event(name="minimize", type="flash.events.Event")]

/**
*  Dispatched when the user restores the window
*/
[Event(name="restore", type="flash.events.Event")]

//--------------------------------------
//  Styles
//--------------------------------------

/**
*  Thickness in pixels of the area where the user can click to resize 
*  the window.
*  This area is centered at the edge of the window
*  A resize cursor appears when the mouse is over this area.
*
*  @default 6
*/

[Style(name="resizeAffordance", type="Number", format="Length", inherit="no")]

/**
 *  The maximize button default skin.
 *
 *  @default null
 */
[Style(name="maximizeButtonSkin", type="Class", inherit="no", states="up, over, down, disabled")]

/**
 *  The maximize button disabled skin.
 *
 *  @default MaximizeButtonDisabled
 */
[Style(name="maximizeButtonDisabledSkin", type="Class", inherit="no")]

/**
 *  The maximize button down skin.
 *
 *  @default MaximizeButtonDown
 */
[Style(name="maximizeButtonDownSkin", type="Class", inherit="no")]

/**
 *  The maximize button over skin.
 *
 *  @default MaximizeButtonOver
 */
[Style(name="maximizeButtonOverSkin", type="Class", inherit="no")]

/**
 *  The maximize button up skin.
 *
 *  @default MaximizeButtonUp
 */
[Style(name="maximizeButtonUpSkin", type="Class", inherit="no")]

/**
 *  The minimize button default skin.
 *
 *  @default null
 */
[Style(name="minimizeButtonSkin", type="Class", inherit="no", states="up, over, down, disabled")]

/**
 *  The minimize button disabled skin.
 *
 *  @default MinimizeButtonDisabled
 */
[Style(name="minimizeButtonDisabledSkin", type="Class", inherit="no")]

/**
 *  The minimize button down skin.
 *
 *  @default MinimizeButtonDown
 */
[Style(name="minimizeButtonDownSkin", type="Class", inherit="no")]

/**
 *  The minimize button over skin.
 *
 *  @default MinimizeButtonOver
 */
[Style(name="minimizeButtonOverSkin", type="Class", inherit="no")]

/**
 *  The minimize button up skin.
 *
 *  @default MinimizeButtonUp
 */
[Style(name="minimizeButtonUpSkin", type="Class", inherit="no")]

//--------------------------------------
//  Excluded APIs
//--------------------------------------

[Exclude(name="doubleClickEnabled", kind="property")]
[Exclude(name="showCloseButton", kind="property")]

/**
*  A ResizableTitleWindow is an extension to a TitleWindow which can be 
*  resized/maximized/minimized/moved/closed. It can be used as a Container to 
*  contain other components.
*  <br>NOTE: Its behavior is not meaningful when it is put inside some other container
* like VBox, VDividedBox etc.
*
*  @mxml
*
*  <p>The <code>&lt;fc:ResizableTitleWindow&gt;</code> tag inherits all the tag attributes
*  of its superclass, and adds the following tag attributes:</p>
*
*  <pre>
*  &lt;fc:ResizableTitleWindow
*    <b>Properties</b>
*    closeable="true"
*    maximizable="true"
*    minimizable="true"
*    
*    <b>Styles</b>
*    resizeAffordance="6"
*    maximizeButtonDisabledSkin="<i>Expects in application</i>"
*    maximizeButtonDownSkin="<i>Expects in application</i>"
*    maximizeButtonOverSkin="<i>Expects in application</i>"
*    maximizeButtonUpSkin="<i>Expects in application</i>"
*
*    minimizeButtonDisabledSkin="<i>Expects in application</i>"
*    minimizeButtonDownSkin="<i>Expects in application</i>"
*    minimizeButtonOverSkin="<i>Expects in application</i>"
*    minimizeButtonUpSkin="<i>Expects in application</i>"
*    
*    <b>Events</b>
*    maximize="<i>No default</i>"
*    minimize="<i>No default</i>"
*    restore="<i>No default</i>"
*  /&gt;
*  </pre>
*
*  @includeExample samples/ResizableTitleWindowSample/ResizableTitleWindowSample.mxml
*
*/                                                                                                                                    
public class ResizableTitleWindow extends TitleWindow
{
    //--------------------------------------------------------------------------
    //
    //  Class Constants
    //
    //--------------------------------------------------------------------------

    // Constants for window edges (see `handleEdge`)                                                                                                                    
    static private var EDGE_NONE : Number = 0;

    static private var EDGE_BOTTOM : Number = 1;
    static private var EDGE_RIGHT : Number = 2;
    static private var EDGE_LEFT : Number = 3;
    static private var EDGE_TOP : Number = 4;

    static private var EDGE_CORNER : Number = 5;
    static private var EDGE_LEFT_BOTTOM : Number = 6;
    static private var EDGE_LEFT_TOP : Number = 7;
    static private var EDGE_RIGHT_TOP : Number = 8;

    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     *  Constructor.
     */
    public function ResizableTitleWindow()
    {
        super();

        isPopUp = true;
        doubleClickEnabled = true;

        setStyle("headerHeight", 21); 
        setStyle("cornerRadius", 4);
        setStyle("borderAlpha", 1);
        setStyle("dropShadowEnabled", true);
        setStyle("resizeAffordance",6);

        addEventListener(Event.CLOSE,closeHandler);
        addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler,true);
        addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler,false);
        addEventListener(MouseEvent.MOUSE_MOVE, systemManager_mouseMoveHandler);
//        addEventListener(MouseEvent.CLICK, bringToFront, false);
        addEventListener(MouseEvent.ROLL_OUT, systemManager_mouseMoveHandler);
    }

    //--------------------------------------------------------------------------
    //
    //  Variables
    //
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    //  Various States of the window
    //--------------------------------------------------------------------------
    /**
     *  @private
     */
    private var stateNormal:State;

    /**
     *  @private
     */
    private var stateMin:State;

    /**
     *  @private
     */
    private var stateMax:State;

    /**
     *  @private
     */
    private var statePseudoMax:State;

    //--------------------------------------------------------------------------
    //  Resize Cursor Classes
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  CursorSymbol used to display Vertical Resize Cursor
     */
    private var vCursorSymbol:Class = VerticalResizeCursor;
    /**
     *  @private
     *  CursorSymbol used to display Horizontal Resize Cursor
     */
    private var hCursorSymbol:Class = HorizontalResizeCursor;
    /**
     *  @private
     *  CursorSymbol used to display Diagonal Resize Cursor
     */
    private var dCursorSymbol:Class = DiagonalResizeCursor;

    /**
     *  @private
     *  CursorSymbol used to display Rotated Diagonal Resize Cursor
     */
    private var rdCursorSymbol:Class = RotatedDiagonalResizeCursor;
    /**
     *  @private
     */
    private var fullTitle:String = "";

    /**
     *  @private
     */
    private var isHandleDragging : Boolean = false;

    /**
     *  @private
     */
    private var handleEdge : Number = EDGE_NONE;

    /**
     *  @private
     */
    private var cursorId:Number=0;

    /**
     *  @private
     */
    private var lastEdge:Number=0;

    /**
     *  @private
     *  A reference to the minimizeButton
     */
    private var minimizeButton:Button;

    /**
     *  @private
     *  A reference to the maximizeButton
     */
    private var maximizeButton:Button;

    /**
     *  @private
     */
    private var origPosition:Point;

    /**
     *  @private
     */
    private var origDimensions:Point;

    /**
     *  @private
     */
    private var origPerDimensions:Point;

    /**
     *  @private
     */
    private var origDimensionsCached:Boolean = false;

    //--------------------------------------------------------------------------
    //
    //  Properties
    //
    //--------------------------------------------------------------------------

    [Inspectable(category="General")]

    /**
     *  Whether to display a Maximize button in the ResizableTitleWindow container.
     *  Set it to <code>false</code> if you don't want to display the Maximize button.
     *  @default true
     *
     */
    private var _maximizable:Boolean=true;
    public function get maximizable():Boolean
    {
        return _maximizable;
    }
    public function set maximizable(value:Boolean):void
    {
        _maximizable = value;
        invalidateDisplayList();
    }

    [Inspectable(category="General")]

    /**
     *  Whether to display a Minimize button in the ResizableTitleWindow container.
     *  Set it to <code>false</code> if you don't want to display the Minimize button.
     *  @default true
     *
     */
    private var _minimizable:Boolean=true;
    public function get minimizable():Boolean
    {
        return _minimizable;
    }
    public function set minimizable(value:Boolean):void
    {
        _minimizable = value;
        invalidateDisplayList();
    }

    [Inspectable(category="General")]
    /**
     *  Whether to display a Close button in the ResizableTitleWindow container.
     *  Set it to <code>false</code> if you don't want to display the Close button.
     *  @default true
     *
     */
    private var _closeable:Boolean=true;
    public function get closeable():Boolean
    {
        return _closeable;
    }
    public function set closeable(value:Boolean):void
    {
        _closeable = value;
        invalidateDisplayList();
    }

    //----------------------------------
    //  _minimizeButtonStyleFilters
    //----------------------------------


    private static var _minimizeButtonStyleFilters:Object = 
    {
        "minimizeButtonUpSkin" : "minimizeButtonUpSkin", 
        "minimizeButtonOverSkin" : "minimizeButtonOverSkin",
        "minimizeButtonDownSkin" : "minimizeButtonDownSkin",
        "minimizeButtonDisabledSkin" : "minimizeButtonDisabledSkin",
        "minimizeButtonSkin" : "minimizeButtonSkin",
        "repeatDelay" : "repeatDelay",
        "repeatInterval" : "repeatInterval"
    };

    /**
     *  The set of styles to pass from the Panel to the minimize button.
     *  @see mx.styles.StyleProxy
     *  @review
     */
    protected function get minimizeButtonStyleFilters():Object
    {
        return _minimizeButtonStyleFilters;
    }
    //----------------------------------
    //  _maximizeButtonStyleFilters
    //----------------------------------


    private static var _maximizeButtonStyleFilters:Object = 
    {
        "maximizeButtonUpSkin" : "maximizeButtonUpSkin", 
        "maximizeButtonOverSkin" : "maximizeButtonOverSkin",
        "maximizeButtonDownSkin" : "maximizeButtonDownSkin",
        "maximizeButtonDisabledSkin" : "maximizeButtonDisabledSkin",
        "maximizeButtonSkin" : "maximizeButtonSkin",
        "repeatDelay" : "repeatDelay",
        "repeatInterval" : "repeatInterval"
    };

    /**
     *  The set of styles to pass from the Panel to the maximize button.
     *  @see mx.styles.StyleProxy
     *  @review
     */
    protected function get maximizeButtonStyleFilters():Object
    {
        return _maximizeButtonStyleFilters;
    }
    //--------------------------------------------------------------------------
    //
    //  Overridden methods
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  Create child objects.
     */
    override protected function createChildren():void
    {
        super.createChildren();

        var sm:ISystemManager;
        if ( parent is ISystemManager)
         	sm = ISystemManager(parent);
        else if(parent is IUIComponent)
        {
        	sm = IUIComponent(parent).systemManager;
            //Register this window to the TaskbarManager
            TaskBarManager.registerWindow(this);
        }
        if(sm)
	        sm.addEventListener(Event.RESIZE, resizeHandler, false, -5);

        //Apply the Resize and Move effects to support Animation
        setEffects();

        fullTitle = title;

        storeOrigDimensions();

        if (titleBar)
        {
            titleBar.height = getHeaderHeight();
            titleBar.addEventListener(MouseEvent.DOUBLE_CLICK, titleBar_mouseClickHandler);
			
            if(closeable)
                showCloseButton = true;

            if (!minimizeButton)
            {
                minimizeButton = new Button();

                minimizeButton.styleName = new StyleProxy(this, minimizeButtonStyleFilters);

                minimizeButton.upSkinName = "minimizeButtonUpSkin";
                minimizeButton.overSkinName = "minimizeButtonOverSkin";
                minimizeButton.downSkinName = "minimizeButtonDownSkin";
                minimizeButton.disabledSkinName = "minimizeButtonDisabledSkin";
                minimizeButton.skinName = "minimizeButtonSkin";

                minimizeButton.explicitWidth = 16;
                minimizeButton.explicitHeight = 16;

                minimizeButton.visible = false;
                minimizeButton.enabled = enabled && parent is IUIComponent;

                minimizeButton.focusEnabled = false;

                minimizeButton.addEventListener(MouseEvent.CLICK, titleBar_mouseClickHandler);
                // Add the minimize button on top of the title/status.
                titleBar.addChild(minimizeButton);
                minimizeButton.owner = this;

            }
            if (!maximizeButton)
            {
                maximizeButton = new Button();
                maximizeButton.styleName = new StyleProxy(this, maximizeButtonStyleFilters);

                maximizeButton.upSkinName = "maximizeButtonUpSkin";
                maximizeButton.overSkinName = "maximizeButtonOverSkin";
                maximizeButton.downSkinName = "maximizeButtonDownSkin";
                maximizeButton.disabledSkinName = "maximizeButtonDisabledSkin";
                maximizeButton.skinName = "maximizeButtonSkin";

                maximizeButton.explicitWidth = 16;
                maximizeButton.explicitHeight = 16;

                maximizeButton.visible = false;
                maximizeButton.enabled = enabled;
                maximizeButton.focusEnabled = false;

                maximizeButton.addEventListener(MouseEvent.CLICK, titleBar_mouseClickHandler);
                // Add the maximize button on top of the title/status.
                titleBar.addChild(maximizeButton);
                maximizeButton.owner = this;

            }
        }
    }

    /**
     *  @private
     */
    override protected function layoutChrome(unscaledWidth:Number,
                                             unscaledHeight:Number):void
    {
        super.layoutChrome(unscaledWidth,unscaledHeight);

        var maxButtonHeight:uint = 0;
        var headerHeight:Number = getHeaderHeight();

        var vm:EdgeMetrics = FlexVersion.compatibilityVersion < FlexVersion.VERSION_3_0 ? borderMetrics : EdgeMetrics.EMPTY;
        var gap:uint = vm.right;
        var x:uint=0;
        gap = 3;
        maximizeButton.visible = maximizable;
        minimizeButton.visible = minimizable;

        if(maximizable)
            maxButtonHeight = Math.max(maxButtonHeight, maximizeButton.getExplicitOrMeasuredHeight());
        if(minimizable)
            maxButtonHeight = Math.max(maxButtonHeight, minimizeButton.getExplicitOrMeasuredHeight());
        if(closeable)
            maxButtonHeight = Math.max(maxButtonHeight, closeButton.getExplicitOrMeasuredHeight());

        //Find out the y for all the buttons
        var y:uint = Math.round(( headerHeight - maxButtonHeight ) / 2 ); 
        x = unscaledWidth - vm.right;

        var w:Number;
        var h:Number;

        if(closeable)
        {
            w = closeButton.getExplicitOrMeasuredWidth();
            h = closeButton.getExplicitOrMeasuredHeight();
            closeButton.setActualSize(w, h);

            x -=  (w + gap);
            closeButton.move(x,y);
        }

        if(maximizable)
        {
            w = maximizeButton.getExplicitOrMeasuredWidth();
            h = maximizeButton.getExplicitOrMeasuredHeight();
            maximizeButton.setActualSize(w, h);
            x -= (w+gap);
            maximizeButton.move( x, y );
        }

        if(minimizable)
        {
            w = minimizeButton.getExplicitOrMeasuredWidth();
            h = minimizeButton.getExplicitOrMeasuredHeight();

            minimizeButton.setActualSize(w, h);

            x -= (w+ gap);
            minimizeButton.move(x, y);
        }

        //Copied from mx.containers.Panel
        var leftOffset:Number = 10;
        var rightOffset:Number = unscaledWidth - vm.right - x;;
        var offset:Number;
        
            // Set the position of the title icon.
        if (titleIconObject)
        {
            h = titleIconObject.height;
            offset = (headerHeight - h) / 2;
            titleIconObject.move(leftOffset, offset);
            leftOffset += titleIconObject.width + 4;
        }

            // Set the position of the title text.  
        h = titleTextField.textHeight;
        offset = (headerHeight - h) / 2;
        
        var borderWidth:Number = vm.left + vm.right;            
        titleTextField.move(leftOffset, offset - 1);
        titleTextField.setActualSize(Math.max(0,
                                   unscaledWidth - leftOffset -
                                              rightOffset - borderWidth),
                                     h + UITextField.TEXT_HEIGHT_PADDING);
        
        // Set the position of the status text.
        h = statusTextField.textHeight;
        offset = (headerHeight - h) / 2;
        var statusX:Number = unscaledWidth - rightOffset - 4 -
            borderWidth - statusTextField.textWidth;
        if (_showCloseButton)
            statusX -= (closeButton.getExplicitOrMeasuredWidth() + 4);
        statusTextField.move(statusX, offset - 1);
        statusTextField.setActualSize(statusTextField.textWidth + 8,
                                      statusTextField.textHeight + UITextField.TEXT_HEIGHT_PADDING);
        
        // Make sure the status text isn't too long.
        // We do simple clipping here.
        var minX:Number = titleTextField.x + titleTextField.textWidth + 8;
        if (statusTextField.x < minX)
        {
            // Show as much as we can.
            statusTextField.width = Math.max(statusTextField.width -
                                             (minX - statusTextField.x), 0);
            statusTextField.x = minX;
        }
    }

    /**
     *  @private
     */
    override protected function measure():void
    {
        super.measure();

        var textSize:Rectangle = measureHeaderText();
        var textWidth:Number = textSize.width;
        var textHeight:Number = textSize.height;

        var bm:EdgeMetrics = borderMetrics;
        textWidth += bm.left + bm.right;    

        var offset:Number = 5;
        textWidth += offset * 2;

        measuredMinWidth = Math.max(textWidth, measuredMinWidth);
        measuredWidth = Math.max(textWidth, measuredWidth);

        if(minimizable)
        {
            measuredWidth += minimizeButton.getExplicitOrMeasuredWidth()+6;
            measuredMinWidth += minimizeButton.getExplicitOrMeasuredWidth()+6;
            measuredHeight = Math.max(measuredHeight, minimizeButton.getExplicitOrMeasuredHeight());
        }
        if(maximizable)
        {
            measuredWidth += maximizeButton.getExplicitOrMeasuredWidth()+6;
            measuredMinWidth += maximizeButton.getExplicitOrMeasuredWidth()+6;
            measuredHeight = Math.max(measuredHeight, maximizeButton.getExplicitOrMeasuredHeight());
        }
    }

    private function measureHeaderText():Rectangle
    {
        var textWidth:Number = 20;
        var textHeight:Number = 14;

        if (titleTextField && titleTextField.text)
        {
            titleTextField.validateNow();
            textWidth = titleTextField.textWidth;
            textHeight = titleTextField.textHeight;
        }

        if (statusTextField)
        {
            statusTextField.validateNow();
            textWidth = Math.max(textWidth, statusTextField.textWidth);
            textHeight = Math.max(textHeight, statusTextField.textHeight);
        }

        return new Rectangle(0, 0, Math.round(textWidth), Math.round(textHeight));
    }

    /**
     *  @private
     */
    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void
    {
        super.updateDisplayList(unscaledWidth, unscaledHeight);

        if(!origDimensionsCached)
        {
            storeOrigDimensions();
            origDimensionsCached = true;
        }
    }

    //--------------------------------------------------------------------------
    //
    //  Methods
    //
    //--------------------------------------------------------------------------

    /**
     *  Set the state for the ResizableTitleWindow. The possible values are:
     *  "maximized", "minimized", "normal"
     *
     */
    protected function setState(state:String):void
    {
        if(state == "minimized" && currentState != "minimized" && parent is IUIComponent)
            toMinimized();
        else if(state == "maximized" && currentState != "maximized")
            toMaximized();
        else if(state == "normal" && currentState != "normal")
            toNormal();

    }

    //--------------------------------------------------------------------------
    //
    //  Event Handlers
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  To keep the window with focus at the TOP.
     */
    private function bringToFront(event:MouseEvent):void
    {
        if(event.currentTarget == this && parent)
            parent.setChildIndex(this,parent.numChildren-1);
    }

    /**
     *  @private
     */
    private function closeHandler(event:Event):void
    {
        if(this.parent)
            this.parent.removeChild(this);
        event.stopPropagation();
    }

    /**
     *  @private
     */
    private function mouseDownHandler(event:MouseEvent):void
    {
        systemManager.addEventListener(
            MouseEvent.MOUSE_MOVE, systemManager_mouseMoveHandler, true);

        systemManager.addEventListener(
            MouseEvent.MOUSE_UP, systemManager_mouseUpHandler, true);

        systemManager_mouseDownHandler(event);
    }

    /**
     *  @private
     */
    private function systemManager_mouseDownHandler(event:MouseEvent):void
    {
        if(handleEdge != EDGE_NONE)
        {
            isHandleDragging = true;
            event.stopPropagation();
        }
    }

    /**
     *  @private
     */
    private function systemManager_mouseMoveHandler(event:MouseEvent):void
    {
        if(this.parent!=null)
        {
            if(isHandleDragging)
                resize(event);
            else
                setResizeCursor(event);
        }
    }

    /**
     *  @private
     */
    private function systemManager_mouseUpHandler(event:MouseEvent):void
    {
        systemManager.removeEventListener(MouseEvent.MOUSE_MOVE, systemManager_mouseMoveHandler, true);
        systemManager.removeEventListener(MouseEvent.MOUSE_UP, systemManager_mouseUpHandler, true);
        systemManager.removeEventListener(MouseEvent.MOUSE_DOWN, systemManager_mouseDownHandler, true);
        isHandleDragging = false;
    }
	
    /**
     *  @private
     */
    private function titleBar_mouseClickHandler(event:MouseEvent):void
    {
        if(this.parent!=null && event is MouseEvent)
        {
            if(event.type == MouseEvent.DOUBLE_CLICK && event.currentTarget == titleBar)
            {
                if(currentState == "normal")
                    toMaximized();
                else if(currentState == "minimized" || currentState == "maximized")
                    toNormal();
            }
            else if(event.type == MouseEvent.CLICK)
            {
                //State Automata between minimized/maximized/normal states
                if(event.target == minimizeButton)
                {
                    if(currentState == "minimized")
                        toNormal();
                    else if(currentState == "maximized" || currentState == "normal")
                        if(parent is IUIComponent)
                            toMinimized();
                }
                else if(event.target == maximizeButton)
                {
                    if(currentState == "maximized")
                        toNormal();
                    else if(currentState == "minimized" || currentState == "normal")
                        toMaximized();

                }
                event.preventDefault();
            } 
        }
    }

    /**
     *  @private
     */
    private function isCursorOnEdge(event:MouseEvent):void
    {
        var point:Point, eventPoint:Point;
        var x1:Number, x2:Number, y1:Number, y2:Number;

        var tolerance:Number = getStyle("resizeAffordance") as Number;

        point = new Point(x,y);
        point = this.parent.localToGlobal(point);
        eventPoint = new Point(event.stageX, event.stageY);

        //Distance from right edge
        x1 = eventPoint.x - this.width - point.x;
        //Distance from left edge
        x2 = eventPoint.x-point.x;
        //Distance from bottom edge       
        y1 = eventPoint.y - this.height-point.y;
        //Distance from top edge
        y2 = eventPoint.y - point.y;

        if (event.type == "rollOut")
        {
            handleEdge = EDGE_NONE;
        }
        else
        {
            handleEdge = EDGE_NONE;
            if(handleEdge == EDGE_NONE)
            {
                if(Math.abs(x1) < tolerance && Math.abs(y1) < tolerance)
                    handleEdge = EDGE_CORNER;
                else if(Math.abs(x1) < tolerance && Math.abs(y2) < tolerance)
                    handleEdge = EDGE_RIGHT_TOP;
                else if(Math.abs(x2) < tolerance && Math.abs(y2) < tolerance)
                    handleEdge = EDGE_LEFT_TOP;
                else if(Math.abs(x2) < tolerance && Math.abs(y1) < tolerance)
                    handleEdge = EDGE_LEFT_BOTTOM;
                else if(Math.abs(y1) < tolerance)
                    handleEdge = EDGE_BOTTOM;
                else if(Math.abs(x1) < tolerance)
                    handleEdge = EDGE_RIGHT;
                else if(Math.abs(x2) < tolerance)
                    handleEdge = EDGE_LEFT;
                else if(Math.abs(y2) < tolerance)
                    handleEdge = EDGE_TOP;
            }
        }
    }

    /**
     *  @private
     */
    private function populateProperty(target:DisplayObject, name:String, value:Number):SetProperty
    {
        var sp:SetProperty = new SetProperty();
        sp.target = target;
        sp.name = name;
        sp.value = value;
        return sp;
    }


    /**
     *  @private
     */
    private function pushStateProperties(state:State, _x:int, _y:int,
                                         _width:uint, _height:uint,
                                         perWidth:uint, perHeight:uint):void
    {
        var sp:SetProperty;

        sp = populateProperty(this, "x", _x);
        state.overrides.push(sp);

        sp = populateProperty(this, "y", _y);
        state.overrides.push(sp);    	

        if(perWidth)
            sp = populateProperty(this, "percentWidth", perWidth);
        else
            sp = populateProperty(this, "width", _width);
        state.overrides.push(sp);    	

        if(perHeight)
            sp = populateProperty(this, "percentHeight", perHeight);
        else	    
            sp = populateProperty(this, "height", _height);

        state.overrides.push(sp);    	

        setCurrentState(state.name);
    }

    /**
     *  @private
     */
    private function resize(event:MouseEvent):void
    {
        var newWidth:Number, newHeight:Number;
        var point:Point = new Point(x,y);
        point = this.parent.localToGlobal(point);

        var eventPoint:Point = new Point(event.stageX, event.stageY);

        if (handleEdge == EDGE_BOTTOM)
            setSize(point.x,point.y,width, eventPoint.y-point.y);
        else if(handleEdge == EDGE_RIGHT)
            setSize(point.x,point.y,eventPoint.x-point.x, height);
        else if(handleEdge == EDGE_LEFT)
            setSize(eventPoint.x,point.y, point.x-eventPoint.x+width, height);
        else if(handleEdge == EDGE_TOP)
            setSize(point.x,eventPoint.y, width, point.y-eventPoint.y+height);
        else if(handleEdge == EDGE_CORNER)
            setSize(point.x,point.y,eventPoint.x-point.x, eventPoint.y-point.y);
        else if(handleEdge == EDGE_LEFT_TOP)
            setSize(eventPoint.x, eventPoint.y, point.x-eventPoint.x+width, point.y-eventPoint.y+height);
        else if(handleEdge == EDGE_LEFT_BOTTOM)
            setSize(eventPoint.x, point.y, point.x-eventPoint.x+width, eventPoint.y - point.y);
        else if(handleEdge == EDGE_RIGHT_TOP)
            setSize(point.x, eventPoint.y, eventPoint.x - point.x, point.y-eventPoint.y+height);
    }

    /**
     *  @private
     */
    private function resizeHandler(event:Event):void
    {
        if(currentState == "maximized")
        {
            toPseudoMax();
            toMaximized();
        }
    }

    /**
     *  @private
     */
    private function setEffects():void
    {
        stateNormal = new State();
        stateNormal.name = "normal";
        states.push(stateNormal);

        stateMin = new State();
        stateMin.name = "minimized";
        states.push(stateMin);

        stateMax = new State();
        stateMax.name = "maximized";
        states.push(stateMax);

        statePseudoMax = new State();
        statePseudoMax.name = "pseudoMax";
        states.push(statePseudoMax);

        var move:Move = new Move();
        move.duration = 200;
        move.easingFunction = Bounce.easeInOut;

        var resize:Resize = new Resize();
        resize.duration = 200;
        resize.easingFunction = Linear.easeIn;

        var parallel:Parallel = new Parallel();
        parallel.children.push(move);
        parallel.children.push(resize);
        parallel.target = this;

        var seq:Sequence = new Sequence();
        seq.children.push(parallel);

        var transition:Transition = new Transition();
        transition.effect = seq;
        transition.fromState = "*";
        transition.toState = "*";

        transitions.push(transition);
        currentState = "normal";
    }

    /**
     *  @private
     */
    private function setResizeCursor(event:MouseEvent):void
    {
        isCursorOnEdge(event);
        if (handleEdge != EDGE_NONE && currentState == "normal")
        {
            if(cursorId==0 || lastEdge != handleEdge)
            {
                if(lastEdge != handleEdge)
                {
                    CursorManager.removeCursor(cursorId);
                    cursorId=0;
                }
                if(handleEdge == EDGE_CORNER || handleEdge == EDGE_LEFT_TOP)
                    cursorId=CursorManager.setCursor(dCursorSymbol, CursorManagerPriority.HIGH);
                else if(handleEdge == EDGE_LEFT_BOTTOM || handleEdge == EDGE_RIGHT_TOP)
                    cursorId=CursorManager.setCursor(rdCursorSymbol, CursorManagerPriority.HIGH);
                else if(handleEdge == EDGE_RIGHT || handleEdge == EDGE_LEFT)
                    cursorId=CursorManager.setCursor(hCursorSymbol, CursorManagerPriority.HIGH);
                else if(handleEdge == EDGE_BOTTOM || handleEdge == EDGE_TOP)
                    cursorId=CursorManager.setCursor(vCursorSymbol, CursorManagerPriority.HIGH);

                lastEdge = handleEdge;
            }
        }
        else
        {
            if(cursorId!=0)
            {
                CursorManager.removeCursor(cursorId);
                cursorId=0;
                lastEdge = EDGE_NONE;
            }
        }
    }



    /**
     *  @private
     */
    private function setSize(newX:Number,newY:Number,newWidth:Number, newHeight:Number):void
    {
        if(newWidth > 100 && newHeight >35)
        {
            var point:Point = new Point(newX, newY);
            point = this.parent.globalToLocal(point);
            x = point.x;
            y = point.y;

            width = newWidth;
            height = newHeight;

            storeOrigDimensions();
        }
    }

    /**
     *  @private
     */
    private function storeOrigDimensions():void
    {
        origPosition = new Point();
        origDimensions = new Point();
        origPerDimensions = new Point();

        //In case this is a Pop up, special care is needed
        if (parent is ISystemManager)
        {
            origPosition.x = parent.x;
            origPosition.y = parent.y;
            origPosition = this.localToGlobal(origPosition);
        }
        else
        {
            origPosition.x = x;
            origPosition.y = y;
        }

        if(percentWidth)
            origPerDimensions.x = percentWidth;
        else
            origDimensions.x = getExplicitOrMeasuredWidth() ;

        if(percentHeight)
            origPerDimensions.y = percentHeight;
        else
            origDimensions.y = getExplicitOrMeasuredHeight();
    }

    /**
     *  @private
     */
    private function toMinimized():void
    {
        TaskBarManager.addToTaskBar(this);
        
        pushStateProperties(stateMin, x, y,
                            200, titleBar.height, 
                            NaN, NaN);
        
        minimizeButton.setStyle("upSkin", getStyle("restoreButtonUpSkin"));
        minimizeButton.setStyle("disabledSkin", getStyle("restoreButtonDisabledSkin"));
        minimizeButton.setStyle("downSkin", getStyle("restoreButtonDownSkin"));
        minimizeButton.setStyle("OverSkin", getStyle("restoreButtonOverSkin"));
        
        maximizeButton.setStyle("upSkin", getStyle("maximizeButtonUpSkin"));
        maximizeButton.setStyle("disabledSkin", getStyle("maximizeButtonDisabledSkin"));
        maximizeButton.setStyle("downSkin", getStyle("maximizeButtonDownSkin"));
        maximizeButton.setStyle("OverSkin", getStyle("maximizeButtonOverSkin"));
        
        dispatchEvent(new Event("minimize"));
    }

    /**
     *  @private
     */
    private function toMaximized():void
    {
		storeOrigDimensions();
		
        var point:Point = new Point(0,0);
        var maxWidth:uint, maxHeight:uint;

        if(currentState == "minimized")
            TaskBarManager.removeFromTaskBar(this);

        var vm:EdgeMetrics = new EdgeMetrics(0,0,6,8+(TaskBarManager.visible ? 27:0));

        if(parent is Application)
        {
            const s:Rectangle = ISystemManager(root).screen;
            maxWidth = s.width+4;
            maxHeight = s.height+4;
        }
        else 
        {
            maxWidth = parent.width+4;
            maxHeight = parent.height+4;

            point = parent.localToGlobal(point);
            point = parent.globalToLocal(point);
        }

        pushStateProperties(stateMax, point.x, point.y,
                            maxWidth-vm.right, maxHeight-vm.bottom,
                            NaN, NaN);

        maximizeButton.setStyle("upSkin", getStyle("restoreButtonUpSkin"));
        maximizeButton.setStyle("disabledSkin", getStyle("restoreButtonDisabledSkin"));
        maximizeButton.setStyle("downSkin", getStyle("restoreButtonDownSkin"));
        maximizeButton.setStyle("OverSkin", getStyle("restoreButtonOverSkin"));

        minimizeButton.setStyle("upSkin", getStyle("minimizeButtonUpSkin"));
        minimizeButton.setStyle("disabledSkin", getStyle("minimizeButtonDisabledSkin"));
        minimizeButton.setStyle("downSkin", getStyle("minimizeButtonDownSkin"));
        minimizeButton.setStyle("OverSkin", getStyle("minimizeButtonOverSkin"));

        dispatchEvent(new Event("maximize"));
    }

    /**
     *  @private
     */
    private function toNormal():void
    {
        if(currentState == "minimized")
            TaskBarManager.removeFromTaskBar(this);
        
        pushStateProperties(stateNormal, origPosition.x, origPosition.y,
                            origDimensions.x, origDimensions.y,
                            origPerDimensions.x, origPerDimensions.y);

        maximizeButton.setStyle("upSkin", getStyle("maximizeButtonUpSkin"));
        maximizeButton.setStyle("disabledSkin", getStyle("maximizeButtonDisabledSkin"));
        maximizeButton.setStyle("downSkin", getStyle("maximizeButtonDownSkin"));
        maximizeButton.setStyle("overSkin", getStyle("maximizeButtonOverSkin"));

        minimizeButton.setStyle("upSkin", getStyle("minimizeButtonUpSkin"));
        minimizeButton.setStyle("disabledSkin", getStyle("minimizeButtonDisabledSkin"));
        minimizeButton.setStyle("downSkin", getStyle("minimizeButtonDownSkin"));
        minimizeButton.setStyle("overSkin", getStyle("minimizeButtonOverSkin"));

        dispatchEvent(new Event("restore"));
    }

    /**
     *  @private
     */
    private function toPseudoMax():void
    {
        if(currentState == "maximized")
            pushStateProperties(statePseudoMax, x, y,
                                width, height,
                                NaN, NaN);
    }

}
}

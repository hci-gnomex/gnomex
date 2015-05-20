/*
* Copyright (c) 2009 Matt Giger http://www.earthbrowser.com/
*
* Permission is hereby granted, free of charge, to any person obtaining a
* copy of this software and associated documentation files (the "Software"),
* to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense,
* and/or sell copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following conditions:
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABIL-
* ITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
* SHALL THE AUTHOR BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package com.earthbrowser.ebutils
{

import flash.display.Stage;
import flash.events.MouseEvent;
import flash.display.InteractiveObject;
import flash.external.ExternalInterface;

/**
*
* Flash does not support the MOUSE_WHEEL event for Macs. This is a tricky solution to the problem
* in a single AS3 class without the need for any external javascript code. The trick here is to inject the
* mouse handling javascript code using ExternalInterface. It finds itself in the DOM, intercepts and passes 
* the mouse wheel events which are then disributed to the current InteractiveObject under the cursor.
*
* Usage: Call MacMouseWheelHandler.init(stage); on your SWF's first STAGE_INIT event.
*       
* Inspired by Gabriel Bucknall's MacMouseWheel class.
*/
public class MacMouseWheelHandler
{       
        static private var      _init                   : Boolean                               = false;
        static private var      _currItem               : InteractiveObject;
        static private var      _clonedEvent    : MouseEvent;
        
        static public function  init(stage:Stage):void
        {
                if(!_init)
                {
                        _init = true;
                        stage.addEventListener(MouseEvent.MOUSE_MOVE, function(e:MouseEvent):void
                        {
                                _currItem = InteractiveObject(e.target);
                                _clonedEvent = MouseEvent(e);
                        });
                        
                        // send in the callbacks
                        if(ExternalInterface.available)
                        {
                                var id:String = 'eb_' + Math.floor(Math.random()*1000000);
                                ExternalInterface.addCallback(id, function():void{});
                                ExternalInterface.call(c_jscode);
                                ExternalInterface.call("eb.InitMacMouseWheel", id);
                                ExternalInterface.addCallback('externalMouseEvent', _externalMouseEvent);       
                        }
                }
        }
        
        static private function _externalMouseEvent(delta:Number):void
        {
                if(_currItem && _clonedEvent)
                        _currItem.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_WHEEL, true, false, 
                                _clonedEvent.localX, _clonedEvent.localY, _clonedEvent.relatedObject,
                                _clonedEvent.ctrlKey, _clonedEvent.altKey, _clonedEvent.shiftKey, _clonedEvent.buttonDown,
                                int(delta)));
        }
        
        // javascript mouse handling code
        static private const    c_jscode : XML =
        <script><![CDATA[
                function()
                {
                        // create unique namespace
                        if(typeof eb == "undefined" || !eb)     eb = {};
                        
                        var userAgent = navigator.userAgent.toLowerCase();
                        eb.platform = {
                                win:/win/.test(userAgent),
                                mac:/mac/.test(userAgent)
                        };
                        eb.browser = {
                                version: (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
                                safari: /webkit/.test(userAgent),
                                opera: /opera/.test(userAgent),
                                msie: /msie/.test(userAgent) && !/opera/.test(userAgent),
                                mozilla: /mozilla/.test(userAgent) && !/(compatible|webkit)/.test(userAgent),
                                chrome: /chrome/.test(userAgent)
                        };
                        
                        // find the function we added
                        eb.findSwf = function(id) {
                                var objects = document.getElementsByTagName("object");
                                for(var i = 0; i < objects.length; i++)
                                        if(typeof objects[i][id] != "undefined")
                                                return objects[i];
                                
                                var embeds = document.getElementsByTagName("embed");
                                for(var j = 0; j < embeds.length; j++)
                                        if(typeof embeds[j][id] != "undefined")
                                                return embeds[j];
                                        
                                return null;
                        }
                        
                        eb.InitMacMouseWheel = function(id) {   
                                var swf = eb.findSwf(id);
                                if(swf && eb.platform.mac) {
                                        
                                        var mouseOver = false;

                                        /// Mouse move detection for mouse wheel support
                                        function _mousemove(event) {
                                                mouseOver = event && event.target && (event.target == swf);
                                        }

                                        /// Mousewheel support
                                        var _mousewheel = function(event) {
                                                if(mouseOver) {
                                                        var delta = 0;
                                                        if(event.wheelDelta)            delta = event.wheelDelta / (eb.browser.opera ? 12 : 120);
                                                        else if(event.detail)           delta = -event.detail;
                                                        if(event.preventDefault)        event.preventDefault();
                                                        if(eb.browser.safari) {
															if(delta > 0){
																delta += 1;
															}
															if(delta < 0){
																delta -= 1;
															}
														} 
                                                        swf.externalMouseEvent(delta);
                                                        return true;
                                                }
                                                return false;
                                        }

                                        // install mouse listeners
                                        if(typeof window.addEventListener != 'undefined') {
                                                window.addEventListener('DOMMouseScroll', _mousewheel, false);
                                                window.addEventListener('DOMMouseMove', _mousemove, false);
                                        }
                                        window.onmousewheel = document.onmousewheel = _mousewheel;
                                        window.onmousemove = document.onmousemove = _mousemove;
                                }
                        }       
                }
        ]]></script>;
}

}
package views
{
    import mx.containers.Panel;
    import mx.core.EdgeMetrics;

    public class ExtendedPanel extends Panel
    {
        /**
         * Static var to indicate top alignment for the control bar.
         */
        public static const TOP:String = "top";
        
        /**
         * Static var to indicate bottom alignment for the control bar.
         */
        public static const BOTTOM:String = "bottom";
        
        /**
         * @private
         */
        private var _controlBarPosition:String = ExtendedPanel.BOTTOM;
        
        [Inspectable(enumeration="top,bottom", defaultValue="bottom")]
        
        /**
         * Position of the ControlBar element (if one is present). Possible values 
         * are &apos;top&apos; or &apos;bottom&apos;.
         */
        public function set controlBarPosition(value:String):void {
            _controlBarPosition = value;
            invalidateDisplayList();
        }
        
        /**
         * @private
         */
        public function get controlBarPosition():String {
            return _controlBarPosition;
        }
        
        /**
         *  Constructor.
         */
        public function ExtendedPanel()
        {
            super();
        }
        
        /**
         *  @private
         */
        override protected function layoutChrome(unscaledWidth:Number, unscaledHeight:Number):void {
            super.layoutChrome(unscaledWidth, unscaledHeight);

            /* If we&apos;ve got a controlbar, then it&apos;s already been placed at the bottom
             * by the Panel class (when we called super.layoutChrome). So now we move
             * it to the top instead.
             */
            if(controlBar && _controlBarPosition == ExtendedPanel.TOP) {
                var headerHeight:Number = getHeaderHeight();
                var bm:EdgeMetrics = borderMetrics;
            
                //we put the controlbar just below the header
                controlBar.move(bm.left,
                    headerHeight + bm.top + 1);
               }
        }
        /**
         * @private  
         *
         * We use the viewMetrics of our Panel superclass first, then we modify that a bit.
         * We check if there&apos;s a control bar visible, if there is then we alter the top and
         * bottom margins if we&apos;re supposed to show the control bar at the top instead of the
         * bottom.
         */
        override public function get viewMetrics():EdgeMetrics
        {
            var vm:EdgeMetrics = super.viewMetrics;
            
            if (controlBar && controlBar.includeInLayout && _controlBarPosition == ExtendedPanel.TOP) {
                var bm:EdgeMetrics = borderMetrics;
            
                var btl:Number = getStyle("borderThicknessLeft");
                var btb:Number = getStyle("borderThicknessBottom");
            
                vm.top += controlBar.getExplicitOrMeasuredHeight();
                vm.bottom = isNaN(btb) ? (isNaN(btl) ? vm.bottom : btl) : btb;
              }

              return vm;
        }
        
    }
}

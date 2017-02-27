package hci.flex.controls
{
	import flash.display.Graphics;
	
	import hci.flex.renderers.RendererFactory;

	import mx.controls.AdvancedDataGrid;
	import mx.controls.DataGrid;
	import mx.controls.Label;
	import mx.core.IFactory;

	public class Label extends mx.controls.Label
    {   public var _dataField:String;
 		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
	    public var missingRequiredFieldBorder:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER;
	    public var missingRequiredFieldBorderThickness:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS;
		public var highlightedColor:uint = RendererFactory.DEFAULT_HIGHLIGHT_COLOR;

		public static function create(dataField:String):IFactory {
				return RendererFactory.create(hci.flex.controls.Label,
				                              {_dataField: dataField});

		}

		public static function createCustom(dataField:String,
		                                    theMissingRequiredFieldBackground:uint,
		                                    theMissingRequiredFieldBorder:uint,
		                                    theMissingRequiredFieldBorderThickness:uint):IFactory {
				return RendererFactory.create(hci.flex.controls.Label,
				{ _dataField: dataField,
				missingRequiredFieldBackground: theMissingRequiredFieldBackground,
				missingRequiredFieldBorder: theMissingRequiredFieldBorder,
				missingRequiredFieldBorderThickness: theMissingRequiredFieldBorderThickness});

		}

        public function set dataField(dataField:String):void {
            this._dataField = dataField;
        }

		override public function set data(value:Object):void{
			super.data = value;
		}
        override protected function initializationComplete():void {
	        initializeFields();
        }
        
        protected function initializeFields():void {        	
        }
		
		private function getToolTip():String{
			if ( listData.owner is AdvancedDataGrid ) {
				var dg:AdvancedDataGrid = listData.owner as AdvancedDataGrid;
				var func:Function = dg.columns[listData.columnIndex].dataTipFunction;
				if(func != null){
					return func.call(this, this.data);
				}else{
					return "";
				}
			} else  {
				var grid:DataGrid = listData.owner as DataGrid;
				return grid.toolTip;
			}
		}

        override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth, unscaledHeight);
            var g:Graphics = graphics;
            g.clear();
            if (data == null) {
                return;
            }
            if (!data.hasOwnProperty(_dataField)) {
                return;
            }
            this.toolTip = getToolTip();

			if(_dataField == "@multiplexGroupNumber"){
            	if (data[_dataField] != '' && isNaN(parseInt(data[_dataField]))) {
                    g.beginFill(errorBackground);
                    g.lineStyle(missingRequiredFieldBorderThickness, missingRequiredFieldBorder);
                    g.drawRect(0, 0, unscaledWidth, unscaledHeight);
                    g.endFill();
				}

			}
            if (data[_dataField] == '') {
                g.beginFill(missingRequiredFieldBackground);
                g.lineStyle(missingRequiredFieldBorderThickness,
                        missingRequiredFieldBorder);
                g.drawRect(0, 0, unscaledWidth, unscaledHeight);
                g.endFill();
            }
            if (_dataField == '@numberSequencingLanes') {
                g.beginFill(highlightedColor);
                g.lineStyle(missingRequiredFieldBorderThickness, highlightedColor);
                g.drawRect(0, 0, unscaledWidth, unscaledHeight);
                g.endFill();
            }


        }
    }
}
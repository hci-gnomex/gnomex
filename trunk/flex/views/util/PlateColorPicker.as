package views.util {

	import flash.events.Event;
	import mx.controls.ColorPicker;
	import mx.events.FlexEvent;
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.StyleManager;

	public class PlateColorPicker extends mx.controls.ColorPicker {
		public var colorArray:Array;

		private var noSelectionText:String = '----';


		public function PlateColorPicker():void {

			super();
			editable = false;
			dataProvider = defaultColorArray;
			setupStyle();
			selectedItem = defaultColorArray[ 0 ];
		}


		// This function sets up the colors for the color picker
		// given a list of names for each color to be called.
		// This is so that you can send in a list of orders and
		// the color picker will set up as many colors as orders
		// each with an order name.
		public function setUpColors( names:Array ):void {

			colorArray = new Array();
			var i:int;

			for ( i = 0; i < names.length; i++ ) {
				colorArray[ i ] = defaultColorArray[ i + 1 ];
				colorArray[ i ].label = names[ i ].toString();
			}
			colorArray.unshift({ color: '0x00FFFFFF', label: noSelectionText });
			dataProvider = colorArray;
			selectedItem = colorArray[ 0 ];
		}


		public function selectNone():void {

			selectedItem = colorArray[ 0 ];
			selectedIndex = 0;
			dispatchEvent( new FlexEvent( FlexEvent.VALUE_COMMIT ));
		}


		public function getColorArray():Array {

			return colorArray;
		}


		public function getColorAt( index:int ):uint {

			return colorArray[ index ].color;
		}


		public function getLabel( index:int ):String {

			return colorArray[ index ].label;
		}


		public function getLabelIndex( label:String ):int {

			var index:int;

			for ( index = 1; index < colorArray.length; index++ ) {
				if ( label == colorArray[ index ].label ) {
					return index;
				}
			}
			return 0;
		}


		// If you need to reset the colors to the default list
		public function resetColors():void {

			colorArray = new Array();
			colorArray.unshift({ color: '0x00FFFFFF', label: noSelectionText });
			dataProvider = colorArray;
			selectedItem = colorArray[ 0 ];
		}


		private function setupStyle():void {

			var swatchStyle:CSSStyleDeclaration = new CSSStyleDeclaration();
			swatchStyle.setStyle( 'swatchWidth', 25 );
			swatchStyle.setStyle( 'swatchHeight', 25 );
			swatchStyle.setStyle( 'textFieldWidth', 100 );
			swatchStyle.setStyle( 'swatchBorderSize', 2 );
			StyleManager.setStyleDeclaration( '.customColorPicker', swatchStyle, true );
			setStyle( 'swatchPanelStyleName', 'customColorPicker' );
		}


		public function getNoSelectionText():String {

			return noSelectionText;
		}


		public function setNoSelectionText( noSelectionText:String ):void {

			this.noSelectionText = noSelectionText;
		}

		// A default list of colors to use
		public var defaultColorArray:Array = [{ color: '0x00FFFFFF', label: noSelectionText }, { color: '0xFF0000FF', label: "" }, { color: '0xFF00FFFF', label: "" }, { color: '0xFF7FFF00', label: "" }, { color: '0xFFFF7F50', label: "" }, { color: '0xFF6495ED', label: "" }, { color: '0xFFDC143C', label: "" }, { color: '0xFF006400', label: "" }, { color: '0xFF8B008B', label: "" }, { color: '0xFF00008B', label: "" }, { color: '0xFF2F4F4F', label: "" }, { color: '0xFF00CED1', label: "" }, { color: '0xFFFF1493', label: "" }, { color: '0xFF008B8B', label: "" }, { color: '0xFF1E90FF', label: "" }, { color: '0xFFFF00FF', label: "" }, { color: '0xFFFF8C00', label: "" }, { color: '0xFFDCDCDC', label: "" }, { color: '0xFFFFD700', label: "" }, { color: '0xFF483D8B', label: "" }, { color: '0xFF808080', label: "" }, { color: '0xFFADFF2F', label: "" }, { color: '0xFF4B0082', label: "" }, { color: '0xFF8FBC8F', label: "" }, { color: '0xFFF0E68C', label: "" }, { color: '0xFFADD8E6', label: "" }, { color: '0xFFF08080', label: "" }, { color: '0xFFADD8E6', label: "" }, { color: '0xFF90EE90', label: "" }, { color: '0xFFFFA07A', label: "" }, { color: '0xFF20B2AA', label: "" }, { color: '0xFF87CEFA', label: "" }, { color: '0xFFB0C4DE', label: "" }, { color: '0xFF32CD32', label: "" }, { color: '0xFF800000', label: "" }, { color: '0xFF66CDAA', label: "" }, { color: '0xFF9370DB', label: "" }, { color: '0xFF00FA9A', label: "" }, { color: '0xFF6B8E23', label: "" }, { color: '0xFFFFA500', label: "" }, { color: '0xFFFF4500', label: "" }, { color: '0xFFDA70D6', label: "" }, { color: '0xFFAFEEEE', label: "" }, { color: '0xFFDB7093', label: "" }, { color: '0xFFFFDAB9', label: "" }, { color: '0xFFCD853F', label: "" }, { color: '0xFFDDA0DD', label: "" }, { color: '0xFFB0E0E6', label: "" }, { color: '0xFFBC8F8F', label: "" }, { color: '0xFF4169E1', label: "" }, { color: '0xFFFA8072', label: "" }, { color: '0xFFF4A460', label: "" }, { color: '0xFF2E8B57', label: "" }, { color: '0xFFFFF5EE', label: "" }, { color: '0xFFC0C0C0', label: "" }, { color: '0xFF6A5ACD', label: "" }, { color: '0xFF00FF7F', label: "" }, { color: '0xFF4682B4', label: "" }, { color: '0xFFD8BFD8', label: "" }, { color: '0xFFFF6347', label: "" }, { color: '0xFF40E0D0', label: "" }, { color: '0xFFEE82EE', label: "" }, { color: '0xFFFFFF00', label: "" }, { color: '0xFF9ACD32', label: "" }, { color: '0xE3CF57', label: "" }, { color: '0x8B475D', label: "" }, { color: '0x6495ED', label: "" }, { color: '0x33A1C9', label: "" }, { color: '0x87CEFF', label: "" }, { color: '0x00C957', label: "" }, { color: '0xFFFF00', label: "" }, { color: '0xFFFAEBD7', label: "" }, { color: '0xFF9932CC', label: "" }, { color: '0xFF5F9EA0', label: "" }, { color: '0xFFE9967A', label: "" }, { color: '0xFFA9A9A9', label: "" }, { color: '0xFFF5F5F5', label: "" }];
	}
}

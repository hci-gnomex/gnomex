package views.renderers 
{
	import flash.display.Graphics;
	
	import mx.controls.Label;

	public class LabelDictionary extends mx.controls.Label
	{ 	
		protected var _dataField:String;
		protected var _dictionary:String;
	    protected var _dictionaryDisplayField:String = "@display";
	    protected var _dictionaryValueField:String   = "@value";
	    protected var _isRequired:Boolean = false;
	    
	    protected var _dictionaryList:XMLList;
	    protected var _dictionaryEntry:Object = null;
	    

		public function set dataField(dataField:String):void {
			this._dataField = dataField;	
		}
 		public function set dictionary(dictionary:String):void {
		    this._dictionary = dictionary;
		}
		    
		public function set dictionaryDisplayField(dictionaryDisplayField:String):void {
		 	this._dictionaryDisplayField = dictionaryDisplayField;
		}

	 	public function set dictionaryValueField(dictionaryValueField:String):void {
			this._dictionaryValueField = dictionaryValueField;
		}
		
		public function set isRequired(isRequired:Boolean):void {
			_isRequired = isRequired;
		}
		
		override protected function initializationComplete():void
        {
        	_dictionaryList = new XMLList(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == _dictionary).DictionaryEntry); 
        }		
		[Bindable]
		override public function get text():String {
			for each(var de:Object in _dictionaryList) {
				if (de[_dictionaryValueField] == data[_dataField]) {
					_dictionaryEntry = de;
					break;
				}
			}
        	if (_dictionaryEntry != null) {
        		super.text = _dictionaryEntry[_dictionaryDisplayField];
        	} else {
        		super.text = "";
        	}
        	
        	return super.text;
		}
		
		override public function set text(text:String):void {
			super.text = text;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          if (_isRequired) {
	          var g:Graphics = graphics;
	          g.clear();
	          g.beginFill( data[_dataField] == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : 0xffffff );
	          g.drawRect(0,0,unscaledWidth,unscaledHeight);
	          g.endFill();
          }

	     }		
	}
}
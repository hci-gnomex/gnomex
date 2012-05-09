package views.renderers {

	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.text.TextField;

	import mx.collections.XMLListCollection;
	import mx.controls.CheckBox;
	import mx.controls.DataGrid;
	import mx.controls.List;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.ListBase;

	public class CheckBoxHeader extends CheckBox {
		private var _data:Object;

		private var partiallySelected:Boolean = false;

		private var addedListener:Boolean = false;


		[Bindable]
		override public function get data():Object {
			return _data;
		}


		override public function set data( o:Object ):void {
			invalidateProperties();
		}


		override protected function initializationComplete():void {
			this.addEventListener( Event.CHANGE, change );
		}


		private function change( event:Event ):void {

			for ( var i:int = 0; i < ListBase( owner ).dataProvider.length; i++ ) {
				ListBase( owner ).dataProvider[ i ].@isSelected = this.selected;
			}

		}


		override protected function commitProperties():void {
			super.commitProperties();

			if ( owner is ListBase ) {
				if ( !addedListener ) {
					addedListener = true;
					owner.addEventListener( "valueCommit", owner_changeHandler, false, 0, true );
					owner.addEventListener( "change", owner_changeHandler, false, 0, true );
				}

				var selectedCount:int = 0;

				for ( var i:int = 0; i < ListBase( owner ).dataProvider.length; i++ ) {
					var chObj:Object = ListBase( owner ).dataProvider[ i ];

					if ( chObj.@isSelected != null && chObj.@isSelected == true ) {
						selectedCount++;
					}
				}

				if ( selectedCount == 0 ) {
					selected = false;
					partiallySelected = false;
				} else if ( ListBase( owner ).dataProvider.length == selectedCount ) {
					selected = true;
					partiallySelected = false;
				} else {
					selected = false;
					partiallySelected = true;
				}

				invalidateDisplayList();
			}
		}


		private function owner_changeHandler( event:Event ):void {
			invalidateProperties();
		}


		override protected function updateDisplayList( w:Number, h:Number ):void {
			super.updateDisplayList( w, h );


			graphics.clear();

			if ( listData is DataGridListData ) {
				var n:int = numChildren;

				for ( var i:int = 0; i < n; i++ ) {
					var c:DisplayObject = getChildAt( i );

					if ( !( c is TextField )) {
						c.x = ( w - c.width ) / 2;
						c.y = 0;
						c.alpha = 1;

						if ( partiallySelected ) {
							graphics.beginFill( 0x000000 );
							graphics.drawRect( c.x, c.y, c.width, c.height );
							graphics.endFill();
							c.alpha = 0.7;
						}
					}
				}
			}

		}
	}
}

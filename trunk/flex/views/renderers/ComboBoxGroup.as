package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.IList;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	
	public class ComboBoxGroup  extends views.renderers.ComboBoxDictionary
	{
			public static function create(dataField:String):IFactory {
				return RendererFactory.create(ComboBoxGroup, 
				{ dataField: dataField,
				  dictionaryValueField: "@idLab",
				  dictionaryDisplayField: "@name"});			
				  
			}	
		    
		    override public function set data(o:Object):void
            {
                _data = o;
                if (parentDocument.labs == null) {
                	return;
                }
                setDataProvider();
				selectItem(); 
            }
		    
		    protected override function setDataProvider():void {
				dataProvider = parentDocument.labs;
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
            override protected function initializationComplete():void
            {   
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = this.dictionaryDisplayField;
            }
            
             protected override function change(event:ListEvent):void {
             	super.change(event);
             	var params:Object = new Object();
             	params.idLab = _data.@idLab;
             	parentDocument.getLab.send(params);
             }
		    
			protected override function assignData():void {
 				super.assignData();
 				_data.@labName  = this.selectedItem.@name;
            }     		
            
	}

}
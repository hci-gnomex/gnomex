package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.IList;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	
	public class ComboBoxGroup  extends views.renderers.ComboBox
	{
			public static function create(dataField:String):IFactory {
				return RendererFactory.create(ComboBoxGroup, 
				{ dataField: dataField,
				  updateData: true,
				  valueField: "@idLab",
				  labelField: "@name"});							  
			}			   		    
            
            override protected function initializationComplete():void
            {   
            	this.dataProvider = parentDocument.labs;
            	super.initializationComplete();            	
            }
            
             protected override function change(event:ListEvent):void {
             	super.change(event);
             	var params:Object = new Object();
             	params.idLab = data.@idLab;
             	parentDocument.getLab.send(params);
             }
		    
			protected override function assignData():void {
 				super.assignData();
 				data.@labName  = this.selectedItem.@name;
            }     		
            
	}

}
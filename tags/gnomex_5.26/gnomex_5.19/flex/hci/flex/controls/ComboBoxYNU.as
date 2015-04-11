// ActionScript file
package hci.flex.controls
{
	import mx.collections.XMLListCollection;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	
	/**
	 * Standalone Usage:
	 * 		set value for combobox (use databinding)
	 * 
	 * Grid Usage:
	 * 		set GRID's data to XML (use databinding)
	 * 		column.itemRenderer = ComboBox.getFactory(dataValueField)
	 * 		dataValueField is attribute in GRID's XML containing the value (Y or N)
	 */	

	public class ComboBoxYNU extends hci.flex.controls.ComboBox
	{							    
	    public function ComboBoxYNU() {
				super();
				
	    	this.appendBlankRow = true;
	    	
				var ac:XMLListCollection= new XMLListCollection();
				ac.addItem(new XML("<Option display='Yes' value='Y'/>"));
				ac.addItem(new XML("<Option display='No' value='N'/>"));
				ac.addItem(new XML("<Option display='Unknown' value='U'/>"));
			     	
				this.dataProvider = ac;
	    }
	    
		public static function getFactory(dataField:String):IFactory {			
			return RendererFactory.create(hci.flex.controls.ComboBoxYNU, {dataField: dataField});				
		} 	   
	}
}
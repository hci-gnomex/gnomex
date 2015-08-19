package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	import mx.core.IFactory;
	import views.renderers.ComboBox;

	public class ComboBoxNumberSequencingCyclesAllowed extends ComboBox
	{
		public static function create(dataProvider:Object):IFactory {
			return RendererFactory.create(ComboBoxNumberSequencingCyclesAllowed, 
				{ dataProvider: dataProvider,
					dataField: '@idNumberSequencingCyclesAllowed',				
					updateData: true,
					valueField: '@value',
					labelField: '@display',
					securityDataField: '@canChangeNumberSequencingCycles'});							  
		}			    	    
		
		protected override function assignData():void {
			super.assignData();
			data.@idSeqRunType  = this.selectedItem.@idSeqRunType;
			data.@idNumberSequencingCycles = this.selectedItem.@idNumberSequencingCycles;
		}           
	}
}
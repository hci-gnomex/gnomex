package views.renderers
{
	
	import mx.controls.advancedDataGridClasses.AdvancedDataGridItemRenderer;
	
	public class BillingADGItemRenderer extends AdvancedDataGridItemRenderer
	{  
		override public function validateProperties():void {
			super.validateProperties();
			
			if ( data && listData) {
				if ( data.@other == 'Y' ) {
					this.setStyle('fontStyle','italic');
					this.setStyle('color','#747170');
					
				} else {
					this.setStyle('fontStyle','plain');
					this.setStyle('color','0x000000');
					
				}
			}
			
		}

	}
}
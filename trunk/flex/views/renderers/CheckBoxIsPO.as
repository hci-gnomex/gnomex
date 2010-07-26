package views.renderers
{
	import flash.events.Event;
	import hci.flex.controls.CheckBox;
	import hci.flex.renderers.RendererFactory;
	import mx.core.IFactory;
	
	public class CheckBoxIsPO extends CheckBox
	{
		  public static function create():IFactory {
		  	return RendererFactory.create(views.renderers.CheckBoxIsPO, 
		  	 {checkedValue:'Y', 
			  uncheckedValue:'N', 
			  dataField:'@isPO', 
			  updateData:true});
				  
		 }	
          protected override function change(event:Event):void {
          	
          	super.change(event);
          	
          	// indicate that the billing account is dirty
          	parentDocument.setDirtyBillingAccount(data);
          }
	}
}
package views.renderers
{
	import flash.events.Event;
	import hci.flex.controls.CheckBox;
	import hci.flex.renderers.RendererFactory;
	import mx.core.IFactory;
	
	public class CheckBoxIsSelectedChipType extends CheckBox
	{
		  public static function create():IFactory {
		  	return RendererFactory.create(views.renderers.CheckBoxIsSelectedChipType, 
		  	 {checkedValue:'true', 
			  uncheckedValue:'false', 
			  dataField:'@isSelected', 
			  updateData:true});
				  
		 }	
          protected override function change(event:Event):void {
          	
          	super.change(event);
          	
          	if (this.selected) {
          		// toggle all other selections off
	          	parentDocument.toggleOtherChipTypeSelections(data.@value);          		
          	}          
	        
	        // check if required fields has been entered
	        parentDocument.checkRequiredChipType();
          	// initialize the samples grid
          	parentDocument.parentDocument.samplesView.initializeSamplesGrid();
          	// propagate selected chip type to samples
          	parentDocument.propagateChipType();       	
          	// check for sampleSetup completeness
          	parentDocument.checkSampleSetupCompleteness();
          }
	}
}
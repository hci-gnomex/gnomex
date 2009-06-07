package views.renderers
{
	import flash.events.Event;
	import hci.flex.renderers.CheckBox;
	
	public class CheckBoxIsSelectedChipType extends CheckBox
	{
          override protected function initializationComplete():void { 
          	this.addEventListener(Event.CHANGE, change);
          }   
          
          protected override function change(event:Event):void {
	      	if (this.selected) {
	      		_data[_dataField] = "true";
          		// toggle all other selections off
	          	parentDocument.toggleOtherChipTypeSelections(_data.@value);
	      	} else {
	      		_data[_dataField] = "false";
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
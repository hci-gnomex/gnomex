package views.renderers
{
	import mx.controls.CheckBox;
	import flash.events.Event;

	public class CheckBoxIsPeerReviewedFunding extends CheckBox
	{
		  private var _data:Object;
		  

          [Bindable]
          override public function get data():Object {
          	return _data;
          }
          override public function set data(o:Object):void {
          	_data = o;
          	if (o != null && o.hasOwnProperty("@isSelectedPeerReviewedFunding") && o.@isSelectedPeerReviewedFunding == "true") {
          		this.selected = true;
          	} else {
          		this.selected = false;
          	}
          }
          override protected function initializationComplete():void {
          	this.addEventListener(Event.CHANGE, change);
          }   
          
          private function change(event:Event):void {
          	if (this.selected) {
          		_data.@isSelectedPeerReviewedFunding = "true";
          	} else {
          		_data.@isSelectedPeerReviewedFunding = "false";
          	}
          }
	}
}
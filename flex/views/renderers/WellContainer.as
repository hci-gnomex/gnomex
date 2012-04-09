package views.renderers 
{
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Box;
	import mx.containers.TitleWindow;
	import mx.controls.Label;
	import mx.controls.TextArea;
	import mx.events.CloseEvent;
	import mx.events.FlexMouseEvent;
	import mx.managers.PopUpManager;
	
	import views.renderers.WellLabel;

	
	public class WellContainer extends mx.containers.Box
	{ 		
		// This is the well's position on the plate, with 0
		// being in the upper left corner and 95 being in the 
		// lower right, either counting DOWN the plate first and then 
		// to the right or to the right first, then down.
		public var index:int;
    public var row:String;
    public var col:int;
		public var sampleName:String;
    public var idSample:int;
		public var color:uint;
		public var groupId:String;
    public var idPlateWell:int;
    public var submitter:String;
    public var submitDate:String;
		
		// Sample object
		 public var sample:Object;
     
     public var hasSample:Boolean = false;
		
		// Temporary constructor - just takes a 'sample name'
		// Later, the constructor will take a well object
		public function WellContainer(sampleName:String):void{
			
			// For the constructor that takes a well object:
			// this.well = well
			super();
			height = 36;
			width = height;
			
			verticalScrollPolicy='off';
			horizontalScrollPolicy='off';
			
			toolTip = sampleName;
			
			setStyle('cornerRadius', height/2);
			setStyle('horizontalAlign','center');
			setStyle('verticalAlign', 'middle');
			setStyle('borderColor', 0x010000);
			setStyle('borderStyle','solid');
			
			this.sampleName = sampleName;
			// The label for the well.
			// Now based on the given name string, later
			// based on the well itself i.e. well.getName() 
			addChild(new WellLabel(sampleName));
			
			addEventListener(MouseEvent.CLICK,onClick);
		}
				
		// function to change the size of the circle
		public function setRadius(radius:int):void{
			height = radius*2;
			width = radius*2;
			setStyle('cornerRadius',radius);
		}
		
		public function setLabel(label:String):void{
			removeAllChildren();
			addChild(new WellLabel(label));
		}
		
		public function setColor(newColor:uint):void{
			setStyle('backgroundColor', newColor);
			color = newColor;
		}
		
		public function getColor():uint{
			return color;
		}
		
		public function getGroupId():String{
			return groupId;
		}
		
		public function setGroupId(id:String):void{
			groupId = id;
		}
    
		// function to get back the sample and its information 
		public function getSample():Object {
			return sample;
		}
		
		// function to set the sample
		public function setSample(sample:Object):void {
			this.sample = sample;
      this.idSample = sample.@idSample;
			this.sampleName = sample.@label;
			this.groupId = sample.@idRequest;
			this.toolTip = sampleName;
      this.hasSample = true;
		}
		
		// Function to display the well information if well is clicked
		public function onClick(event:Event):void{
			displayWellInfo();
		}
		private function displayWellInfo():void{
			if (sample != null) {
				var tw:TitleWindow = new TitleWindow();
				tw.title = 'Well ' + (index + 1) + ', ' + row + col;
        if ( idPlateWell!=0 ) {
          tw.title += ', ' + idPlateWell;
        }
				tw.addEventListener(FlexMouseEvent.MOUSE_DOWN_OUTSIDE,closeWellInfo)
				tw.layout = "vertical";
				
				var sampleLabel:TextArea = new TextArea();
				var requestLabel:TextArea = new TextArea();
				var notesLabel:TextArea = new TextArea();
				
				sampleLabel.text = 'Sample: ' + sampleName;
				requestLabel.text = 'Request #: ' + groupId;
				notesLabel.text = 'Notes: ' + sample.@description;
				
				tw.addChild(sampleLabel);
				tw.addChild(requestLabel);
				tw.addChild(notesLabel);
				
				PopUpManager.addPopUp(tw,this,true);
				PopUpManager.centerPopUp(tw);
			}
		}
		
		public function closeWellInfo(event:FlexMouseEvent):void{
			PopUpManager.removePopUp(TitleWindow(event.target));
		}
	}

}
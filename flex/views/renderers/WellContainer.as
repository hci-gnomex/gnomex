package views.renderers {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Box;
	import mx.containers.TitleWindow;
	import mx.controls.Label;
	import mx.controls.Text;
	import mx.controls.TextArea;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.events.FlexMouseEvent;
	import mx.managers.PopUpManager;
	
	import views.plate.NavPlateView;
	import views.renderers.WellLabel;
	
	public class WellContainer extends mx.containers.Box {
		public var plateView:NavPlateView;
		// This is the well's position on the plate, with 0
		// being in the upper left corner and 95 being in the
		// lower right, either counting DOWN the plate first and then
		// to the right or to the right first, then down.
		public var position:int;
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
		public function WellContainer( sampleName:String ):void {
			
			// For the constructor that takes a well object:
			// this.well = well
			super();
			height = 26;
			width = height;
			verticalScrollPolicy = 'off';
			horizontalScrollPolicy = 'off';
			toolTip = getToolTip();
			setStyle( 'cornerRadius', height / 2 );
			setStyle( 'horizontalAlign', 'center' );
			setStyle( 'verticalAlign', 'middle' );
			setStyle( 'borderColor', 0x010000 );
			setStyle( 'borderStyle', 'solid' );
			this.sampleName = sampleName;

			addChild( new WellLabel( sampleName ));
			addEventListener( MouseEvent.CLICK, onClick );
		}
		
		
		// function to change the size of the circle
		public function setRadius( radius:int ):void {
			
			height = radius * 2;
			width = radius * 2;
			setStyle( 'cornerRadius', radius );
		}
		
		
		public function setLabel( label:String ):void {
			
			removeAllChildren();
			addChild( new WellLabel( label ));
		}
		
		
		public function setColor( newColor:uint ):void {
			
			setStyle( 'backgroundColor', newColor );
			color = newColor;
		}
		
		
		public function getColor():uint {
			
			return color;
		}
		
		
		public function getGroupId():String {
			
			return groupId;
		}
		
		
		public function setGroupId( id:String ):void {
			
			groupId = id;
		}
		
		
		// function to get back the sample and its information
		public function getSample():Object {
			
			return sample;
		}
		
		
		// function to set the sample
		public function setSample( sample:Object ):void {
			
			this.sample = sample;
			this.idSample = sample.@idSample != null ? sample.@idSample : 0;
			this.sampleName = sample.@name != null ? sample.@name : '';
			this.groupId = sample.@idRequest != null ? sample.@idRequest : '';
			this.setToolTip();
			this.hasSample = true;
		}
		
		public function loadWellObject( pw:Object ):void {
			
			this.idPlateWell = pw.@idPlateWell;
			this.groupId = pw.@idRequest;
			this.idSample = pw.@idSample;
			this.sampleName = pw.@sampleName;
			this.submitter = pw.@requestSubmitter;
			this.submitDate = pw.@requestSubmitDate;
			this.setToolTip();
			this.hasSample = true;
		}
		
		public function onClick( event:Event ):void {
			
			if ( plateView != null ) {
				plateView.colorPick.selectedItem = plateView.colorPick.colorArray[ plateView.colorPick.getLabelIndex( this.groupId )];
				plateView.colorPick.selectedIndex = plateView.colorPick.getLabelIndex( this.groupId );
				plateView.colorPick.dispatchEvent( new FlexEvent( FlexEvent.VALUE_COMMIT ));
				event.stopPropagation();
			}
		}
		
		
		private function displayWellInfo():void {
			
			if ( sample != null ) {
				var tw:TitleWindow = new TitleWindow();
				tw.title = 'Well ' + ( position + 1 ) + ', ' + row + col;
				
				if ( idPlateWell != 0 ) {
					tw.title += ', ' + idPlateWell;
				}
				tw.addEventListener( FlexMouseEvent.MOUSE_DOWN_OUTSIDE, closeWellInfo )
				tw.layout = "vertical";
				var sampleLabel:mx.controls.Label  = new mx.controls.Label();
				var requestLabel:mx.controls.Label = new mx.controls.Label();
				var notesLabel:Text                = new mx.controls.Text();
				sampleLabel.text = 'Sample: ' + sampleName;
				requestLabel.text = 'Request #: ' + groupId;
				notesLabel.text = 'Sample Desc: ' + sample.@description;
				tw.addChild( sampleLabel );
				tw.addChild( requestLabel );
				tw.addChild( notesLabel );
				PopUpManager.addPopUp( tw, this, true );
				PopUpManager.centerPopUp( tw );
			}
		}
		
		public function setToolTip():void {
			this.toolTip = getToolTip();
		}
		
		private function getToolTip():String {
			
			var str:String = "";
			if ( sample != null ) {
				str += 'Well ' + ( position + 1 ) + ', ' + row + col + "";
				
				if ( idPlateWell != 0 ) {
					str += ', ' + idPlateWell;
				}
				
				str += '\rSample: ' + sampleName;
				str += '\rRequest #: ' + groupId;
				str += '\rSample Desc: ' + sample.@description;
			}
			return str;
		}
		
		public function closeWellInfo( event:FlexMouseEvent ):void {
			
			PopUpManager.removePopUp( TitleWindow( event.target ));
		}
	}
}

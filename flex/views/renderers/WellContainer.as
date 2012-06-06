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
		
		// PlateWell Fields
		public var idPlateWell:int;
		public var position:int;
		public var row:String;
		public var col:int;
		public var idSample:int;
		public var sampleName:String;
		public var idRequest:int;
		public var codeReactionType:String;
		public var idAssay:int;
		public var idPrimer:int;
		public var redoFlag:String;
		
		public var submitter:String;
		public var submitDate:String;
		
		// Renderer Fields
		public var color:uint;
		public var groupId:String;
		
		// Sample object
		public var sample:Object;
		public var hasSample:Boolean = false;
		
		// Sample object
		public var wellObject:Object;
		
		
		// Temporary constructor - just takes a 'sample name'
		// Later, the constructor will take a well object
		public function WellContainer( label:String ):void {
			
			super();
			this.sampleName = label;
			
			height = 26;
			width = height;
			verticalScrollPolicy = 'off';
			horizontalScrollPolicy = 'off';
			setStyle( 'cornerRadius', height / 2 );
			setStyle( 'horizontalAlign', 'center' );
			setStyle( 'verticalAlign', 'middle' );
			setStyle( 'borderColor', 0x010000 );
			setStyle( 'borderStyle', 'solid' );
			
			toolTip = getToolTip();
			addChild( new WellLabel( label ));
			addEventListener( MouseEvent.CLICK, onClick );
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
			this.wellObject = pw;
			
			this.groupId = pw.@idRequest!= null ? pw.@idRequest : '';
			
			this.idPlateWell = pw.@idPlateWell != null ? pw.@idPlateWell : 0;
			this.position = pw.@position != null ? pw.@position : 0;
			this.row = pw.@row != null ? pw.@row : '';
			this.col = pw.@col != null ? pw.@col : 0;
			this.idSample = pw.@idSample != null ? pw.@idSample : 0;
			this.sampleName = pw.@sampleName != null ? pw.@sampleName : '';
			this.idRequest = pw.@idRequest != null ? pw.@idRequest : 0;
			this.codeReactionType = pw.@codeReactionType != null ? pw.@codeReactionType : '' ;
			this.idAssay = pw.@idAssay != null ? pw.@idAssay : 0;
			this.idPrimer = pw.@idPrimer != null ? pw.@idPrimer : 0;
			this.redoFlag = pw.@redoFlag != null ? pw.@redoFlag : '';
			
			this.submitter = pw.@requestSubmitter != null ? pw.@requestSubmitter : '';
			this.submitDate = pw.@requestSubmitDate != null ?pw.@requestSubmitDate : '';
			
			this.hasSample = true;
			this.setToolTip();
		}
		
		// Click to highlight wells in same grouping on plate
		public function onClick( event:Event ):void {
			
			if ( plateView != null ) {
				plateView.colorPick.selectedItem = plateView.colorPick.colorArray[ plateView.colorPick.getLabelIndex( this.groupId )];
				plateView.colorPick.selectedIndex = plateView.colorPick.getLabelIndex( this.groupId );
				plateView.colorPick.dispatchEvent( new FlexEvent( FlexEvent.VALUE_COMMIT ));
				event.stopPropagation();
			}
		}
		
		// OLD
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
		// OLD
		public function closeWellInfo( event:FlexMouseEvent ):void {
			
			PopUpManager.removePopUp( TitleWindow( event.target ));
		}
		
		public function setToolTip():void {
			this.toolTip = getToolTip();
		}
		
		private function getToolTip():String {
			
			var str:String = "";
			if ( wellObject != null ) {
				str += 'Well ' + ( position + 1 ) + ', ' + row + col + "";
				
				if ( idPlateWell != 0 ) {
					str += ', ' + idPlateWell;
				}
				
				str += '\rSample: ' + sampleName;
				str += '\rRequest #: ' + groupId;
				if ( sample != null && sample.@description != null ){
					str += '\rSample Desc: ' + sample.@description;
				}
			}
			return str;
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
	}
}

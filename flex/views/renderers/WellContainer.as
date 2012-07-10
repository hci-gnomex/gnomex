package views.renderers {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Box;
	import mx.containers.TitleWindow;
	import mx.controls.Label;
	import mx.controls.Text;
	import mx.controls.TextArea;
	import mx.core.UIComponent;
	import mx.events.CloseEvent;
	import mx.events.DragEvent;
	import mx.events.FlexEvent;
	import mx.events.FlexMouseEvent;
	import mx.managers.DragManager;
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
		public var isControl:String = "N";
		
		public var submitter:String;
		public var submitDate:String;
		
		// Renderer Fields
		public var color:uint = 0xFFFFFF;
		public var groupId:String;
		
		// Sample object
		public var sample:Object;
		public var hasSample:Boolean = false;
		
		// Sample object
		public var sourceWell:Object;
		
		
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
			addEventListener( DragEvent.DRAG_ENTER, dragOverWell );
			addEventListener( DragEvent.DRAG_OVER, dragOverWell );
			addEventListener( DragEvent.DRAG_DROP, dropOnWell );
			addEventListener( DragEvent.DRAG_EXIT, dragExitWell );
		}
				
		
		// function to get back the sample and its information
		public function getSample():Object {
			return sample;
		}
		
		
		// function to set the sample
		public function setSample( sample:Object ):void {
			this.sample = sample;
			if ( sample != null ) {
				this.idSample = sample.@idSample != null ? sample.@idSample : 0;
				this.sampleName = sample.@name != null ? sample.@name : '';
				this.groupId = sample.@idRequest != null ? sample.@idRequest : '';
				this.hasSample = true;
				this.isControl = "N";
				sample.@isOnPlate = true;
				
			} else {
				this.sourceWell = null;
				
				this.idSample =  0;
				this.sampleName =  '';
				this.groupId =  '';
				this.idRequest = 0;
				this.codeReactionType = '' ;
				this.idAssay = 0;
				this.idPrimer = 0;
				this.submitter = '';
				this.submitDate = '';
				
				this.hasSample = false;
			}
			this.setToolTip();
		}
		
		public function loadSourceWell( pw:Object ):void {
			if ( pw == null ) {
				setSample( null );
				sourceWell = null;
				return;
			}
			
			this.isControl = pw.@isControl != null ? pw.@isControl : '';
			if ( this.isControl == "Y" ) {
				this.setControl( true );
				return;
			} 
			pw.@isOnPlate = true;
			this.sourceWell = pw;
			this.idPlateWell = pw.@idPlateWell != null ? pw.@idPlateWell : 0;
			this.idRequest = pw.@idRequest != null ? pw.@idRequest : 0;
			if ( pw.@groupId != null && pw.@groupId != '' ) {
				this.groupId = pw.@groupId;
			} else {
				this.groupId = idRequest.toString();
			}
			
			this.codeReactionType = pw.@codeReactionType != null ? pw.@codeReactionType : '' ;
			this.idSample = pw.@idSample != null ? pw.@idSample : 0;
			this.sampleName = pw.@sampleName != null ? pw.@sampleName : '';
			this.idAssay = pw.@idAssay != null ? pw.@idAssay : 0;
			this.idPrimer = pw.@idPrimer != null ? pw.@idPrimer : 0;
			
			this.submitter = pw.@requestSubmitter != null ? pw.@requestSubmitter : '';
			this.submitDate = pw.@requestSubmitDate != null ?pw.@requestSubmitDate : '';
			
			this.setToolTip();
			this.hasSample = true;
		}
				
		// Drag and Drop functions
		public function dragOverWell( event:Event ):void {
			DragManager.acceptDragDrop(event.currentTarget as UIComponent );
			if ( plateView != null ) {
				plateView.plateDropIndex = position;
				this.setStyle( 'backgroundColor', 0xFFCC66 );
			}
		}
		public function dropOnWell( event:Event ):void {
			if ( plateView != null ) {
				plateView.plateDropIndex = position;
				this.setStyle( 'backgroundColor', color );
				plateView.addButtonClick();
			}
		}
		public function dragExitWell( event:Event ):void {
			if ( plateView != null ) {
				plateView.plateDropIndex = 0;
			}
			this.setStyle( 'backgroundColor', color );
		}
		
		// Click to highlight wells in same grouping on plate
		public function onClick( event:Event ):void {
			if ( plateView != null ) {
				if ( this.hasSample ) {
					plateView.colorPick.selectedItem = plateView.colorPick.colorArray[ plateView.colorPick.getLabelIndex( this.groupId )];
					plateView.colorPick.selectedIndex = plateView.colorPick.getLabelIndex( this.groupId );
					plateView.colorPick.dispatchEvent( new FlexEvent( FlexEvent.VALUE_COMMIT ));
					event.stopPropagation();
				} else if ( plateView.currentState.substr(0, 4) != 'view' ) {
					this.setControl( this.isControl == "N" );
					plateView.dirty.setDirty();
				}
			}
		}
		

		public function setToolTip():void {
			this.toolTip = getToolTip();
		}
		
		private function getToolTip():String {
			
			if ( isControl=="Y" ){
				return "Control";
			}
			
			var str:String = "";
			if ( sourceWell != null ) {
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
		
		public function setControl( control:Boolean ):void {
			if ( control ) { 
				isControl = "Y";
				setLabel("C");
				setColor(0x484848);
				setSample( null );
			} else {
				isControl = "N";
				setColor(0xFFFFFF);
				setLabel((this.position + 1).toString());
			}
			setToolTip();
		}
		
	}
}

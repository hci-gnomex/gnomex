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
		public var wellLabel:WellLabel = new WellLabel('');
		
		public var plateView:NavPlateView;
		
		// PlateWell Fields
		public var idPlateWell:int;
		public var position:int;
		public var row:String;
		public var col:int;
		public var idSample:int;
		public var sampleName:String;
		public var idRequest:int;
		public var requestNumber:String;
		public var codeReactionType:String;
		public var idAssay:int;
		public var idPrimer:int;
		public var redoFlag:String = "N";
		public var isControl:String = "N";
		
		public var submitter:String;
		public var submitDate:String;
		
		public var sourceWell:Object;
		public var hasSample:Boolean = false;
		
		// Renderer Fields
		public var color:uint = 0xFFFFFF;
		public var groupId:String;
		
		
		
		// Constructor
		public function WellContainer( label:String, ht:int ):void {
			
			super();
			this.sampleName = label;
			
			if ( label == '' ) {
				this.loadSourceWell( null );
			}
			
			height = ht;
			width = height;
			verticalScrollPolicy = 'off';
			horizontalScrollPolicy = 'off';
			setStyle( 'cornerRadius', height / 2 );
			setStyle( 'horizontalAlign', 'center' );
			setStyle( 'verticalAlign', 'middle' );
			setStyle( 'borderColor', 0x010000 );
			setStyle( 'borderStyle', 'solid' );
			
			toolTip = getToolTip();
			addChild( this.wellLabel );
			addEventListener( MouseEvent.CLICK, onClick );
			addEventListener( DragEvent.DRAG_ENTER, dragOverWell );
			addEventListener( DragEvent.DRAG_OVER, dragOverWell );
			addEventListener( DragEvent.DRAG_DROP, dropOnWell );
			addEventListener( DragEvent.DRAG_EXIT, dragExitWell );
		}
		
		public function resizeWell( ht:int ):void {
			height = ht;
			width = height;
			setStyle( 'cornerRadius', height / 2 );
		}
		
		public function getSample():Object {
			return this.sourceWell;
		}
		
		public function loadSourceWell( pw:Object ):void {
			if ( pw == null ) {
				this.sourceWell = null;
				
				this.idSample =  0;
				this.sampleName =  '';
				this.groupId =  '';
				this.idRequest = 0;
				this.requestNumber = '';
				this.codeReactionType = '' ;
				this.idAssay = 0;
				this.idPrimer = 0;
				this.submitter = '';
				this.submitDate = '';
				
				this.hasSample = false;
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
			this.requestNumber = pw.@requestNumber != null ? pw.@requestNumber : '';
			if ( pw.@groupId != null && pw.@groupId != '' ) {
				this.groupId = pw.@groupId;
			} else {
				this.groupId = requestNumber;
			}
			this.codeReactionType = pw.@codeReactionType != null ? pw.@codeReactionType : '' ;
			this.idSample = pw.@idSample != null ? pw.@idSample : 0;
			this.sampleName = pw.@sampleName != null ? pw.@sampleName : '';
			this.idAssay = pw.@idAssay != null ? pw.@idAssay : 0;
			this.idPrimer = pw.@idPrimer != null ? pw.@idPrimer : 0;
			this.redoFlag = pw.@redoFlag != null ? pw.@redoFlag : "N";
			if ( this.redoFlag == "Y" ) {
				this.wellLabel.highlightLabel();
			} else {
				this.wellLabel.resetLabel();
			}
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
				} else if ( plateView.currentState.substr(0, 4) != 'view' && plateView.buildState != 'RUN' ) {
					this.setControl( this.isControl == "N" );
					plateView.setDirty();
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
				if ( this.redoFlag == "Y" ) {
					str += "REDO\r";
				}
				str += 'Well ' + ( position + 1 ) + ', ' + row + col + "";
				
				if ( idPlateWell != 0 ) {
					str += ', ' + idPlateWell;
				}
				
				str += '\rSample: ' + sampleName;
				str += '\rOrder #: ' + groupId;
				
			} else if ( plateView != null ) {
				if ( plateView.currentState.substr(0, 4) != 'view' && plateView.buildState != 'RUN' ) {
					str += 'Click to toggle control';
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
			this.wellLabel.text = label;
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
				loadSourceWell( null );
			} else {
				isControl = "N";
				setColor(0xFFFFFF);
				setLabel((this.position + 1).toString());
			}
			setToolTip();
		}
		
	}
}

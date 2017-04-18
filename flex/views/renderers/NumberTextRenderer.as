/**
 * Created by u0566434 on 2/17/2017.
 */
package views.renderers {

import flash.display.Graphics;
import flash.events.MouseEvent;
import flash.geom.Point;

import mx.controls.dataGridClasses.DataGridListData;
import mx.core.IUIComponent;

import mx.controls.ToolTip;
import mx.managers.ToolTipManager;



import hci.flex.renderers.RendererFactory;

import mx.controls.AdvancedDataGrid;


import mx.core.IFactory;

import views.util.AdvancedDataGridWithCustomRowColors;

import views.util.SampleNumberValidator;


public class NumberTextRenderer extends mx.controls.Label {

    public var _dataField:String;
    public var _isRequired:Boolean;
    public var _toolTipField:String;
    public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
    public var missingRequiredFieldBorder:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER;
    public var missingRequiredFieldBorderThickness:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS;
    public var highlightedColor:uint = RendererFactory.DEFAULT_HIGHLIGHT_COLOR;
    public var errorBackground:uint = RendererFactory.DEFAULT_ERROR_BACKGROUND;
    protected var applicableToolTip:ToolTip;
    public var validate:SampleNumberValidator;




    public function NumberTextRenderer() {
        addEventListener(MouseEvent.ROLL_OUT,destroyToolTip);
        addEventListener(MouseEvent.ROLL_OVER,createToolTip);
        validate = new SampleNumberValidator();


    }
    private function createToolTip(event:MouseEvent):void{
            var toolTipMessage:String = validateNumber();
            if(toolTipMessage!= null) {
                var stagePoint:Point = event.target.localToGlobal(new Point(0, -30));
                applicableToolTip = ToolTipManager.createToolTip(
                                toolTipMessage,
                                stagePoint.x,
                                stagePoint.y,
                                null,
                                IUIComponent(event.currentTarget)
                        ) as ToolTip;
                applicableToolTip.visible = true;
                applicableToolTip.enabled = true;
            }


    }
    private function destroyToolTip(event:MouseEvent):void{
            if(applicableToolTip != null){
                ToolTipManager.destroyToolTip(applicableToolTip);
                ToolTipManager.currentToolTip = null;
                applicableToolTip = null;

            }
    }



    public static function create(dataField:String,
                                  isRequired:Boolean = false,
                                  toolTipField:String=""):IFactory {
        return RendererFactory.create(views.renderers.NumberTextRenderer,
                                        {_dataField: dataField,
                                        _isRequired:isRequired,
                                        _toolTipField:toolTipField});

    }

    public function validateNumber():String{

        if (listData.owner is AdvancedDataGrid) {

            if(_dataField == "@meanLibSizeActual"
                    || _dataField == "@qualFragmentSizeFrom"
                    ||_dataField == "@qualFragmentSizeTo"
                    || _dataField == "@multiplexGroupNumber") {
                return validate.validateInteger(data[_dataField], _isRequired);
            }
            else if(_dataField == "@numberSequencingLanes"){
                var result:String = validate.validateInteger(data[_dataField], _isRequired);
                if(result == null){
                    return _toolTipField;
                }
                return result;
            }
            else if(_dataField == "@qual260nmTo230nmRatio" || _dataField == "@qual260nmTo280nmRatio" ) {
               return validate.validateDecimal(data[_dataField],parentDocument.QC_260_RATIO_MAX);
            }
            else if(_dataField == "@qualCalcConcentration" || _dataField == "@sampleVolume"){
                return validate.validateDecimal(data[_dataField],parentDocument.QC_CONCENTRATION_MAX);
            }
            else if(_dataField == "@concentration"){
                return validate.validateDecimal(data[_dataField],parentDocument.CONCENTRATION_MAX);
            }
        }

        return null;
    }


    override public function set data(value:Object):void {
        super.data = value;
    }


    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var g:Graphics = graphics;
        g.clear();

        if (data == null) {
            return;
        }
        if (!data.hasOwnProperty(_dataField)) {
            return;
        }

        if(data[_dataField] == "" && _isRequired){ // required fields are empty
            g.beginFill(missingRequiredFieldBackground);
            g.lineStyle(missingRequiredFieldBorderThickness,missingRequiredFieldBackground);
            g.drawRect(0, 0, unscaledWidth, unscaledHeight);
            g.endFill();
            return;
        }

        var invalidMessage:String = validateNumber();


        if(_dataField == "@numberSequencingLanes"){
            if(invalidMessage != null && _toolTipField != invalidMessage){
                g.beginFill(errorBackground);
                g.lineStyle(missingRequiredFieldBorderThickness, errorBackground);
                g.drawRect(0, 0, unscaledWidth, unscaledHeight);
                g.endFill();
                return;
            }
            g.beginFill(highlightedColor);
            g.lineStyle(missingRequiredFieldBorderThickness, highlightedColor);
            g.drawRect(0, 0, unscaledWidth, unscaledHeight);
            g.endFill();
        }
        else if (invalidMessage != null) { // error
            g.beginFill(errorBackground);
            g.lineStyle(missingRequiredFieldBorderThickness, errorBackground);
            g.drawRect(0, 0, unscaledWidth, unscaledHeight);
            g.endFill();
        }



    }
}
}

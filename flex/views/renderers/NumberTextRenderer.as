/**
 * Created by u0566434 on 2/17/2017.
 */
package views.renderers {

import flash.display.Graphics;

import hci.flex.renderers.RendererFactory;

import mx.controls.AdvancedDataGrid;
import mx.controls.DataGrid;
import mx.controls.listClasses.IDropInListItemRenderer;
import mx.events.FlexEvent;

import views.util.AdvancedDataGridWithCustomRowColors;

import mx.core.IFactory;


public class NumberTextRenderer extends mx.controls.Label {

    public var _dataField:String;
    public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
    public var missingRequiredFieldBorder:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER;
    public var missingRequiredFieldBorderThickness:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS;
    public var highlightedColor:uint = RendererFactory.DEFAULT_HIGHLIGHT_COLOR;
    public var errorBackground:uint = RendererFactory.DEFAULT_ERROR_BACKGROUND;

    public function NumberTextRenderer() {
    }



    public static function create(dataField:String):IFactory {
        return RendererFactory.create(views.renderers.NumberTextRenderer,
                {_dataField: dataField});

    }

    public function isAlpha():Boolean{

        if (listData.owner is AdvancedDataGrid) {
            if (_dataField == "@concentration" || _dataField == "@sampleVolume" || _dataField == "@meanLibSizeActual"
                    || _dataField == "@qualCalcConcentration" || _dataField == "@qualFragmentSizeTo"
                    || _dataField == "@qualFragmentSizeFrom" || _dataField == "@qual260_230Col"
                    || _dataField == "@qual260nmTo280nmRatio" ) {
                if (data[_dataField] != '' && isNaN(parseInt(data[_dataField]))) {
                    return true
                }
            }
        }

        return false;
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

        if(isAlpha()) {

            g.beginFill(errorBackground);
            g.lineStyle(missingRequiredFieldBorderThickness, errorBackground);
            g.drawRect(0, 0, unscaledWidth, unscaledHeight);
            g.endFill();
        }

    }
}
}
